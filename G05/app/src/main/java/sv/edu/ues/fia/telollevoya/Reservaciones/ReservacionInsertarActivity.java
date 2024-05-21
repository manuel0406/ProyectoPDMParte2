package sv.edu.ues.fia.telollevoya.Reservaciones;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.DetallePedido;
import sv.edu.ues.fia.telollevoya.Producto;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservacion;

public class ReservacionInsertarActivity extends Activity {

    private LinearLayout contenedorElementos;
    ArrayList<String> listaSpinner= new ArrayList<>();
    ArrayList<Integer> listaCantidad= new ArrayList<Integer>();
    ControlBD helper;
   // private int contadorElementos=0;
   ArrayList<Producto> productos= new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservacion_insertar);
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
            public void onClick(View v) { agregarElementos();
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
        if (Anticipo >= pagoMinimo && Anticipo <= totalReservacion){

            Reservacion reservacion= new Reservacion();
            reservacion.setIdCliente(Integer.parseInt( idCliente));
            reservacion.setDescripcionReservacion(descripcionRe);
            reservacion.setAnticipoReservacion(Anticipo);
            reservacion.setMontoPediente(totalReservacion-Anticipo);
            reservacion.setFechaEntregaR(fechaEntrega);
            reservacion.setHoraEntrega(horaEntrega);
            reservacion.setTotalRerservacion(totalReservacion);

            helper.abrir();
            regInsertados= helper.insertar(reservacion);
            helper.cerrar();
            //Toast.makeText(this, regInsertados,Toast.LENGTH_SHORT).show();

            insertarDetallePedido(Integer.parseInt(regInsertados));
            limpiarTexto();
            Intent intent = new Intent(ReservacionInsertarActivity.this, ReservacionesConsultarActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "El anticipo debe de ser mayor o igual al 50% del costo total, pero no mayor al total",Toast.LENGTH_SHORT).show();
        }



    }
    public void insertarDetallePedido(int idReservacion){
        obtenerDatos();
//        int idReservacion=0;
//        helper.abrir();
//        idReservacion= helper.ultimaReservacion();
//        helper.cerrar();

       int i=0;

        ArrayList<DetallePedidoR> listDetalle= new ArrayList<>();

        for (Producto producto:   productos  ) {

            for (String nombre: listaSpinner){

                if (producto.getNombre().equals(nombre)){

                    DetallePedidoR detallePedido = new DetallePedidoR();

                    detallePedido.setIdProducto(producto.getId());
                    detallePedido.setIdReservacion(idReservacion);
                    detallePedido.setCantidadDetalle(listaCantidad.get(i));
                    detallePedido.setSubTotal(producto.getPrecio() * listaCantidad.get(i));
                    listDetalle.add(detallePedido);
                    i++;
                }
            }

        }

        String regInsertados;

        helper.abrir();
        regInsertados= helper.insertar(listDetalle);
        helper.cerrar();
        Toast.makeText(this, regInsertados,Toast.LENGTH_SHORT).show();

    }


    private void agregarElementos() {
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

        helper.abrir();
        productos = helper.getProductos();
        helper.cerrar();
        ArrayList<String> nombreProductos = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.isExistencia()) {
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

                // Obtener los datos seleccionados e introducidos
               listaSpinner.add( spinner.getSelectedItem().toString());
               listaCantidad.add(Integer.parseInt( editText.getText().toString()));


                // Utilizar los datos como desees
            }
        }
    }

    private void actualizarTotal() {
        double totalActualizado = 0;
        // Recorrer todos los elementos para calcular el total
        for (int i = 0; i < contenedorElementos.getChildCount(); i++) {
            View childView = contenedorElementos.getChildAt(i);
            if (childView instanceof LinearLayout) {
                Spinner spinner = ((LinearLayout) childView).findViewWithTag("Spinner_" + i);
                EditText editText = ((LinearLayout) childView).findViewWithTag("EditText_" + i);

                // Obtener el producto seleccionado
                String productoSeleccionado = spinner.getSelectedItem().toString();
                // Obtener la cantidad seleccionada
                String cantidadString = editText.getText().toString().trim(); // Obtener el texto y eliminar espacios en blanco
                if (!cantidadString.isEmpty()) { // Verificar si el texto no está vacío
                    int cantidad = Integer.parseInt(cantidadString);
                    // Encontrar el precio del producto seleccionado
                    double precioProducto = 0;
                    for (Producto producto : productos) {
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

        // Actualizar los campos de texto del total y el pago mínimo con los nuevos valores calculados
        total = totalActualizado;
        pagoMinimo = total * 0.5;
        edtTotal.setText(String.valueOf(total));
        edtPagoMinimo.setText(String.valueOf(pagoMinimo));
    }


}


