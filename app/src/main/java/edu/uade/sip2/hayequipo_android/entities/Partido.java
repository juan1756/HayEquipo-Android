package edu.uade.sip2.hayequipo_android.entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usuario on 06/11/2017.
 */

public class Partido {

    private int id;

    private String lugar;

    private Date fecha;

    private String hora;

    private String descripcion;

    private int participantes = 0;


    public Partido(int id,String lugar, Date fecha, String hora, String desc) {
        this.id = id;
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = desc;




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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
}
