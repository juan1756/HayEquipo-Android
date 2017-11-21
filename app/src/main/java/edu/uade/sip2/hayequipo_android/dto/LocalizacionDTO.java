package edu.uade.sip2.hayequipo_android.dto;

import java.io.Serializable;

public class LocalizacionDTO implements Serializable {

    private String direccion;
    private Double latitud;
    private Double longitud;

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

}
