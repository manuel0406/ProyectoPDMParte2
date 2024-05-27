package sv.edu.ues.fia.telollevoya.pedidos.negocio;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.telollevoya.Cliente;
import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Repartidor;

public class PedidosPendientesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView pedidosActivosListView;
    private ArrayList<Pedido> pedidosList;
    private ArrayList<Repartidor> repartidoresList;
    private List<Spinner> spínners;
    private ControlBD controlBD;
    private int idNegocio;
    private final String URL_PEDIDOS_PENDIENTES_SERVICIO = "https://telollevoya.000webhostapp.com/Pedidos/pedidos_pendientes.php?negocio=";
    private final String URL_ACTUALIZAR_REPARTIDOR_PEDIDO = "https://telollevoya.000webhostapp.com/Pedidos/actualizar_repartidor_pedido.php";
    private final String URL_REPARTIDORES_SERVICIO = "https://telollevoya.000webhostapp.com/Pedidos/repartidores.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedidos_pendientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pedidosActivosListView = findViewById(R.id.pedidos_activos_listView);
        pedidosList = new ArrayList<>();
        repartidoresList = new ArrayList<>();
        spínners = new ArrayList<>();

        controlBD = new ControlBD(PedidosPendientesActivity.this);
        //Recibir id de negocio;
        idNegocio = 1;
