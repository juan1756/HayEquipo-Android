package edu.uade.sip2.hayequipo_android.dto;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.EstadoSolicitudEnum;

public class SolicitudDTO extends BaseDTO {

    private PartidoDTO partido;
    private JugadorDTO jugador;
    private EstadoSolicitudEnum estado;

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
}
