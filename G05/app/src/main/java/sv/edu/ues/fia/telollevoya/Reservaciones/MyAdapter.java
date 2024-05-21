package sv.edu.ues.fia.telollevoya.Reservaciones;

import android.content.Context;
import android.content.Intent;
import android.service.controls.Control;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.ControlBD;
import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.Reservacion;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Reservacion> reservacion;
    private  ControlBD db;

    public MyAdapter(Context context, ArrayList<Reservacion> reservacion) {
        this.context = context;
        this.reservacion = reservacion;
        this.db = new ControlBD(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.consultar_reservacion, parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reservacion reservacion1 = reservacion.get(position);
        holder.idreservacion.setText(String.valueOf(reservacion1.getIdReservacion()));
        holder.negocio.setText(reservacion1.getNombreNegocion());
        holder.fechaEntrega.setText(reservacion1.getFechaEntregaR());
        holder.horaEntrega.setText(reservacion1.getHoraEntrega());

        holder.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de la reserva en la posición actual
                Reservacion reservaActual = reservacion.get(position);

                // Crear un Intent para iniciar la actividad ReservacionActualizarActivity
                Intent intent = new Intent(context, ReservacionActualizarActivity.class);

                // Pasar los datos de la reserva a través de putExtra()
                intent.putExtra("idReservacion", reservaActual.getIdReservacion());
                intent.putExtra("nombreNegocio", reservaActual.getNombreNegocion());
                intent.putExtra("fechaEntrega", reservaActual.getFechaEntregaR());
                intent.putExtra("horaEntrega",reservaActual.getHoraEntrega());

                // Iniciar la actividad
                context.startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la posición del elemento en la lista
                int posicion = holder.getAdapterPosition();
                if (posicion != RecyclerView.NO_POSITION) {
                    // Obtener la reservación correspondiente a esa posición
                    Reservacion reservacionActual = reservacion.get(posicion);

                    // Llamar al método de eliminación en la base de datos
                    db.abrir();
                    db.cancelarReservacion(reservacionActual);
                    db.cerrar();

                    // Eliminar la reservación de la lista
                    reservacion.remove(posicion);

                    // Notificar al adaptador que se ha eliminado un elemento
                    notifyItemRemoved(posicion);
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        return reservacion.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idreservacion, negocio, fechaEntrega, horaEntrega;

        Button btnActualizar, btnEliminar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idreservacion=itemView.findViewById(R.id.txtIdReservacion);
            negocio=itemView.findViewById(R.id.txtNameNegocio);
            fechaEntrega=itemView.findViewById(R.id.txtFecha);
            btnActualizar=itemView.findViewById(R.id.btnActualizar);
            btnEliminar=itemView.findViewById(R.id.btnEliminar);
            horaEntrega=itemView.findViewById(R.id.txtHora);

        }
    }
}
