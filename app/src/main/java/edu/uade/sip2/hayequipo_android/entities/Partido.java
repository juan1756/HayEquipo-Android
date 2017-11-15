package edu.uade.sip2.hayequipo_android.entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usuario on 06/11/2017.
 */

public class Partido {

    private int id;

    private String lugar;

    private String fecha;

    private String hora;

    private String descripcion;

    private String cantidad_participantes;

    private int participantes = 0;



    public Partido(int id,String lugar, String fecha, String hora, String desc,String cantidad) {
        this.id = id;
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = desc;
        this.cantidad_participantes = cantidad;
        this.participantes = participantes+1; // AGREGO AL CREADOR COMO PARTICIPANTE




    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public String getCantidad_participantes() {
        return cantidad_participantes;
    }

    public void setCantidad_participantes(String cantidad_participantes) {
        this.cantidad_participantes = cantidad_participantes;
    }
}
