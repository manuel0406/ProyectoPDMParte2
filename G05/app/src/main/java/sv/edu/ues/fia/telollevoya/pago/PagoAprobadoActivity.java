package sv.edu.ues.fia.telollevoya.pago;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.MisPedidosActivity;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.SeleccionarProductoActivity;

public class PagoAprobadoActivity extends AppCompatActivity {
    Pedido pedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_aprobado);
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");
    }

    //Metodos para los botones
    public void solicitarVerFactura(View view) {
        Intent intent = new Intent(this, FacturaActivity.class);
        intent.putExtra("pedido", (Serializable) pedido);
        startActivity(intent);
    }

    public void verEstadoPedido(View view) {
        Intent intent = new Intent(this, MisPedidosActivity.class);
        intent.putExtra("idCliente", pedido.getCliente().getIdCliente());
        startActivity(intent);
    }
}
