package sv.edu.ues.fia.telollevoya.negocio.producto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
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
        listaProductos = findViewById(R.id.rvListaProductos);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));

        helper = new ControlBD(this);

        //listaArrayProductos = new ArrayList<Product>();

        idNegocioRecuperado = getIntent().getIntExtra("idNegocioRecuperado", 5);
        helper.abrir();
        ListaProductosAdaptader adapter = new ListaProductosAdaptader(helper.obtenerProductosPorIdNegocio(idNegocioRecuperado));
        helper.cerrar();
        listaProductos.setAdapter(adapter);
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
        helper = new ControlBD(this);
        helper.abrir();
        ListaProductosAdaptader adapter = new ListaProductosAdaptader(helper.obtenerProductosPorIdNegocio(idNegocioRecuperado));
        helper.cerrar();
        listaProductos.setAdapter(adapter);
    }
}