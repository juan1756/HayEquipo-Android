package edu.uade.sip2.hayequipo_android.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.adapter.DireccionAutoCompleteAdapter;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.dto.BusquedaPartidoDTO;
import edu.uade.sip2.hayequipo_android.dto.JugadorDTO;
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.dto.SolicitudDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.EstadoSolicitudEnum;
import edu.uade.sip2.hayequipo_android.utils.JsonArrayRequest;
import edu.uade.sip2.hayequipo_android.utils.PermisosUtils;

/**
 * Created by josue on 13/11/17.
 */

public class MapaPartidoActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraIdleListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_GOOGLE_REQUEST_CODE = 100;

    public static final String EXTRA_LOCALIZACION_MARCADA = "EXTRA_LOCALIZACION_MARCADA";
    public static final String EXTRA_LOCALIZACION_TITULO = "EXTRA_LOCALIZACION_TITULO";
    public static final String EXTRA_ACCION = "ACCION";
    public static final String EXTRA_POSICION = "POSICION";
    public static final String EXTRA_JUGADOR = "JUGADOR";
    public static final int BUSCAR = 1;
    public static final int SELECCIONAR = 2;
    public static final int RESULTADO_PARTIDO_BUSCADO = 10;
    public static final int RESULTADO_UBICACION_SELECCIONADA = 20;

    private boolean primeraVez; // NO AGREGO LA PRIMERA LOCALIZACION QUE ENCUENTRE
    private ObjectMapper mapper; // OBJECTO JACKSON
    private Integer accion;
    private JugadorDTO usuarioLogeado; // JUGADOR LOGEADO
    private Geocoder geocoder;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;

    private LatLng posicionMarcada; // POSICION MARCADA POR EL USUARIO
    private Location ultimaLocalizacion; // POSICION DEL GPS USUARIO
    private Long partidoMarcado; // PARTIDO SELECCIONADO (CODIGO)
    private List<PartidoDTO> partidoPublicoEncontrado; // GUARDO LOS PARTIDOS PUBLICOS ENCONTRADOS

    private boolean permisoDenegado = false;
    private BottomSheetBehavior layoutMapaDetalleBehavior;
    private Marker marcadorSeleccionado; // LO GUARDO PARA ELIMINAR DESPUES EL TEXTO

    @Bind(R.id.boton_mi_localizacion)
    FloatingActionButton botonMiLocalizacion;
    @Bind(R.id.boton_mapa_detalle_aceptar)
    ImageButton botonSeleccionarLocalizacion;
    @Bind(R.id.boton_mapa_detalle_seleccionar)
    Button botonDetalleSeleccionar;
    @Bind(R.id.layout_mapa_detalle)
    LinearLayout layoutMapaDetalle;
    @Bind(R.id.layout_mapa_detalle_1)
    RelativeLayout layoutMapaDetalle1;
    @Bind(R.id.texto_mapa_detalle_titulo)
    TextView textoDetalleTitulo;
    @Bind(R.id.texto_mapa_detalle_precio)
    TextView textoDetallePrecio;
    @Bind(R.id.texto_mapa_detalle_faltantes)
    TextView textoDetalleFaltantes;
    @Bind(R.id.texto_mapa_detalle_descripcion)
    TextView textoDetalleDescripcion;
    @Bind(R.id.texto_mapa_detalle_nombre)
    TextView textoDetalleNombre;
    @Bind(R.id.texto_mapa_detalle_fecha)
    TextView textoDetalleFecha;
    @Bind(R.id.texto_mapa_detalle_hora)
    TextView textoDetalleHora;
    @Bind(R.id.input_mapa_detalle_buscador)
    AutoCompleteTextView inputBuscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se crea una vez y se utiliza
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        usuarioLogeado = (JugadorDTO) getIntent().getSerializableExtra(EXTRA_JUGADOR);
        accion = getIntent().getIntExtra(EXTRA_ACCION, BUSCAR);
        posicionMarcada = getIntent().getParcelableExtra(EXTRA_POSICION);

        setContentView(R.layout.activity_mapa_partido);
        ButterKnife.bind(this);

        layoutMapaDetalleBehavior = BottomSheetBehavior.from(layoutMapaDetalle);
        layoutMapaDetalleBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if ( (newState == BottomSheetBehavior.STATE_DRAGGING && accion.equals(SELECCIONAR)) ||
                     (newState == BottomSheetBehavior.STATE_DRAGGING && accion.equals(BUSCAR) && partidoMarcado == null)
                ){
                    layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layout_mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_GOOGLE_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ultimaLocalizacion = obtenerUltimaLocalizacion();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                mMap.setContentDescription("Mapa de Partidos a Jugar");
                mMap.setOnMapClickListener(MapaPartidoActivity.this);
                mMap.setOnCameraIdleListener(MapaPartidoActivity.this);
                mMap.setOnMarkerClickListener(MapaPartidoActivity.this);

                // OCULTO LOS BOTONES DE LOCALIZACION
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                // MODIFICO LAS PREFERENCIAS DEL ZOOM
                mMap.setMinZoomPreference(5.0f);
                mMap.setMaxZoomPreference(20.0f);

                if (PermisosUtils.checkPlayServices(MapaPartidoActivity.this)) {
                    configurarLocalizacion();
                    configurarLocalizacionGoogle();
                    configurarMiLocalizacion();
                    configurarSeleccionar();
                    configurarBuscador();

                    switch (accion){
                        case BUSCAR:
                            partidoPublicoEncontrado = new ArrayList<>();
                            botonSeleccionarLocalizacion.setVisibility(View.GONE);

                            break;
                        case SELECCIONAR:
                            mMap.setOnMapLongClickListener(MapaPartidoActivity.this);
                            if (posicionMarcada != null){
                                agregarMarcador(posicionMarcada, "Localizacion Escrita", true);
                                actualizarDetalleMapa();
                            } else {
                                primeraVez = true;
                            }
                            break;
                    }
                }
            }
        });
    }

    private void configurarSeleccionar() {
        botonDetalleSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonSeleccionarLocalizacion.callOnClick();
            }
        });
        botonSeleccionarLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (accion) {
                    case BUSCAR:
                        // ENVIO LA SOLICITUD
                        if (partidoMarcado != null){
                            enviarSolicitudPartido();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapaPartidoActivity.this);
                            builder.setMessage("DEBE SELECIONAR UN PARTIDO")
                                    .setTitle("Alerta");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    case SELECCIONAR:
                        if (primeraVez){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapaPartidoActivity.this);
                            builder.setMessage("DEBE SELECIONAR UNA DIRECCION")
                                    .setTitle("Alerta");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_LOCALIZACION_MARCADA, posicionMarcada);
                            intent.putExtra(EXTRA_LOCALIZACION_TITULO, textoDetalleTitulo.getText());

                            setResult(RESULTADO_UBICACION_SELECCIONADA, intent);
                            finish();
                        }
                        break;
                }
            }
        });
    }

    private void configurarMiLocalizacion() {
        botonMiLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primeraVez = false;
                posicionMarcada = null;
                obtenerUltimaLocalizacion();

                if (accion.equals(BUSCAR)){
                    onMapClick(null);
                }
            }
        });
    }

    private Location obtenerUltimaLocalizacion() {
        try {
            ultimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (ultimaLocalizacion != null && accion.equals(SELECCIONAR) && posicionMarcada == null){
                posicionMarcada = new LatLng(ultimaLocalizacion.getLatitude(), ultimaLocalizacion.getLongitude());

                agregarMarcador(posicionMarcada, "Localizacion Actual", true);
                actualizarDetalleMapa();
            } else if (ultimaLocalizacion != null && accion.equals(BUSCAR)) {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(ultimaLocalizacion.getLatitude(), ultimaLocalizacion.getLongitude()));
                LatLngBounds bounds = builder.build();

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            return ultimaLocalizacion;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void configurarLocalizacionGoogle() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // EN MILISEGUNDOS
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        obtenerUltimaLocalizacion();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapaPartidoActivity.this, LOCATION_GOOGLE_REQUEST_CODE);

                        } catch (IntentSender.SendIntentException ignored) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void configurarLocalizacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            PermisosUtils.requestPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    true
            );
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermisosUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            configurarLocalizacion();
        } else {
            permisoDenegado = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermisosUtils.checkPlayServices(this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permisoDenegado) {
            showMissingPermissionError();
            permisoDenegado = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showMissingPermissionError() {
        PermisosUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.error_servicio_google, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        primeraVez = false;
        posicionMarcada = latLng;
        agregarMarcador(latLng, "Localizacion Seleccionada");
        actualizarDetalleMapa();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ocultarDetalle();
        if (accion.equals(BUSCAR)){
            eliminarPosicionMarcada();
        }
    }

    private void eliminarPosicionMarcada() {
        posicionMarcada = null;
        partidoMarcado = null;

        if (marcadorSeleccionado != null){
            marcadorSeleccionado.hideInfoWindow();
        }

        textoDetalleTitulo.setText(getString(R.string.titulo_seleccionar_partido_mapa));
        textoDetalleTitulo.setTextColor(getColor(R.color.black));

        layoutMapaDetalle1.setBackgroundColor(getColor(R.color.white));
    }

    @SuppressLint({"StringFormatInvalid", "SimpleDateFormat"})
    @Override
    public boolean onMarkerClick(Marker marker) {
        // CUANDO REALIZO UNA SELECCION DE UN PARTIDO
        marcadorSeleccionado = marker;
        Object data = marker.getTag();

        if (data != null && data instanceof PartidoDTO){
            PartidoDTO partidoDTO = (PartidoDTO) data;
            partidoMarcado = partidoDTO.getCodigo();

            StringBuilder texto;

            texto = new StringBuilder().append(partidoDTO.getLocalizacion().getDireccion());
            textoDetalleTitulo.setText(texto);

            texto = new StringBuilder().append(
                    getString(
                            R.string.texto_faltantes_mapa,
                            new Object[]{
                                partidoDTO.getCantidadAceptado(),
                                partidoDTO.getModalidad().getMinimo()
                            }
                    ));

            textoDetalleFaltantes.setText(texto);

            if (partidoDTO.getComentario() != null){
                texto = new StringBuilder().append(partidoDTO.getComentario());
                textoDetalleDescripcion.setText(texto);
            }

            if (partidoDTO.getPrecio() != null){
                texto = new StringBuilder().append("");
                texto.append(partidoDTO.getPrecio());
                textoDetallePrecio.setText(texto);
            }

            texto = new StringBuilder().append(partidoDTO.getApodo());
            textoDetalleNombre.setText(texto);

            SimpleDateFormat sdf;

            sdf = new SimpleDateFormat("EEE dd/MM/yy", new Locale("es","ES"));
            textoDetalleFecha.setText(sdf.format(partidoDTO.getFecha()));

            sdf = new SimpleDateFormat("hh:mm a", new Locale("es","ES"));
            textoDetalleHora.setText(sdf.format(partidoDTO.getFecha()));

            layoutMapaDetalle1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (layoutMapaDetalleBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                        layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            });

            layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            layoutMapaDetalle1.setBackgroundColor(getColor(R.color.colorPrimary));
            textoDetalleTitulo.setTextColor(getColor(R.color.white));
        }
        return false;
    }

    @Override
    public void onCameraIdle() {
        switch (accion) {
            case BUSCAR:
                // SE ACTIVA CUANDO DEJA DE MOVERSE LA CAMARA DE LA VISTA
                VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
                double radio = calcularRadioVisible(visibleRegion);
                LatLng posicion = mMap.getCameraPosition().target;
                obtenerPartidoPublicos(posicion.latitude, posicion.longitude, radio);
                break;
        }
    }

    private void obtenerPartidoPublicos(double latitude, double longitude, double radio) {
        // Obtengo los partidos publicos
        try {
            BusquedaPartidoDTO busqueda = new BusquedaPartidoDTO();

            List<Long> partidoRepetidos = new ArrayList<>();
            for (PartidoDTO p : partidoPublicoEncontrado){
                partidoRepetidos.add(p.getCodigo());
            }

            busqueda.setJugador(usuarioLogeado);
            busqueda.setLatitud(latitude);
            busqueda.setLongitud(longitude);
            busqueda.setRadio(radio);
            busqueda.setCodigoRepetido(partidoRepetidos);

            VolleySingleton
                    .getInstance(getApplicationContext())
                    .addToRequestQueue(
                            new JsonArrayRequest(
                                    getString(R.string.servicio_url) + getString(R.string.servicio_obtener_partidos_por_localizacion),
                                    busqueda.toJsonObject(),
                                    new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                PartidoDTO[] m = mapper.readValue(response.toString(), PartidoDTO[].class);
                                                agregarPartidosPublico(Arrays.asList(m));

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

    private void agregarPartidosPublico(List<PartidoDTO> partidos) {
        List<LatLng> puntos = new ArrayList<>();
        List<String> titulos = new ArrayList<>();
        List<Object> datas = new ArrayList<>();

        ArrayList<PartidoDTO> partidos_mutable = new ArrayList<>(partidos);
        filtrarPartidosLlenos(partidos_mutable);

        for (PartidoDTO partidoDTO : partidos_mutable) {
            LatLng punto = new LatLng(partidoDTO.getLocalizacion().getLatitud(), partidoDTO.getLocalizacion().getLongitud());

            puntos.add(punto);
//            titulos.add(partidoDTO.getApodo());
            titulos.add("Partido Seleccionado");
            datas.add(partidoDTO);

            partidoPublicoEncontrado.add(partidoDTO);
        }

        this.agregarMarcador(puntos, datas, titulos, true, false);
    }


    private void filtrarPartidosLlenos(List<PartidoDTO> lista) {
        int i = 0;
        while (lista != null && i < lista.size()) {
            PartidoDTO p = lista.get(i);
            if(estaLleno(p)) {
                lista.remove(p);
            } else {
                i++;
            }
        }
    }

    private boolean estaLleno(PartidoDTO partido) {
        return partido.getCantidadAceptado() >= partido.getModalidad().getMinimo();
    }

    private void ocultarDetalle() {
        layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        hideKeyboard(layoutMapaDetalle);
        inputBuscador.clearFocus();
    }

    private void actualizarDetalleMapa() {
        ocultarDetalle();
        try {
            List<Address> direcciones = geocoder.getFromLocation(posicionMarcada.latitude, posicionMarcada.longitude, 1);
            if (direcciones.size() > 0 && !primeraVez){
                Address direccion = direcciones.get(0);
                StringBuilder direccionCompleta = new StringBuilder()
                        .append(direccion.getThoroughfare())
                        .append(direccion.getSubThoroughfare() == null ? "" : " " + direccion.getSubThoroughfare() )
                        .append(", ")
                        .append(direccion.getLocality());

                textoDetalleTitulo.setText(direccionCompleta);

                botonSeleccionarLocalizacion.setBackgroundColor(getColor(R.color.colorPrimary));
                botonSeleccionarLocalizacion.setImageResource(R.drawable.ic_accion_seleccionar_blanco);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcionalidad que dado un RegionVisible muestra el radio del mismo
     * @param visibleRegion
     * @return Valor del radio en Metros
     */
    private double calcularRadioVisible(VisibleRegion visibleRegion) {
        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );

        return (distanceWidth[0] < distanceHeight[0]) ? distanceWidth[0] / 2 : distanceHeight[0] / 2;
    }

    private void agregarMarcador(LatLng posicion, String titulo){
        this.agregarMarcador(posicion, titulo, false);
    }

    private void agregarMarcador(LatLng posicion, String titulo, Boolean mueveCamaraMapa){
        this.agregarMarcador(Collections.singletonList(posicion), Collections.singletonList(null), Collections.singletonList(titulo), true, mueveCamaraMapa );
    }

    private void agregarMarcador(List<LatLng> posiciones, List<Object> data, List<String> tituloPosiciones, Boolean limpiaMapa, Boolean mueveCamaraMapa){
        if (limpiaMapa){
            mMap.clear();
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng p : posiciones){
            if (!primeraVez) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(p)
                        .title(tituloPosiciones.get(posiciones.indexOf(p)))
                );
                marker.setTag(data.get(posiciones.indexOf(p)));
            }

            if (mueveCamaraMapa){
                builder.include(p);
            }
        }

        if (mueveCamaraMapa){
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    private void enviarSolicitudPartido() {
        SolicitudDTO solicitudDTO = new SolicitudDTO();

        PartidoDTO partidoDTO = new PartidoDTO();
        partidoDTO.setCodigo(partidoMarcado);

        solicitudDTO.setJugador(usuarioLogeado);
        solicitudDTO.setPartido(partidoDTO);
        solicitudDTO.setEstado(EstadoSolicitudEnum.ACEPTADO);

        try {
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
                                                SolicitudDTO solicitudDTO = mapper.readValue(response.toString(), SolicitudDTO.class);
                                                if (solicitudDTO.getCodigo() != null && solicitudDTO.getCodigo() > 0){
                                                    Intent intent = new Intent();
                                                    setResult(RESULTADO_PARTIDO_BUSCADO, intent);
                                                    finish();
                                                }

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

    private void configurarBuscador() {
        inputBuscador.setThreshold(5);
        inputBuscador.setAdapter(new DireccionAutoCompleteAdapter(getApplicationContext()));
        inputBuscador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Address direccion = (Address) adapterView.getItemAtPosition(position);
                StringBuilder direccionCompleta = new StringBuilder()
                        .append(direccion.getThoroughfare())
                        .append(direccion.getSubThoroughfare() == null ? "" : " " + direccion.getSubThoroughfare() )
                        .append(", ")
                        .append(direccion.getLocality());

                inputBuscador.setText(direccionCompleta);

                if (accion.equals(SELECCIONAR)){
                    primeraVez = false;
                    posicionMarcada = new LatLng(direccion.getLatitude(), direccion.getLongitude());
                    agregarMarcador(posicionMarcada, "Localizacion Seleccionada", true);
                    actualizarDetalleMapa();
                } else {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(direccion.getLatitude(), direccion.getLongitude()));
                    LatLngBounds bounds = builder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    ocultarDetalle();
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
