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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

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
            StringEntity nuevaEntidad = new StringEntity(obj.toString());
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

    /*
    * Este metodo es que el insertar los datos a las BD pero en segundo plano
    * por el momento no funcionara
    * */
//    public static void insertarDepartamento(final String peticion, final Context ctx){
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... voids) {
//                return obtenerRespuestaPeticion(peticion, ctx);
//            }
//
//            @Override
//            protected void onPostExecute(String json) {
//                try {
//                    JSONObject resultado = new JSONObject(json);
//
//                    Toast.makeText(ctx, "Registro ingresado"+
//                                    resultado.getJSONArray("resultado").toString(), Toast.LENGTH_LONG)
//                            .show();
//                    int respuesta = resultado.getInt("resultado");
//                    if (respuesta == 1)
//                        Toast.makeText(ctx, "Registro ingresado",
//                                        Toast.LENGTH_LONG)
//                                .show();
//                    else
//                        Toast.makeText(ctx, "Error registro duplicado",
//                                Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute();
//    }

    public static void insertarReservacion(final String peticion, Context ctx) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return obtenerRepuestaPeticion(peticion, ctx);
            }

            @Override
            protected void onPostExecute(String json) {
                try {
                    JSONObject resultado = new JSONObject(json);

                    Toast.makeText(ctx, "Registro ingresado"+ resultado.getJSONArray("resultado").toString(), Toast.LENGTH_LONG).show();
                    int respuesta = resultado.getInt("resultado");
                    if (respuesta == 1)
                        Toast.makeText(ctx, "Registro ingresado",
                                        Toast.LENGTH_LONG)
                                .show();
                    else
                        Toast.makeText(ctx, "Error registro duplicado",
                                Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    //********************************************************************************************
    //***                        INSERSICION DE USUARIO EN 000WEBHOST                          ***
    //********************************************************************************************
    public static String insertarUsuario(String peticion, Context ctx) {
        String resp="";
        String json = obtenerRepuestaPeticion(peticion, ctx);

        try {
            JSONObject resultado = new JSONObject(json);

            // Obtén el valor de "resultado" como una cadena
            String respuesta = resultado.getString("resultado");
            Toast.makeText(ctx, respuesta, Toast.LENGTH_LONG).show();
            resp = respuesta;

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error procesando la respuesta del servidor", Toast.LENGTH_LONG).show();
        }
        return resp;
    }

    //********************************************************************************************
    //***                        INSERSICION DE USUARIO EN 000WEBHOST                          ***
    //********************************************************************************************
    public static JSONObject insertarCliente(String peticion, Context ctx) {
        JSONObject resp = null;
        String json = obtenerRepuestaPeticion(peticion, ctx);

        try {
            JSONObject resultado = new JSONObject(json);

            // Obtén el valor de "resultado" como una cadena
            String respuesta = resultado.getString("resultado");
            Toast.makeText(ctx, respuesta, Toast.LENGTH_LONG).show();
            resp = resultado;

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error procesando la respuesta del servidor", Toast.LENGTH_LONG).show();
        }
        return resp;
    }

    //********************************************************************************************
    //***                        CONSULTAR USUARIO EN 000WEBHOST                               ***
    //********************************************************************************************
    public static String obtenerRespuestaPeticion(String urlString, Context ctx) {
        String respuesta = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Crear la URL
            URL url = new URL(urlString);

            // Abrir la conexión
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000); // Tiempo de espera de conexión
            urlConnection.setReadTimeout(5000);    // Tiempo de espera de lectura

            // Conectar a la URL
            urlConnection.connect();

            // Obtener el código de respuesta
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leer la respuesta del InputStream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // No hay nada que hacer
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linea;
                while ((linea = reader.readLine()) != null) {
                    buffer.append(linea).append("\n");
                }

                if (buffer.length() == 0) {
                    // La respuesta está vacía
                    return null;
                }
                respuesta = buffer.toString();
            } else {
                Toast.makeText(ctx, "Error en la conexion: " + responseCode, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(ctx, "Error en la conexion", Toast.LENGTH_LONG).show();
            Log.v("Error de Conexion", e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Error cerrando stream", e.toString());
                }
            }
        }
        return respuesta;
    }
}
