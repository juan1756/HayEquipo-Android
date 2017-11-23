package edu.uade.sip2.hayequipo_android.dto.serializador;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import edu.uade.sip2.hayequipo_android.dto.enumerado.SexoEnum;

public class SexoEnumSerializer extends JsonSerializer<SexoEnum> {

    @Override
    public void serialize(SexoEnum sexoEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (sexoEnum != null) {
            jsonGenerator.writeString(sexoEnum.getClave());
        }
    }
}
