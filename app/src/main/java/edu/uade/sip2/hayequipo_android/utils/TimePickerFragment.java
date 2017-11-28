package edu.uade.sip2.hayequipo_android.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Usuario on 17/11/2017.
 */

public  class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), listener, hour,minute,false);
        return dialog;
    }
}