package sv.edu.ues.fia.telollevoya.pedidos.cliente;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservaciones.ReservacionesConsultarActivity;
import sv.edu.ues.fia.telollevoya.seguridad.IniciarSesionActivity;

public class MisPedidosActivity extends Activity {

    //Esta variable almacena el idCliente
    //String idCliente;
    private ArrayList<Pedido> pedidosActivosList;
    private ArrayList<Pedido> pedidosRelizadosList;
    private ListView pedidosActivosListView;
    private ListView pedidosRealizListView;
    private final int PEDIDO_ACTIVO = 1;
    private final int PEDIDO_CANCELADO = 2;
    private final int PEDIDO_REALIZADO = 3;
    private int idCliente;
    public String idClienteStr;

    ControlBD controlBD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pedidos);

        controlBD = new ControlBD(MisPedidosActivity.this);
        pedidosRelizadosList = new ArrayList<>();
        pedidosActivosList = new ArrayList<>();
        pedidosActivosListView = findViewById(R.id.pedidos_activos_listView);
        pedidosRealizListView = findViewById(R.id.pedidos_realiz_listView);
        getPedidos();
        PedidosActivosAdapter activosAdapter = new PedidosActivosAdapter(MisPedidosActivity.this, 0, pedidosActivosList);
        pedidosActivosListView.setAdapter(activosAdapter);
        PedidosRealizadosAdapter realizadosAdapter = new PedidosRealizadosAdapter(MisPedidosActivity.this, 0, pedidosRelizadosList);
        pedidosRealizListView.setAdapter(realizadosAdapter);

    }

    public void getPedidos(){
        try {
            String enviado = getIntent().getExtras().getString("idCliente");
            if (enviado != null) {
                idCliente = Integer.parseInt(enviado);
                controlBD.abrir();
                List<Pedido> pedidos = controlBD.getPedidos(idCliente);
                pedidos.forEach(p -> {
                    String tipo = p.getEstadoOrden().getTipo();
                    if (tipo.equalsIgnoreCase("A") || tipo.equalsIgnoreCase("ACTIVO"))//Pedido activo
                        pedidosActivosList.add(p);
                    else//Pedidos realizados o cancelados
                        pedidosRelizadosList.add(p);
                });
            }

        }catch(NullPointerException ex){
        ex.printStackTrace();
            Toast.makeText(MisPedidosActivity.this, "No  hay nada por mostrar", Toast.LENGTH_SHORT);
        }
    }


    //----------------------------------------------------------------------------------------------
    //                          METODO PARA CERRAR SESION
    //----------------------------------------------------------------------------------------------
    public void cerrarSesion(View v) {

        //Limpio base de usuarios disponibles
        controlBD = new ControlBD(this);
        controlBD.abrir();
        controlBD.LimpiarUsuario();
        controlBD.cerrar();

        Toast.makeText(this, "Vuelve pronto", Toast.LENGTH_SHORT).show();

        // Crear un Intent para iniciar sesion "Iniciar Sesion"
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        intent.putExtra("desdeInicioApp", false);
        startActivity(intent);
        finish();
    }

    public void backButton(View v){
        finish();
    }

    public void irSeleccionProducto(View v){
        Intent intent = new Intent(MisPedidosActivity.this, SeleccionarProductoActivity.class);
        intent.putExtra("idCliente", idCliente);
        startActivity(intent);
    }

    public void irReservacionesConsultar(View v){

        Toast.makeText(this, idClienteStr,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MisPedidosActivity.this, ReservacionesConsultarActivity.class);
        intent.putExtra("idCliente", idClienteStr);

        startActivity(intent);
    }

    public class PedidosActivosAdapter extends ArrayAdapter<Pedido>{

        public PedidosActivosAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pedido_card_custom, parent, false);
            TextView codTextView = convertView.findViewById(R.id.cod_textView);
            TextView descripTextView = convertView.findViewById(R.id.desc_textView);
            TextView montoTextView = convertView.findViewById(R.id.monto_textView);
            TextView fechaPedido = convertView.findViewById(R.id.fechaPedido_textView);
            TextView estado =convertView.findViewById(R.id.estado_textView);
            Button cancelarBtn = convertView.findViewById(R.id.cancelar_btn);

            Pedido pedido = pedidosActivosList.get(position);
            codTextView.setText(Integer.toString(pedido.getId()));
            descripTextView.setText(pedido.getDescripcionOrden());
            montoTextView.setText("$"+Float.toString(pedido.getTotalAPagar()));
            fechaPedido.setText(pedido.getFechaPedido().toString());
            estado.setText("Activo");

            cancelarBtn.setTag(pedido.getId());
            cancelarBtn.setOnClickListener(v ->{
                //query a modificar estado de pedido
                int idPedido = (int) v.getTag();
                controlBD.abrir();
                boolean result = controlBD.actualizarEstadoPedido(idPedido, PEDIDO_CANCELADO);// siendo 2 estado CANCELADO
                controlBD.cerrar();
                if(result) {
                    v.setEnabled(false);
                    v.setBackgroundColor(Color.GRAY);
                    Toast.makeText(MisPedidosActivity.this, "Pedido cancelado", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MisPedidosActivity.this, "No se pudo cancelar :(", Toast.LENGTH_SHORT).show();

            });

            return convertView;
        }
    }

    public class PedidosRealizadosAdapter extends ArrayAdapter<Pedido>{
        public PedidosRealizadosAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pedido_realizado_card_custom, parent, false);
            TextView codTextView = convertView.findViewById(R.id.cod_textView);
            TextView descripTextView = convertView.findViewById(R.id.desc_textView);
            TextView montoTextView = convertView.findViewById(R.id.monto_textView);
            TextView estado =convertView.findViewById(R.id.estado_textView);
            TextView fechaEntregado = convertView.findViewById(R.id.fechaEntregado_textView);

            Pedido pedido = pedidosRelizadosList.get(position);
            codTextView.setText(Integer.toString(pedido.getId()));
            descripTextView.setText(pedido.getDescripcionOrden());
            switch(pedido.getEstadoOrden().getId()){
                case PEDIDO_REALIZADO:
                    estado.setText("Realizado");
                    break;
                case PEDIDO_CANCELADO:
                    estado.setText("Cancelado");
                    break;
            }
            montoTextView.setText("$" + Float.toString(pedido.getTotalAPagar()));
            fechaEntregado.setText(pedido.getFechaPedido().toString());

            return convertView;
        }
    }
}