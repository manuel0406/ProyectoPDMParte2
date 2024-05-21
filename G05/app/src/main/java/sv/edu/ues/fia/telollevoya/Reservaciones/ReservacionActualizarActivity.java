package sv.edu.ues.fia.telollevoya.Reservaciones;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservacion;

public class ReservacionActualizarActivity extends Activity {

    ControlBD helper;

    EditText edtIdReservacion, edtFecha, edtHora;
    private  int dia, mes, ano, hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservacion_actualizar);
        edtIdReservacion= (EditText)findViewById(R.id.edtIdReservacion);
        edtFecha=(EditText) findViewById(R.id.edtFecha);
        edtHora= (EditText)  findViewById(R.id.edtHoraEn);


        int idReservacion;
        String nombreNegocio;
        String fechaEntrega;
        String horaEntrega;


// Recuperar los datos pasados a través del Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idReservacion = extras.getInt("idReservacion");
            nombreNegocio = extras.getString("nombreNegocio");
            fechaEntrega = extras.getString("fechaEntrega");
            horaEntrega=extras.getString("horaEntrega");

            edtIdReservacion.setText(String.valueOf(idReservacion));
            edtFecha.setText(fechaEntrega);
            edtHora.setText(horaEntrega);

            // Aquí puedes usar los datos recuperados para actualizar la interfaz de usuario según sea necesario
        }
}
public void actualizarReservacion(View v){
    Reservacion reservacion= new Reservacion();
    reservacion.setIdReservacion(Integer.parseInt( edtIdReservacion.getText().toString()));
    reservacion.setFechaEntregaR(edtFecha.getText().toString());
    reservacion.setHoraEntrega(edtHora.getText().toString());

    helper.abrir();
    String estado= helper.actualizar(reservacion);
    helper.cerrar();

    Toast.makeText(this, estado, Toast.LENGTH_SHORT).show();
}

public void limpiarTexto(View v){
        edtHora.setText("");
        edtFecha.setText("");
        edtIdReservacion.setText("");
}

    public void fecha(View v) {

        if (v==edtFecha){
            final Calendar c=Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    edtFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, ano, mes, dia);
            datePickerDialog.show();;

        }
        if (v== edtHora){
            final Calendar c= Calendar.getInstance();
            hora= c.get(Calendar.HOUR_OF_DAY);
            minutos= c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    edtHora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();

        }



    }
}