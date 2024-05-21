package sv.edu.ues.fia.telollevoya.pedidos.negocio;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Repartidor;

public class PedidosPendientesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView pedidosActivosListView;
    private ArrayList<Pedido> pedidosList;
    private ArrayList<Repartidor> repartidoresList;
    private List<Spinner> spínners;
    private ControlBD controlBD;
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

        pedidosActivosListView = findViewById(R.id.pedidos_activos_listView);
        pedidosList = new ArrayList<>();
        repartidoresList = new ArrayList<>();
        spínners = new ArrayList<>();
        controlBD = new ControlBD(PedidosPendientesActivity.this);
        getPedidosPendientes();
        getPedidosPendientesConRepartidor();
        getRepartidores();
        PedidosActivosAdapter adapter = new PedidosActivosAdapter(PedidosPendientesActivity.this, 0, pedidosList);
        pedidosActivosListView.setAdapter(adapter);
    }

    public void getPedidosPendientes(){
        controlBD.abrir();
        pedidosList = controlBD.getPedidosPendientes();
        controlBD.cerrar();
    }

    public void getPedidosPendientesConRepartidor(){
        controlBD.abrir();
        pedidosList.addAll(controlBD.getPedidosPendientesConRepartidor());
        controlBD.cerrar();
    }

    public void getRepartidores(){
        controlBD.abrir();
        repartidoresList = controlBD.getRepartidores();
        controlBD.cerrar();
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

            guardarBtn.setTag(pedido.getId());
            guardarBtn.setOnClickListener(v ->{
                controlBD.abrir();
                Repartidor repartidor = (Repartidor) repartidorSpinner.getTag();
                int idPedido = (int) v.getTag();
                if(controlBD.actualizarRepartidorPedido(Integer.parseInt(repartidor.getIdRepartidor()), idPedido)){
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