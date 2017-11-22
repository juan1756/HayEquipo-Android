package edu.uade.sip2.hayequipo_android.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Usuario on 22/11/2017.
 */

public class WaitTime extends AsyncTask<Void, Void, Void> {

    private ProgressDialog mDialog;

    public WaitTime(ProgressDialog pr){
        this.mDialog = pr;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog.setCancelable(false);
        mDialog.setTitle("Cargando...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }
    protected void onPostExecute() {
        mDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
        super.onCancelled();
    }

    @Override
    protected Void doInBackground(Void... params) {
        long delayInMillis = 3500;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mDialog.dismiss();
            }
        }, delayInMillis);
        return null;
    }
}
