package edu.uade.sip2.hayequipo_android.entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usuario on 06/11/2017.
 */

public class Partido {

    private int id;

    private String nombre;

    private String lugar;

    private String fecha;

    private String hora;

    private int precio = 0;

    private String descripcion;

    private String cantidad_participantes;

    private int participantes = 0;

    private String avatar = "";



    public Partido(int id,String nombre, String lugar, String fecha, String hora,int precio, String desc,String cantidad,String avatar) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = desc;
        this.precio = precio;
        this.cantidad_participantes = cantidad;
        this.participantes = participantes+1; // AGREGO AL CREADOR COMO PARTICIPANTE
        this.avatar = avatar;




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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
