package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.app.Activity;
import android.content.Intent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import sv.edu.ues.fia.telollevoya.*;
import sv.edu.ues.fia.telollevoya.R;

public class EditarMiNegocioActivity extends Activity {
    Spinner spinnerDepartamento, spinnerMunicipio, spinnerDistrito;
    EditText editUbicacionDescripcion, editNegocioTelefono, editNegocioNombre;
    Restaurant negocio;
    Ubicacion ubicacion;
    Distrito distrito;
    Departamento departamento;
    Municipio municipio;
    int idUbicacion, idNegocio;
    ControlBD helper;
    Button btnApertura, btnCierre;
    String horaapertura = "07:00 AM";
    String horacierre = "07:00 AM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mi_negocio);

        helper = new ControlBD(this);
        btnApertura = (Button) findViewById(R.id.btnHoraApertura);
        btnCierre = findViewById(R.id.btnHoraCierre);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);
        spinnerMunicipio = findViewById(R.id.spinnerMunicipio);
        spinnerDistrito = findViewById(R.id.spinnerDistrito);
        editUbicacionDescripcion = findViewById(R.id.editUbicacionDescripcion);
        editNegocioNombre = findViewById(R.id.editNegocioNombre);
        editNegocioTelefono = findViewById(R.id.editNegocioTelefono);
        editNegocioTelefono.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));

        cargarDepartamentos();

        // Listener para el spinner de departamentos
        spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String departamentoSeleccionado = (String) spinnerDepartamento.getSelectedItem();
                cargarMunicipios(departamentoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Listener para el spinner de municipios
        spinnerMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String municipioSeleccionado = (String) spinnerMunicipio.getSelectedItem();
                cargarDistritos(municipioSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intent = getIntent();
        idNegocio = intent.getIntExtra("idNegocio",0);

        //Obtenemos el negocio
        helper.abrir();
        negocio = helper.verNegocio(idNegocio);
        helper.cerrar();

        //Consultamos la descripcion de la ubicacion
        helper.abrir();
        ubicacion = helper.consultarUbicacion(Integer.toString(negocio.getIdUbicacion()));
        helper.cerrar();

        //Consultamos el idDistrito
        helper.abrir();
        distrito = helper.consultarDistritoId(Integer.toString(ubicacion.getDistrito().getIdDistrito()));
        helper.cerrar();

        //Consutamos el idMunicipio
        helper.abrir();
        municipio = helper.consultarMunicipioId(Integer.toString(distrito.getIdMunicipio()));
        helper.cerrar();

        //Consutamos el idDepartamento
        helper.abrir();
        departamento = helper.consultarDepartamentooId(Integer.toString(municipio.getIdDepartamento()));
        helper.cerrar();

        //Marcado el departamento
        ArrayAdapter<String> adapterD = (ArrayAdapter<String>) spinnerDepartamento.getAdapter();
        int index = adapterD.getPosition(departamento.getNombreDepartamento());
        spinnerDepartamento.setSelection(index);

        if (negocio != null){
            editNegocioNombre.setText(negocio.getNombre());
            editNegocioTelefono.setText(negocio.getTelefono());
            idUbicacion = negocio.getIdUbicacion();
            editUbicacionDescripcion.setText(ubicacion.getDescripcion());
        }
    }

    //Actualizando el negocio
    public void actualizarNegocio(View v){
        // Verificar si los campos requeridos están vacíos
        if (editNegocioNombre.getText().toString().isEmpty() || editNegocioTelefono.getText().toString().isEmpty() || editUbicacionDescripcion.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcion = editUbicacionDescripcion.getText().toString();
        String nombre = editNegocioNombre.getText().toString();
        String telefono = editNegocioTelefono.getText().toString();
        String distrito = (String) spinnerDistrito.getSelectedItem();

        Restaurant restaurant = new Restaurant();
        restaurant.setNombre(nombre);
        restaurant.setTelefono(telefono);
        restaurant.setIdNegocio(idNegocio);
        restaurant.setHorarioApertura(horaapertura);
        restaurant.setHorarioCierre(horacierre);
        restaurant.setDescripcionUbicacion(descripcion);

        helper.abrir();
        helper.actualizarUbicacion(restaurant, idUbicacion, distrito);
        String mensaje = helper.actualizarNegocio(restaurant);
        helper.cerrar();
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    public void abrirTimePicker(View v) {
        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            String amPm;
            if (hourOfDay < 12) {
                amPm = "AM";
                if (hourOfDay == 0) {
                    hourOfDay = 12;
                }
            } else {
                amPm = "PM";
                if (hourOfDay > 12) {
                    hourOfDay %= 12;
                }
            }
            horaapertura = String.format(Locale.US, "%02d:%02d %s", hourOfDay, minute, amPm);
            btnApertura.setText("Hora apertura: " + horaapertura);
        }, hora, minuto, false);

        tpd.show();
    }


    public void abrirTimePicker2(View v) {
        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            String amPm;
            if (hourOfDay < 12) {
                amPm = "AM";
                if (hourOfDay == 0) {
                    hourOfDay = 12;
                }
            } else {
                amPm = "PM";
                if (hourOfDay > 12) {
                    hourOfDay %= 12;
                }
            }
            horacierre = String.format(Locale.US, "%02d:%02d %s", hourOfDay, minute, amPm);
            btnCierre.setText("Hora apertura: " + horacierre);
        }, hora, minuto, false);

        tpd.show();
    }


    //Cargando direcciones
    private void cargarDepartamentos() {
        ArrayList<String> listaArrayDepartamentos;

        // Inicializar la base de datos
        helper = new ControlBD(this);
        // Obtener la lista de nombres de departamentos
        helper.abrir();
        listaArrayDepartamentos = helper.obtenerNombresDepartamentos();
        helper.cerrar();
        // Crear un adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaArrayDepartamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Establecer el adaptador en el spinner
        spinnerDepartamento.setAdapter(adapter);
    }

    private void cargarMunicipios(String departamento) {
        ArrayList<String> listaArrayMunicipios;

        // Inicializar la base de datos
        helper = new ControlBD(this);
        // Obtener la lista de nombres de departamentos
        helper.abrir();
        listaArrayMunicipios = helper.obtenerNombresMunicipios(departamento);
        helper.cerrar();
        // Crear un adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaArrayMunicipios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Establecer el adaptador en el spinner
        spinnerMunicipio.setAdapter(adapter);
    }

    private void cargarDistritos(String municipio) {
        ArrayList<String> listaArrayDistritos;

        // Inicializar la base de datos
        helper = new ControlBD(this);
        // Obtener la lista de nombres de departamentos
        helper.abrir();
        listaArrayDistritos = helper.obtenerNombresDistritos(municipio);
        helper.cerrar();
        // Crear un adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaArrayDistritos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Establecer el adaptador en el spinner
        spinnerDistrito.setAdapter(adapter);
    }
}