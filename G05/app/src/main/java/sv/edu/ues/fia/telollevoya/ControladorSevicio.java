package sv.edu.ues.fia.telollevoya;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ControladorSevicio {


    public static String obtenerRepuestaPeticion(String url, Context ctx) {
        String respuesta="";

        //Establecimeinto tiempo de espera del servicio
        HttpParams parametros = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parametros, 3000);
        HttpConnectionParams.setSoTimeout(parametros, 5000);

        //Creando objetos de conexion
        HttpClient cliente= new DefaultHttpClient(parametros);
        HttpGet httpGet= new HttpGet(url);
        try{
            HttpResponse httpRespuesta= cliente.execute(httpGet);
            StatusLine estado= httpRespuesta.getStatusLine();
            int codigoEstado= estado.getStatusCode();
            if(codigoEstado==200){
                HttpEntity entidad= httpRespuesta.getEntity();
                respuesta= EntityUtils.toString(entidad);
                //Log.v("respuesta", respuesta);
            }

        }catch (Exception e){
            Toast.makeText(ctx,"Error en la conexion" +e, Toast.LENGTH_LONG).show();
            // Desplegando el error en el LogCat
            Log.v("Error de Conexion", e.toString());
        }
        return respuesta;
    }


    public static String obtenerRespuestaPost(String url, JSONObject obj, Context ctx) {
        String respuesta = " ";
        try {
            HttpParams parametros = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(parametros, 3000);
            HttpConnectionParams.setSoTimeout(parametros, 5000);
            HttpClient cliente = new DefaultHttpClient(parametros);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("content-type", "application/json");
            StringEntity nuevaEntidad = new
                    StringEntity(obj.toString());
            httpPost.setEntity(nuevaEntidad);
            Log.v("Peticion", url);
            Log.v("POST", httpPost.toString());
            HttpResponse httpRespuesta = cliente.execute(httpPost);
            StatusLine estado = httpRespuesta.getStatusLine();
            int codigoEstado = estado.getStatusCode();
            if (codigoEstado == 200) {
                respuesta = Integer.toString(codigoEstado);
                Log.v("respuesta", respuesta);
            } else {
                Log.v("respuesta", Integer.toString(codigoEstado));
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en la conexion",
                            Toast.LENGTH_LONG)
                    .show();
// Desplegando el error en el LogCat
            Log.v("Error de Conexion", e.toString());
        }
        return respuesta;
    }
    public static List<Reservacion> obtenerReservaciones(String json, Context ctx) {

        List<Reservacion> listaReservaciones = new ArrayList<Reservacion>();
        try {
            JSONArray reservacionesJSON = new JSONArray(json);

            for (int i = 0; i < reservacionesJSON.length(); i++) {
                JSONObject obj = reservacionesJSON.getJSONObject(i);
                Reservacion reservaciones = new Reservacion();
                reservaciones.setIdReservacion( obj.getInt("idReservacion"));
                reservaciones.setNombreNegocion(obj.getString("nombreNegocio"));
                reservaciones.setFechaEntregaR(obj.getString("fechaEntregar"));
                reservaciones.setHoraEntrega(obj.getString("horaEntregar"));


                listaReservaciones.add(reservaciones);
            }
            return listaReservaciones;
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en parseOO de JSON",
                            Toast.LENGTH_LONG)
                    .show();
            return null;
        }
    }

    /*
    * Este metodo es que el insertar los datos a las BD pero en segundo plano
    * por el momento no funcionara
    * */
    public static void insertarDetalle(final String peticion, final Context ctx){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return obtenerRepuestaPeticion(peticion, ctx);
            }

            @Override
            protected void onPostExecute(String json) {
                try {
                    JSONObject resultado = new JSONObject(json);

                    Toast.makeText(ctx, "Registro ingresado"+
                                    resultado.getJSONArray("resultado").toString(), Toast.LENGTH_LONG)
                            .show();
                    int respuesta = resultado.getInt("resultado");
                    if (respuesta == 1)
                        Toast.makeText(ctx, "Registro ingresado",
                                        Toast.LENGTH_LONG)
                                .show();
                    else
                        Toast.makeText(ctx, "Error registro duplicado",
                                Toast.LENGTH_LONG).show();
                    // Llamar al método de callback con el ID de la reservación

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }.execute();
    }

    public interface ReservacionInsertListener {
        void onReservacionInserted(int idReservacion);
    }

    public static void insertarReservacion(final String peticion, final Context ctx, final ReservacionInsertListener listener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return obtenerRepuestaPeticion(peticion, ctx);
            }

            @Override
            protected void onPostExecute(String json) {
                int idReservacion=0;
                try {
                    JSONObject resultado = new JSONObject(json);
                    int respuesta = resultado.getInt("resultado");
                     idReservacion = resultado.getInt("idReservacion");
                    Log.v("idReservacionC",String.valueOf( idReservacion));

                    if (respuesta == 1) {
                        Toast.makeText(ctx, "Registro ingresado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ctx, "Error registro duplicado", Toast.LENGTH_LONG).show();
                        idReservacion = 0; // Reiniciar idReservacion en caso de error
                    }
                    // Llamar al método de callback con el ID de la reservación
                    listener.onReservacionInserted(idReservacion);
                } catch (JSONException e) {
                    e.printStackTrace();
                    idReservacion = 0; // Reiniciar idReservacion en caso de error
                    // Llamar al método de callback con el ID de la reservación
                    listener.onReservacionInserted(idReservacion);
                }
            }
        }.execute();
    }




}
