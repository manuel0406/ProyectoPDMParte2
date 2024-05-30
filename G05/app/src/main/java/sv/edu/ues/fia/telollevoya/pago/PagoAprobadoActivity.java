package sv.edu.ues.fia.telollevoya.pago;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.Factura;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.MisPedidosActivity;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.SeleccionarProductoActivity;

public class PagoAprobadoActivity extends AppCompatActivity {
    Pedido pedido;
    //Factura factura;
    private String urlFactura = "https://telollevoya.000webhostapp.com/Pago/obtener_factura_por_id.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_aprobado);
        pedido = (Pedido) getIntent().getSerializableExtra("pedido");

//        //Código para hacer pruebas nada más
//        factura = pedido.getFactura();
//        Toast.makeText(this, "id Factura: "+ factura.getId(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "tipoPago: "+ factura.getMetodoPago().getTipoPago(), Toast.LENGTH_SHORT).show();
//        consultarFactura();
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

//    private void consultarFactura() {
//        int idFactura = factura.getId();
//        String urlConsulta = urlFactura + "?idFactura=" + idFactura;
//        Factura facturaConsultada = ControladorSevicio.obtenerFacturaPorId(urlConsulta, this);
//
//        if (facturaConsultada != null) {
//            mostrarDatosFactura(facturaConsultada);
//        } else {
//            Toast.makeText(this, "Error al obtener la factura", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void mostrarDatosFactura(Factura factura) {
//        String mensaje = "ID Factura: " + factura.getId() + "\n" ;
//               // "Total Pagado: " + factura.getTotalPagado() + "\n" +
//               // "Fecha Emisión: " + factura.getFechaEmision() + "\n";
//
//        // Si tienes el tipo de pago disponible en la factura
//        if (factura.getMetodoPago() != null) {
//            mensaje += //"IdPago: "+ factura.getMetodoPago().getId()+
//                    "Tipo de Pago: " + factura.getMetodoPago().getTipoPago() + "\n";
//        }
//
//        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
//    }

}