//        getPedidosPendientes();
//        getPedidosPendientesConRepartidor();
        getAllPedidos();
        getRepartidores();
        PedidosActivosAdapter adapter = new PedidosActivosAdapter(PedidosPendientesActivity.this, 0, pedidosList);
        pedidosActivosListView.setAdapter(adapter);
    }

    public void getAllPedidos() {
        String url = URL_PEDIDOS_PENDIENTES_SERVICIO + idNegocio;
        String respuesta = ControladorSevicio.obtenerRepuestaPeticion(url, getApplicationContext());
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            JSONArray jsonSinRep = jsonArray.getJSONObject(0).getJSONArray("sin");
            JSONArray jsonConRep = jsonArray.getJSONObject(1).getJSONArray("con");
            //PEDIDOS SIN REPARTIDOR
            for (int i = 0; i <= jsonSinRep.length() - 1; i++){
                Pedido pedido = new Pedido();
                JSONObject object = jsonSinRep.getJSONObject(i);

                pedido.setId(object.getInt("IDPEDIDO"));
                Cliente cliente = new Cliente();
                cliente.setIdCliente(Integer.toString(object.getInt("IDCLIENTE")));
                pedido.setCliente(cliente);
                pedido.setTotalAPagar(Float.parseFloat(object.getString("TOTALAPAGAR")));
                String fechaPed = object.getString("FECHAPEDIDO");
                if (!fechaPed.equalsIgnoreCase("null"))
                    pedido.setFechaPedido(Timestamp.valueOf(fechaPed));
                else
                    pedido.setFechaPedido(null);

                pedido.setDescripcionOrden(object.getString("DESCRIPCIONORDEN"));

                pedidosList.add(pedido);
            }

            //PEDIDOS CON REPARTIDORES
            for(int i = 0; i <= jsonConRep.length()-1; i++){
                Pedido pedido = new Pedido();
                JSONObject object = jsonConRep.getJSONObject(i);
                pedido.setId(object.getInt("IDPEDIDO"));
                pedido.setTotalAPagar(Float.parseFloat(object.getString("TOTALAPAGAR")));
                String fechaPed = object.getString("FECHAPEDIDO");
                if (!fechaPed.equalsIgnoreCase("null"))
                    pedido.setFechaPedido(Timestamp.valueOf(fechaPed));
                else
                    pedido.setFechaPedido(null);

                String fechaEn = object.getString("FECHAENTREGAP");
                if (!fechaEn.equalsIgnoreCase("null"))
                    pedido.setFechaEntrega(Timestamp.valueOf(fechaEn));
                else
                    pedido.setFechaEntrega(null);
                pedido.setDescripcionOrden(object.getString("DESCRIPCIONORDEN"));
                Repartidor rep = new Repartidor();
                rep.setIdRepartidor(Integer.toString(object.getInt("IDREPARTIDOR")));
                rep.setNombre(object.getString("NOMBREREPARTIDOR"));
                rep.setApellido(object.getString("APELLIDOREPARTIDOR"));
                pedido.setRepartidor(rep);

                Cliente cliente = new Cliente();
                cliente.setIdCliente(Integer.toString(object.getInt("IDCLIENTE")));
                pedido.setCliente(cliente);
                pedidosList.add(pedido);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void getPedidosPendientes(){
////        controlBD.abrir();
////        pedidosList = controlBD.getPedidosPendientes();
////        controlBD.cerrar();
//    }
//
//    public void getPedidosPendientesConRepartidor(){
//        controlBD.abrir();
//        pedidosList.addAll(controlBD.getPedidosPendientesConRepartidor());
//        controlBD.cerrar();
//    }

    public void getRepartidores(){
        String respuesta = ControladorSevicio.obtenerRespuestaPeticion(URL_REPARTIDORES_SERVICIO, getApplicationContext());
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Repartidor repartidor = new Repartidor();
                repartidor.setIdRepartidor(jsonObject.getString("IDREPARTIDOR"));
                repartidor.setNombre(jsonObject.getString("NOMBREREPARTIDOR"));
                repartidor.setApellido(jsonObject.getString("APELLIDOREPARTIDOR"));
                repartidor.setSexo(jsonObject.getString("SEXOREPARTIDOR"));
                repartidor.setCorreo(jsonObject.getString("CORREOREPARTIDOR"));
                repartidor.setNacimiento(jsonObject.getString("FECHANACIMIENTOR"));

                repartidoresList.add(repartidor);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void backButton(View v){
        finish();
    }

    public class PedidosActivosAdapter extends ArrayAdapter<Pedido> {
        public PedidosActivosAdapter(@NonNull Context context, int resource, @NonNull List<Pedido> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pedido_card_negocio_custom, parent, false);
            TextView codTextView = convertView.findViewById(R.id.cod_textView);
            TextView descripTextView = convertView.findViewById(R.id.desc_textView);
            TextView montoTextView = convertView.findViewById(R.id.monto_textView);
            TextView fechaPedidoTextView = convertView.findViewById(R.id.fechaPedido_textView);
            TextView clienteTextView = convertView.findViewById(R.id.cliente_textView);
            TextView repartidorTextView = convertView.findViewById(R.id.repart_textView);
            Spinner repartidorSpinner = convertView.findViewById(R.id.repartidor_spinner);
            Button guardarBtn = convertView.findViewById(R.id.guardar_btn);

            Pedido pedido = pedidosList.get(position);
            codTextView.setText(Integer.toString(pedido.getId()));
            descripTextView.setText(pedido.getDescripcionOrden());
            montoTextView.setText("$"+Float.toString(pedido.getTotalAPagar()));
            fechaPedidoTextView.setText(pedido.getFechaPedido().toString());
            clienteTextView.setText(pedido.getCliente().getIdCliente());
            if(pedido.getRepartidor() != null && pedido.getRepartidor().getNombre() != null)
                repartidorTextView.setText(pedido.getRepartidor().getNombre());
            else
                repartidorTextView.setText("No asignado");

            ArrayAdapter<Repartidor> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, repartidoresList);
            repartidorSpinner.setAdapter(spinnerAdapter);
            if(pedido.getRepartidor() != null) {
                Repartidor repAsignado = pedido.getRepartidor();
                repartidorSpinner.setSelection(repartidoresList.indexOf(repAsignado), false);
            }
            repartidorSpinner.setOnItemSelectedListener(PedidosPendientesActivity.this);
            repartidorSpinner.setTag(repartidoresList.get(0));
            guardarBtn.setTag(pedido.getId());
            guardarBtn.setOnClickListener(v ->{
                controlBD.abrir();
                Repartidor repartidor = (Repartidor) repartidorSpinner.getTag();
                int idPedido = (int) v.getTag();
                String url = URL_ACTUALIZAR_REPARTIDOR_PEDIDO+"?repartidor="+repartidor.getIdRepartidor()+"&pedido="+idPedido;
                String respuesta = ControladorSevicio.obtenerRespuestaPeticion(url, getApplicationContext());
                if(respuesta.toLowerCase().contains("actualizado")){
                    Toast.makeText(PedidosPendientesActivity.this, "Repartidor asignado con éxito",
                            Toast.LENGTH_SHORT).show();
                    repartidorTextView.setText(repartidor.getNombre());
                }else Toast.makeText(PedidosPendientesActivity.this, "Ocurrió un problema",
                        Toast.LENGTH_SHORT).show();
            });
            return convertView;
        }
    }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Repartidor repartidor = repartidoresList.get(position);
            view.setTag(repartidor);
            Toast.makeText(PedidosPendientesActivity.this, "Repartidor "+repartidor.getIdRepartidor() , Toast.LENGTH_SHORT);
            //método de actualizar repartidor de pedido en la BD
        }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}