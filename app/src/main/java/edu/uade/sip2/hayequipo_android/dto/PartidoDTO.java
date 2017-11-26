package edu.uade.sip2.hayequipo_android.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.TipoPrivacidadEnum;
import edu.uade.sip2.hayequipo_android.dto.serializador.TipoPrivacidadEnumDeserializer;
import edu.uade.sip2.hayequipo_android.dto.serializador.TipoPrivacidadEnumSerializer;

public class PartidoDTO extends BaseDTO {

    private String apodo;
    private Date fecha;
    private Double precio;
    private String avatar;
    private String comentario;
    private ModalidadDTO modalidad;
    private LocalizacionDTO localizacion;

    @JsonSerialize(using = TipoPrivacidadEnumSerializer.class)
    @JsonDeserialize(using = TipoPrivacidadEnumDeserializer.class)
    private TipoPrivacidadEnum tipoPrivacidad;
    private JugadorDTO creador;

    private Integer cantidadFaltante;
    private Integer cantidadPendiente;
    private Integer cantidadAceptado;
    private Integer cantidadRechado;
    private List<JugadorDTO> jugadoresPendiente;
    private List<JugadorDTO> jugadoresAceptado;
    private List<JugadorDTO> jugadoresRechazado;

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

    public TipoPrivacidadEnum getTipoPrivacidad() {
        return tipoPrivacidad;
    }
    public void setTipoPrivacidad(TipoPrivacidadEnum tipoPrivacidad) {
        this.tipoPrivacidad = tipoPrivacidad;
    }

    public JugadorDTO getCreador() {
        return creador;
    }
    public void setCreador(JugadorDTO creador) {
        this.creador = creador;
    }

    public Integer getCantidadFaltante() {
        return cantidadFaltante;
    }
    public void setCantidadFaltante(Integer cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }

    public Integer getCantidadPendiente() {
        return cantidadPendiente;
    }
    public void setCantidadPendiente(Integer cantidadPendiente) {
        this.cantidadPendiente = cantidadPendiente;
    }

    public Integer getCantidadAceptado() {
        return cantidadAceptado;
    }
    public void setCantidadAceptado(Integer cantidadAceptado) {
        this.cantidadAceptado = cantidadAceptado;
    }

    public Integer getCantidadRechado() {
        return cantidadRechado;
    }
    public void setCantidadRechado(Integer cantidadRechado) {
        this.cantidadRechado = cantidadRechado;
    }

    public List<JugadorDTO> getJugadoresPendiente() {
        return jugadoresPendiente;
    }
    public void setJugadoresPendiente(List<JugadorDTO> jugadoresPendiente) {
        this.jugadoresPendiente = jugadoresPendiente;
    }

    public List<JugadorDTO> getJugadoresAceptado() {
        return jugadoresAceptado;
    }
    public void setJugadoresAceptado(List<JugadorDTO> jugadoresAceptado) {
        this.jugadoresAceptado = jugadoresAceptado;
    }

    public List<JugadorDTO> getJugadoresRechazado() {
        return jugadoresRechazado;
    }
    public void setJugadoresRechazado(List<JugadorDTO> jugadoresRechazado) {
        this.jugadoresRechazado = jugadoresRechazado;
    }
}
