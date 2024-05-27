package sv.edu.ues.fia.telollevoya.pedidos.negocio;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.ControladorSevicio;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.negocio.producto.ProductosActivity;

public class NegociosOpcionesActivity extends Activity {
    TextView texto1;
    String idNegocioRecuperado;
    int idNegocio = -1, idUbicacion,idAdmin;
    String name;
    ControlBD helper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios_opciones);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        helper = new ControlBD(this);
        texto1 = findViewById(R.id.txtRaa);

        //Recuperamos id de Negocio
        Intent intent = getIntent();
        idNegocio = intent.getIntExtra("idNegocio", 5);
        idUbicacion = intent.getIntExtra("idUbicacion", 5);
        idAdmin = intent.getIntExtra("idAdministrador", 1);
        name = intent.getStringExtra("name");
        String negocioNombre = getResources().getString(R.string.negocio_nombre);
        texto1.setText(negocioNombre + " " + name);


        String message = "id Admin: " + idAdmin + "\nid negocio: " + idNegocio + "\nid Ubicacion: " + idUbicacion + "\nnombre negocio: " + name;


        //Solo es para informar, borrarlo despues
        new AlertDialog.Builder(this)
                .setTitle("En esta pantalla tienen disponibles estos datos")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Código para ejecutar cuando el usuario presiona OK
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String negocioNombre = getResources().getString(R.string.negocio_nombre);
        texto1.setText(negocioNombre + " " + name);
    }



}