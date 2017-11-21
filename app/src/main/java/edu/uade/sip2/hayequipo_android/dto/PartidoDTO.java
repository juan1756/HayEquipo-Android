package edu.uade.sip2.hayequipo_android.dto;

import java.util.Date;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;

public class PartidoDTO extends BaseDTO {

    private String apodo;
    private Date fecha;
    private Double precio;
    private String avatar;
    private String comentario;
    private ModalidadDTO modalidad;
    private LocalizacionDTO localizacion;

    public String getApodo() {
        return apodo;
    }
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getPrecio() {
        return precio;
    }
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public ModalidadDTO getModalidad() {
        return modalidad;
    }
    public void setModalidad(ModalidadDTO modalidad) {
        this.modalidad = modalidad;
    }

    public LocalizacionDTO getLocalizacion() {
        return localizacion;
    }
    public void setLocalizacion(LocalizacionDTO localizacion) {
        this.localizacion = localizacion;
    }

}
