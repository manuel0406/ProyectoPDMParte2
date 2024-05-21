package sv.edu.ues.fia.telollevoya.seguridad;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import sv.edu.ues.fia.telollevoya.*;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.negocio.negocio.MiNegocioActivity;
import sv.edu.ues.fia.telollevoya.pedidos.cliente.MisPedidosActivity;

public class rolIniciarSesionActivity extends Activity {

    ControlBD helper;
    String correo, contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol_iniciar_sesion);
        helper = new ControlBD(this);
        //Recupero informacion
        Intent intent = getIntent();
        correo = intent.getStringExtra("correo");
        contra = intent.getStringExtra("contraseña");
    }

    public void soyCliente(View v) {

        //Validamos existencia de correo Cliente
        helper.abrir();
        Cliente validadorCliente = helper.consultarCliente(correo);
        helper.cerrar();
        if(validadorCliente == null){
            Toast.makeText(this, "¡Su credencial de cliente no existe!", Toast.LENGTH_LONG).show();
        }
        else{
            //Validamos contraseña
            if(!validadorCliente.getContra().equals(contra)){
                Toast.makeText(this, "¡Su contraseña es incorrecta!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "¡Ya puedes empezar ha hacer tus pedidos!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MisPedidosActivity.class);
                Toast.makeText(this,  validadorCliente.getIdCliente(),Toast.LENGTH_SHORT).show();
                intent.putExtra("idCliente", validadorCliente.getIdCliente());

                startActivity(intent);
            }
        }
    }

    public void soyAdministrador(View v) {

        //Validamos existencia de correo Cliente
        helper.abrir();
        Administrador validadorAdministrador = helper.consultarAdministrador(correo);
        helper.cerrar();
        if(validadorAdministrador == null){
            Toast.makeText(this, "¡Su credencial de administrador no existe!", Toast.LENGTH_LONG).show();
        }
        else{
            //Validamos contraseña
            if(!validadorAdministrador.getContra().equals(contra)){

                Toast.makeText(this, "¡Su contraseña es incorrecta!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "¡Ya puedes empezar ha gestionar tu negocio!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MiNegocioActivity.class);
                intent.putExtra("idAdministrador", validadorAdministrador.getIdAdministrador());
                startActivity(intent);
            }

        }
    }

    public void soyRepartidor(View v) {

        //Validamos existencia de correo Cliente
        helper.abrir();
        Repartidor validadorRepartidor = helper.consultarRepartidor(correo);
        helper.cerrar();
        if(validadorRepartidor == null){
            Toast.makeText(this, "¡Su credencial de repartidor no existe!", Toast.LENGTH_LONG).show();
        }
        else{
            //Validamos contraseña
            if(!validadorRepartidor.getContra().equals(contra)){

                Toast.makeText(this, "¡Su contraseña es incorrecta!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "¡Ya puedes empezar ha repartir!", Toast.LENGTH_LONG).show();
            }

        }
    }
}