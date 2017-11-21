package edu.uade.sip2.hayequipo_android.core;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.adapter.DireccionAutoCompleteAdapter;
import edu.uade.sip2.hayequipo_android.conn.VolleySingleton;
import edu.uade.sip2.hayequipo_android.conn.requestToBackend;
import edu.uade.sip2.hayequipo_android.data.Menus;
import edu.uade.sip2.hayequipo_android.dto.ModalidadDTO;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
import edu.uade.sip2.hayequipo_android.entities.Partido;
import edu.uade.sip2.hayequipo_android.entities.Usuario;
import edu.uade.sip2.hayequipo_android.utils.AvatarPickerDialogFragment;
import edu.uade.sip2.hayequipo_android.utils.Avatars;
import edu.uade.sip2.hayequipo_android.utils.DatePickerFragment;
import edu.uade.sip2.hayequipo_android.utils.TimePickerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AvatarPickerDialogFragment.AvatarPickerDialogListener {

    private static final int ACTIVIDAD_MAPA = 100;
    private static int menus = 1;

    private ListView lv;
    private FancyAdapter mFancyAdapter;
    private int mMethod;
    private String usuario = "german";
    private String avatar = "";
    String hora = "";
    private ArrayList<String> horas = new ArrayList<>();
    private String hora = "";
    private ArrayList<Partido> partidos = HarcodedUsersAndPlays.obtenerPartidos();

    // CAMPOS PARA EL DIALOGO
    private AutoCompleteTextView campoLugar;
    private Spinner campoModalidad;
    private List<ModalidadDTO> modalidades = new ArrayList<>();
    private LatLng posicionMarcada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(android.R.id.list);
        mFancyAdapter = new FancyAdapter(getResources().getStringArray(R.array.partidos_mock));
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);

        actualizarLista();
        setValuesHoras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("LOG","partido pos: "+position);
                cambiarPartido(position);
            }
        });

        // TODO hacer un progress o algo para indicar que debe cargar

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
                                        ObjectMapper mapper = new ObjectMapper();
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
    }


    private void cambiarAmigos(){


        mFancyAdapter = new FancyAdapter(Menus.AMIGOS);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);

        ArrayList<Usuario> usuarios = requestToBackend.obtenerUsuarios(usuario, getBaseContext());

        if (usuarios == null) {
            Toast.makeText(getBaseContext(), "usuarios NULL", Toast.LENGTH_SHORT).show();
        }

        /*
        for(Partido p : partidos) {

            LayoutInflater inflater = getLayoutInflater();

            View result;
            int specialId = R.drawable.list_item_selector_special;

            result = inflater.inflate(R.layout.item, null, true);
            TextView txtTitle = (TextView) result.findViewById(R.id.textNombre);
            TextView txtLugar = (TextView) result.findViewById(R.id.textLugar);
            TextView txtParticipantes = (TextView) result.findViewById(R.id.textParticipantes);
            // TextView extratxt = (TextView) result.findViewById(R.id.textView1);

            result.setBackgroundResource(specialId);
            txtTitle.setText(p.getDescripcion());
            txtLugar.setText(p.getLugar());
            txtParticipantes.setText("Participantes: " + String.valueOf(p.getParticipantes()));

        }
*/

    }


    private void cambiarPartidos() {
        mFancyAdapter = new FancyAdapter(getResources().getStringArray(R.array.partidos_mock));
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);
        getSupportActionBar().setTitle("Mis Partidos");
        actualizarLista();
    }


    private void cambiarSolicitudesAmigos() {
        // mFancyAdapter = new FancyAdapter(Menus.SOLICITUDES);
        // lv.setSelector(R.drawable.list_selector);
        // lv.setDrawSelectorOnTop(false);
        // lv.setAdapter(mFancyAdapter);
        //TODO: hacer josue
        Toast.makeText(getBaseContext(), "TODO JOSUE", Toast.LENGTH_SHORT).show();
    }


    private void cambiarCancha() {
        Intent intent = new Intent(this, CanchaActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void cambiarPartido(int position) {
        Intent intent = new Intent(this, PartidoActivity.class);
        intent.putExtra("id", position);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void buscarPartidosPublicos() {
        //TODO: hacer josue
        Toast.makeText(getBaseContext(), "TODO JOSUE", Toast.LENGTH_SHORT).show();
    }


    private void showTimePickerDialog(final TextView hora) {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {


            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                // +1 because january is zero
                final String selectedTime = String.valueOf(hour)+":"+String.valueOf(minutes);
                Log.e("date picker:", " " + selectedTime.toString());
                hora.setText(selectedTime);
                hora.setHint(selectedTime.toString());
            }
        });
        newFragment.show(getFragmentManager(), "timePicker");

    }

    private void showDatePickerDialog(final TextView fecha) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                Log.e("date picker:", " " + selectedDate.toString());
                fecha.setText(selectedDate);
                fecha.setHint(selectedDate.toString());
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");

    }


    private void showPartidoDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.crear_partido);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        ImageView botonCerrarDialogo = dialog.findViewById(R.id.black_cross);
        Button botonCrearPartido = dialog.findViewById(R.id.btn_crear_partido);
        Spinner campoHora = dialog.findViewById(R.id.spinner_hora);
        final EditText campoNombre = dialog.findViewById(R.id.input_nombre_partido);
        final EditText campoDescripcion = dialog.findViewById(R.id.input_descripcion_partido);
        final TextView campoFecha = dialog.findViewById(R.id.input_fecha_partido);
        final TextView campoHora = dialog.findViewById(R.id.input_hora_partido);
        final EditText campoPrecio = dialog.findViewById(R.id.input_precio);
        final TextView avatarView = dialog.findViewById(R.id.input_avatar_partido);

        // Configuro el campo de modalidad
        campoModalidad = dialog.findViewById(R.id.campo_modalidad);
        ArrayAdapter<ModalidadDTO> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, modalidades);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        campoModalidad.setAdapter(adapter);

        // Configuro el boton del mapa
        ImageButton botonSeleccionarMapa = dialog.findViewById(R.id.boton_seleccionar_mapa);
        botonSeleccionarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapaPartidoActivity.class);
                intent.putExtra(MapaPartidoActivity.ACCION_MAPA, MapaPartidoActivity.SELECCIONAR);
                if (posicionMarcada != null){
                    intent.putExtra(MapaPartidoActivity.POSICION_MAPA, posicionMarcada);
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

        botonCerrarDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int) (displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int) (displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getApplication(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.horas));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoHora.setAdapter(spinnerArrayAdapter);

        dialog.show();


        campoHora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {


                hora = arg0.getSelectedItem().toString();
                Log.e("hora seleccionada:", " " + hora.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAvatarSelectDialog();

            }
        });


        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("hora on click", "on");
                showTimePickerDialog(hora);
            }
        });

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

                String descripcion_partido = campoDescripcion.getText().toString();
                String fecha_partido = campoFecha.getText().toString();
                String lugar_partido = campoLugar.getText().toString();
