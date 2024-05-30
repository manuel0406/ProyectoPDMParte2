package sv.edu.ues.fia.telollevoya.pago;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.DetallePedido;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.MisPedidosActivity;

public class FacturaActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private AdaptadorProductosAPagar adaptador;
    private ControlBD helper;
    private Pedido pedido;
    TextView txNumFactura;
    TextView txDireccion;
    TextView txFechaEntrega;
    TextView txMetodoPago;
    TextView txSumaDeSubtotales;
    Double sumaSubtotales = 0.0;
    TextView txcostoEnvio;
    TextView txTotal;
    final static Double costoEnvio = 1.0; //$1 el costo de envío por ahora
    String urlDetallesPedido = "https://telollevoya.000webhostapp.com/Pago/obtener_detalles_pedido.php?idPedido=";
    String urlSumaSubtotales = "https://telollevoya.000webhostapp.com/Pago/calcular_suma_subtotales.php?idPedido=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_factura);

        helper = new ControlBD(this);
        //se recibe el pedido desde la pantalla anterior
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");

        //helper.abrir(); //abriendo la base de datos
        //pedido = new Pedido();// solo necesito un pedido para hacer pruebas del modulo de Pago
        List<DetallePedido> detallesPedido = new ArrayList<>();
        //detallesPedido = helper.consultarDetallesPedido(pedido.getId());


        //detallesPedido = ControladorSevicio.obtenerDetallesPedidoPorId(urlDetallesPedido + pedido.getId(), this);
        detallesPedido = ControladorSevicio.obtenerDetallesPedidoPorId(urlDetallesPedido + 3, this);

        txDireccion = (TextView) findViewById(R.id.txtDireccion);
        txDireccion.setText(pedido.getUbicacion().toString());

        txNumFactura = (TextView) findViewById(R.id.txtNumeroDePedido);
        txNumFactura.setText(String.valueOf(pedido.getFactura().getId()));

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

        txSumaDeSubtotales = (TextView) findViewById(R.id.txtSubtotal);
        //double suma = calcularSumaSubtotales(detallesPedido);

        String url = urlSumaSubtotales + 3;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Llamada al servicio en segundo plano
                sumaSubtotales = ControladorSevicio.calcularSumaSubtotales(url, FacturaActivity.this);

                // Actualización de la UI en el hilo principal
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(FacturaActivity.this, "La suma de los subtotales es: " + sumaSubtotales, Toast.LENGTH_LONG).show();
                        txSumaDeSubtotales.setText(String.format("$%.2f", sumaSubtotales));

                        // Calcular el total una vez que sumaSubtotales haya sido asignado
                        double sumaTotal = sumaSubtotales + costoEnvio;
                        txTotal.setText(String.format("$%.2f", sumaTotal));
                    }
                });
            }
        });



        txcostoEnvio = (TextView) findViewById(R.id.txtCostoEnvio);
        txcostoEnvio.setText(String.format("$%.2f",costoEnvio));

        txTotal = (TextView) findViewById(R.id.txtTotalPagar);
        //double sumaTotal = calcularTotal(detallesPedido); //Metodo para obtener total a pagar
        //double sumaTotal = pedido.getTotalAPagar(); //Metodo alternativo para obtener el total a pagar.
        //double sumaTotal = pedido.getFactura().getTotalPagado(); Otra alternativa para obtener el total a pagar.

        txMetodoPago = (TextView) findViewById(R.id.txtMetodoPago);
        txMetodoPago.setText(pedido.getFactura().getMetodoPago().toString());


    }

//    //Métodos que ya no usaré
//    private double calcularSumaSubtotales(List<DetallePedido> detallesPedido) {
//        double suma = 0.0;
//        for (DetallePedido dp : detallesPedido) {
//            suma += dp.getSubTotal();
//        }
//        return suma;
//    }
//
//    private double calcularTotal(List<DetallePedido> detallesPedido) {
//        double total = 0.0;
//        double costoEnvio = 1.0; //un dolar por default
//        total = calcularSumaSubtotales(detallesPedido) + costoEnvio;
//        return total;
//    }

    public void regresarAPantallaInicial(View view) {
        Intent intent = new Intent(this, MisPedidosActivity.class);
        startActivity(intent);
    }

}
