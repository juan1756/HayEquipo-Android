package edu.uade.sip2.hayequipo_android.dto.enumerado;

public enum TipoPrivacidadEnum {

    PUBLICO("publico"),
    PRIVADO("privado")
    ;

    private String clave;

    TipoPrivacidadEnum(String clave){
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }
}
