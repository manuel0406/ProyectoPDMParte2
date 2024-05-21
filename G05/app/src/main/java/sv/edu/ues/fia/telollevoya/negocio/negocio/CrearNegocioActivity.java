package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.text.method.DigitsKeyListener;


import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;

public class CrearNegocioActivity extends Activity {
    Spinner spinnerDepartamento, spinnerMunicipio, spinnerDistrito;
    EditText editUbicacionDescripcion, editNegocioTelefono, editNegocioNombre;
    Button btnApertura, btnCierre;
    ControlBD helper;
    String horaapertura = "07:00 AM";
    String horacierre = "07:00 AM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_negocio);

        helper = new ControlBD(this);
        btnApertura = (Button) findViewById(R.id.btnHoraApertura);
        btnCierre = findViewById(R.id.btnHoraCierre);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);
        spinnerMunicipio = findViewById(R.id.spinnerMunicipio);
        spinnerDistrito = findViewById(R.id.spinnerDistrito);
        editUbicacionDescripcion = findViewById(R.id.editUbicacionDescripcion);
        editNegocioNombre = findViewById(R.id.editNegocioNombre);
        editNegocioTelefono = findViewById(R.id.editNegocioTelefono);
        editNegocioTelefono.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
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
    }

    public void insertarNegocio(View v) {
        // Verificar si los campos requeridos están vacíos
        if (editNegocioNombre.getText().toString().isEmpty() || editNegocioTelefono.getText().toString().isEmpty() || editUbicacionDescripcion.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = editNegocioNombre.getText().toString();
        String telefono = editNegocioTelefono.getText().toString();
        String distrito = (String) spinnerDistrito.getSelectedItem();
        String descripcion = editUbicacionDescripcion.getText().toString();
        int idAdministradorRecuperadoRecuperado = getIntent().getIntExtra("idAdministradorRecuperado", 5);

        //Crea el objeto negocio
        Restaurant negocio = new Restaurant();
        negocio.setIdAdministrador(idAdministradorRecuperadoRecuperado);
        negocio.setNombre(nombre);
        negocio.setTelefono(telefono);
        negocio.setHorarioApertura(horaapertura);
        negocio.setHorarioCierre(horacierre);
        negocio.setDescripcionUbicacion(descripcion);

        helper.abrir();
        int mensaje = helper.insertarUbicacion1(negocio, distrito);
        negocio.setIdUbicacion(mensaje);
        String mensaje2 = helper.insertarNegocio(negocio);
        helper.cerrar();
        Toast.makeText(this, mensaje2, Toast.LENGTH_SHORT).show();

        //Llamamos a mi negocio
        String idAdministrador = Integer.toString(idAdministradorRecuperadoRecuperado);
        Intent intent = new Intent(this, MiNegocioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("idAdministrador", idAdministrador);
        startActivity(intent);
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