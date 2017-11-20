package edu.uade.sip2.hayequipo_android.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.conn.requestToBackend;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
import edu.uade.sip2.hayequipo_android.entities.Partido;

/**
 * Created by Usuario on 07/11/2017.
 */

public class PartidoActivity extends AppCompatActivity {


    @Bind(R.id.input_descripcion)
    EditText _descripcion;
    @Bind(R.id.input_fecha_partido)
    EditText _fecha;
    @Bind(R.id.input_hora_partido)
    EditText _hora;
    @Bind(R.id.input_lugar)
    EditText _lugar;
    @Bind(R.id.cantidad)
    EditText _participantes;
    @Bind(R.id.input_precio)
    EditText _precio;
    @Bind(R.id.btn_invitar_amigos)
    Button _invitar_amigos;
    @Bind(R.id.btn_guardar_cambios)
    Button  _guardar_cambios;
    @Bind(R.id.btn_publicar_partido)
    Button  _publicar_partido;
    @Bind(R.id.img_partido)
    ImageView  _img_partido;


    Button _agregar_usuario;
    Button _buscar_usuario;
    EditText _usuario;
    TextView _label_usuario;


    ArrayList<String> horas = new ArrayList<String>();
    private static int mi_partido_actual;
    private static int id_usr;
    private String usuario;
    private static String yo;
    String hora = "";
    Partido p = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);
        ButterKnife.bind(this);
        _participantes.setEnabled(false);
        _hora.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
       final int id_partido = intent.getIntExtra("id",0);
       final String yo = intent.getStringExtra("usuario");

        mi_partido_actual = id_partido;
        this.yo = yo;


        p  = HarcodedUsersAndPlays.getPartido(id_partido);
        if(p==null){
            Toast.makeText(getBaseContext(),"error, no se encontro partido",Toast.LENGTH_LONG);
        }else{

            _descripcion.setText(p.getDescripcion().toString());
            _lugar.setText(p.getLugar().toString());
            _fecha.setText(p.getFecha().toString());
            _hora.setText(p.getHora().toString());
            if(p.getPrecio()==0) {
                _precio.setText("0");
            }else{
                _precio.setText(String.valueOf(p.getPrecio()));
            }
            _participantes.setText(String.valueOf(p.getParticipantes())+"/"+p.getCantidad_participantes());
        }


        if(!p.getAvatar().equals("")){
            Context context = _img_partido.getContext();
            int id = context.getResources().getIdentifier(p.getAvatar(), "drawable", context.getPackageName());
            _img_partido.setImageResource(id);
        }


        _invitar_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAmigos(PartidoActivity.this);
            }
        });

        _guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nueva_descripcion = _descripcion.getText().toString();
                String nuevo_lugar = _lugar.getText().toString();
                String nueva_fecha = _fecha.getText().toString();
                String nueva_hora = hora;
                if(nueva_descripcion.equals("") || nuevo_lugar.equals("") || nueva_fecha.equals("") || nueva_hora.equals("")){
                    //TODO: error
                }else{
                    boolean resultado = HarcodedUsersAndPlays.cambiarPartido(mi_partido_actual,nueva_descripcion,nuevo_lugar,nueva_fecha,nueva_hora);
                    if(resultado){
                        Toast.makeText(getBaseContext(),"cambios guardados!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getBaseContext(),"error en los cambios",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });






                _publicar_partido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //TODO : BUSCAR LOCACION MAPA Y GUARDAR COORDERNADAS
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                        startActivity(intent);
                    }
                });

    }



    private void showDialogAmigos(Context context) {

        //TODO: CAMBIAR HARCODEO DE ACA
        HarcodedUsersAndPlays.agregarUsuariosHarcodeados();

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.busqueda);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        _agregar_usuario = (Button) dialog.findViewById(R.id.btn_agregar_usuario);
        _buscar_usuario = (Button) dialog.findViewById(R.id.btn_buscar_usuario);
        _label_usuario = (TextView) dialog.findViewById(R.id.label_usuario);
        _usuario = (EditText) dialog.findViewById(R.id.input_usr);

        ImageView view = (ImageView) dialog.findViewById(R.id.black_cross);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
        _agregar_usuario.setEnabled(false);




        _buscar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id_usr = HarcodedUsersAndPlays.buscarUsuario(_usuario.getText().toString());

                if(!_usuario.equals("")){


                    if(id_usr!=-1){
                        usuario = _usuario.getText().toString();
                        _label_usuario.setText("se encontro el usuario");
                        _label_usuario.setTextColor(getColor(R.color.black));
                        _agregar_usuario.setEnabled(true);


                    }else{
                        _label_usuario.setText("no se ha encontrado el usuario");
                        _label_usuario.setTextColor(getColor(R.color.black));
                    }

                    hideKeyboard(view);

                }
            }
        });


        _agregar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mi partido actual:  "," "+mi_partido_actual);
                boolean resultado = HarcodedUsersAndPlays.agregarUsuario(mi_partido_actual,id_usr);
                if(resultado) {
                    Log.e("agregar usr a partido", "agregado!");
                    requestToBackend.enviarPeticion(mi_partido_actual,yo,usuario,getBaseContext());
                    Toast.makeText(getBaseContext(), "agregado!", Toast.LENGTH_LONG).show();
                    _label_usuario.setText("Agregado!");
                    String participantes = _participantes.getText().toString();
                    if(p!=null){
                        int cantidad = p.getParticipantes();
                        _participantes.setText(String.valueOf(cantidad+1));
                    }else{
                        _participantes.setText("-");
                    }



                }else{
                    Log.e("agregar usr a partido", "error! ");
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
                    _label_usuario.setText("Error(?!");
                }

            }
        });


    }



    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


        }


}
