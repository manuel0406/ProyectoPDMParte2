package sv.edu.ues.fia.telollevoya.pago;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.DetallePedido;
import sv.edu.ues.fia.telollevoya.Factura;
import sv.edu.ues.fia.telollevoya.Pedido;
import sv.edu.ues.fia.telollevoya.R;

public class SeleccionPagoActivity extends AppCompatActivity {

    private EditText numeroTarjeta;
    private EditText fechaExpiracion;
    private EditText cvc;
    private EditText nombreTitular;
    private EditText correo;
    private ConstraintLayout layoutFormularioTarjetaCredito;
    private ConstraintLayout layoutFormularioBitcoin;
    private ConstraintLayout layoutFormularioCorreo;
    private ControlBD controlBD;
    private MetodoPago metodoPago;
    private boolean tipoPagoSeleccionado;
    private ImageView codigoQR;
    private TextView txtDireccionBitcoin;
    private Button btnCopiarDireccion;
    Pedido pedido;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_seleccion);

        controlBD = new ControlBD(SeleccionPagoActivity.this);
        layoutFormularioTarjetaCredito = findViewById(R.id.layoutFormularioTarjetaCredito);
        layoutFormularioTarjetaCredito.setVisibility(View.GONE);
        layoutFormularioBitcoin = findViewById(R.id.layoutFormularioBitcoin);
        layoutFormularioBitcoin.setVisibility(View.GONE);
        layoutFormularioCorreo = findViewById(R.id.layoutFormularioCorreo);
        codigoQR = findViewById(R.id.imgCodigoQR);
        txtDireccionBitcoin = findViewById(R.id.txtDireccionBitcoin);
        btnCopiarDireccion = findViewById(R.id.btnCopiarDireccion);

        metodoPago = new MetodoPago();
        pedido = new Pedido();//AQUI SE ASIGNARA EL PEDIDO QUE SE TRAE DE LA VISTA ANTERIOR
        tipoPagoSeleccionado = false; //cambiará a true cuando se seleccione "efectivo" o "tarjeta"
        codigoQR.setImageResource(R.drawable.codigo_qr);

        pedido = (Pedido) getIntent().getExtras().getSerializable("pedido");

        RadioGroup radioGroupMetodoPago = findViewById(R.id.radioGroupMetodoPago);
        radioGroupMetodoPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonEfectivo) {
                    layoutFormularioTarjetaCredito.setVisibility(View.GONE);
                    layoutFormularioBitcoin.setVisibility(View.GONE);
                    layoutFormularioCorreo.setTranslationY(0);
                    metodoPago.setTipoPago("Efectivo");
                } else if (checkedId == R.id.radioButtonTarjeta) {
                    layoutFormularioTarjetaCredito.setVisibility(View.VISIBLE);
                    layoutFormularioBitcoin.setVisibility(View.GONE);
                    layoutFormularioCorreo.setTranslationY(900);
                    metodoPago.setTipoPago("Tarjeta");
                } else if(checkedId == R.id.radioButtonBitcoin){
                    layoutFormularioTarjetaCredito.setVisibility(View.GONE);
                    layoutFormularioBitcoin.setVisibility(View.VISIBLE);
                    layoutFormularioCorreo.setTranslationY(0);
                    metodoPago.setTipoPago("Bitcoin");
                }
                tipoPagoSeleccionado = true; //Ya se seleccionó un tipo de pago
            }
        });

        numeroTarjeta = (EditText) findViewById(R.id.editNumeroTarjeta);
        fechaExpiracion = (EditText) findViewById(R.id.editFechaExpiracion);
        cvc = (EditText) findViewById(R.id.editCVC);
        nombreTitular = (EditText) findViewById(R.id.editNombreTitular);

        correo = (EditText) findViewById(R.id.editCorreo);

        //Manejo de Formatos para el campo que permite ingresar el numero de tarjeta de credito o debito
        numeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!TextUtils.isEmpty(input)) {
                    // Eliminar guiones actuales para evitar duplicados
                    String cleanString = input.replace("-", "");
                    // Agregando guiones cada 4 dígitos
                    StringBuilder formattedString = new StringBuilder();
                    int index = 0;
                    while (index < cleanString.length()) {
                        formattedString.append(cleanString, index, Math.min(index + 4, cleanString.length()));
                        if (index + 4 < cleanString.length()) {
                            formattedString.append("-");
                        }
                        index += 4;
                    }
                    // Actualizar el texto del EditText
                    numeroTarjeta.removeTextChangedListener(this);
                    numeroTarjeta.setText(formattedString.toString());
                    numeroTarjeta.setSelection(formattedString.length());
                    numeroTarjeta.addTextChangedListener(this);
                }
            }
        });

        //Manejo de formato para el campo que permite poner la fecha de expiracion de la tarjeta de credito/debito
        fechaExpiracion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!TextUtils.isEmpty(input) && input.length() == 2 && !input.contains("/")) {
                    // Si el usuario ingresa los dos primeros dígitos del mes, automáticamente agrega una barra ("/")
                    fechaExpiracion.setText(input + "/");
                    fechaExpiracion.setSelection(fechaExpiracion.getText().length());
                }
            }
        });


    }

    public void procesarPago(View view) {
        String numeroTarjetaStr = numeroTarjeta.getText().toString();
        String fechaExpiracionStr = fechaExpiracion.getText().toString();
        String cvcStr = cvc.getText().toString();
        String nombreTitularStr = nombreTitular.getText().toString();
        String correoStr = correo.getText().toString();

        boolean datosValidos = validarDatos(numeroTarjetaStr, fechaExpiracionStr, cvcStr, nombreTitularStr);

        if (tipoPagoSeleccionado == false) {
            Toast.makeText(this, "Por favor seleccione una de las opciones de Pago", Toast.LENGTH_SHORT).show();
        } else if (!validarDatos(numeroTarjetaStr, fechaExpiracionStr, cvcStr, nombreTitularStr)) {
            Toast.makeText(this, "Error, Datos de la tarjeta no son válidos", Toast.LENGTH_SHORT).show();
        } else if(!isValidEmail(correoStr)){
            Toast.makeText(this, "Error, ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
        }else{
            generarFactura(pedido);
            Intent intent = new Intent(this, PagoAprobadoActivity.class);
            intent.putExtra("pedido", (Serializable) pedido);
            startActivity(intent);
        }

    }

    private boolean validarDatos(String numeroTarjeta, String fechaExpiracion, String cvc, String nombreTitular) {
        if (layoutFormularioTarjetaCredito.getVisibility() == View.GONE) {
            return true; // No se requiere validación para efectivo
        } else {
            // Si la tarjeta de crédito está seleccionada, se validan los campos relacionados
            return !numeroTarjeta.isEmpty() && !fechaExpiracion.isEmpty() && !cvc.isEmpty() && !nombreTitular.isEmpty() && numeroTarjeta.length() == 19 && cvc.length() == 3 && fechaExpiracion.length() ==5;
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void generarFactura(Pedido pedido) {
        Factura factura = new Factura();
        factura.setMetodoPago(metodoPago);
        factura.setTotalPagado(pedido.getTotalAPagar());
        factura.setFechaEmision(pedido.getFechaEntrega());

        pedido.setFactura(factura);
        controlBD.abrir();
        controlBD.insertar(factura, pedido);
        controlBD.cerrar();

    }

    public void copiarDireccionBitcoin(View view) {
        String direccionBitcoin = txtDireccionBitcoin.getText().toString().split(": ")[1];
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Dirección Bitcoin", direccionBitcoin);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Dirección Bitcoin copiada al portapapeles", Toast.LENGTH_SHORT).show();
    }



}
