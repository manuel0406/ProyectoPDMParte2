package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.seguridad.IniciarSesionActivity;

public class MiNegocioActivity extends Activity {
    RecyclerView listaNegocios;
    ArrayList<Restaurant> listaArrayNegocios;
    ControlBD helper;
    String idAdministrador;
    int idAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_negocio);

        Intent intent = getIntent();
        idAdministrador = intent.getStringExtra("idAdministrador");
        idAdmin = Integer.parseInt(idAdministrador);

        listaNegocios = findViewById(R.id.rvListaNegocios);
        listaNegocios.setLayoutManager(new LinearLayoutManager(this));

        helper = new ControlBD(this);
        listaArrayNegocios = new ArrayList<Restaurant>();

        helper.abrir();
        ListaNegociosAdapter adapter = new ListaNegociosAdapter(helper.obtenernegociosPorAdministrador(idAdmin));
        helper.cerrar();
        listaNegocios.setAdapter(adapter);
    }

    public void irNuevoNegocio(View v) {
        // Crear un Intent para iniciar la actividad "RegistrarseActivity"
        Intent intent = new Intent(this, CrearNegocioActivity.class);
        intent.putExtra("idAdministradorRecuperado", idAdmin);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //                          METODO PARA CERRAR SESION
    //----------------------------------------------------------------------------------------------
    public void cerrarSesion(View v) {

        //Limpio base de usuarios disponibles
        helper = new ControlBD(this);
        helper.abrir();
        helper.LimpiarUsuario();
        helper.cerrar();
        Toast.makeText(this, "Vuelve pronto", Toast.LENGTH_SHORT).show();

        // Crear un Intent para iniciar sesion "Iniciar Sesion"
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        intent.putExtra("desdeInicioApp", false);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaNegocios();
    }

    private void actualizarListaNegocios() {
        String idAdministrador = getIntent().getStringExtra("idAdministrador");
        int idAdmin = Integer.parseInt(idAdministrador);
        helper = new ControlBD(this);
        helper.abrir();
        ListaNegociosAdapter adapter = new ListaNegociosAdapter(helper.obtenernegociosPorAdministrador(idAdmin));
        helper.cerrar();
        listaNegocios.setAdapter(adapter);
    }

}