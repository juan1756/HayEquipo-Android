package edu.uade.sip2.hayequipo_android.core;

import butterknife.Bind;
import edu.uade.sip2.hayequipo_android.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import edu.uade.sip2.hayequipo_android.conn.requestToBackend;
import edu.uade.sip2.hayequipo_android.data.Menus;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
import edu.uade.sip2.hayequipo_android.entities.Partido;
import edu.uade.sip2.hayequipo_android.entities.Usuario;
import edu.uade.sip2.hayequipo_android.utils.DatePickerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Bind(R.id.btn_buscar_usuario)
    Button _buscar_usuario;
    @Bind(R.id.input_usr)
    EditText _usuario;
    @Bind(R.id.label_usuario)
    TextView _label_usuario;
    @Bind(R.id.btn_agregar_usuario)
    Button _agregar_usuario;
    @Bind(R.id.input_descripcion_partido)
    EditText _descripcion_partido;
    @Bind(R.id.input_fecha_partido)
    TextView _fecha_partido;
    @Bind(R.id.input_lugar_partido)
    EditText _lugar_partido;
    @Bind(R.id.btn_crear_partido)
    Button _crear_partido;

    private static int menus = 1;
    private ListView lv;
    private FancyAdapter mFancyAdapter;
    private int mMethod;
    private String usuario = "german";
    ArrayList<String> horas = new ArrayList<String>();
    String hora = "";
    private ArrayList<Partido> partidos = HarcodedUsersAndPlays.obtenerPartidos();
    public static final String nombre_partidos[] = {
            "prueba 1"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = (ListView) findViewById(android.R.id.list);




        mFancyAdapter = new FancyAdapter(nombre_partidos);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);

        actualizarLista();
        setValuesHoras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPartidoDialog(MainActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("click","partido pos:"+position);

                cambiarPartido(position);



            }
        });


    }


    private void cambiarAmigos(){


        mFancyAdapter = new FancyAdapter(Menus.AMIGOS);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);

        ArrayList<Usuario> usuarios = requestToBackend.obtenerUsuarios(usuario,getBaseContext());

        if(usuarios==null){
            Toast.makeText(getBaseContext(),"usuarios NULL",Toast.LENGTH_SHORT).show();
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


    private void cambiarPartidos(){
        mFancyAdapter = new FancyAdapter(nombre_partidos);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);
        actualizarLista();
    }


    private void cambiarSolicitudesAmigos(){
       // mFancyAdapter = new FancyAdapter(Menus.SOLICITUDES);
       // lv.setSelector(R.drawable.list_selector);
       // lv.setDrawSelectorOnTop(false);
       // lv.setAdapter(mFancyAdapter);
        //TODO: hacer josue
        Toast.makeText(getBaseContext(),"TODO JOSUE",Toast.LENGTH_SHORT).show();
    }


    private void cambiarCancha(){
        Intent intent = new Intent(this, CanchaActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void cambiarPartido(int position){
        Intent intent = new Intent(this, PartidoActivity.class);
        intent.putExtra("id", position);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void buscarPartidosPublicos(){
        //TODO: hacer josue
        Toast.makeText(getBaseContext(),"TODO JOSUE",Toast.LENGTH_SHORT).show();
    }



    private void showDatePickerDialog(final TextView fecha) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener(){



            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                Log.e("date picker:"," "+selectedDate.toString());
                fecha.setText(selectedDate);
                fecha.setHint(selectedDate.toString());
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");

    }


    private void showPartidoDialog(final Context context){
        final Dialog dialog = new Dialog(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.crear_partido);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        ImageView view = (ImageView) dialog.findViewById(R.id.black_cross);
        Button crear = (Button) dialog.findViewById(R.id.btn_crear_partido);
        Spinner spinner = dialog.findViewById(R.id.spinner_hora);
       final EditText descripcion = (EditText) dialog.findViewById(R.id.input_descripcion_partido);
       final TextView fecha = (TextView) dialog.findViewById(R.id.input_fecha_partido);
       final EditText lugar = (EditText) dialog.findViewById(R.id.input_lugar_partido);
        final EditText cantidad = (EditText) dialog.findViewById(R.id.input_cantidad_participantes);


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



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
               getApplication(), android.R.layout.simple_spinner_item, horas);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        dialog.show();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int i, long l) {


                hora = arg0.getSelectedItem().toString();
                Log.e("hora seleccionada:"," "+hora.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("fecha on click","on");
                showDatePickerDialog(fecha);

            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String descripcion_partido = descripcion.getText().toString();
                String fecha_partido = fecha.getText().toString();
                String lugar_partido = lugar.getText().toString();
                String cantidad_participantes = cantidad.getText().toString();
                Log.e("hora seleccionada"," "+hora);

                if(!descripcion_partido.equals("") && !fecha_partido.equals("") && !lugar_partido.equals("") && !hora.equals("") && !cantidad_participantes.equals("")){

                    int cant = Integer.parseInt(cantidad_participantes);

                    if(cant>=5) {

                        int id_random = HarcodedUsersAndPlays.getIdPartido(0);
                        Partido partido = new Partido(id_random, lugar_partido, fecha_partido, hora, descripcion_partido, cantidad_participantes);
                        HarcodedUsersAndPlays.agregarPartido(partido);

                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "el partido se ha agregado exitostamente!", Toast.LENGTH_LONG).show();
                        agregarPartido(partido);

                    }else{
                        Log.e("error","cantidad participantes");
                        Toast.makeText(context,"no puede crearse un partido con menos de 5 participantes!!",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Log.e("error","error no todos los datos estan");
                    Toast.makeText(context,"los campos no pueden estar vacios!",Toast.LENGTH_LONG).show();

                }


            }
        });
    }



    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void actualizarLista(){

        ArrayList<Partido> partidos = HarcodedUsersAndPlays.obtenerPartidos();

        for(Partido p : partidos){

            LayoutInflater inflater= getLayoutInflater();

            View result;
            int specialId = R.drawable.list_item_selector_special;

            result =inflater.inflate(R.layout.item, null,true);
            TextView txtTitle = (TextView) result.findViewById(R.id.textNombre);
            TextView txtLugar = (TextView) result.findViewById(R.id.textLugar);
            TextView txtParticipantes = (TextView) result.findViewById(R.id.textParticipantes);
           // TextView extratxt = (TextView) result.findViewById(R.id.textView1);

            result.setBackgroundResource(specialId);
            txtTitle.setText(p.getDescripcion());
            txtLugar.setText(p.getLugar());
            txtParticipantes.setText("Participantes: "+String.valueOf(p.getParticipantes())+"/"+p.getCantidad_participantes());

            lv.addFooterView(result);

        }

    }

    private void agregarPartido(Partido p){

        Log.e("actualizando lista","actualizando..");
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

        LayoutInflater inflater= getLayoutInflater();



        View result;
        int specialId = R.drawable.list_item_selector_special;

            result =inflater.inflate(R.layout.item, null,true);
            TextView txtNombre = (TextView) result.findViewById(R.id.textNombre);
              TextView txtLugar = (TextView) result.findViewById(R.id.textLugar);
            TextView txtParticipantes = (TextView) result.findViewById(R.id.textParticipantes);
           TextView txtFecha = (TextView) result.findViewById(R.id.textFecha);

            result.setBackgroundResource(specialId);

        txtNombre.setText(p.getDescripcion());
        txtLugar.setText(p.getLugar());
        txtFecha.setText(p.getFecha().toString());
        txtParticipantes.setText("Participantes: "+String.valueOf(p.getParticipantes())+"/"+p.getCantidad_participantes());
            lv.addFooterView(result);

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
       //     return true;
      //  }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
           cambiarAmigos();
        } else if (id == R.id.nav_gallery) {
            cambiarPartidos();
        } else if (id == R.id.nav_slideshow) {
            cambiarCancha();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_salir) {
            onBackPressed();
        } else if (id == R.id.solicitudesAmigos) {
            cambiarSolicitudesAmigos();
        } else if (id == R.id.buscarPartidos) {
            buscarPartidosPublicos();
        } else if (id == R.id.solicitudesPartidos) {
            buscarPartidosPublicos();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setValuesHoras(){

        horas.add("");
        horas.add("1");
        horas.add("2");
        horas.add("3");
        horas.add("4");
        horas.add("5");
        horas.add("6");
        horas.add("7");
        horas.add("8");
        horas.add("9");
        horas.add("10");
        horas.add("11");
        horas.add("12");
        horas.add("13");
        horas.add("14");
        horas.add("15");
        horas.add("16");
        horas.add("17");
        horas.add("18");
        horas.add("19");
        horas.add("20");
        horas.add("21");
        horas.add("22");
        horas.add("23");
        horas.add("24");

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


            LayoutInflater inflater= getLayoutInflater();



            View result = convertView;
            int normalId;
            int specialId;

            if (convertView == null) {
                // result = (TextView) getLayoutInflater().inflate(R.layout.text_item, parent, false);
                // result.setTextColor(Color.WHITE);
                 result =inflater.inflate(R.layout.item, null,true);
                TextView txtNombre = (TextView) result.findViewById(R.id.textNombre);
                TextView txtLugar = (TextView) result.findViewById(R.id.textLugar);
                ImageView imageView = (ImageView) result.findViewById(R.id.icon);
               TextView txtFecha = (TextView) result.findViewById(R.id.textFecha);

                if(partidos!=null) {
                    //txtTitle.setText(partidos.get(position).getNombre());
                }


            } else {
                 result =  convertView;
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
