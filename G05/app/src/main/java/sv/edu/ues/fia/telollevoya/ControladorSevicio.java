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

import sv.edu.ues.fia.telollevoya.negocio.negocio.Restaurant;

public class ControladorSevicio {

    public static String obtenerRespuestaPeticion(String url, Context ctx) {
        String respuesta = " ";
        // Estableciendo tiempo de espera del servicio
        HttpParams parametros = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parametros, 3000);
        HttpConnectionParams.setSoTimeout(parametros, 5000);
        // Creando objetos de conexion
        HttpClient cliente = new DefaultHttpClient(parametros);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpRespuesta = cliente.execute(httpGet);
            StatusLine estado = httpRespuesta.getStatusLine();
            int codigoEstado = estado.getStatusCode();
            if (codigoEstado == 200) {
                HttpEntity entidad = httpRespuesta.getEntity();
                respuesta = EntityUtils.toString(entidad);
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en la conexion", Toast.LENGTH_LONG)
                    .show();
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



    //Bryan
    public static int obtenerIdUbicacion(String url, Context ctx) {
        int idUbicacion = -1;
        String json = obtenerRespuestaPeticion(url, ctx);
        try {
            JSONObject resultado = new JSONObject(json);
            if (resultado.has("idUbicacion")) {
                idUbicacion = resultado.getInt("idUbicacion");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error al procesar la respuesta", Toast.LENGTH_LONG).show();
        }
        return idUbicacion;
    }

    public static void obtenerNegociosDesdeServicio(final String url, final Context ctx, final OnNegociosObtenidosListener listener) {
        new AsyncTask<Void, Void, ArrayList<Restaurant>>() {
            @Override
            protected ArrayList<Restaurant> doInBackground(Void... voids) {
                ArrayList<Restaurant> listaNegocios = new ArrayList<>();
                try {
                    HttpParams parametros = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(parametros, 3000);
                    HttpConnectionParams.setSoTimeout(parametros, 5000);
                    HttpClient cliente = new DefaultHttpClient(parametros);
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpRespuesta = cliente.execute(httpGet);
                    StatusLine estado = httpRespuesta.getStatusLine();
                    int codigoEstado = estado.getStatusCode();

                    if (codigoEstado == 200) {
                        HttpEntity entidad = httpRespuesta.getEntity();
                        String respuesta = EntityUtils.toString(entidad);

                        if (!respuesta.isEmpty()) {
                            JSONArray jsonArray = new JSONArray(respuesta);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Restaurant restaurant = new Restaurant();
                                restaurant.setIdNegocio(jsonObject.getInt("idNegocio"));
                                restaurant.setIdUbicacion(jsonObject.getInt("idUbicacion"));
                                restaurant.setIdAdministrador(jsonObject.getInt("idAdministrador"));
                                restaurant.setNombre(jsonObject.getString("nombre"));
                                restaurant.setTelefono(jsonObject.getString("telefono"));
                                restaurant.setHorarioApertura(jsonObject.getString("horarioApertura"));
                                restaurant.setHorarioCierre(jsonObject.getString("horarioCierre"));
                                restaurant.setDescripcionUbicacion(jsonObject.getString("descripcionUbicacion"));
                                listaNegocios.add(restaurant);
                            }
                        } else {
                            Log.e("Error", "Respuesta vacía del servidor");
                        }
                    } else {
                        Log.e("Error", "Error en la conexión: Código de estado " + codigoEstado);
                    }
                } catch (Exception e) {
                    Log.e("Error", "Error en la conexión", e);
                }
                return listaNegocios;
            }

            @Override
            protected void onPostExecute(ArrayList<Restaurant> listaNegocios) {
                super.onPostExecute(listaNegocios);
                if (listener != null) {
                    listener.onNegociosObtenidos(listaNegocios);
                }
            }
        }.execute();
    }

    public interface OnNegociosObtenidosListener {
        void onNegociosObtenidos(ArrayList<Restaurant> listaNegocios);
    }



    public static String obtenerRespuestaPeticionCadena(String url, Context ctx) {
        String respuesta = "";
        // Estableciendo tiempo de espera del servicio
        HttpParams parametros = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parametros, 3000);
        HttpConnectionParams.setSoTimeout(parametros, 5000);
        // Creando objetos de conexion
        HttpClient cliente = new DefaultHttpClient(parametros);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpRespuesta = cliente.execute(httpGet);
            StatusLine estado = httpRespuesta.getStatusLine();
            int codigoEstado = estado.getStatusCode();
            if (codigoEstado == 200) {
                HttpEntity entidad = httpRespuesta.getEntity();
                respuesta = EntityUtils.toString(entidad);
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en la conexion", Toast.LENGTH_LONG).show();
            // Desplegando el error en el LogCat
            Log.v("Error de Conexion", e.toString());
        }
        return respuesta;
    }


    public static ArrayList<Restaurant> obtenerNegociosPorAdmin(String url, Context ctx) {
        ArrayList<Restaurant> negocios = new ArrayList<>();
        String json = obtenerRespuestaPeticionCadena(url, ctx);

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Restaurant negocio = new Restaurant();
                negocio.setIdNegocio(jsonObject.getInt("idNegocio"));
                negocio.setIdUbicacion(jsonObject.getInt("idUbicacion"));
                negocio.setIdAdministrador(jsonObject.getInt("idAdministrador"));
                negocio.setNombre(jsonObject.getString("nombre"));
                negocio.setTelefono(jsonObject.getString("telefono"));
                negocio.setHorarioApertura(jsonObject.getString("horarioApertura"));
                negocio.setHorarioCierre(jsonObject.getString("horarioCierre"));
                negocio.setDescripcionUbicacion(jsonObject.getString("descripcionUbicacion"));

                negocios.add(negocio);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error al procesar la respuesta", Toast.LENGTH_LONG).show();
        }

        return negocios;
    }



    public static void insertar(String peticion, Context ctx) {

        String json = obtenerRespuestaPeticion(peticion, ctx);
        try {
            JSONObject resultado = new JSONObject(json);

            Toast.makeText(ctx, "Registro ingresado" + resultado.getJSONArray("resultado").toString(), Toast.LENGTH_LONG)
                    .show();
            int respuesta = resultado.getInt("resultado");
            if (respuesta == 1)
                Toast.makeText(ctx, "Registro ingresado", Toast.LENGTH_LONG)
                        .show();
            else
                Toast.makeText(ctx, "Error registro duplicado",
                        Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
