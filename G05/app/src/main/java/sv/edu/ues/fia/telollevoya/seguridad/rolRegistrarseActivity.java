package sv.edu.ues.fia.telollevoya.seguridad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.*;

public class rolRegistrarseActivity extends AppCompatActivity {

    ControlBD helper;

    //Variables recuperadas
    String correo, nombre, apellido, contra, sexo, nacimiento;
    ImageView imgCliente, imgAdministrador, imgRepartidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol_registrarse);
        helper = new ControlBD(this);


        //Obtenemos la imagen
        imgCliente = findViewById(R.id.imgCliente);
        imgAdministrador = findViewById(R.id.imgAdministrador);
        imgRepartidor = findViewById(R.id.imgRepartidor);

        //Recupero informacion
        Intent intent = getIntent();
        correo = intent.getStringExtra("correo");
        nombre = intent.getStringExtra("nombre");
        apellido = intent.getStringExtra("apellido");
        contra = intent.getStringExtra("contraseña");
        sexo = intent.getStringExtra("sexo");
        nacimiento = intent.getStringExtra("nacimiento");

    }

    //Se asigna el rol cliente
    public void soyCliente(View v) {
        String regInsertados;
        int banderaExistencia = 0;

        //Validamos existencia de correo Cliente ya creador
        helper.abrir();
        Cliente validadorCliente = helper.consultarCliente(correo);
        helper.cerrar();
        if(validadorCliente == null){
            Toast.makeText(this, "Creando nuevo cliente...", Toast.LENGTH_LONG).show();
            //Insersion a tabla cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setSexo(sexo);
            cliente.setCorreo(correo);
            cliente.setContra(contra);
            cliente.setNacimiento(nacimiento);
            helper.abrir();
            regInsertados = helper.insertar(cliente);
            validadorCliente = helper.consultarCliente(correo);
            helper.cerrar();
        }
        else{
            Toast.makeText(this, "¡Ya existe el cliente!", Toast.LENGTH_LONG).show();
            banderaExistencia = 1;
        }

        //Validamos existencia de correo Usuario ya creado
        helper.abrir();
        Usuario validadorUsuario = helper.consultarUsuario(correo);
        helper.cerrar();
        if(validadorUsuario == null){
            //Insersion a tabla usuario
            Usuario usuario = new Usuario();
            usuario.setClave(contra);
            usuario.setNombreUsuario(correo);
            helper.abrir();
            regInsertados = helper.insertar(usuario);
            helper.cerrar();
            Toast.makeText(this, "Creando nuevo usuario...", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "¡Ya existe el usuario!", Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent(banderaExistencia, validadorCliente);
        startActivity(intent);
    }

    //Validamos para donde nos vamos
    @NonNull
    private Intent getIntent(int banderaExistencia, Cliente validadorCliente) {
        Intent intent;

        if (banderaExistencia == 0){
            //Me voy a registrar mi Ubicacion
            intent = new Intent(this, registrarUbicacionActivity.class);
            intent.putExtra("idCliente", validadorCliente.getIdCliente());
        }
        else{
            //Me voy a iniciar sesion
            intent = new Intent(this, IniciarSesionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("desdeInicioApp", false);
        }
        return intent;
    }

    //Se asigna el rol administrador
    public void soyAdministrador(View v){
        String regInsertados;

        //Validamos existencia de correo Administrador ya creado
        helper.abrir();
        Administrador validadorAdministrador = helper.consultarAdministrador(correo);
        helper.cerrar();
        if(validadorAdministrador == null){
            //Insersion a tabla cliente
            Administrador administrador = new Administrador();
            administrador.setNombre(nombre);
            administrador.setApellido(apellido);
            administrador.setSexo(sexo);
            administrador.setCorreo(correo);
            administrador.setContra(contra);
            administrador.setNacimiento(nacimiento);
            helper.abrir();
            regInsertados = helper.insertar(administrador);
            helper.cerrar();
            Toast.makeText(this, "Creando nuevo administrador...", Toast.LENGTH_LONG).show();

        } else{
            Toast.makeText(this, "¡Ya existe el administrador!", Toast.LENGTH_LONG).show();
        }

        //Validamos existencia de correo Usuario ya creado
        helper.abrir();
        Usuario validadorUsuario = helper.consultarUsuario(correo);
        helper.cerrar();
        if(validadorUsuario == null){
            //Insersion a tabla usuario
            Usuario usuario = new Usuario();
            usuario.setClave(contra);
            usuario.setNombreUsuario(correo);
            helper.abrir();
            regInsertados = helper.insertar(usuario);
            helper.cerrar();
            Toast.makeText(this, "Creando nuevo usuario...", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "¡Ya existe el usuario!", Toast.LENGTH_LONG).show();
        }

        //Me voy a iniciar sesion
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("desdeInicioApp", false);
        startActivity(intent);
    }

    //Se asigna el rol repartidor
    public void soyRepartidor(View v){
        String regInsertados;

        //Validamos existencia de correo Reprtidor ya creado
        helper.abrir();
        Repartidor validadorRepartidor = helper.consultarRepartidor(correo);
        helper.cerrar();
        if(validadorRepartidor == null){
            //Insersion a tabla cliente
            Repartidor repartidor = new Repartidor();
            repartidor.setNombre(nombre);
            repartidor.setApellido(apellido);
            repartidor.setSexo(sexo);
            repartidor.setCorreo(correo);
            repartidor.setContra(contra);
            repartidor.setNacimiento(nacimiento);
            helper.abrir();
            regInsertados = helper.insertar(repartidor);
            helper.cerrar();
            Toast.makeText(this, "Creando nuevo repartidor...", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, "¡Ya existe el repartidor!", Toast.LENGTH_LONG).show();
        }

        //Validamos existencia de correo Usuario ya creado
        helper.abrir();
        Usuario validadorUsuario = helper.consultarUsuario(correo);
        helper.cerrar();
        if(validadorUsuario == null){
            //Insersion a tabla usuario
            Usuario usuario = new Usuario();
            usuario.setClave(contra);
            usuario.setNombreUsuario(correo);
            helper.abrir();
            regInsertados = helper.insertar(usuario);
            helper.cerrar();
            Toast.makeText(this, "Creando nuevo usuario...", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "¡Ya existe el usuario!", Toast.LENGTH_LONG).show();
        }

        //Me voy a iniciar sesion
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("desdeInicioApp", false);
        startActivity(intent);
    }
}