package edu.uade.sip2.hayequipo_android.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.dto.JugadorDTO;
import edu.uade.sip2.hayequipo_android.dto.ModalidadDTO;
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.dto.SolicitudDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.TipoPrivacidadEnum;
import edu.uade.sip2.hayequipo_android.utils.Avatars;

/**
 * Created by Usuario on 07/11/2017.
 */
public class PartidoActivity extends AppCompatActivity {

    public static final String EXTRA_PARTIDO = "partido";
    public static final String EXTRA_MODALIDADES = "modalidades";

    @Bind(R.id.input_descripcion)
    EditText campoComentario;
    @Bind(R.id.input_fecha_partido)
    EditText campoFecha;
    @Bind(R.id.input_hora_partido)
    EditText campoHora;
    @Bind(R.id.input_lugar)
    EditText campoLugar;
    @Bind(R.id.campo_modalidad)
    Spinner campoModalidad;
    @Bind(R.id.input_precio)
    EditText campoPrecio;
    @Bind(R.id.campo_jugadores)
    EditText campoJugadores;
    @Bind(R.id.btn_invitar_amigos)
    Button botonInvitar;
    @Bind(R.id.btn_guardar_cambios)
    Button botonGuardar;
    @Bind(R.id.btn_publicar_partido)
    Button botonPublicar;
    @Bind(R.id.img_partido)
    ImageView imagenAvatar;

    Button _agregar_usuario;
    Button _buscar_usuario;
    EditText _usuario;
    TextView _label_usuario;

    private ObjectMapper mapper;
    // Objecto a modificar
    private PartidoDTO partido;
    // Modalidades
    private List<ModalidadDTO> modalidades;
    //JUGADOR AL QUE LE QUIERO ENVIAR SOLICITUD
    private JugadorDTO elegido;

