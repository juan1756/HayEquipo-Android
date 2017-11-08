package edu.uade.sip2.hayequipo_android.entities;

import java.util.ArrayList;

/**
 * Created by Usuario on 06/11/2017.
 */

public class Partido {

    private int id;

    private String nombre;

    private String lugar;

    private String fecha;

    private int participantes = 0;


    public Partido(int id,String nombre, String lugar, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }
}
