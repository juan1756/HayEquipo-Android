package edu.uade.sip2.hayequipo_android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uade.sip2.hayequipo_android.core.MainActivity;
import edu.uade.sip2.hayequipo_android.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.data.Encryption;

import static java.lang.Thread.sleep;


public class SignupActivity extends AppCompatActivity {

    private String estadoFinal = "-1";
    private boolean flagRequest = false;
    private boolean ok = false;
    private static final String TAG = "SignupActivity";
    private boolean hayInternet = false;
    private boolean flag = true;
    private boolean sharedP = false;
    private boolean storage = false;
    private boolean registrado = false;
    private String version = "";

    @Bind(R.id.input_usuario)
    EditText _matriculaText;
    @Bind(R.id.input_contraseña)
    EditText _codigoText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    //@Bind(R.id.link_login)
   // TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });



        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "start");
                try {
                    // CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordlayout);
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                    final Snackbar snackbar = Snackbar.make(coordinatorLayout, "         DEBE TENER CONEXIÓN A INTERNET", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            snackbar.getView().getViewTreeObserver().removeOnPreDrawListener(this);
                            ((CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams()).setBehavior(null);
                            return true;
                        }
                    });
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.RED);
                  //  view.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    view.setFocusable(false);

                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;


                    view.setLayoutParams(params);


                    while (true && flag) {

                        Log.e("loop", "looop");

                        if (!isNetAvailable()) {
                            hayInternet = false;
                            if (!snackbar.isShown()) {
                                view = snackbar.getView();
                                view.setBackgroundColor(Color.RED);
                              //  view.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                                view.setFocusable(false);

                                CoordinatorLayout.LayoutParams params2 = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                                params2.gravity = Gravity.TOP;


                                view.setLayoutParams(params2);
                                snackbar.show();
                            }
                        } else {
                            hayInternet = true;
                            if (snackbar.isShown())
                                snackbar.dismiss();
                        }

                        sleep(1500);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }


            }


        }).start();


    }


    private void signup() {
        Log.d(TAG, "Signup");

        if (!hayInternet) {
            return;
        }

        if (!validate()) {
            onSignupFailed();
            return;
        }


        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();


        final String usuario = _matriculaText.getText().toString();
        final String contraseña = _codigoText.getText().toString();
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //SEND MESSAGE AND WRITE ON SHP AND EXT

        boolean connection = isNetAvailable();


        if (connection) {



            //TODO : HARCODEO PARA PASAR EL LOGIN
            if(usuario.equals("admin") && contraseña.equals("admin") ){
                accesoPrueba();
                return;
            }

            final String k = "A";

                    //this.generateK(matricula);

            if (!k.equals("")) {

                version = getVersion();

                sendMessege("VALIDACION", usuario, contraseña);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                switch (estadoFinal) {

                                    case "-1":
                                        Toast.makeText(getBaseContext(), "No se pudo conectar con el servidor, por favor intente más tarde", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;


                                    case "0":
                                        Toast.makeText(getBaseContext(), "No se pudo conectar con el servidor, por favor intente más tarde", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;


                                    case "1":

                                        // TODO: ESCRIBIR DATOS EN PREFERENCIAS

                                        //  sharedP = writeOnPreferences(k, matricula, codigo);
                                      //  storage = writeOnStorage(k, matricula, codigo);
                                        break;


                                    case "2":
                                        Toast.makeText(getBaseContext(), "Error en el envío de datos", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;


                                    case "3":
                                        Toast.makeText(getBaseContext(), "El nombre del jugador ya esta registrado", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;

                                    case "4":
                                        Toast.makeText(getBaseContext(), "El usuario no puede estar vacio", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;

                                    case "5":
                                        Toast.makeText(getBaseContext(), "rror conexion BD", Toast.LENGTH_LONG).show();
                                        returnToStart();
                                        break;


                                    default:
                                        sharedP = false;
                                        storage = false;
                                        break;

                                }


                                if (sharedP && storage) {


                                    if (registrado) {


                                        estadoFinal = "-1";

                                      //  sendMessege("EXITO", matricula, codigo, android_id, k, version);

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {


                                                        switch (estadoFinal) {

                                                            case "-1":
                                                                Toast.makeText(getBaseContext(), "No se pudo conectar con el servidor, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                                                                break;

                                                            case "0":
                                                                Toast.makeText(getBaseContext(), "No se pudo conectar con el servidor, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                                                                returnToStart();
                                                                break;


                                                            case "1":
                                                                Toast.makeText(getBaseContext(), "EXITO SEGUNDO REQUEST", Toast.LENGTH_SHORT).show();
                                                                onSignupSuccess();
                                                                break;


                                                            case "2":
                                                                Toast.makeText(getBaseContext(), "Error en el envío de datos", Toast.LENGTH_LONG).show();
                                                                returnToStart();
                                                                break;

                                                            case "3":
                                                                Toast.makeText(getBaseContext(), "Error en la operacíón del servidor, por favor intente más tarde", Toast.LENGTH_LONG).show();
                                                                returnToStart();
                                                                break;

                                                            case "4":
                                                                Toast.makeText(getBaseContext(), "No se pudo concretar la operación", Toast.LENGTH_LONG).show();
                                                                returnToStart();
                                                                break;

                                                            case "5":
                                                                Toast.makeText(getBaseContext(), "Error bd", Toast.LENGTH_LONG).show();
                                                                returnToStart();
                                                                break;


                                                            case "6":
                                                                deviceNoRegistred();
                                                                break;


                                                            default:
                                                                sharedP = false;
                                                                storage = false;
                                                                break;

                                                        }

                                                        estadoFinal = "-1";
                                                        if (progressDialog.isShowing())
                                                            progressDialog.dismiss();
                                                    }
                                                }, 5500);


                                    } else {
                                        Toast.makeText(getBaseContext(), "Su matrícula ya se encuentra registrada", Toast.LENGTH_SHORT).show();
                                        estadoFinal = "-1";
                                    }

                                } else {

                                    if (!storage && sharedP) {
                                        Toast.makeText(getBaseContext(), "no se pudo guardar clave, revisar permisos de almacenamiento", Toast.LENGTH_LONG).show();
                                        deleteFromPreferences();
                                        estadoFinal = "-1";
                                    } else if (storage && !sharedP) {
                                        Toast.makeText(getBaseContext(), "no se pudo guardar clave", Toast.LENGTH_LONG).show();
                                        deleteFromExt();
                                        estadoFinal = "-1";
                                    } else if (!storage && !sharedP) {
                                        Toast.makeText(getBaseContext(), "no se pudo guardar clave", Toast.LENGTH_LONG).show();
                                        estadoFinal = "-1";
                                    }

                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();


                                    estadoFinal = "-1";
                                    returnToStart();
                                }


                            }
                        }, 5500);


            } else {
                Toast.makeText(this, "encriptación fallida", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

        } else {

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            onInternetFailed();
        }


    }


    private void deviceNoRegistred() {

        Toast.makeText(this, "La matrícula o el código ingresado es incorrecto o no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
        flag = true;
        sharedP = false;
        storage = false;
        estadoFinal = "-1";
        _signupButton.setEnabled(true);
        _matriculaText.requestFocus();
       // _matriculaText.setSelection(0);
        /* Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        */
    }


    private void deviceInUse() {

        Toast.makeText(this, "Su código ya está en uso", Toast.LENGTH_LONG).show();
        flag = true;
        sharedP = false;
        storage = false;
        estadoFinal = "-1";
        _signupButton.setEnabled(true);
        _matriculaText.requestFocus();
        //_matriculaText.setSelection(0);
        /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
    }


    private void returnToStart() {
        flag = true;
        sharedP = false;
        storage = false;
        estadoFinal = "-1";
        _signupButton.setEnabled(true);
        _matriculaText.requestFocus();
       // _matriculaText.setSelection(0);
        //Intent intent new Intent(getApplicationContext(), LoginActivity.class);
       // startActivity(intent);
       // finish();
       // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }


    private void deleteFromExt(){


        try {

            String state = Environment.getExternalStorageState();
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File folder = folder = new File(extStorageDirectory, "CacaavInfo");
                if (folder.exists()) {
                    folder.delete();
                }

            }

        } catch (Exception ex) {
            Log.e("", "Error al borrar fichero a ALM. EXT");
            ex.printStackTrace();

        }


    }


    private void deleteFromPreferences(){

        try {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("k");
            editor.remove("m");
            editor.remove("c");
            editor.commit();


            /*
            Map<String, ?> prefsMap = sharedPref.getAll();
            int i = 1;
            for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {

                Log.e("pref:", i + "): " + entry.getKey() + " value : " + entry.getValue().toString());
                i++;

            }

            */

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void sendMessege(String accion, String usuario, String contraseña) {


        JSONObject jsonObj = new JSONObject();


        try {
            jsonObj.put("accion", accion);
            jsonObj.put("usuario", usuario);
            jsonObj.put("contraseña", contraseña);


        } catch (Exception e) {

            Toast.makeText(this, "no se pudo parsear a json", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            estadoFinal = "0";
            flagRequest = true;
        }

        Log.e("json a enviar: ", jsonObj.toString());

        try {


            // Petición POST
            VolleySingleton.
                    getInstance(this).
                    addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    "http://10.20.30.7/hayequipo/validarUsuario.php",
                                    jsonObj,
                                    new Response.Listener<JSONObject>() {


                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Procesar la respuesta Json
                                            String estado = "";

                                            try {
                                                Log.e(TAG, "OK Volley: " + response.toString());
                                                estado = response.getString("estado");
                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }

                                            switch (estado) {
                                                case "1": // EXITO

                                                    Toast.makeText(getBaseContext(), "estado 1 exito", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "1";

                                                    break;

                                                case "2": // FALLIDO
                                                    Toast.makeText(getBaseContext(), "estado 2 fallido", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "2";
                                                    break;

                                                case "3": //JUGADOR YA EXISTE
                                                    Toast.makeText(getBaseContext(), "el nombre de usuario ya esta registrado", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "3";
                                                    break;

                                                case "4": //USUARIO VACIO
                                                    Toast.makeText(getBaseContext(), "usr vacio", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "4";
                                                    break;
                                                case "5": //ERROR BD
                                                    Toast.makeText(getBaseContext(), "ERROR BD", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "5";
                                                    break;

                                                default:
                                                   // Toast.makeText(getBaseContext(), "no hay estado", Toast.LENGTH_SHORT).show();
                                                    estadoFinal = "-1";
                                                    break;
                                            }

                                            flagRequest = true;

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Toast.makeText(getBaseContext(), "error volley!", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "Error Volley: " + error.getMessage() + "   net response: " + error.networkResponse);
                                            error.printStackTrace();
                                            estadoFinal = "0";
                                            flagRequest = true;


                                        }
                                    }


                            )
                    );


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "error en volley!", Toast.LENGTH_SHORT).show();
            estadoFinal = "0";
            flagRequest = true;

        }


    }


    private void onSignupSuccess() {
        _signupButton.setEnabled(false);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Su cuenta ha sido creada", Toast.LENGTH_LONG).show();
       // setContentView(R.layout.fancy_list);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("menu", "menuPrincipal");
        flag = false;
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "ingrese los datos correctamente", Toast.LENGTH_SHORT).show();

        _signupButton.setEnabled(true);
    }

    private void onInternetFailed() {
        Toast.makeText(this, "la conexión a internet falló", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;


        String address = _matriculaText.getText().toString();
        String codigo = _codigoText.getText().toString();


        if (address.isEmpty()) {
            _matriculaText.setError("El campo Usuario no puede estár vacio");
            valid = false;
        } else {
            _matriculaText.setError(null);
        }

        if (codigo.isEmpty()) {
            _codigoText.setError("El campo Contraseña no puede estár vacio");
            valid = false;
        } else {
            _codigoText.setError(null);
        }


        return valid;




    }


    private String generateK(String matricula) {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Encryption encryption = Encryption.getDefault(matricula, matricula + android_id, new byte[16]);
        String encrypted = encryption.encryptOrNull(matricula);
        if (encrypted != null || !encrypted.equals("")) {
            return encrypted;
        } else {
            return "";
        }
    }


    private boolean writeOnStorage(String k, String m, String c) {


        try {

            String state = Environment.getExternalStorageState();
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File folder = folder = new File(extStorageDirectory, "CacaavInfo");
                if (!folder.exists()) {
                    folder.mkdir();
                }

                File f = new File(folder, "CACAAV-infok.txt");

                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                new FileOutputStream(f));

                fout.write(k);
                fout.close();
                Log.e("EXTERNAL", "GUARDADO INFO K");

                File fi = new File(folder, "CACAAV-infom.txt");

                fout =
                        new OutputStreamWriter(
                                new FileOutputStream(fi));

                fout.write(m);
                fout.close();
                Log.e("EXTERNAL", "GUARDADO INFO M");


                File fil = new File(folder, "CACAAV-infoc.txt");

                fout =
                        new OutputStreamWriter(
                                new FileOutputStream(fil));

                fout.write(c);
                fout.close();
                Log.e("EXTERNAL", "GUARDADO INFO C");


                folder.setReadOnly();

                Toast.makeText(getBaseContext(), "GUARDADO TEXTO EN EXT", Toast.LENGTH_SHORT).show();

                return true;


            } else {
                Toast.makeText(getBaseContext(), " NO HAY ACCESO A ALM.EXT. ", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "error guardado EXT. ", Toast.LENGTH_SHORT).show();
            Log.e("Ficheros", "Error al escribir fichero a ALM. EXT");
            ex.printStackTrace();
            return false;
        }


    }


    private boolean writeOnPreferences(String k, String m, String c) {

        try {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("k", k);
            editor.putString("m", m);
            editor.putString("c", c);
            editor.commit();


            //////////////////debug : cuento preferencias.
            Map<String, ?> prefsMap = sharedPref.getAll();
            int i = 1;
            for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {

                Log.e("pref:", i + "): " + entry.getKey() + " value : " + entry.getValue().toString());
                i++;

            }

            ////////////////////////////////////////////////////////

            Toast.makeText(getBaseContext(), " GUARDADO EN SHP!", Toast.LENGTH_SHORT).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    private synchronized boolean isNetAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable());
    }




    private String getVersion() {

        try {

            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            int version = Build.VERSION.SDK_INT;
            String versionRelease = Build.VERSION.RELEASE;


            return manufacturer+" "+model+" API Level: " + version;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }


    public void accesoPrueba(){
        _signupButton.setEnabled(false);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Su cuenta ha sido creada", Toast.LENGTH_LONG).show();
        // setContentView(R.layout.fancy_list);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("menu", "menuPrincipal");
        flag = false;
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    @Override
    public void onBackPressed() {
       // moveTaskToBack(true);
        flag = false;
       // Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
       // startActivity(intent);
        finish();
        finishAffinity();
       // overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }


}