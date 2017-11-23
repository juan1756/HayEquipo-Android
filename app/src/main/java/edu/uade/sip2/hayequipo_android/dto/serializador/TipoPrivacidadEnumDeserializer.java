package edu.uade.sip2.hayequipo_android.dto.serializador;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import edu.uade.sip2.hayequipo_android.dto.enumerado.TipoPrivacidadEnum;

public class TipoPrivacidadEnumDeserializer extends JsonDeserializer<TipoPrivacidadEnum> {

    @Override
    public TipoPrivacidadEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String jsonValue = jsonParser.getText();
        for (final TipoPrivacidadEnum enumValue : TipoPrivacidadEnum.values()) {
            if (enumValue.getClave().compareToIgnoreCase(jsonValue) == 0) {
                return enumValue;
            }
        }
        return null;
    }
}
