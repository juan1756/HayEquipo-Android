package edu.uade.sip2.hayequipo_android.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;

import edu.uade.sip2.hayequipo_android.R;

/**
 * Created by Usuario on 26/10/2017.
 */

public class CanchaActivity extends AppCompatActivity {


    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;
    private Button button;
    private Button button2;
    private int cantService = 0;
    private TextView textView;
    private CounterFab counterFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancha);

        askForSystemOverlayPermission();

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        // textView = (TextView) findViewById(R.id.textView);


        int badge_count = getIntent().getIntExtra("badge_count", 0);

        // textView.setText(badge_count + " messages received previously");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAnotherJug();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(CanchaActivity.this)) {

                    switch (cantService) {
                        case 0:
                            startService(new Intent(CanchaActivity.this, FloatingWidgetService.class));
                            cantService++;
                            Log.e("error","uno");
                            break;
                        case 1:
                            startService(new Intent(CanchaActivity.this, SecondService.class));
                            Toast.makeText(CanchaActivity.this,"dos",Toast.LENGTH_SHORT);
                            cantService++;
                            Log.e("error","dos");
                            break;
                    }




                } else {
                    errorToast();
                }
            }
        });

    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        // To prevent starting the service if the required permission is NOT granted.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(new Intent(CanchaActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));
            finish();
        } else {
            errorToast();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //Permission is not available. Display error text.
                    errorToast();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void errorToast() {
        Toast.makeText(this, "Draw over other app permission not available. Can't start the application without the permission.", Toast.LENGTH_LONG).show();
    }


    private void addAnotherJug(){
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.leftMargin = (int) 200; //(xCordinate*pixels_grid_X);
        params1.topMargin = (int) 50; //(yCordinate*pixels_grid_Y);
        //final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        //layout.setLayoutParams(params1);

        View mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout2, null);
        mOverlayView.setLayoutParams(params1);
/*
        View rootView = null;
        View currentFocus = getWindow().getCurrentFocus();
        if (currentFocus != null)
            rootView = currentFocus.getRootView();
*/
        RelativeLayout my = (RelativeLayout) findViewById(R.id.activity_cancha);

        /**
         * The parent layout in which the button is to be displayed.
         * Finally adding the button to the parent layout.
         * layout is the reference to ur relative layout.
         */

        my.addView(mOverlayView,params1);
    }

}




