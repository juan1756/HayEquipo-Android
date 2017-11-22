package edu.uade.sip2.hayequipo_android.core;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.conn.requestToBackend;
import edu.uade.sip2.hayequipo_android.dto.ModalidadDTO;
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
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

    // Objecto a modificar
    private PartidoDTO partido;
    // Modalidades
    private List<ModalidadDTO> modalidades;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(partido.getFecha());
        sdf = new SimpleDateFormat("hh:mm");
        String horaFormateada = sdf.format(partido.getFecha());

        campoFecha.setText(fechaFormateada);
        campoHora.setText(horaFormateada);
        campoComentario.setText(partido.getComentario());
        campoLugar.setText(partido.getLocalizacion().getDireccion());
        campoPrecio.setText(partido.getPrecio().toString());
        campoModalidad.setSelection(posicionModalidad);
        if (partido.getAvatar() != null && !partido.getAvatar().isEmpty()){
            imagenAvatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), partido.getAvatar()));
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

        botonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    }


    private void showDialogAmigos(Context context) {

//        //TODO: CAMBIAR HARCODEO DE ACA
//        HarcodedUsersAndPlays.agregarUsuariosHarcodeados();
//
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.busqueda);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(true);
//
//        _agregar_usuario = (Button) dialog.findViewById(R.id.btn_agregar_usuario);
//        _buscar_usuario = (Button) dialog.findViewById(R.id.btn_buscar_usuario);
//        _label_usuario = (TextView) dialog.findViewById(R.id.label_usuario);
//        _usuario = (EditText) dialog.findViewById(R.id.input_usr);
//
//        ImageView view = (ImageView) dialog.findViewById(R.id.black_cross);
//
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
//        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
//        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
//
//        dialog.show();
//        _agregar_usuario.setEnabled(false);
//
//
//
//
//        _buscar_usuario.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                id_usr = HarcodedUsersAndPlays.buscarUsuario(_usuario.getText().toString());
//
//                if(!_usuario.equals("")){
//
//
//                    if(id_usr!=-1){
//                        usuario = _usuario.getText().toString();
//                        _label_usuario.setText("se encontro el usuario");
//                        _label_usuario.setTextColor(getColor(R.color.black));
//                        _agregar_usuario.setEnabled(true);
//
//
//                    }else{
//                        _label_usuario.setText("no se ha encontrado el usuario");
//                        _label_usuario.setTextColor(getColor(R.color.black));
//                    }
//
//                    hideKeyboard(view);
//
//                }
//            }
//        });
//
//
//        _agregar_usuario.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("mi partido actual:  "," "+mi_partido_actual);
//                boolean resultado = HarcodedUsersAndPlays.agregarUsuario(mi_partido_actual,id_usr);
//                if(resultado) {
//                    Log.e("agregar usr a partido", "agregado!");
//                    requestToBackend.enviarPeticion(mi_partido_actual,yo,usuario,getBaseContext());
//                    Toast.makeText(getBaseContext(), "agregado!", Toast.LENGTH_LONG).show();
//                    _label_usuario.setText("Agregado!");
//                    String participantes = _participantes.getText().toString();
//                    if(p!=null){
//                        int cantidad = p.getParticipantes();
//                        _participantes.setText(String.valueOf(cantidad+1));
//                    }else{
//                        _participantes.setText("-");
//                    }
//
//
//
//                }else{
//                    Log.e("agregar usr a partido", "error! ");
//                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
//                    _label_usuario.setText("Error(?!");
//                }
//
//            }
//        });
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
