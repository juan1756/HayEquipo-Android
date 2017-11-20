package edu.uade.sip2.hayequipo_android.dto.enumerado;

public enum SexoEnum {
    MASCULINO("M"),
    FEMENINO("F"),
    OTRO("O"),
    NO_INDICA("N");

    private String clave;

    SexoEnum(String clave){
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }
}
