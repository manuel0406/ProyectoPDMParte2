package sv.edu.ues.fia.telollevoya.Reservaciones;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservacion;

public class ReservacionesConsultarActivity extends Activity {

    RecyclerView recyclerView;
    ArrayList<Reservacion> reservacion;
    ControlBD db;
    MyAdapter adapter;
    String idCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservaciones_consultar);
        db= new ControlBD(this);
        //Reservacion reservacion1= reservacion.get()

        recyclerView= findViewById(R.id.recycolerView);
        reservacion = obtenerReservaciones();
        adapter = new MyAdapter(this, reservacion);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();

        Intent intent= getIntent();
        idCliente= intent.getStringExtra("idCliente");




    }
    private ArrayList<Reservacion> obtenerReservaciones() {
        ArrayList<Reservacion> reservaciones = new ArrayList<>();
        db.abrir();
        reservaciones=db.consultaReservacion();
        db.cerrar();
       /*
        reservaciones.add(new Reservacion(1, "Negocio 1", " 12/02/24"));
        reservaciones.add(new Reservacion(2, "Negocio 2", ""));
        reservaciones.add(new Reservacion(3, "Negocio 3", " "));
        */
        return reservaciones;
    }
    private void displaydata(){
        //Cursor cursor= db.query();
    }

    public void cambiarPantalla(View v){
        Toast.makeText(this, idCliente,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ReservacionesConsultarActivity.this, ReservacionInsertarActivity.class);
        intent.putExtra("idCliente", idCliente);
        startActivity(intent);
    }
}