//                String cantidad_participantes = cantidad.getText().toString();
//                Log.e("hora seleccionada", " " + hora);

//                if (!descripcion_partido.equals("") && !fecha_partido.equals("") && !lugar_partido.equals("") && !hora.equals("") && !cantidad_participantes.equals("")) {
//
//                    int cant = Integer.parseInt(cantidad_participantes);
//
//                    if (cant >= 5) {
//
//                        int id_random = HarcodedUsersAndPlays.getIdPartido(0);
//                        Partido partido = new Partido(id_random, lugar_partido, fecha_partido, hora, descripcion_partido, cantidad_participantes);
//                        HarcodedUsersAndPlays.agregarPartido(partido);
//
//                        dialog.dismiss();
//                        Toast.makeText(getBaseContext(), "el partido se ha agregado exitostamente!", Toast.LENGTH_LONG).show();
//                        agregarPartido(partido);
//
//                    } else {
//                        Log.e("error", "cantidad participantes");
//                        Toast.makeText(context, "no puede crearse un partido con menos de 5 participantes!!", Toast.LENGTH_LONG).show();
//                    }
//
//                } else {
//                    Log.e("error", "error no todos los datos estan");
//                    Toast.makeText(context, "los campos no pueden estar vacios!", Toast.LENGTH_LONG).show();
//
//                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ACTIVIDAD_MAPA:
                switch (resultCode){
                    case RESULT_OK:
                        posicionMarcada = data.getExtras().getParcelable(MapaPartidoActivity.LOCALIZACION_MARCADA);
                        String posicionTitulo = data.getExtras().getString(MapaPartidoActivity.LOCALIZACION_TITULO);
                        campoLugar.setText(posicionTitulo);
                        break;
                }
                break;
            default:
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void actualizarLista(){

        ArrayList<Partido> partidos = HarcodedUsersAndPlays.obtenerPartidos();

        for (Partido p : partidos) {

            LayoutInflater inflater = getLayoutInflater();

            View result;
            int specialId = R.drawable.list_item_selector_special;

            result = inflater.inflate(R.layout.item, null, true);
            TextView txtTitle = result.findViewById(R.id.textNombre);
            TextView txtLugar = result.findViewById(R.id.textLugar);
            TextView txtParticipantes = result.findViewById(R.id.textParticipantes);
            ImageView imgAvatar = result.findViewById(R.id.icon);
            if(!p.getAvatar().equals("")) {
                Context context = imgAvatar.getContext();
                int id = context.getResources().getIdentifier(p.getAvatar(), "drawable", context.getPackageName());
                imgAvatar.setImageResource(id);
            }
            result.setBackgroundResource(specialId);

            txtTitle.setText(p.getNombre());
            txtLugar.setText(p.getLugar());
            txtParticipantes.setText("Participantes: " + String.valueOf(p.getParticipantes()) + "/" + p.getCantidad_participantes());

            lv.addFooterView(result);

        }

    }


    private void createAvatarSelectDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AvatarPickerDialogFragment avatarPickerDialog = new AvatarPickerDialogFragment();
        avatarPickerDialog.show(fm, "avatar_picker");
    }

    private void agregarPartido(Partido p) {

        Log.e("actualizando lista", "actualizando..");
        /*
        partidos = HarcodedUsers.obtenerPartidos();
        if(partidos!=null) {
            for (Partido p : partidos) {
                int i = p.getId();
                nombre_partidos[i] = p.getNombre();
            }
        }


        mFancyAdapter = new FancyAdapter(nombre_partidos);
        */
        // lv.setSelector(R.drawable.list_selector);
        //lv.setDrawSelectorOnTop(false);
        // lv.setAdapter(mFancyAdapter);

        LayoutInflater inflater = getLayoutInflater();


        View result;
        int specialId = R.drawable.list_item_selector_special;

        result = inflater.inflate(R.layout.item, null, true);
        TextView txtNombre = result.findViewById(R.id.textNombre);
        TextView txtLugar = result.findViewById(R.id.textLugar);
        TextView txtParticipantes = result.findViewById(R.id.textParticipantes);
        TextView txtFecha = result.findViewById(R.id.textFecha);
        ImageView imgAvatar = result.findViewById(R.id.icon);

        result.setBackgroundResource(specialId);

        txtNombre.setText(p.getDescripcion());
        txtLugar.setText(p.getLugar());
        txtFecha.setText(p.getFecha().toString()+", "+p.getHora());
        txtParticipantes.setText("Participantes: " + String.valueOf(p.getParticipantes()) + "/" + p.getCantidad_participantes());
        if(!p.getAvatar().equals("")){
            Context context = imgAvatar.getContext();
            int id = context.getResources().getIdentifier(p.getAvatar(), "drawable", context.getPackageName());
            imgAvatar.setImageResource(id);

        }
        lv.addFooterView(result);

    }





    @Override
    public void onAvatarSelected(AvatarPickerDialogFragment dialog, String avatarResourceName) {
       // TextView input = (TextView) findViewById(R.id.input_username);
        Toast.makeText(getBaseContext(),"avatar: "+avatarResourceName,Toast.LENGTH_SHORT).show();
        avatar = avatarResourceName;
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
                            return;
                        }
                    });


            AlertDialog alert = builder.create();
            alert.show();


        /* Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
            //finish();
            // finishAffinity();
            // System.exit(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.accion_buscar_mapa) {
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Buscando...", Snackbar.LENGTH_SHORT);
//            snackbar.show();
//
//            startActivity(new Intent(this, MapaPartidoActivity.class));
//
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            cambiarAmigos();
        } else if (id == R.id.nav_gallery) {
            cambiarPartidos();
        } else if (id == R.id.nav_slideshow) {
            cambiarCancha();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.solicitudesAmigos) {
            cambiarSolicitudesAmigos();
        } else if (id == R.id.buscarPartidos) {
            //buscarPartidosPublicos();
        } else if (id == R.id.solicitudesPartidos) {
            //buscarPartidosPublicos();
        } else if (id == R.id.menu_buscar_mapa){
            // Busca los partidos cercanos
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Buscando...", Snackbar.LENGTH_SHORT);
            snackbar.show();

            Intent intent = new Intent(this, MapaPartidoActivity.class);
            intent.putExtra(MapaPartidoActivity.ACCION_MAPA, MapaPartidoActivity.BUSCAR);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setValuesHoras(){
        for (int i = 0; i < 24; i++){
            horas.add(""+i);
        }
    }


    private class FancyAdapter extends BaseAdapter {

        private String[] mData;

        public FancyAdapter(String[] data) {
            mData = data;
        }


        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public String getItem(int position) {
            return mData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = getLayoutInflater();


            View result = convertView;
            int normalId;
            int specialId;

            if (convertView == null) {
                // result = (TextView) getLayoutInflater().inflate(R.layout.text_item, parent, false);
                // result.setTextColor(Color.WHITE);
                result = inflater.inflate(R.layout.item, null, true);
                TextView txtNombre = (TextView) result.findViewById(R.id.textNombre);
                TextView txtLugar = (TextView) result.findViewById(R.id.textLugar);
                ImageView imageView = (ImageView) result.findViewById(R.id.icon);
                TextView txtFecha = (TextView) result.findViewById(R.id.textFecha);

                if (partidos != null) {
                    //txtTitle.setText(partidos.get(position).getNombre());
                }


            } else {
                result = convertView;
            }

            final String cheese = getItem(position);

            //result.setText(cheese);


            normalId = R.drawable.list_item_selector_normal;
            specialId = R.drawable.list_item_selector_special;


            result.setBackgroundResource(specialId);


            return result;
        }


    }


}
