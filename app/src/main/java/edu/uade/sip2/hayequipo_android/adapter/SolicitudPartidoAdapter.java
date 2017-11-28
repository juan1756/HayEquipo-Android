package edu.uade.sip2.hayequipo_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.dto.JugadorDTO;
import edu.uade.sip2.hayequipo_android.dto.SolicitudDTO;


public class SolicitudPartidoAdapter extends BaseAdapter {

    private List<SolicitudDTO> solicitudes ;
    private Context context;

    public SolicitudPartidoAdapter(ArrayList<SolicitudDTO> solicitudes, Context context) {
        this.context = context;
        this.solicitudes = solicitudes;
    }

    @Override
    public int getCount() {
        return solicitudes.size();
    }

    @Override
    public Object getItem(int posicion) {
        return solicitudes.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }


    public void agregarSolicitud(List<SolicitudDTO> lista) {
        solicitudes.addAll(lista);
        notifyDataSetChanged();
    }

    public void agregarSolicitud(SolicitudDTO solicitud){
        solicitudes.add(solicitud);
        notifyDataSetChanged();
    }

    @SuppressLint({"SimpleDateFormat", "InflateParams", "ViewHolder"})
    @Override
    public View getView(int posicion, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate(R.layout.item_solicitud, parent, false);

        }

        TextView apodo = view.findViewById(R.id.item_solicitud_apodo);
        TextView direccion = view.findViewById(R.id.item_solicitud_direccion);
        TextView fecha = view.findViewById(R.id.item_solicitud_fecha);
        TextView desc = view.findViewById(R.id.item_solicitud_descripcion);


        if (solicitudes != null) {
            SolicitudDTO solicitud = (SolicitudDTO) getItem(posicion);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            apodo.setText("Invitación de: "+solicitud.getJugador().getNombre().toString());
            direccion.setText(solicitud.getPartido().getLocalizacion().getDireccion().toString());
            fecha.setText(solicitud.getPartido().getFecha().toString());
            desc.setText(solicitud.getPartido().getComentario());

           // avatar.setImageResource(R.drawable.pelota);
           // if (partido.getAvatar() != null && !partido.getAvatar().isEmpty()){
             //   imagenAvatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), partido.getAvatar()));
          //  }
        }
        return view;
    }
}
