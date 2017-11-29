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
import edu.uade.sip2.hayequipo_android.dto.PartidoDTO;
import edu.uade.sip2.hayequipo_android.utils.Avatars;

/**
 * Created by Usuario on 26/11/2017.
 */

public class JugadorAdapter extends BaseAdapter {

    private List<JugadorDTO> jugadores;
    private Context context;
    private String yo;

    public JugadorAdapter(ArrayList<JugadorDTO> jugadores,Context context) {
        this.context = context;
        this.jugadores = jugadores;

    }

    @Override
    public int getCount() {
        return jugadores.size();
    }

    @Override
    public Object getItem(int posicion) {
        return jugadores.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }


    public void agregarJugador(List<JugadorDTO> lista) {
        jugadores.addAll(lista);
        notifyDataSetChanged();
    }

    public void agregarJugador(JugadorDTO jugador){
        jugadores.add(jugador);
        notifyDataSetChanged();
    }

    @SuppressLint({"SimpleDateFormat", "InflateParams", "ViewHolder"})
    @Override
    public View getView(int posicion, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate(R.layout.item_jugador, parent, false);

        }

        TextView apodoJugador = view.findViewById(R.id.item_jugador_apodo);
        TextView emailJugador = view.findViewById(R.id.item_jugador_email);
        ImageView avatar = view.findViewById(R.id.item_jugador_avatar);

        //TODO: HARCODEADO QUE NO ME PONGA A MI ( EL LOGEADO )
        if (jugadores != null) {
            JugadorDTO jugador = (JugadorDTO) getItem(posicion);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            apodoJugador.setText(jugador.getNombre());
            emailJugador.setText(jugador.getEmail());


            avatar.setImageResource(R.drawable.ic_launcher);
           // if (partido.getAvatar() != null && !partido.getAvatar().isEmpty()){
             //   imagenAvatar.setImageResource(Avatars.getAvatarResourceId(getApplicationContext(), partido.getAvatar()));
          //  }
        }
        return view;
    }
}
