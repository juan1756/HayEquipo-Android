package edu.uade.sip2.hayequipo_android.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
}




