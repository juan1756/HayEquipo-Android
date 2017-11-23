package edu.uade.sip2.hayequipo_android.dto.serializador;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import edu.uade.sip2.hayequipo_android.dto.enumerado.TipoPrivacidadEnum;

public class TipoPrivacidadEnumSerializer extends JsonSerializer<TipoPrivacidadEnum> {

    @Override
    public void serialize(TipoPrivacidadEnum tipoPrivacidadEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (tipoPrivacidadEnum != null) {
            jsonGenerator.writeString(tipoPrivacidadEnum.getClave());
        }
    }
}
