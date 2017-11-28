package edu.uade.sip2.hayequipo_android.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Usuario on 10/11/2017.
 */

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    int year;
    int month;
    int day;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        int year    = calendar.get(Calendar.YEAR);
        int month   = calendar.get(Calendar.MONTH);
        int day     = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

    public String getDateSelected(){
        return String.valueOf(this.day)+"/"+String.valueOf(this.month)+"/"+String.valueOf(this.day);
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
}