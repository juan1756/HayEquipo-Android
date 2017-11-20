package edu.uade.sip2.hayequipo_android.dto.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class BaseDTO implements Serializable {

    private Long codigo;

    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public JSONObject toJsonObject() throws JsonProcessingException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject json;

        json = new JSONObject(mapper.writeValueAsString(this));
        return json;
    }
}
