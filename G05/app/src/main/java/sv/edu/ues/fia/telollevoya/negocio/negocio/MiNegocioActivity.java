package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.R;

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
        new ObtenerRestaurantesTask().execute();

        /*helper = new ControlBD(this);
        listaArrayNegocios = new ArrayList<Restaurant>();
        helper.abrir();
        ListaNegociosAdapter adapter = new ListaNegociosAdapter(helper.obtenernegociosPorAdministrador(idAdmin));
        helper.cerrar();
        listaNegocios.setAdapter(adapter);*/
    }


    private class ObtenerRestaurantesTask extends AsyncTask<Void, Void, ArrayList<Restaurant>> {
        @Override
        protected ArrayList<Restaurant> doInBackground(Void... voids) {
            String url = "https://telollevoya.000webhostapp.com/Negocio/obtener_negocios_por_administrador.php?idAdministrador=1"; // Reemplaza "URL_DEL_SERVICIO" con la URL real del servicio
            return ControladorSevicio.obtenerRestaurantesDesdeServicio(url, MiNegocioActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> listaRestaurantes) {
            // Crear el adaptador y establecerlo en la lista de negocios
            ListaNegociosAdapter adapter = new ListaNegociosAdapter(listaRestaurantes);
            listaNegocios.setAdapter(adapter);
        }
    }

    public void irNuevoNegocio(View v) {
        // Crear un Intent para iniciar la actividad "RegistrarseActivity"
        Intent intent = new Intent(this, CrearNegocioActivity.class);
        intent.putExtra("idAdministradorRecuperado", idAdmin);
        startActivity(intent);
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaNegocios();
    }

    private void actualizarListaNegocios() {
        String idAdministrador = getIntent().getStringExtra("idAdministrador");
        int idAdmin = Integer.parseInt(idAdministrador);
        new ObtenerRestaurantesTask().execute();

        /*
        helper = new ControlBD(this);
        helper.abrir();
        ListaNegociosAdapter adapter = new ListaNegociosAdapter(helper.obtenernegociosPorAdministrador(idAdmin));
        helper.cerrar();
        listaNegocios.setAdapter(adapter);*/
    }
}