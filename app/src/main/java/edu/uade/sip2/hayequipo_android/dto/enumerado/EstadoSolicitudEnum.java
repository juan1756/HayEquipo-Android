package edu.uade.sip2.hayequipo_android.dto.enumerado;

public enum EstadoSolicitudEnum {

    PENDIENTE("P"),
    ACEPTADO("A"),
    RECHAZADO("R")
    ;

    private String clave;

    EstadoSolicitudEnum(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }
}
