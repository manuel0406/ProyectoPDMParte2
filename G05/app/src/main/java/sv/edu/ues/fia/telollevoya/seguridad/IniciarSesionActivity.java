package sv.edu.ues.fia.telollevoya.seguridad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;

public class IniciarSesionActivity extends Activity {

    EditText txtCorreo, txtContra;
    boolean  desdeInicioApp;
    String contra,correo;

    ControlBD BDhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        desdeInicioApp = getIntent().getBooleanExtra("desdeInicioApp", true);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtContra = findViewById(R.id.txtContra);

        BDhelper= new ControlBD(this);
        try{
            Class<?> clase=Class.forName("sv.edu.ues.fia.telollevoya");
            Intent inte = new Intent(this,clase);
            this.startActivity(inte);

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        BDhelper.abrir();
        String tost=BDhelper.llenarBD();
        BDhelper.cerrar();

        if(tost.equals("Guardado correctamente"))
        {
            tost = "¡Bienvenido!";
        }
        else{
            tost = "¡Error en BD!";
        }

        //Validamos si es por inicio de aplicacion
        if(desdeInicioApp)
        {
            Toast.makeText(this, tost, Toast.LENGTH_SHORT).show();
        }

    }

    //----------------------------------------------------------------------------------------------
    // METODO PARA REGISTRARSE YA SEA COMO ADMINISTRADOR, CLIENTE O REPARTIDOR
    //----------------------------------------------------------------------------------------------
    public void irRegistrarse(View v){
        // Crear un Intent para iniciar la actividad "RegistrarseActivity"
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    // METODO PARA INICIAR SESION YA SEA COMO ADMINISTRADOR, CLIENTE O REPARTIDOR
    //----------------------------------------------------------------------------------------------
    public void irRolIniciarSesion(View v){
        //Obtener informacion de los campos
        int banderaCorreo = 0, banderaContra=0;
        correo = txtCorreo.getText().toString().trim();
        contra = txtContra.getText().toString().trim();

        // REGEX para validar correo
        String patronCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        // Compilar el patrón en un objeto Pattern
        Pattern pattern = Pattern.compile(patronCorreo);// Crear un objeto Matcher para aplicar el patrón al correo
        Matcher matcher = pattern.matcher(correo);

        // Validar si el correo cumple con el patrón ---------------------------------
        if (matcher.matches()) {
            banderaCorreo = 1;
        } else {
            txtCorreo.setText("");
            txtCorreo.setHintTextColor(ContextCompat.getColor(this, R.color.error)); // Cambia el color del hint a rojo
            txtCorreo.setHint("¡Correo invalido!"); // Cambia el texto del hint a "FALTA CORREO"
        }

        //Validamos compos de contraseño----------------------------
        if (!contra.isEmpty()) {
            banderaContra = 1;
        } else {
            txtContra.setHintTextColor(ContextCompat.getColor(this, R.color.error)); // Cambia el color del hint a rojo
            txtContra.setHint("¡Contraseña invalido!");
        }

        //VALIDAMOS SI DAMOS EL PASE----------------------------
        if (banderaCorreo==1 && banderaContra==1) {
            Intent intent = new Intent(this, rolIniciarSesionActivity.class);
            // Envio Informacion
            intent.putExtra("correo", correo);
            intent.putExtra("contraseña", contra);
            startActivity(intent);
        }
    }

}