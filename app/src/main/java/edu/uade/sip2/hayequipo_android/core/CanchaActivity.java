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
import android.widget.Toast;

import edu.uade.sip2.hayequipo_android.R;

/**
 * Created by Usuario on 26/10/2017.
 */

public class CanchaActivity extends AppCompatActivity {


    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;
    private Button button;
    private int cantService = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancha);

        askForSystemOverlayPermission();

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(CanchaActivity.this)) {

                    switch (cantService) {
                        case 0:
                            startService(new Intent(CanchaActivity.this, FloatingWidgetService.class));
                            cantService++;
                            Log.e(getString(R.string.error_tag), getString(R.string.uno_s));
                            break;
                        case 1:
                            Toast.makeText(CanchaActivity.this, getString(R.string.dos_s), Toast.LENGTH_SHORT);
                            cantService++;
                            Log.e(getString(R.string.error_tag), getString(R.string.dos_s));
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
                    Uri.parse(getString(R.string._package) + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        // To prevent starting the service if the required permission is NOT granted.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            startService(new Intent(CanchaActivity.this, FloatingWidgetService.class).putExtra(getString(R.string.background), true));
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
        Toast.makeText(this, getString(R.string.draw_error), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }


}