    private String usuario_logeado = "";

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        usuario_logeado = getIntent().getStringExtra("usuario_logeado");
        partido = (PartidoDTO) getIntent().getSerializableExtra(EXTRA_PARTIDO);
        modalidades = (List<ModalidadDTO>) getIntent().getSerializableExtra(EXTRA_MODALIDADES);
        ArrayAdapter<ModalidadDTO> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, modalidades);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        setContentView(R.layout.activity_partido);
        ButterKnife.bind(this);

        campoModalidad.setAdapter(adapter);
        editableCampos(false);

        int posicionModalidad = buscarModalidad(partido.getModalidad().getCodigo());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es","ES"));
        String fechaFormateada = sdf.format(partido.getFecha());
        sdf = new SimpleDateFormat("hh:mm", new Locale("es","ES"));
        String horaFormateada = sdf.format(partido.getFecha());

        campoFecha.setText(fechaFormateada);
        campoHora.setText(horaFormateada);
        campoComentario.setText(partido.getComentario());
        campoLugar.setText(partido.getLocalizacion().getDireccion());
        campoPrecio.setText(partido.getPrecio().toString());
        campoModalidad.setSelection(posicionModalidad);
        List<JugadorDTO> jugadores = partido.getJugadoresAceptado();
        if(jugadores!=null){
            for(JugadorDTO j : jugadores){

                try{
                    campoJugadores.setText(campoJugadores.getText().toString()+" - "+j.getNombre().toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        if (partido.getAvatar() != null && !partido.getAvatar().isEmpty()){
            imagenAvatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), partido.getAvatar()));
        }

        try {
          //  Toast.makeText(this,"usr logeado:"+usuario_logeado.toString(),Toast.LENGTH_LONG).show();
           if (!usuario_logeado.equals(partido.getCreador().getNombre().toString())) {
                botonPublicar.setEnabled(false);
                botonPublicar.setActivated(false);
                botonPublicar.setBackgroundColor(Color.DKGRAY);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

//        campoHora.setText(p.getHora().toString());
//        if(p.getPrecio()==0) {
//            campoPrecio.setText("0");
//        }else{
//            campoPrecio.setText(String.valueOf(p.getPrecio()));
//        }
//        _participantes.setText(String.valueOf(p.getParticipantes())+"/"+p.getCantidad_participantes());

        botonInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAmigos(PartidoActivity.this);
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String nueva_descripcion = campoComentario.getText().toString();
//                String nuevo_lugar = campoLugar.getText().toString();
//                String nueva_fecha = campoFecha.getText().toString();
//                String nueva_hora = hora;
//                if(nueva_descripcion.equals("") || nuevo_lugar.equals("") || nueva_fecha.equals("") || nueva_hora.equals("")){
//                    //TODO: error
//                }else{
//                    boolean resultado = HarcodedUsersAndPlays.cambiarPartido(mi_partido_actual,nueva_descripcion,nuevo_lugar,nueva_fecha,nueva_hora);
//                    if(resultado){
//                        Toast.makeText(getBaseContext(),"cambios guardados!",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(getBaseContext(),"error en los cambios",Toast.LENGTH_LONG).show();
//                    }
//                }
            }
        });

        if (partido.getTipoPrivacidad().compareTo(TipoPrivacidadEnum.PUBLICO) == 0){
            botonPublicar.setVisibility(View.GONE);
        }

        botonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(PartidoActivity.this);
                builder.setTitle("Confirmacion Publicacion")
                        .setMessage("El partido sera publicado, para que cualquier persona pueda unirse al partido.\n\nSi esta de acuerdo, presione Publicar")
                        .setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Publico el partido
                                try {
                                    VolleySingleton
                                            .getInstance(getApplicationContext())
                                            .addToRequestQueue(
                                                    new JsonObjectRequest(
                                                            getString(R.string.servicio_url) + getString(R.string.servicio_publicar_partido),
                                                            partido.toJsonObject(),
                                                            new Response.Listener<JSONObject>() {

                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        ObjectMapper mapper = new ObjectMapper();
                                                                        PartidoDTO partidoPublicado = mapper.readValue(response.toString(), PartidoDTO.class);

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {

                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    error.printStackTrace();
                                                                }
                                                            }
                                                    ));
                                } catch (JsonProcessingException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                alert = builder.create();
                alert.show();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private int buscarModalidad(Long codigo) {
        for (int i = 0; i < modalidades.size(); i++){
            ModalidadDTO modalidad = modalidades.get(i);
            if (modalidad.getCodigo().compareTo(codigo) == 0){
                return i;
            }
        }
        return -1;
    }

    private void editableCampos(boolean edita) {
        campoModalidad.setEnabled(edita);
        campoComentario.setEnabled(edita);
        campoFecha.setEnabled(edita);
        campoHora.setEnabled(edita);
        campoLugar.setEnabled(edita);
        campoPrecio.setEnabled(edita);
        campoJugadores.setEnabled(edita);

    }


    private void showDialogAmigos(Context context) {


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

                String apodo = _usuario.getText().toString();

               if(!apodo.equals("")){
                   try {
                       JugadorDTO j = new JugadorDTO();
                       j.setNombre(apodo);

                       VolleySingleton
                               .getInstance(getApplicationContext())
                               .addToRequestQueue(
                                       new JsonObjectRequest(
                                               Request.Method.POST,
                                               getString(R.string.servicio_url) + getString(R.string.servicio_buscar_jugador),
                                               j.toJsonObject(),
                                               new Response.Listener<JSONObject>() {

                                                   @Override
                                                   public void onResponse(JSONObject response) {
                                                       try {
                                                         JugadorDTO encontrado = mapper.readValue(response.toString(), JugadorDTO.class);
                                                         Log.e("response",response.toString());
                                                         if(encontrado!=null && encontrado.getCodigo()>0){
                                                             elegido = encontrado;
                                                             _label_usuario.setText("se encontro el usuario");
                                                              _label_usuario.setTextColor(getColor(R.color.black));
                                                            _agregar_usuario.setEnabled(true);
                                                         }else{
                                                             _label_usuario.setText("no se ha encontrado el jugador");
                                                             _label_usuario.setTextColor(getColor(R.color.black));
                                                             elegido = null;
                                                         }


                                                       } catch (Exception e) {
                                                           e.printStackTrace();
                                                           Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();
                                                           elegido = null;
                                                       }
                                                   }
                                               },
                                               new Response.ErrorListener() {

                                                   @Override
                                                   public void onErrorResponse(VolleyError error) {
                                                       error.printStackTrace();
                                                       elegido = null;
                                                   }
                                               }
                                       )
                               )
                       ;

                   }catch(Exception e){
                       e.printStackTrace();
                       elegido = null;
                   }
                   hideKeyboard(view);

                }else{
                   Toast.makeText(getBaseContext(),"por favor ingrese un nombre",Toast.LENGTH_SHORT).show();
               }
            }
        });

        _agregar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(elegido!=null){
                    try {
                        SolicitudDTO solicitudDTO = new SolicitudDTO();
                        solicitudDTO.setJugador(elegido);
                        solicitudDTO.setPartido(partido);

                        VolleySingleton
                                .getInstance(getApplicationContext())
                                .addToRequestQueue(
                                        new JsonObjectRequest(
                                                getString(R.string.servicio_url) + getString(R.string.servicio_solicitud_agregar_jugador),
                                                solicitudDTO.toJsonObject(),
                                                new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            Log.e("reta: ",response.toString());
                                                            Toast.makeText(getBaseContext(),"se ha enviado la peticion al usuario!",Toast.LENGTH_LONG).show();


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                }
                                        )
                                )
                        ;

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    hideKeyboard(view);

                }else{
                    Log.e("agregar usr a partido", "error! ");
                    Toast.makeText(getBaseContext(), "error, no hay jugador seleccionado", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
