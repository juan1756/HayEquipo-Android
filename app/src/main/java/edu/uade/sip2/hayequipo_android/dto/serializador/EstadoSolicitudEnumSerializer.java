package edu.uade.sip2.hayequipo_android.dto.serializador;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import edu.uade.sip2.hayequipo_android.dto.enumerado.EstadoSolicitudEnum;


public class EstadoSolicitudEnumSerializer extends JsonSerializer<EstadoSolicitudEnum> {

    @Override
    public void serialize(EstadoSolicitudEnum sexoEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (sexoEnum != null) {
            jsonGenerator.writeString(sexoEnum.getClave());
        }
    }
}
