package sv.edu.ues.fia.telollevoya.negocio.negocio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.R;
import sv.edu.ues.fia.telollevoya.negocio.producto.EditarProductoActivity;
import sv.edu.ues.fia.telollevoya.negocio.negocio.ListaNegociosAdapter;
import sv.edu.ues.fia.telollevoya.negocio.negocio.Restaurant;
import sv.edu.ues.fia.telollevoya.negocio.producto.ListaProductosAdaptader;
import sv.edu.ues.fia.telollevoya.negocio.producto.ProductosActivity;

public class ListaNegociosAdapter extends RecyclerView.Adapter<ListaNegociosAdapter.NegociosViewHolder> {
    ArrayList<Restaurant> listaNegocios;

    public ListaNegociosAdapter(ArrayList<Restaurant> listaRestaurantes) {this.listaNegocios = listaRestaurantes;}

    @NonNull
    @Override
    public ListaNegociosAdapter.NegociosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_negocio, null, false);
        return new NegociosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNegociosAdapter.NegociosViewHolder holder, int position) {
        holder.txtNegocioNombre.setText("Negocio: " + listaNegocios.get(position).getNombre());
        holder.txtNegocioId.setText("ID del negocio: " + listaNegocios.get(position).getIdNegocio());
        holder.txtNegocioUbicacionId.setText("ID de la ubicación: " + listaNegocios.get(position).getIdUbicacion());
        holder.txtNegocioAdministradorId.setText("ID del administrador: " + listaNegocios.get(position).getIdAdministrador());
        holder.txtNegocioTelefono.setText("Telefono: " + listaNegocios.get(position).getTelefono());
        holder.txtNegocioApertura.setText("Hora de apertura: " + listaNegocios.get(position).getHorarioApertura());
        holder.txtNegocioCierre.setText("Hora de cierre: " + listaNegocios.get(position).getHorarioCierre());
        holder.txtNegocioDireccion.setText("Direccion: " + listaNegocios.get(position).getDescripcionUbicacion());
    }

    @Override
    public int getItemCount() {
        return listaNegocios.size();
    }

    public class NegociosViewHolder extends RecyclerView.ViewHolder {
        TextView txtNegocioNombre, txtNegocioId, txtNegocioUbicacionId, txtNegocioAdministradorId,
                txtNegocioTelefono, txtNegocioApertura, txtNegocioCierre, txtNegocioDireccion;

        public NegociosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNegocioNombre = itemView.findViewById(R.id.txtNegocioNombre);
            txtNegocioId = itemView.findViewById(R.id.txtNegocioId);
            txtNegocioUbicacionId = itemView.findViewById(R.id.txtNegocioUbicacionId);
            txtNegocioAdministradorId = itemView.findViewById(R.id.txtNegocioAdministradorId);
            txtNegocioTelefono = itemView.findViewById(R.id.txtNegocioTelefono);
            txtNegocioApertura = itemView.findViewById(R.id.txtNegocioApertura);
            txtNegocioCierre = itemView.findViewById(R.id.txtNegocioCierre);
            txtNegocioDireccion = itemView.findViewById(R.id.txtNegocioDireccion);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MiNegocioOpcionesActivity.class);
                    int idNegocio = listaNegocios.get(getAdapterPosition()).getIdNegocio();
                    String name = listaNegocios.get(getAdapterPosition()).getNombre();
                    intent.putExtra("idNegocio", idNegocio);
                    intent.putExtra("name", name);  // Agregar el nombre como extra en el Intent
                    context.startActivity(intent);
                }
            });
        }
    }
}
