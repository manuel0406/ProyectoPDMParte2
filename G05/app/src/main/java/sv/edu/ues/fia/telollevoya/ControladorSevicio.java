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


import java.util.ArrayList;

import sv.edu.ues.fia.telollevoya.negocio.negocio.Restaurant;
import sv.edu.ues.fia.telollevoya.negocio.producto.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Producto> obtenerProductos(String json, Context ctx) {

        List<Producto> listaProductos= new ArrayList<Producto>();
        try {
            JSONArray productoJSON = new JSONArray(json);

            for (int i = 0; i < productoJSON.length(); i++) {

                JSONObject obj = productoJSON.getJSONObject(i);
                Producto producto= new Producto();
                producto.setId(obj.getInt("IDPRODUCTO"));
                producto.setIdNegocio(obj.getInt("IDNEGOCIO"));
                producto.setNombre(obj.getString("NOMBREPRODUCTO"));
                producto.setTipo( obj.getString("TIPOPRODUCTO"));
                producto.setDescripcion(obj.getString("DESCRIPCIONPRODUCTO"));
                producto.setPrecio(Float.parseFloat( obj.getString("PRECIOPRODUCTO")));
                producto.setExistencia(obj.getString("EXISTENCIAPRODUCTO").equals("1"));



                listaProductos.add(producto);
            }
            return listaProductos;
        } catch (Exception e) { Toast.makeText(ctx, "Error en parseOO de JSON",
                            Toast.LENGTH_LONG)
                    .show();
            return null;
        }
    }




    /*-------------------------------------------------------------------------------------------------------
    -- Mis metodos para los servicios - BRYAN - NEGOCIOS Y PRODUCTOS
    -------------------------------------------------------------------------------------------------------*/


    //Obtener todos los negocios de un administrador
    public static ArrayList<Restaurant> obtenerRestaurantesDesdeServicio(String url, Context ctx) {
        ArrayList<Restaurant> restaurantes = new ArrayList<>();
        String respuesta = obtenerRespuestaPeticion(url, ctx);

        try {
            // Verificar si la respuesta es un array JSON
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Crear un objeto Restaurant a partir de los datos JSON
                Restaurant restaurante = new Restaurant();
                restaurante.setIdNegocio(jsonObject.getInt("idNegocio"));
                restaurante.setIdUbicacion(jsonObject.getInt("idUbicacion"));
                restaurante.setIdAdministrador(jsonObject.getInt("idAdministrador"));
                restaurante.setNombre(jsonObject.getString("nombre"));
                restaurante.setTelefono(jsonObject.getString("telefono"));
                restaurante.setHorarioApertura(jsonObject.getString("horarioApertura"));
                restaurante.setHorarioCierre(jsonObject.getString("horarioCierre"));
                restaurante.setDescripcionUbicacion(jsonObject.getString("descripcionUbicacion"));

                // Agregar el restaurante a la lista
                restaurantes.add(restaurante);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error arraylist restaurantes", e.toString());
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }

        return restaurantes;
    }

    //Obtener los detalles de un negocio en especifico
    public static void obtenerNegocioDesdeServicio(String url, Context ctx, OnRestaurantReceivedListener listener) {
        new ObtenerNegocioTask(url, ctx, listener).execute();
    }
    private static class ObtenerNegocioTask extends AsyncTask<Void, Void, Restaurant> {
        private String url;
        private Context ctx;
        private OnRestaurantReceivedListener listener;

        public ObtenerNegocioTask(String url, Context ctx, OnRestaurantReceivedListener listener) {
            this.url = url;
            this.ctx = ctx;
            this.listener = listener;
        }

        @Override
        protected Restaurant doInBackground(Void... voids) {
            String respuesta = obtenerRespuestaPeticion(url, ctx);
            if (respuesta != null && !respuesta.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(respuesta);
                    Restaurant negocio = new Restaurant();
                    negocio.setIdNegocio(jsonObject.getInt("IDNEGOCIO"));
                    negocio.setIdUbicacion(jsonObject.getInt("IDUBICACION"));
                    negocio.setIdAdministrador(jsonObject.getInt("IDADMINISTRADOR"));
                    negocio.setNombre(jsonObject.getString("NOMBRENEGOCIO"));
                    negocio.setTelefono(jsonObject.getString("TELEFONONEGOCIO"));
                    negocio.setHorarioApertura(jsonObject.getString("HORARIOAPERTURA"));
                    negocio.setHorarioCierre(jsonObject.getString("HORARIOCIERRE"));
                    return negocio;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Restaurant negocio) {
            if (listener != null) {
                listener.onRestaurantReceived(negocio);
            }
        }
    }

    //Insertar ubicacion y obtener su id
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

    // Insertar, editar y eliminar un negocio
    public static void generico(String peticion, Context ctx) {
        String json = obtenerRespuestaPeticion(peticion, ctx);
        try {
            JSONObject resultado = new JSONObject(json);

            if (resultado.has("resultado")) {
                String mensajeExito = resultado.getString("resultado");
                Toast.makeText(ctx, mensajeExito, Toast.LENGTH_LONG).show();
            } else if (resultado.has("error")) {
                String mensajeError = resultado.getString("error");
                Toast.makeText(ctx, "Error: " + mensajeError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ctx, "Respuesta inesperada del servidor", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Desplegar el error en el LogCat
            Log.e("Error de JSON", e.toString());
            Toast.makeText(ctx, "Error al procesar la respuesta del servidor", Toast.LENGTH_LONG).show();
        }
    }

    //Actualizar ubicacion de negocio
    public static void actualizarUbicacion(String url, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Llamando al método de obtener respuesta de petición GET
                String respuesta = obtenerRespuestaPeticion(url, ctx);
                // Manejar la respuesta según sea necesario
                Log.v("Respuesta", respuesta);
                try {
                    JSONObject jsonResponse = new JSONObject(respuesta);
                    // Aquí puedes manejar la respuesta JSON según tus necesidades
                    // Por ejemplo, mostrar un mensaje en un Toast
                    String mensaje = jsonResponse.getString("resultado");
                    // Si no necesitas mostrar un Toast, puedes omitir esta línea
                    // mostrarMensajeEnMainThread(ctx, mensaje);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error raro?", e.toString());

                    // Manejar cualquier error de análisis JSON aquí
                }
            }
        });
    }

    //Obtener todos los productos de un negocio
    public static ArrayList<Product> obtenerProductosPorIdNegocio(String url, Context ctx) {
        ArrayList<Product> productos = new ArrayList<>();
        String respuesta = obtenerRespuestaPeticion(url, ctx);

        try {
            // Verificar si la respuesta es un array JSON
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Crear un objeto Product a partir de los datos JSON
                Product producto = new Product();
                producto.setIdProducto(jsonObject.getInt("idProducto"));
                producto.setIdNegocio(jsonObject.getInt("idNegocio"));
                producto.setNombreProducto(jsonObject.getString("nombreProducto"));
                producto.setTipoProducto(jsonObject.getString("tipoProducto"));
                producto.setDescripcionProducto(jsonObject.getString("descripcionProducto"));
                producto.setPrecioProducto((float) jsonObject.getDouble("precioProducto"));
                producto.setExistenciaProducto(jsonObject.getInt("existenciaProducto") == 1);

                // Agregar el producto a la lista
                productos.add(producto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error arraylist productos", e.toString());
            return new ArrayList<>(); // Devolver una lista vacía en caso de error
        }

        return productos;
    }

    //Obtener los detalles de un producto en especifico
    public static Product obtenerProducto(String url, Context ctx) {
        Product producto = null;

        String respuesta = obtenerRespuestaPeticion(url, ctx);

        try {
            JSONObject jsonObject = new JSONObject(respuesta);
            producto = new Product();
            producto.setIdProducto(jsonObject.getInt("IDPRODUCTO"));
            producto.setIdNegocio(jsonObject.getInt("IDNEGOCIO"));
            producto.setNombreProducto(jsonObject.getString("NOMBREPRODUCTO"));
            producto.setTipoProducto(jsonObject.getString("TIPOPRODUCTO"));
            producto.setDescripcionProducto(jsonObject.getString("DESCRIPCIONPRODUCTO"));
            producto.setPrecioProducto((float) jsonObject.getDouble("PRECIOPRODUCTO"));
            producto.setExistenciaProducto(jsonObject.getInt("EXISTENCIAPRODUCTO") == 1);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error al procesar la respuesta del servidor", Toast.LENGTH_LONG).show();
            Log.e("Error al obtener producto", e.toString());
        }
        return producto;
    }

    // Interfaz para manejar la devolución del objeto Restaurant
    public interface OnRestaurantReceivedListener {
        void onRestaurantReceived(Restaurant restaurant);
    }

    /*-------------------------------------------------------------------------------------------------------
    -- Fin de mis metodos - BRYAN - NEGOCIOS Y PRODUCTOS
    -------------------------------------------------------------------------------------------------------*/

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
                    int respuesta = resultado.getInt("resultado");
//                    idReservacion = resultado.getInt("idReservacion");
//                    Log.v("idReservacionC",String.valueOf( idReservacion));

                    if (respuesta == 1) {
                       // Toast.makeText(ctx, "Registro ingresado", Toast.LENGTH_LONG).show();
                    } else {
                       // Toast.makeText(ctx, "Error registro duplicado", Toast.LENGTH_LONG).show();
//                        idReservacion = 0; // Reiniciar idReservacion en caso de error
                    }
                    // Llamar al método de callback con el ID de la reservación
                  //  listener.onReservacionInserted(idReservacion);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }.execute();
    }
    public static void updateReservacion(String peticion, Context ctx) {
        String json = obtenerRespuestaPeticion(peticion, ctx);
        try {
            JSONObject resultado = new JSONObject(json);
            int respuesta = resultado.getInt("resultado");
//                    idReservacion = resultado.getInt("idReservacion");
//                    Log.v("idReservacionC",String.valueOf( idReservacion));

            if (respuesta == 1) {
                Toast.makeText(ctx, "Registro ingresado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ctx, "Error registro duplicado", Toast.LENGTH_LONG).show();
//                        idReservacion = 0; // Reiniciar idReservacion en caso de error
            }
            // Llamar al método de callback con el ID de la reservación
            //  listener.onReservacionInserted(idReservacion);
        } catch (JSONException e) {
            e.printStackTrace();

        }
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
