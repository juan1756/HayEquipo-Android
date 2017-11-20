package edu.uade.sip2.hayequipo_android.dto;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;

public class ModalidadDTO extends BaseDTO {

    private String descripcion;
    private Integer minimo;

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMinimo() {
        return minimo;
    }
    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    @Override
    public String toString() {
        return descripcion + " (" + minimo + ")";
    }
}
