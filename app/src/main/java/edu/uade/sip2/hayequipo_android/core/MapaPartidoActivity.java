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
import android.os.Parcelable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.dto.LocalizacionDTO;
import edu.uade.sip2.hayequipo_android.dto.ModalidadDTO;
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.utils.PermisosUtils;

/**
 * Created by josue on 13/11/17.
 */

public class MapaPartidoActivity extends AppCompatActivity
    implements
//                    GoogleMap.OnMyLocationButtonClickListener,
//                    GoogleMap.OnMyLocationClickListener,
                    GoogleMap.OnMarkerClickListener,
                    GoogleMap.OnMapClickListener,
                    GoogleMap.OnMapLongClickListener,
                    OnMapReadyCallback,
                    ActivityCompat.OnRequestPermissionsResultCallback,
                    GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener
    {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_GOOGLE_REQUEST_CODE = 100;

    public static final String LOCALIZACION_MARCADA = "LOCALIZACION_MARCADA";
    public static final String LOCALIZACION_TITULO = "LOCALIZACION_TITULO";
    public static final String ACCION_MAPA = "ACCION";
    public static final String POSICION_MAPA = "POSICION";
    public static final int BUSCAR = 1;
    public static final int SELECCIONAR = 2;

    private Integer accion;
    private Geocoder geocoder;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;

    private LatLng posicionMarcada; // POSICION MARCADA POR EL USUARIO
    private Location ultimaLocalizacion; // POSICION DEL GPS USUARIO
    private Long partidoMarcado; // PARTIDO SELECCIONADO (CODIGO)

    private boolean permisoDenegado = false;
    private BottomSheetBehavior layoutMapaDetalleBehavior;

    @Bind(R.id.boton_mi_localizacion)
    FloatingActionButton botonMiLocalizacion;
    @Bind(R.id.boton_seleccionar_localizacion)
    Button botonSeleccionarLocalizacion;
    @Bind(R.id.layout_mapa_detalle)
    LinearLayout layoutMapaDetalle;
    @Bind(R.id.texto_mapa_detalle_titulo)
    TextView textoDetalleTitulo;
    @Bind(R.id.texto_mapa_detalle_descripcion)
    TextView textoDetalleDescripcion;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accion = getIntent().getIntExtra(ACCION_MAPA, BUSCAR);
        posicionMarcada = getIntent().getParcelableExtra(POSICION_MAPA);

        setContentView(R.layout.activity_mapa_partido);
        ButterKnife.bind(this);

        layoutMapaDetalleBehavior = BottomSheetBehavior.from(layoutMapaDetalle);
        layoutMapaDetalleBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING && accion.equals(SELECCIONAR)) {
                    layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        layoutMapaDetalle.setVisibility(View.INVISIBLE);

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

                    switch (accion){
                        case BUSCAR:
                            mMap.setOnMarkerClickListener(MapaPartidoActivity.this);

                            agregarMarcadores();
                            break;
                        case SELECCIONAR:
                            mMap.setOnMapLongClickListener(MapaPartidoActivity.this);

                            layoutMapaDetalle.setVisibility(View.VISIBLE);

                            if (posicionMarcada != null){
                                agregarMarcador(posicionMarcada, "Localizacion Escrita", true);
                                actualizarDetalleMapa();
                            }
                            break;
                    }
                }
            }
        });
    }

    private void configurarSeleccionar() {
        botonSeleccionarLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (accion) {
                    case BUSCAR:
                        // ENVIO LA SOLICITUD
                        if (partidoMarcado != null){
//                        partidoMarcado
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapaPartidoActivity.this);
                            builder.setMessage("DEBE SELECIONAR UN PARTIDO")
                                    .setTitle("Alerta");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    case SELECCIONAR:
                        Intent intent = new Intent();
                        intent.putExtra(LOCALIZACION_MARCADA, posicionMarcada);
                        intent.putExtra(LOCALIZACION_TITULO, textoDetalleTitulo.getText());

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });
    }

    private void configurarMiLocalizacion() {
        botonMiLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionMarcada = null;
                obtenerUltimaLocalizacion();
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

    /**
     * METODO QUE BUSCA EN EL SERVIDOR LOS PARTIDOS PUBLICOS
     */
    private void agregarMarcadores() {
        List<LatLng> puntos = new ArrayList<>();
        List<String> titulos = new ArrayList<>();
        List<Object> datas = new ArrayList<>();

        PartidoDTO partido = new PartidoDTO();
        partido.setCodigo(1l);
        partido.setApodo("APODO");
        partido.setComentario("COMENTARIO");
        partido.setFecha(new Date());
        ModalidadDTO modalidadDTO = new ModalidadDTO();
        modalidadDTO.setDescripcion("ALGA");
        modalidadDTO.setDescripcion("asdasd");
        partido.setModalidad(modalidadDTO);
        LocalizacionDTO localizacionDTO = new LocalizacionDTO();
        localizacionDTO.setLatitud(-31.90f);
        localizacionDTO.setLongitud(115.86f);
        localizacionDTO.setDireccion("AUSTRALIA");
        partido.setLocalizacion(localizacionDTO);

        LatLng punto = new LatLng(partido.getLocalizacion().getLatitud(), partido.getLocalizacion().getLongitud());

        puntos.add(punto);
        titulos.add(partido.getApodo());
        datas.add(partido);

        this.agregarMarcador(puntos, datas, titulos, true, true);
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
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(p)
                    .title( tituloPosiciones.get(posiciones.indexOf(p)) )
            );
            marker.setTag(data.get(posiciones.indexOf(p)));

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
        posicionMarcada = latLng;
        agregarMarcador(latLng, "Localizacion Seleccionada");
        actualizarDetalleMapa();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ocultarDetalle();
    }

    private void ocultarDetalle() {
        layoutMapaDetalleBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void actualizarDetalleMapa() {
        ocultarDetalle();
        try {
            List<Address> direcciones = geocoder.getFromLocation(posicionMarcada.latitude, posicionMarcada.longitude, 1);
            if (direcciones.size() > 0){
                Address direccion = direcciones.get(0);
                StringBuilder direccionCompleta = new StringBuilder()
                        .append(direccion.getThoroughfare())
                        .append(direccion.getSubThoroughfare() == null ? "" : " " + direccion.getSubThoroughfare() )
                        .append(", ")
                        .append(direccion.getLocality());

                textoDetalleTitulo.setText(direccionCompleta);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // CUANDO REALIZO UNA SELECCION DE UN PARTIDO
        layoutMapaDetalle.setVisibility(View.VISIBLE);
        Object data = marker.getTag();

        if (data != null && data instanceof PartidoDTO){
            PartidoDTO partidoDTO = (PartidoDTO) data;
            partidoMarcado = partidoDTO.getCodigo();
            StringBuilder direccionCompleta = new StringBuilder()
                    .append(partidoDTO.getLocalizacion().getDireccion())
                    ;

            textoDetalleTitulo.setText(direccionCompleta);
        }

        return false;
    }
}
