package sv.edu.ues.fia.telollevoya.pago;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.DetallePedido;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.SeleccionarProductoActivity;

public class FacturaActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private AdaptadorProductosAPagar adaptador;
    private ControlBD helper;
    private Pedido pedido;
    TextView txDireccion;
    TextView txFechaEntrega;
    TextView txMetodoPago;
    TextView sumaDeSubtotales;
    TextView costoEnvio;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_factura);

        helper = new ControlBD(this);
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");

        helper.abrir(); //abriendo la base de datos
        //pedido = new Pedido();// solo necesito un pedido para hacer pruebas del modulo de Pago
        List<DetallePedido> detallesPedido = new ArrayList<>();
        //detallesPedido = helper.consultarDetallesPedido(pedido.getId());
        detallesPedido = pedido.getDetallePedidoList();

        txDireccion = (TextView) findViewById(R.id.txtDireccion);
        txDireccion.setText(pedido.getUbicacion().toString());

        txFechaEntrega = (TextView) findViewById(R.id.txtFechaEntrega);
        Date fechaEntrega = pedido.getFechaEntrega();

        if (fechaEntrega != null) {
            txFechaEntrega.setText(String.valueOf(fechaEntrega));
        } else {
            // Si la fecha de entrega es nula, establece la fecha actual
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaActual = dateFormat.format(new Date());
            txFechaEntrega.setText(fechaActual);
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new AdaptadorProductosAPagar(detallesPedido);
        recyclerView.setAdapter(adaptador);

        sumaDeSubtotales = (TextView) findViewById(R.id.txtSubtotal);
        double suma = calcularSumaSubtotales(detallesPedido);
        sumaDeSubtotales.setText(String.format("$%.2f", suma));

        costoEnvio = (TextView) findViewById(R.id.txtCostoEnvio);
        costoEnvio.setText("$1.00");//costo de envío predeterminado

        total = (TextView) findViewById(R.id.txtTotalPagar);
        double sumaTotal = calcularTotal(detallesPedido); //Metodo para obtener total a pagar
        //double sumaTotal = pedido.getTotalAPagar(); //Metodo alternativo para obtener el total a pagar.
        //double sumaTotal = pedido.getFactura().getTotalPagado(); Otra alternativa para obtener el total a pagar.
        total.setText(String.format("$%.2f", sumaTotal));

        txMetodoPago = (TextView) findViewById(R.id.txtMetodoPago);
        txMetodoPago.setText(pedido.getFactura().getMetodoPago().toString());


    }


    private double calcularSumaSubtotales(List<DetallePedido> detallesPedido) {
        double suma = 0.0;
        for (DetallePedido dp : detallesPedido) {
            suma += dp.getSubTotal();
        }
        return suma;
    }

    private double calcularTotal(List<DetallePedido> detallesPedido) {
        double total = 0.0;
        double costoEnvio = 1.0; //un dolar por default
        total = calcularSumaSubtotales(detallesPedido) + costoEnvio;
        return total;
    }

    public void regresarAPantallaInicial(View view) {
        Intent intent = new Intent(this, SeleccionarProductoActivity.class);
        startActivity(intent);
    }

}
