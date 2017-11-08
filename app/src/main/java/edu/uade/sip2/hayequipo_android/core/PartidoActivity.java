package edu.uade.sip2.hayequipo_android.core;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
import edu.uade.sip2.hayequipo_android.entities.Partido;

/**
 * Created by Usuario on 07/11/2017.
 */

public class PartidoActivity extends AppCompatActivity {


    @Bind(R.id.input_nombre)
    EditText _nombre;
    @Bind(R.id.input_fecha)
    EditText _fecha;
    @Bind(R.id.input_lugar)
    EditText _lugar;
    @Bind(R.id.cantidad)
    EditText _participantes;
    @Bind(R.id.btn_invitar_amigos)
    Button _invitar_amigos;
    @Bind(R.id.btn_guardar_cambios)
    Button  _guardar_cambios;



    Button _agregar_usuario;
    Button _buscar_usuario;
    EditText _usuario;
    TextView _label_usuario;


    private static int mi_partido_actual;
    private static int id_usr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);
        ButterKnife.bind(this);
        _participantes.setEnabled(false);
        Intent intent = getIntent();
       final int id_partido = intent.getIntExtra("id",0);
       mi_partido_actual = id_partido;

        Partido p  = HarcodedUsersAndPlays.getPartido(id_partido);
        if(p==null){
            Toast.makeText(getBaseContext(),"error no hay partido (?)",Toast.LENGTH_LONG);
        }else{

            _nombre.setText(p.getNombre().toString());
            _lugar.setText(p.getLugar().toString());
            _fecha.setText(p.getFecha().toString());
            _participantes.setText(String.valueOf(p.getParticipantes()));
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
                String nuevo_nombre = _nombre.getText().toString();
                String nuevo_lugar = _lugar.getText().toString();
                String nueva_fecha = _fecha.getText().toString();
                if(nuevo_nombre.equals("") || nuevo_lugar.equals("") || nueva_fecha.equals("")){
                    //TODO: error
                }else{
                    boolean resultado = HarcodedUsersAndPlays.cambiarPartido(mi_partido_actual,nuevo_nombre,nuevo_lugar,nueva_fecha);
                    if(resultado){
                        Toast.makeText(getBaseContext(),"cambios guardados!",Toast.LENGTH_LONG);
                    }else{
                        Toast.makeText(getBaseContext(),"error en los cambios!",Toast.LENGTH_LONG);
                    }
                }
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
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.80);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.80);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
        _agregar_usuario.setEnabled(false);




        _buscar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id_usr = HarcodedUsersAndPlays.buscarUsuario(_usuario.getText().toString());

                if(!_usuario.equals("")){


                    if(id_usr!=-1){

                        _label_usuario.setText("se encontro el usuario");
                        _label_usuario.setTextColor(getColor(R.color.white));
                        _agregar_usuario.setEnabled(true);


                    }else{
                        _label_usuario.setText("no se ha encontrado el usuario");
                        _label_usuario.setTextColor(getColor(R.color.white));
                    }
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
                    Toast.makeText(getBaseContext(), "agregado!", Toast.LENGTH_LONG);
                    _label_usuario.setText("Agregado!");
                    String participantes = _participantes.getText().toString();
                    int p = Integer.parseInt(participantes);
                    _participantes.setText(String.valueOf(p+1));

                }else{
                    Log.e("agregar usr a partido", "error! no se encontro partido ?");
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG);
                    _label_usuario.setText("Error(?!");
                }

            }
        });


    }





    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


        }


}
