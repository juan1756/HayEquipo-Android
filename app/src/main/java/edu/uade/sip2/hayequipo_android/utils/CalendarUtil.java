package edu.uade.sip2.hayequipo_android.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by josue on 21/11/17.
 */

public class CalendarUtil {

    @SuppressLint("SimpleDateFormat")
    public static Date formatearFechaHora(String fecha, String hora) throws ParseException {
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("es","ES"));
        Date fechaFormateada = sdf.parse(fecha);
        sdf= new SimpleDateFormat("hh:mm", new Locale("es","ES"));
        Date horaFormateada = sdf.parse(hora);

        Calendar calendarHora = Calendar.getInstance();
        calendarHora.setTime(horaFormateada);

        Calendar calendarFecha = Calendar.getInstance();
        calendarFecha.setTime(fechaFormateada);
        calendarFecha.set(Calendar.HOUR_OF_DAY, calendarHora.get(Calendar.HOUR_OF_DAY));
        calendarFecha.set(Calendar.MINUTE, calendarHora.get(Calendar.MINUTE));
        calendarFecha.set(Calendar.SECOND, 0);
        calendarFecha.set(Calendar.MILLISECOND, 0);

        return calendarFecha.getTime();
    }
}
