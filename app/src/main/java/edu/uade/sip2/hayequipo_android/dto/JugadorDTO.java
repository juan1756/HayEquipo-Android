package edu.uade.sip2.hayequipo_android.dto;

import edu.uade.sip2.hayequipo_android.dto.base.BaseDTO;
import edu.uade.sip2.hayequipo_android.dto.enumerado.SexoEnum;

public class JugadorDTO extends BaseDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String celular;
    private SexoEnum sexo;

    public JugadorDTO() {
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }

    public SexoEnum getSexo() {
        return sexo;
    }
    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

}
