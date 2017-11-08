package edu.uade.sip2.hayequipo_android.core;

import butterknife.Bind;
import edu.uade.sip2.hayequipo_android.R;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.uade.sip2.hayequipo_android.data.Menus;
import edu.uade.sip2.hayequipo_android.entities.HarcodedUsersAndPlays;
import edu.uade.sip2.hayequipo_android.entities.Partido;

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
    @Bind(R.id.input_nombre_partido)
    EditText _nombre_partido;
    @Bind(R.id.input_fecha_partido)
    EditText _fecha_partido;
    @Bind(R.id.input_lugar_partido)
    EditText _lugar_partido;
    @Bind(R.id.btn_crear_partido)
    Button _crear_partido;

    private static int menus = 1;
    private ListView lv;
    private FancyAdapter mFancyAdapter;
    private int mMethod;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO", Snackbar.LENGTH_LONG)
                        .setAction("TODO", null).show();
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

    }


    private void cambiarPartidos(){
        mFancyAdapter = new FancyAdapter(Menus.PARTIDOS);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);
    }


    private void cambiarSolicitudes(){
        mFancyAdapter = new FancyAdapter(Menus.SOLICITUDES);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);
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
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void showPartidoDialog(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.crear_partido);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        ImageView view = (ImageView) dialog.findViewById(R.id.black_cross);
        Button crear = (Button) dialog.findViewById(R.id.btn_crear_partido);
       final EditText nombre = (EditText) dialog.findViewById(R.id.input_nombre_partido);
       final EditText fecha = (EditText) dialog.findViewById(R.id.input_fecha_partido);
       final EditText lugar = (EditText) dialog.findViewById(R.id.input_lugar_partido);

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


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre_partido = nombre.getText().toString();
                String fecha_partido = fecha.getText().toString();
                String lugar_partido = lugar.getText().toString();

                if(!nombre_partido.equals("") && !fecha_partido.equals("") && !lugar_partido.equals("")){

                   int id_random = HarcodedUsersAndPlays.getIdPartido(0);
                    Partido partido = new Partido(id_random,nombre_partido,lugar_partido,fecha_partido);
                    HarcodedUsersAndPlays.agregarPartido(partido);

                    dialog.dismiss();
                    Toast.makeText(getBaseContext(),"el partido se ha agregado exitostamente!",Toast.LENGTH_LONG);
                    agregarPartido(partido);

                }else{

                    Toast.makeText(context,"los campos no pueden estar vacios!",Toast.LENGTH_LONG);

                }


            }
        });
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
            txtTitle.setText(p.getNombre());
            txtLugar.setText(p.getLugar());
            txtParticipantes.setText("Participantes: "+String.valueOf(p.getParticipantes()));

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

        txtNombre.setText(p.getNombre());
        txtLugar.setText(p.getLugar());
        txtFecha.setText(p.getFecha());
        txtParticipantes.setText("Participantes: "+String.valueOf(p.getParticipantes()));
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
        if (id == R.id.action_settings) {
            return true;
        }

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
        } else if (id == R.id.solicitudes) {
            cambiarSolicitudes();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
