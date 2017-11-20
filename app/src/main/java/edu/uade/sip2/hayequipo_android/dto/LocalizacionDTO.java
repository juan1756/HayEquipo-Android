package edu.uade.sip2.hayequipo_android.dto;

import java.io.Serializable;

public class LocalizacionDTO implements Serializable {

    private String direccion;
    private Float latitud;
    private Float longitud;

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Float getLatitud() {
        return latitud;
    }
    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }
    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

}
