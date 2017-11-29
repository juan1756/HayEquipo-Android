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


/*
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
                   view.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                   view.setFocusable(false);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                    params.gravity = Gravity.TOP;
/

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
*/

    }


    private void signup() {
        Log.d(TAG, "Signup");


        final String usuario = _matriculaText.getText().toString();
        final String contraseña = _codigoText.getText().toString();
        if(usuario.equals("") || contraseña.equals("")){
            Toast.makeText(getBaseContext(),"los campos no pueden estar vacios",Toast.LENGTH_SHORT).show();
        }else{


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("jugador", usuario);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }


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