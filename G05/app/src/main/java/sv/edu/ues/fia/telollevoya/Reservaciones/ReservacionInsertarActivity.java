package sv.edu.ues.fia.telollevoya.Reservaciones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;

import sv.edu.ues.fia.telollevoya.Producto;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservacion;
@SuppressLint("NewApi")
public class ReservacionInsertarActivity extends AppCompatActivity implements ControladorSevicio.ReservacionInsertListener {

    private LinearLayout contenedorElementos;
    ArrayList<String> listaSpinner= new ArrayList<>();
    ArrayList<Integer> listaCantidad= new ArrayList<Integer>();
    ControlBD helper;
   // private int contadorElementos=0;
   List<Producto> listaproductos;
    EditText edtFecha;
    EditText  edtTotal;
    EditText edtPagoMinimo;
    EditText edtDescripcion;
    EditText edtAnticipo;
    EditText edtHora;
    Button btnPagar;
    double total, precio,cantidad;
    double pagoMinimo=0;
    private  int dia, mes, ano, hora, minutos;
    String idCliente;
    ScrollView scrollView;
    private int contadorElementos = 0;
    int idReservacionR;
    private String urlReservacion="https://telollevoya.000webhostapp.com/Reservaciones/reservacion_insert.php";
    private String urldetalle="https://telollevoya.000webhostapp.com/Reservaciones/reservacion_detalle_insertar.php";
    private  String urlProducto="https://telollevoya.000webhostapp.com/Reservaciones/productos_query.php";
    @Override
    public void onReservacionInserted(int idReservacion) {
        idReservacionR = idReservacion;
        Log.v("idReservacionInt", String.valueOf(idReservacionR));
        insertarDetallePedido(idReservacionR);
        Intent intent = new Intent(ReservacionInsertarActivity.this, ReservacionesConsultarActivity.class);
        intent.putExtra("idCliente", idCliente);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_reservacion_insertar);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        listaproductos= new ArrayList<Producto>();

        contenedorElementos = findViewById(R.id.contenedorElementos);
        FloatingActionButton agregarBoton = findViewById(R.id.floatingActionButton);
        scrollView = findViewById(R.id.scrollView2);

        helper= new ControlBD(this);
        edtFecha= (EditText) findViewById(R.id.editTextDateReservacion);
        edtHora=(EditText) findViewById(R.id.edtHora);
        edtTotal = (EditText) findViewById(R.id.editTextTotal);
        edtPagoMinimo= (EditText) findViewById(R.id.editTextPagoMinimo);
        edtDescripcion =(EditText) findViewById(R.id.editTextTextMultiLine);
        edtAnticipo= (EditText) findViewById(R.id.editTextAnticipo);


        Intent intent= getIntent();
        idCliente= intent.getStringExtra("idCliente");
        Toast.makeText(this, idCliente,Toast.LENGTH_SHORT).show();



        agregarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agregarElementos();
            }
        });

    }


    public void fecha(View v) {

        if (v==edtFecha){
            final Calendar c=Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    edtFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, ano, mes, dia);
            datePickerDialog.show();;

        }
        if (v== edtHora){
            final Calendar c= Calendar.getInstance();
            hora= c.get(Calendar.HOUR_OF_DAY);
            minutos= c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    edtHora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();

        }
    }

    public void limpiarTexto(){
        edtFecha.setText("");
        edtHora.setText("");
        edtTotal.setText("");
        edtPagoMinimo.setText("");
        edtAnticipo.setText("");
        edtDescripcion.setText("");
    }
    public void insertarReservacion(View v){


        String regInsertados;
        String fechaEntrega= edtFecha.getText().toString();
        String horaEntrega=edtHora.getText().toString();
        double totalReservacion =Double.parseDouble( edtTotal.getText().toString());
        double Anticipo= Double.parseDouble(edtAnticipo.getText().toString());
        double pagoMinimo= Double.parseDouble(edtPagoMinimo.getText().toString());
        String descripcionRe= edtDescripcion.getText().toString();

        String fechaEntregar="";
        SimpleDateFormat dateFormatEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dateFormatSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            java.util.Date fecha = dateFormatEntrada.parse(fechaEntrega);
            fechaEntregar = dateFormatSalida.format(fecha);

        } catch (ParseException e) {
            e.printStackTrace();
            // Manejar el error de análisis de fecha aquí
        }

        if (Anticipo >= pagoMinimo && Anticipo <= totalReservacion){

            Reservacion reservacion= new Reservacion();
            reservacion.setIdCliente(Integer.parseInt(idCliente));
            reservacion.setDescripcionReservacion(descripcionRe);
            reservacion.setAnticipoReservacion(Anticipo);
            reservacion.setMontoPediente(totalReservacion-Anticipo);
            reservacion.setFechaEntregaR(fechaEntrega);
            reservacion.setHoraEntrega(horaEntrega);
            reservacion.setTotalRerservacion(totalReservacion);

            JSONObject datosReservacion = new JSONObject();

            String idClienteCodificado = "",descripcionCodificado="",anticipoCodificado="",montoPendienteCodificado="",
                    fechaEntregaCodificado="",horaEntegaCodificado="",totalReservacionCodficado="";
        try {
            idClienteCodificado= URLEncoder.encode(String.valueOf(reservacion.getIdCliente()), "UTF-8");
            descripcionCodificado = URLEncoder.encode(reservacion.getDescripcionReservacion(), "UTF-8");
            anticipoCodificado=URLEncoder.encode(String.valueOf(reservacion.getAnticipoReservacion()), "UTF-8");
            montoPendienteCodificado=URLEncoder.encode(String.valueOf(reservacion.getMontoPediente()), "UTF-8");
            fechaEntregaCodificado=fechaEntregar;
            horaEntegaCodificado=URLEncoder.encode(reservacion.getHoraEntrega(), "UTF-8");
            totalReservacionCodficado= URLEncoder.encode(String.valueOf(reservacion.getTotalRerservacion()), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //return;
            //return "Error al codificar el nombre del departamento";
        }

        String url = urlReservacion +"?IDCLIENTE="+idClienteCodificado+ "&DESCRIPCIONRESERVACION=" +
                descripcionCodificado+"&ANTICIPORESERVACION="+anticipoCodificado+"&MONTOPENDIENTE="+
                montoPendienteCodificado+"&FECHAENTREGAR="+fechaEntregaCodificado+"&HORAENTREGAR="+horaEntegaCodificado+
                "&TOTALRESERVACION="+totalReservacionCodficado;
            Toast.makeText(this, url, Toast.LENGTH_LONG).show();
           ControladorSevicio.insertarReservacion(url, this,this);
          // Log.v("idreservacionI",String.valueOf( idReservacionR));


//            helper.abrir();
//          //  regInsertados= helper.insertar(reservacion);
//            helper.cerrar();
//            //Toast.makeText(this, regInsertados,Toast.LENGTH_SHORT).show();


            limpiarTexto();

        }else{
            Toast.makeText(this, "El anticipo debe de ser mayor o igual al 50% del costo total, pero no mayor al total",Toast.LENGTH_SHORT).show();
        }



    }
    public void insertarDetallePedido(int idReservacion) {
        obtenerDatos();

        // Verificar que las listas estén sincronizadas
        if (listaSpinner.size() != listaCantidad.size()) {
            Log.e("insertarDetallePedido", "La cantidad de elementos en listaSpinner y listaCantidad no coinciden");
            return;
        }

        ArrayList<DetallePedidoR> listDetalle = new ArrayList<>();

        for (int i = 1; i < listaproductos.size(); i++) {
            Producto producto = listaproductos.get(i);

            for (int j = 0; j < listaSpinner.size(); j++) {
                String nombre = listaSpinner.get(j);

                if (producto.getNombre().equals(nombre)) {
                    if (j < listaCantidad.size()) {
                        DetallePedidoR detallePedido = new DetallePedidoR();
                        detallePedido.setIdProducto(producto.getId());
                        detallePedido.setIdReservacion(idReservacion);

                        Log.v("dentero del forCantidad", String.valueOf(listaCantidad.get(j)));

                        detallePedido.setCantidadDetalle(listaCantidad.get(j));
                        detallePedido.setSubTotal(producto.getPrecio() * listaCantidad.get(j));
                        listDetalle.add(detallePedido);

                        String idReservacionCodificado = "", idProductoCodificado = "", cantidadDetalleCodificado = "", subdtotalCodificado = "";
                        try {
                            idReservacionCodificado = URLEncoder.encode(String.valueOf(detallePedido.getIdReservacion()), "UTF-8");
                            idProductoCodificado = URLEncoder.encode(String.valueOf(detallePedido.getIdProducto()), "UTF-8");
                            cantidadDetalleCodificado = URLEncoder.encode(String.valueOf(detallePedido.getCantidadDetalle()), "UTF-8");
                            subdtotalCodificado = URLEncoder.encode(String.valueOf(detallePedido.getSubTotal()), "UTF-8");
                            String url = urldetalle + "?IDRESERVACION=" + idReservacionCodificado + "&IDPRODUCTO=" +
                                    idProductoCodificado + "&CANTIDADDETALLE=" + cantidadDetalleCodificado + "&SUBTOTAL=" + subdtotalCodificado;

                            ControladorSevicio.insertarDetalle(url, this);
                            Log.v("Url Detalle", url);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }



                    } else {
                        Log.e("insertarDetallePedido", "Index j out of bounds for listaCantidad: " + j);
                    }
                }
            }
        }


    }

    private void agregarElementos() {
        String productosR = ControladorSevicio.obtenerRepuestaPeticion(urlProducto,this);



        try {
            listaproductos.addAll(ControladorSevicio.obtenerProductos(productosR, this));
            //actualizarListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Crear un nuevo LinearLayout horizontal para contener el Spinner, el EditText y el botón de eliminar
        LinearLayout nuevoLayout = new LinearLayout(this);
        nuevoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        nuevoLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Crear el Spinner y configurar el adaptador
        Spinner nuevoSpinner = new Spinner(this);
        nuevoSpinner.setTag("Spinner_" + contadorElementos);

        LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        nuevoSpinner.setLayoutParams(spinnerLayoutParams);


//        helper.abrir();
//        productos = helper.getProductos();
//        helper.cerrar();

        ArrayList<String> nombreProductos = new ArrayList<>();
        Log.v("UrlConsulta", String.valueOf(listaproductos));
        for (Producto producto : listaproductos) {
           // Log.v("UrlConsultaFor", String.valueOf(producto.isExistencia()));
            if (producto.isExistencia()) {
              //  Log.v("UrlConsultaFor",producto.getNombre());
                nombreProductos.add(producto.getNombre());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombreProductos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nuevoSpinner.setAdapter(adapter);
        nuevoLayout.addView(nuevoSpinner);

        // Crear el EditText de tipo número
        EditText nuevoEditText = new EditText(this);
        nuevoEditText.setTag("EditText_" + contadorElementos);
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        nuevoEditText.setLayoutParams(editTextLayoutParams);
        nuevoEditText.setHint("0");
        nuevoEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        nuevoLayout.addView(nuevoEditText);

        // Crear el botón de eliminar
        ImageButton botonEliminar = new ImageButton(this);
        botonEliminar.setImageResource(R.drawable.borrar);

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Eliminar el conjunto cuando se hace clic en el botón de eliminar
                contenedorElementos.removeView(nuevoLayout);
                contadorElementos--;
               actualizarTotal();
            }
        });
        nuevoLayout.addView(botonEliminar);

        // Agregar un espacio al final del nuevo conjunto
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 0); // Ajustar el margen para el espacio entre conjuntos
        nuevoLayout.setLayoutParams(layoutParams);

        // Agregar el nuevo LinearLayout al contenedor principal
        contenedorElementos.addView(nuevoLayout);
        contadorElementos++;

        // Actualizar el total después de agregar un nuevo conjunto
        actualizarTotal();
    }


    // Método para obtener el texto seleccionado en el Spinner y el valor introducido en el EditText
    private void obtenerDatos() {

        // Iterar sobre los elementos en el contenedor principal
        for (int i = 0; i < contenedorElementos.getChildCount(); i++) {
            View childView = contenedorElementos.getChildAt(i);

            if (childView instanceof LinearLayout) {
                // Obtener el Spinner y el EditText utilizando las etiquetas (tags)
                Spinner spinner = ((LinearLayout) childView).findViewWithTag("Spinner_" + i);
                EditText editText = ((LinearLayout) childView).findViewWithTag("EditText_" + i);

                //Log.v("producto",spinner.getSelectedItem().toString());
              //  Log.v("Cantidad",editText.getText().toString());

                // Obtener los datos seleccionados e introducidos
               listaSpinner.add( spinner.getSelectedItem().toString());
               listaCantidad.add(Integer.parseInt(editText.getText().toString()));


                // Utilizar los datos como desees

            }
        }
        Log.v("obtenerDatos", "Tamaño de listaSpinner: " + listaSpinner.size());
        Log.v("obtenerDatos", "Tamaño de listaCantidad: " + listaCantidad.size());
    }

    private void actualizarTotal() {

        double totalActualizado = 0;
        // Recorrer todos los elementos para calcular el total
        for (int i = 0; i < contenedorElementos.getChildCount(); i++) {
            View childView = contenedorElementos.getChildAt(i);
            if (childView instanceof LinearLayout) {
                Spinner spinner = ((LinearLayout) childView).findViewWithTag("Spinner_" + i);
                EditText editText = ((LinearLayout) childView).findViewWithTag("EditText_" + i);


                if (spinner != null && editText != null) {
                    // Resto del código aquí


                    // Obtener el producto seleccionado
                    String productoSeleccionado = spinner.getSelectedItem().toString();
                    // Obtener la cantidad seleccionada
                    String cantidadString = editText.getText().toString().trim(); // Obtener el texto y eliminar espacios en blanco
                    if (!cantidadString.isEmpty()) { // Verificar si el texto no está vacío
                        int cantidad = Integer.parseInt(cantidadString);
                        // Encontrar el precio del producto seleccionado
                        double precioProducto = 0;
                        for (Producto producto : listaproductos) {
                            if (producto.getNombre().equals(productoSeleccionado)) {
                                precioProducto = producto.getPrecio();
                                break;
                            }
                        }

                        // Sumar al total actualizado
                        totalActualizado += precioProducto * cantidad;
                    }
                }
            }
        }

        // Actualizar los campos de texto del total y el pago mínimo con los nuevos valores calculados
        total = totalActualizado;
        pagoMinimo = total * 0.5;
        edtTotal.setText(String.valueOf(total));
        edtPagoMinimo.setText(String.valueOf(pagoMinimo));
    }



}


