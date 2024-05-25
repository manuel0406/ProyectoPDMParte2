package sv.edu.ues.fia.telollevoya.negocio.producto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.negocio.negocio.MiNegocioActivity;

public class ProductosActivity extends AppCompatActivity {
    RecyclerView listaProductos;
    //ArrayList<Product> listaArrayProductos;
    ControlBD helper;
    int idNegocioRecuperado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Onda rara
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listaProductos = findViewById(R.id.rvListaProductos);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));

        idNegocioRecuperado = getIntent().getIntExtra("idNegocioRecuperado", 5);
        Toast.makeText(this, "idNegocio " + idNegocioRecuperado, Toast.LENGTH_LONG).show();

        String url = "https://telollevoya.000webhostapp.com/Producto/obtenerProductosPorIdNegocio.php?idNegocio=" + idNegocioRecuperado;
        new ObtenerProductosTask(this).execute(url);



        /*helper = new ControlBD(this);
        idNegocioRecuperado = getIntent().getIntExtra("idNegocioRecuperado", 5);
        helper.abrir();
        ListaProductosAdaptader adapter = new ListaProductosAdaptader(helper.obtenerProductosPorIdNegocio(idNegocioRecuperado));
        helper.cerrar();
        listaProductos.setAdapter(adapter);*/
    }

    /*private class ObtenerProductosTask extends AsyncTask<String, Void, ArrayList<Product>> {
        private Context context;

        public ObtenerProductosTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Product> doInBackground(String... urls) {
            String url = urls[0];
            return ControladorSevicio.obtenerProductosPorIdNegocio(url, context);
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productos) {
            if (productos != null && !productos.isEmpty()) {
                ListaProductosAdaptader adapter = new ListaProductosAdaptader(productos);
                listaProductos.setAdapter(adapter);
            } else {
                Toast.makeText(context, "No se encontraron productos", Toast.LENGTH_LONG).show();
            }
        }
    }*/
    private class ObtenerProductosTask extends AsyncTask<String, Void, ArrayList<Product>> {
        private Context context;

        public ObtenerProductosTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Product> doInBackground(String... urls) {
            String url = urls[0];
            return ControladorSevicio.obtenerProductosPorIdNegocio(url, context);
        }

        @Override
        protected void onPostExecute(ArrayList<Product> productos) {
            if (productos != null && !productos.isEmpty()) {
                ListaProductosAdaptader adapter = new ListaProductosAdaptader(productos);
                listaProductos.setAdapter(adapter);
            } else {
                Toast.makeText(context, "No se encontraron productos", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void irNuevoProducto(View v){
        Intent intent = new Intent(this, CrearProductoActivity.class);
        intent.putExtra("idNegocioRecuperado", idNegocioRecuperado);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarListaProductos();
    }

    private void actualizarListaProductos() {
        idNegocioRecuperado = getIntent().getIntExtra("idNegocioRecuperado", 0);
        String url = "https://telollevoya.000webhostapp.com/Producto/obtenerProductosPorIdNegocio.php?idNegocio=" + idNegocioRecuperado;
        new ObtenerProductosTask(this).execute(url);
        /*
        helper = new ControlBD(this);
        helper.abrir();
        ListaProductosAdaptader adapter = new ListaProductosAdaptader(helper.obtenerProductosPorIdNegocio(idNegocioRecuperado));
        helper.cerrar();
        listaProductos.setAdapter(adapter);*/
    }
}