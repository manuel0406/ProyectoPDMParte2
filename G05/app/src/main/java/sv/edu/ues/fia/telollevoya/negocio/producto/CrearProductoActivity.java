package sv.edu.ues.fia.telollevoya.negocio.producto;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;

public class CrearProductoActivity extends AppCompatActivity {
    ControlBD helper;
    EditText editProductoNombre, editProductoPrecio, editProductoDescripción;
    Switch switchExistencias;
    RadioGroup radioGroupTipo;
    int idNegocioRecuperado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_producto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        helper = new ControlBD(this);
        editProductoNombre = (EditText) findViewById(R.id.editProductoNombre);
        editProductoPrecio = (EditText) findViewById(R.id.editProductoPrecio);
        editProductoDescripción = (EditText) findViewById(R.id.editProductoDescripción);
        switchExistencias = (Switch) findViewById(R.id.switchExistencias);
        radioGroupTipo = (RadioGroup) findViewById(R.id.radioGroupTipo);
        editProductoPrecio.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        idNegocioRecuperado = getIntent().getIntExtra("idNegocioRecuperado", 5);
        Toast.makeText(this, "idNegocio " + idNegocioRecuperado, Toast.LENGTH_LONG).show();

    }

    public void insertarProducto(View v) {
        // Verificar si los campos requeridos están vacíos
        if (editProductoNombre.getText().toString().isEmpty() || editProductoPrecio.getText().toString().isEmpty() || editProductoDescripción.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = editProductoNombre.getText().toString();
        float precio = Float.parseFloat(editProductoPrecio.getText().toString());
        String descripcion = editProductoDescripción.getText().toString();
        boolean existencia = switchExistencias.isChecked();

        // Obtener el texto del RadioButton seleccionado
        int selectedRadioButtonId = radioGroupTipo.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String tipo = selectedRadioButton.getText().toString();

        Product producto = new Product();
        producto.setIdNegocio(idNegocioRecuperado);//Despues debo ingresar un id dinamico
        producto.setNombreProducto(nombre);
        producto.setPrecioProducto(precio);
        producto.setDescripcionProducto(descripcion);
        producto.setExistenciaProducto(existencia);
        producto.setTipoProducto(tipo);

        helper.abrir();
        String mensaje = helper.insertar(producto);
        helper.cerrar();
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}