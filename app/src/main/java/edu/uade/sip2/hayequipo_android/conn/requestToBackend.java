package edu.uade.sip2.hayequipo_android.conn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.uade.sip2.hayequipo_android.entities.Usuario;

/**
 * Created by Usuario on 12/11/2017.
 */

public class requestToBackend extends AppCompatActivity {


    public static boolean enviarPeticion(int miPartidoActual, String yo, String usuario, Context context) {
        JSONObject jsonObj = new JSONObject();
        usuario = "rpueba";

        try {
            jsonObj.put("accion", "peticion");
            jsonObj.put("emisor", yo);
            jsonObj.put("receptor", usuario);
            jsonObj.put("idPartido", miPartidoActual);



        } catch (Exception e) {

            Log.e("enviarPeticion", "no se pudo parsear el Json");
            e.printStackTrace();
            return false;
        }

        Log.e("json a enviar: ", jsonObj.toString());

        try {


            // Petición POST
            VolleySingleton.
                    getInstance(context).
                    addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    "http://10.100.1.178/hayequipo/Peticiones.php",
                                    jsonObj,
                                    new Response.Listener<JSONObject>() {


                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Procesar la respuesta Json
                                            String estado = "";

                                            try {
                                                Log.e("msj", "OK Volley: " + response.toString());
                                                estado = response.getString("estado");
                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }

                                            switch (estado) {
                                                case "1": // EXITO

                                                    try {
                                                        Log.e("ok volley", "response: " + response.getString("estado"));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                    }

                                                    break;

                                                case "2": // FALLIDO
                                                    Log.e("error volley", "json error");

                                                    break;


                                                case "4": //USUARIO VACIO
                                                    Log.e("error volley", "usuario vacio");

                                                    break;
                                                case "5": //ERROR BD
                                                    Log.e("error volley", "error BD");
                                                    break;

                                                default:
                                                    Log.e("error volley", "uknownn");
                                                    break;
                                            }


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Toast.makeText(getBaseContext(), "error volley!", Toast.LENGTH_SHORT).show();
                                            Log.e("msj volley", "Error Volley: " + error.getMessage() + "   net response: " + error.networkResponse);
                                            error.printStackTrace();


                                        }
                                    }


                            )
                    );


        } catch (Exception e) {
            e.printStackTrace();


        }


   return false;
    }






        public static ArrayList<Usuario> obtenerUsuarios (String yo, Context context){

            JSONObject jsonObj = new JSONObject();


            try {
                jsonObj.put("accion", "obtenerUsuarios");
                jsonObj.put("usuario", yo);


            } catch (Exception e) {

                Log.e("obtenerUsuarios", "no se pudo parsear el Json");
                e.printStackTrace();
                return null;
            }

            Log.e("json a enviar: ", jsonObj.toString());

            try {


                // Petición POST
                VolleySingleton.
                        getInstance(context).
                        addToRequestQueue(
                                new JsonObjectRequest(
                                        Request.Method.POST,
                                        "http://192.168.1.107/hayequipo/Usuarios.php",
                                        jsonObj,
                                        new Response.Listener<JSONObject>() {


                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // Procesar la respuesta Json
                                                String estado = "";

                                                try {
                                                    Log.e("msj", "OK Volley: " + response.toString());
                                                    estado = response.getString("estado");
                                                } catch (Exception e) {
                                                    e.printStackTrace();

                                                }

                                                switch (estado) {
                                                    case "1": // EXITO

                                                        try {
                                                            Log.e("ok volley", "response: " + response.getString("estado"));

                                                        } catch (Exception e) {
                                                            e.printStackTrace();

                                                        }

                                                        break;

                                                    case "2": // FALLIDO
                                                        Log.e("error volley", "json error");

                                                        break;


                                                    case "4": //USUARIO VACIO
                                                        Log.e("error volley", "usuario vacio");

                                                        break;
                                                    case "5": //ERROR BD
                                                        Log.e("error volley", "error BD");
                                                        break;

                                                    default:
                                                        Log.e("error volley", "uknownn");
                                                        break;
                                                }


                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Toast.makeText(getBaseContext(), "error volley!", Toast.LENGTH_SHORT).show();
                                                Log.e("msj volley", "Error Volley: " + error.getMessage() + "   net response: " + error.networkResponse);
                                                error.printStackTrace();


                                            }
                                        }


                                )
                        );


            } catch (Exception e) {
                e.printStackTrace();


            }

            return null;
        }


    }


