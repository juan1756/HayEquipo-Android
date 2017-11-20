package edu.uade.sip2.hayequipo_android.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.uade.sip2.hayequipo_android.R;

/**
 * Created by josue on 20/11/17.
 */

public class DireccionAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAXIMO_RESULTADOS = 5;
    private Context context;
    private Geocoder geocoder;
    private ArrayList<Address> resultado = new ArrayList<>();

    public DireccionAutoCompleteAdapter(Context context) {
        this.context = context;
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    @Override
    public int getCount() {
        return resultado.size();
    }

    @Override
    public Object getItem(int index) {
        return resultado.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_dropdown_item2line, parent, false);
        }

        Address direccion = (Address) getItem(posicion);
        StringBuilder builder;

        builder = new StringBuilder()
                .append(direccion.getThoroughfare() == null ? "" : direccion.getThoroughfare())
                .append(direccion.getSubThoroughfare() == null ? "" : " " + direccion.getSubThoroughfare() )
        ;
        String linea1 = builder.toString();

        builder = new StringBuilder()
                .append(direccion.getLocality())
                .append(" - ")
                .append(direccion.getCountryName())
        ;
        String linea2 = builder.toString();

        ((TextView) view.findViewById(R.id.text1)).setText(linea1);
        ((TextView) view.findViewById(R.id.text2)).setText(linea2);
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        List<Address> direcciones = geocoder.getFromLocationName(constraint.toString(), MAXIMO_RESULTADOS);
                        filterResults.values = direcciones;
                        filterResults.count = direcciones.size();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultado = (ArrayList<Address>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }
}
