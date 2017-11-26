package edu.uade.sip2.hayequipo_android.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BusquedaPartidoDTO implements Serializable {

    private JugadorDTO jugador;
    private Double latitud;
    private Double longitud;
    private Double radio;
    private List<Long> codigoRepetido;

    public BusquedaPartidoDTO() {
        codigoRepetido = new ArrayList<>();
    }

    public JugadorDTO getJugador() {
        return jugador;
    }
    public void setJugador(JugadorDTO jugador) {
        this.jugador = jugador;
    }

    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getRadio() {
        return radio;
    }
    public void setRadio(Double radio) {
        this.radio = radio;
    }

    public List<Long> getCodigoRepetido() {
        return codigoRepetido;
    }
    public void setCodigoRepetido(List<Long> codigoRepetido) {
        this.codigoRepetido = codigoRepetido;
    }

    public JSONObject toJsonObject() throws JsonProcessingException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject json;

        json = new JSONObject(mapper.writeValueAsString(this));
        return json;
    }
}
