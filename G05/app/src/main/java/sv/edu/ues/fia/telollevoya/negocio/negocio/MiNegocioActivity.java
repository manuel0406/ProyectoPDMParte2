package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.R;

public class MiNegocioActivity extends Activity {
    RecyclerView listaNegocios;
    String idAdministrador;
    int idAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_negocio);

        Intent intent = getIntent();
        idAdministrador = intent.getStringExtra("idAdministrador");
        idAdmin = Integer.parseInt(idAdministrador);
        idAdmin = 1;

        listaNegocios = findViewById(R.id.rvListaNegocios);
        listaNegocios.setLayoutManager(new LinearLayoutManager(this));
        String url = "https://telollevoya.000webhostapp.com/Negocio/obtener_negocios_por_administrador.php?idAdministrador=" + idAdmin;
        new ObtenerRestaurantesTask(this).execute(url);
    }


    private class ObtenerRestaurantesTask extends AsyncTask<String, Void, ArrayList<Restaurant>> {
        private Context context;

        public ObtenerRestaurantesTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... urls) {
            String url = urls[0];
            return ControladorSevicio.obtenerRestaurantesDesdeServicio(url, context);
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> listaRestaurantes) {
            if (listaRestaurantes != null && !listaRestaurantes.isEmpty()) {
                ListaNegociosAdapter adapter = new ListaNegociosAdapter(listaRestaurantes);
                listaNegocios.setAdapter(adapter);
            } else {
                Toast.makeText(context, "No se encontraron restaurantes", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void irNuevoNegocio(View v) {
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
        idAdmin = Integer.parseInt(idAdministrador);
        //String url = "https://telollevoya.000webhostapp.com/Negocio/obtener_negocios_por_administrador.php?idAdministrador=2";
        //new ObtenerRestaurantesTask(this).execute(url);
    }
}