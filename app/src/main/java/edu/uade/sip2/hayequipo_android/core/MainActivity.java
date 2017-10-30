package edu.uade.sip2.hayequipo_android.core;

import edu.uade.sip2.hayequipo_android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.uade.sip2.hayequipo_android.data.Menus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static int menus = 1;
    private ListView lv;
    private FancyAdapter mFancyAdapter;
    private int mMethod;
    private final String[] partidos ={
            "Partido 1",
            "Partido 2",
            "Partido 3",
            "Partido 4",
            "Partido 5",
            "Partido 6",
            "Partido 7",
            "ETC"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = (ListView) findViewById(android.R.id.list);

        mFancyAdapter = new FancyAdapter(Menus.PARTIDOS);
        lv.setSelector(R.drawable.list_selector);
        lv.setDrawSelectorOnTop(false);
        lv.setAdapter(mFancyAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO", Snackbar.LENGTH_LONG)
                        .setAction("TODO", null).show();
                showMyDialog(MainActivity.this);
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

                switch(menus){

                    case 0:

                        break;

                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    case 5:

                        break;

                    default:

                        break;
                }


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


    private void showMyDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.busqueda);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

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

        } else if (id == R.id.nav_send) {

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
                TextView txtTitle = (TextView) result.findViewById(R.id.item);
                ImageView imageView = (ImageView) result.findViewById(R.id.icon);
                TextView extratxt = (TextView) result.findViewById(R.id.textView1);

                txtTitle.setText(partidos[position]);

            } else {
                 result =  convertView;
            }

            final String cheese = getItem(position);

           // result.setText(cheese);



            normalId = R.drawable.list_item_selector_normal;
            specialId = R.drawable.list_item_selector_special;


            result.setBackgroundResource(specialId);


            return result;
        }


    }


}
