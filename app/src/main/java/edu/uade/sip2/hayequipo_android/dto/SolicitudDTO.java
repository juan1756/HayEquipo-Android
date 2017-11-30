package edu.uade.sip2.hayequipo_android.dto;

import java.util.Date;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.EstadoSolicitudEnum;

public class SolicitudDTO extends BaseDTO {

    private PartidoDTO partido;
    private JugadorDTO jugador;
    private EstadoSolicitudEnum estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    public PartidoDTO getPartido() {
        return partido;
    }
    public void setPartido(PartidoDTO partido) {
        this.partido = partido;
    }

    public JugadorDTO getJugador() {
        return jugador;
    }
    public void setJugador(JugadorDTO jugador) {
        this.jugador = jugador;
    }

    public EstadoSolicitudEnum getEstado() {
        return estado;
    }
    public void setEstado(EstadoSolicitudEnum estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
