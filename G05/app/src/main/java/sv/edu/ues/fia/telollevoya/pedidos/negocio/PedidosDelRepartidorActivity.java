package sv.edu.ues.fia.telollevoya.pedidos.negocio;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Repartidor;

public class PedidosDelRepartidorActivity extends AppCompatActivity {

    private ArrayList<Pedido> pedidos;
    private ListView pedidosListView;
    ControlBD controlBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pedidos_del_repartidor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pedidos = new ArrayList<>();
        pedidosListView = findViewById(R.id.pedidos_listView);
        controlBD = new ControlBD(PedidosDelRepartidorActivity.this);
        getPedidos();
        PedidosAdapter adapter = new PedidosAdapter(PedidosDelRepartidorActivity.this, 0, pedidos);
        pedidosListView.setAdapter(adapter);
    }

    public void getPedidos(){
        try {
            String enviado = getIntent().getExtras().getString("idRepartidor");
            if (enviado != null) {
                int idRepartidor = Integer.parseInt(enviado);
                controlBD.abrir();
                pedidos = controlBD.getPedidosActivosPorRepartidor(idRepartidor);
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void backButton(View v){
        finish();
    }

    public class PedidosAdapter extends ArrayAdapter<Pedido> {
        public PedidosAdapter(@NonNull Context context, int resource, @NonNull List<Pedido> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pedido_repartidor_card_custom, parent, false);
            TextView codTextView = convertView.findViewById(R.id.cod_textView);
            TextView descripTextView = convertView.findViewById(R.id.desc_textView);
            TextView montoTextView = convertView.findViewById(R.id.monto_textView);
            TextView fechaPedidoTextView = convertView.findViewById(R.id.fechaPedido_textView);
            TextView ubicacionTextView = convertView.findViewById(R.id.ubicacion_textView);
            Button finalizarBtn = convertView.findViewById(R.id.finalizar_btn);

            Pedido pedido = pedidos.get(position);
            codTextView.setText(Integer.toString(pedido.getId()));
            descripTextView.setText(pedido.getDescripcionOrden());
            montoTextView.setText("$"+Float.toString(pedido.getTotalAPagar()));
            fechaPedidoTextView.setText(pedido.getFechaPedido().toString());
            ubicacionTextView.setText(pedido.getUbicacion().getDescripcion());

            finalizarBtn.setTag(pedido.getId());
            finalizarBtn.setOnClickListener(v ->{
                controlBD.abrir();
                int idPedido = (int) v.getTag();
                if (controlBD.actualizarEstadoPedido(idPedido, 3, new java.util.Date())){//Dando por ENTREGADO el pedido con la fecha actual
                    Toast.makeText(PedidosDelRepartidorActivity.this, "Estado de pedido actualizado a REALIZADO!!", Toast.LENGTH_SHORT).show();
                    v.setEnabled(false);
                    v.setBackgroundColor(Color.GRAY);
                }
            });
            return convertView;
        }
    }
}