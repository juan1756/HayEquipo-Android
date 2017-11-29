package edu.uade.sip2.hayequipo_android.core;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.adapter.DireccionAutoCompleteAdapter;
import edu.uade.sip2.hayequipo_android.adapter.JugadorAdapter;
import edu.uade.sip2.hayequipo_android.adapter.SolicitudPartidoAdapter;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.dto.JugadorDTO;
import edu.uade.sip2.hayequipo_android.dto.LocalizacionDTO;
import edu.uade.sip2.hayequipo_android.dto.ModalidadDTO;
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.dto.SolicitudDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.TipoPrivacidadEnum;
import edu.uade.sip2.hayequipo_android.utils.AvatarPickerDialogFragment;
import edu.uade.sip2.hayequipo_android.utils.Avatars;
import edu.uade.sip2.hayequipo_android.utils.CalendarUtil;
import edu.uade.sip2.hayequipo_android.utils.DatePickerFragment;
import edu.uade.sip2.hayequipo_android.utils.JsonArrayRequest;
import edu.uade.sip2.hayequipo_android.utils.TimePickerFragment;
import edu.uade.sip2.hayequipo_android.utils.WaitTime;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AvatarPickerDialogFragment.AvatarPickerDialogListener,
SwipeRefreshLayout.OnRefreshListener {


    private static final int ACTIVIDAD_MAPA = 100;

    private JugadorDTO usuarioLogeado; // USUARIO QUE ESTA LOGUEADO
    private ObjectMapper mapper; // OBJECTO JACKSON
    private FancyAdapter listaAdapter;
    private String avatarSeleccionado = "";
    private String lista;

    // CAMPOS PARA EL DIALOGO
    private AutoCompleteTextView campoLugar;
    private TextView campoFecha;
    private TextView campoHora;
    private Spinner campoModalidad;
    private List<ModalidadDTO> modalidades = new ArrayList<>();
    private List<JugadorDTO> jugadores = new ArrayList<>();
    private LatLng posicionMarcada;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se crea una vez y se utiliza
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ListView listaView = findViewById(android.R.id.list);

        listaAdapter = new FancyAdapter(new ArrayList<PartidoDTO>());
        listaView.setSelector(R.drawable.list_selector);
        listaView.setDrawSelectorOnTop(false);
        listaView.setAdapter(listaAdapter);
        this.lista = "partidos";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis Partidos");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPartidoDialog(MainActivity.this);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int posicion, long id) {
                if(lista.equals("partidos")) {
                    cambiarPartido((PartidoDTO) listaAdapter.getItem(posicion));
                }else if(lista.equals("solicitudes")){
                    final ImageView item_aceptar = (ImageView) findViewById(R.id.item_solicitud_aceptar);

                    item_aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("solicitud","acepto");

                            SolicitudDTO sol = (SolicitudDTO) adapterView.getAdapter().getItem(posicion);


                            if(sol!=null){
                                enviarSolicitudAcepto(sol);
                                Toast.makeText(getBaseContext(),"remover solicitud!"+sol.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    final ImageView item_rechazar = (ImageView) findViewById(R.id.item_solicitud_rechazar);
                    item_rechazar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("solicitud","rechazo");
                            SolicitudDTO sol = (SolicitudDTO) adapterView.getAdapter().getItem(posicion);
                            Toast.makeText(getBaseContext(),"remover solicitud!"+sol.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Realizo un login (En caso de que no tengo disponible un usuario)
        try {
            hacerLogin();
        } catch (Exception e){
            JugadorDTO ejemplo = new JugadorDTO();
            ejemplo.setNombre("lis");
            usuarioLogeado = ejemplo;
            e.printStackTrace();
        }



        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        Toast.makeText(getBaseContext(),"TODO: on refresh list swipe",Toast.LENGTH_SHORT).show();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                       // myUpdateOperation();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

//        todosLosPartidos(1);
    }

    /**
     * METODO QUE NO VA IR POR EL TEMA DE HACER EL LOGIN Y GUARDARME EN LA VARIABLE
     * usuarioLogeado, EL OBJETO DE JugadorDTO
     */
    private void hacerLogin() throws JsonProcessingException, JSONException {
        JugadorDTO ejemplo = new JugadorDTO();
      ejemplo.setNombre("lis");
    //   ejemplo.setNombre("pepe");

        VolleySingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(
                        new JsonObjectRequest(
                                getString(R.string.servicio_url) + getString(R.string.servicio_buscar_jugador),
                                ejemplo.toJsonObject(),
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            usuarioLogeado = mapper.readValue(response.toString(), JugadorDTO.class);
                                            todosLosPartidos(1);

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
                        )
                )
        ;
    }

    private void todosLosPartidos(int refresh) {

        getSupportActionBar().setTitle("Mis Partidos");
        // PROGRESS DIALOG MUY SIMPLE
        try {
            ProgressDialog mDialog = new ProgressDialog(this);
            WaitTime wait = new WaitTime(mDialog);
            wait.execute();
        }catch(Exception e){
            e.printStackTrace();
        }

        //Para cuando entro a travès del drawertoogle
        if(refresh == 0){
            ListView listaView = findViewById(android.R.id.list);
            listaAdapter = new FancyAdapter(new ArrayList<PartidoDTO>());
            listaView.setSelector(R.drawable.list_selector);
            listaView.setDrawSelectorOnTop(false);
            listaView.setAdapter(listaAdapter);
        }

        // Obtengo las modalidades
        VolleySingleton
                .getInstance(getApplicationContext())
                .addToRequestQueue(
                        new JsonArrayRequest(
                                getString(R.string.servicio_url) + getString(R.string.servicio_obtener_modalidad),
                                new Response.Listener<JSONArray>() {

                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try {
                                            ModalidadDTO[] m = mapper.readValue(response.toString(), ModalidadDTO[].class);
                                            modalidades = Arrays.asList(m);
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
                        )
                )
        ;

        // Obtengo los partidos
        try {
            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonArrayRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_obtener_partidos_por_jugador),
                                    usuarioLogeado.toJsonObject(),
                                    new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                PartidoDTO[] m = mapper.readValue(response.toString(), PartidoDTO[].class);
                                                listaAdapter.agregarPartido(Arrays.asList(m));
                                                lista = "partidos";

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
                            )
                    )
            ;




        } catch (JsonProcessingException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void enviarSolicitudAcepto(SolicitudDTO solicitud){
        try {
            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonObjectRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_solicitud_aceptar_jugador),
                                    solicitud.toJsonObject(),
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject jsonObject) {
                                            try {
                                                Toast.makeText(getBaseContext(),"solicitud aceptada!",Toast.LENGTH_LONG).show();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getBaseContext(),"error solicitud!",Toast.LENGTH_LONG).show();
                                            error.printStackTrace();
                                        }
                                    }
                            )
                    )
            ;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void enviarSolicitudRechazo(SolicitudDTO solicitud){
        try {

            Log.e("solicitud id",solicitud.getCodigo().toString());

            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonObjectRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_solicitud_rechazar_jugador),
                                    solicitud.toJsonObject(),
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject jsonObject) {
                                            try {
                                                Toast.makeText(getBaseContext(),"solicitud rechazada!",Toast.LENGTH_LONG).show();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getBaseContext(),"error solicitud!",Toast.LENGTH_LONG).show();
                                            error.printStackTrace();

                                        }
                                    }
                            )
                    )
            ;
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private void cambiarAmigos(){
        getSupportActionBar().setTitle("Mis Amigos");
        try {
            ProgressDialog mDialog = new ProgressDialog(this);
            WaitTime wait = new WaitTime(mDialog);
            wait.execute();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonArrayRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_obtener_jugadores),
                                    new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                JugadorDTO[] j = mapper.readValue(response.toString(), JugadorDTO[].class);
                                              //  modalidades = Arrays.asList(m);
                                                //Log.e("jug",j.toString());
                                                JugadorAdapter jugadorAdapter = new JugadorAdapter(new ArrayList<JugadorDTO>(),getBaseContext());
                                                ListView listaView = findViewById(android.R.id.list);
                                                listaView.setSelector(R.drawable.list_selector);
                                                  listaView.setDrawSelectorOnTop(false);

                                                jugadorAdapter.agregarJugador(Arrays.asList(j));
                                                listaView.setAdapter(jugadorAdapter);
                                                lista = "amigos";

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
                            )
                    )
            ;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void cambiarSolicitudesPartidos() {

        getSupportActionBar().setTitle("Mis Solicitudes Partidos");

        try {
            SolicitudDTO solicitudDTO = new SolicitudDTO();
            solicitudDTO.setJugador(usuarioLogeado);

            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonArrayRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_solicitud_pendiente),
                                    solicitudDTO.toJsonObject(),
                                    new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                SolicitudDTO[] j = mapper.readValue(response.toString(), SolicitudDTO[].class);
                                                Log.e("solicitud partido",j.toString());
                                                //  modalidades = Arrays.asList(m);
                                                //Log.e("jug",j.toString());
                                                SolicitudPartidoAdapter solicitud = new SolicitudPartidoAdapter(new ArrayList<SolicitudDTO>(),getBaseContext());
                                                ListView listaView = findViewById(android.R.id.list);
                                                listaView.setSelector(R.drawable.list_selector);
                                                listaView.setDrawSelectorOnTop(false);
                                                lista = "solicitudes";
                                                solicitud.agregarSolicitud(Arrays.asList(j));
                                                listaView.setAdapter(solicitud);




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
                            )
                    )
            ;
        }catch(Exception e){
            e.printStackTrace();
        }




    }

    private void cambiarCancha() {
        Intent intent = new Intent(this, CanchaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void cambiarPartido(PartidoDTO partido) {
        Intent intent = new Intent(this, PartidoActivity.class);
        intent.putExtra(PartidoActivity.EXTRA_PARTIDO, partido);
        intent.putExtra(PartidoActivity.EXTRA_MODALIDADES, (Serializable) modalidades);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void showTimePickerDialog(final TextView horaView) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {

            @SuppressLint("SimpleDateFormat")
            public void onTimeSet(TimePicker timePicker, int hora, int minutos) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minutos);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String horaFormateada = sdf.format(calendar.getTime());

                horaView.setText(horaFormateada);
                horaView.setHint(horaFormateada);
            }
        });
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog(final TextView fechaView) {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SimpleDateFormat")
            public void onDateSet(DatePicker datePicker, int anno, int mes, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, anno);
                calendar.set(Calendar.MONTH, mes);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaFormateada = sdf.format(calendar.getTime());

                fechaView.setText(fechaFormateada);
                fechaView.setHint(fechaFormateada);
                campoHora.callOnClick();
            }
        });
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showPartidoDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.crear_partido);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        posicionMarcada = null;
        ImageView botonCerrarDialogo = dialog.findViewById(R.id.black_cross);
        Button botonCrearPartido = dialog.findViewById(R.id.btn_crear_partido);

        final EditText campoNombre = dialog.findViewById(R.id.input_nombre_partido);
        final EditText campoDescripcion = dialog.findViewById(R.id.input_descripcion_partido);
        campoFecha = dialog.findViewById(R.id.input_fecha_partido);
        campoHora = dialog.findViewById(R.id.input_hora_partido);
        final EditText campoPrecio = dialog.findViewById(R.id.input_precio);
        final TextView avatarView = dialog.findViewById(R.id.input_avatar_partido);
        final CheckBox campoEsPublico = dialog.findViewById(R.id.campo_es_publico);

        // Configuro el campo de modalidad
        campoModalidad = dialog.findViewById(R.id.campo_modalidad);
        ArrayAdapter<ModalidadDTO> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, modalidades);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        campoModalidad.setAdapter(adapter);

        // Configuro el boton del mapa
        final ImageButton botonSeleccionarMapa = dialog.findViewById(R.id.boton_seleccionar_mapa);
        botonSeleccionarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapaPartidoActivity.class);
                intent.putExtra(MapaPartidoActivity.EXTRA_JUGADOR, usuarioLogeado);
                intent.putExtra(MapaPartidoActivity.EXTRA_ACCION, MapaPartidoActivity.SELECCIONAR);
                if (posicionMarcada != null){
                    intent.putExtra(MapaPartidoActivity.EXTRA_POSICION, posicionMarcada);
                }
                startActivityForResult(intent, ACTIVIDAD_MAPA);
            }
        });
        botonSeleccionarMapa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, R.string.titulo_seleccionar_partido_mapa, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Configuro el campo lugar para que se pueda buscar una direccion, cuando es 5 letras
        campoLugar = dialog.findViewById(R.id.input_lugar_partido);
        campoLugar.setThreshold(5);
        campoLugar.setAdapter(new DireccionAutoCompleteAdapter(context));
        campoLugar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Address direccion = (Address) adapterView.getItemAtPosition(position);
                StringBuilder direccionCompleta = new StringBuilder()
                        .append(direccion.getThoroughfare())
                        .append(direccion.getSubThoroughfare() == null ? "" : " " + direccion.getSubThoroughfare() )
                        .append(", ")
                        .append(direccion.getLocality());

                posicionMarcada = new LatLng(direccion.getLatitude(), direccion.getLongitude());
                campoLugar.setText(direccionCompleta);
            }
        });
        campoLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoLugar.getText().toString().isEmpty()){
                    botonSeleccionarMapa.performClick();
                }
            }
        });

        botonCerrarDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int) (displayMetrics.widthPixels * 0.95);
        int dialogHeight = (int) (displayMetrics.heightPixels * 0.95);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        dialog.show();

        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAvatarSelectDialog();
            }
        });

        // CONFIGURACION DE LA HORA
        campoHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(campoHora);
            }
        });

        // CONFIGURACION DE LA FECHA
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("fecha on click", "on");
                showDatePickerDialog(campoFecha);
            }
        });

        botonCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partidoValido(campoNombre.getText().toString(),campoFecha.getText().toString(),campoHora.getText().toString(),campoLugar.getText().toString())){
                    try {
                        final PartidoDTO partido = new PartidoDTO();
                        partido.setCreador(usuarioLogeado);

                        LocalizacionDTO localizacion = new LocalizacionDTO();
                        localizacion.setDireccion(campoLugar.getText().toString());
                        localizacion.setLatitud(posicionMarcada.latitude);
                        localizacion.setLongitud(posicionMarcada.longitude);

                        ModalidadDTO modalidad = (ModalidadDTO) campoModalidad.getSelectedItem();

                        Double precio = 0D;
                        if (campoPrecio.getText() != null && !campoPrecio.getText().toString().isEmpty()){
                            precio = Double.valueOf(campoPrecio.getText().toString());
                        }

                        partido.setFecha(CalendarUtil.formatearFechaHora(campoFecha.getText().toString(), campoHora.getText().toString()));
                        partido.setApodo(campoNombre.getText().toString());
                        partido.setComentario(campoDescripcion.getText().toString());
                        partido.setPrecio(precio);
                        partido.setAvatar(avatarSeleccionado);
                        partido.setModalidad(modalidad);
                        partido.setLocalizacion(localizacion);
                        partido.setTipoPrivacidad( campoEsPublico.isChecked() ? TipoPrivacidadEnum.PUBLICO : TipoPrivacidadEnum.PRIVADO);

                        // Creo el partido
                        VolleySingleton
                                .getInstance(getApplicationContext())
                                .addToRequestQueue(
                                        new JsonObjectRequest(
                                                getString(R.string.servicio_url) + getString(R.string.servicio_crear_partido),
                                                partido.toJsonObject(),
                                                new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            PartidoDTO partidoCreado = mapper.readValue(response.toString(), PartidoDTO.class);

                                                            listaAdapter.agregarPartido(partidoCreado);
                                                            dialog.dismiss();
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
                    } catch (JsonProcessingException | JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"por favor complete todos los campos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean partidoValido(String nombre,String fecha,String hora,String lugar) {
        return !(nombre.equals("") || fecha.equals("") || hora.equals("") || lugar.equals(""));
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        Toast.makeText(getBaseContext(),"on refresh list swipe",Toast.LENGTH_SHORT).show();
        //todosLosPartidos(0);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ACTIVIDAD_MAPA:
                switch (resultCode){
                    case MapaPartidoActivity.RESULTADO_UBICACION_SELECCIONADA:
                        posicionMarcada = data.getExtras().getParcelable(MapaPartidoActivity.EXTRA_LOCALIZACION_MARCADA);
                        String posicionTitulo = data.getExtras().getString(MapaPartidoActivity.EXTRA_LOCALIZACION_TITULO);
                        campoLugar.setText(posicionTitulo);
                        break;
                    case MapaPartidoActivity.RESULTADO_PARTIDO_BUSCADO:
                        todosLosPartidos(0);
                        break;
                }
                break;
            default:
        }
    }

    private void createAvatarSelectDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AvatarPickerDialogFragment avatarPickerDialog = new AvatarPickerDialogFragment();
        avatarPickerDialog.show(fm, "avatar_picker");
    }

    @Override
    public void onAvatarSelected(AvatarPickerDialogFragment dialog, String avatarResourceName) {
        // TextView input = (TextView) findViewById(R.id.input_username);
        Toast.makeText(getBaseContext(),"Avatar Seleccionado: "+avatarResourceName,Toast.LENGTH_SHORT).show();
        avatarSeleccionado = avatarResourceName;
    }


    @Override
    public void onCancelled(AvatarPickerDialogFragment dialog) {
        // TextView input = (TextView) findViewById(R.id.input_username);
        Toast.makeText(getBaseContext(),"cancel!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Desea salir de la aplicación?")
                    .setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            cambiarAmigos();
        } else if (id == R.id.nav_gallery) {
            todosLosPartidos(0);
        } else if (id == R.id.canchas) {
            cambiarCancha();
        } else if (id == R.id.salir) {
            cerrarApp();
        } else if (id == R.id.solicitudesPartidos) {
            cambiarSolicitudesPartidos();
        } else if (id == R.id.buscarPartidos) {

        } else if (id == R.id.solicitudesAmigos) {

        } else if (id == R.id.menu_buscar_mapa){
            // Busca los partidos cercanos
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Buscando...", Snackbar.LENGTH_SHORT);
            snackbar.show();

            Intent intent = new Intent(this, MapaPartidoActivity.class);
            intent.putExtra(MapaPartidoActivity.EXTRA_JUGADOR, usuarioLogeado);
            intent.putExtra(MapaPartidoActivity.EXTRA_ACCION, MapaPartidoActivity.BUSCAR);
            startActivity(intent);
            Toast.makeText(getBaseContext(),"hola",Toast.LENGTH_LONG).show();
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea salir de la aplicación?")
                .setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        finishAffinity();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private class FancyAdapter extends BaseAdapter {

        private List<PartidoDTO> partidos;

        FancyAdapter(ArrayList<PartidoDTO> partidos) {
            this.partidos = partidos;
        }

        @Override
        public int getCount() {
            return partidos.size();
        }

        @Override
        public Object getItem(int posicion) {
            return partidos.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }


        void agregarPartido(List<PartidoDTO> lista) {
            partidos.addAll(lista);
            notifyDataSetChanged();
        }

        void agregarPartido(PartidoDTO partido){
            partidos.add(partido);
            notifyDataSetChanged();
        }

        @SuppressLint({"SimpleDateFormat", "InflateParams", "ViewHolder", "SetTextI18n"})
        @Override
        public View getView(int posicion, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_partido, parent, false);
            }

            TextView apodoPartido = view.findViewById(R.id.item_partido_apodo);
            TextView direccionPartido = view.findViewById(R.id.item_partido_direccion);
            TextView descripcionPartido = view.findViewById(R.id.item_partido_descripcion);
            TextView fechaPartido = view.findViewById(R.id.item_partido_fecha);
            TextView horaPartido = view.findViewById(R.id.item_partido_hora);
            TextView cantidadPartido = view.findViewById(R.id.item_partido_cantidad);
            TextView precioPartido = view.findViewById(R.id.item_partido_precio);
            ImageView imagenAvatar = view.findViewById(R.id.item_partido_avatar);

            if (partidos != null) {
                PartidoDTO partido = (PartidoDTO) getItem(posicion);
                SimpleDateFormat sdf;

                apodoPartido.setText(partido.getApodo());
                direccionPartido.setText(partido.getLocalizacion().getDireccion());
                descripcionPartido.setText(partido.getComentario());

                sdf = new SimpleDateFormat("EEE dd/MM/yy");
                fechaPartido.setText(sdf.format(partido.getFecha()));

                sdf = new SimpleDateFormat("hh:mm a");
                horaPartido.setText(sdf.format(partido.getFecha()));

                cantidadPartido.setText(
                        getString(
                                R.string.texto_faltantes_item,
                                new Object[]{
                                        partido.getCantidadAceptado(),
                                        partido.getModalidad().getMinimo()
                                }
                        ));

                precioPartido.setText(partido.getPrecio().toString());

                imagenAvatar.setImageResource(R.drawable.pelota);
                if (partido.getAvatar() != null && !partido.getAvatar().isEmpty()){
                    imagenAvatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), partido.getAvatar()));
                }
            }
            return view;
        }
    }
}