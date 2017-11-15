package edu.uade.sip2.hayequipo_android.entities;

/**
 * Created by Usuario on 06/11/2017.
 */

public class Usuario {


    private int id;

    private String nombre;



    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
