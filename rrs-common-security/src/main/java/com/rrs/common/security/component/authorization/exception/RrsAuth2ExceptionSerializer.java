package com.rrs.common.security.component.authorization.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @author zjm
 */
public class RrsAuth2ExceptionSerializer extends StdSerializer<RrsAuth2Exception> {
    private static final long serialVersionUID = 1L;

    public RrsAuth2ExceptionSerializer() {
        super(RrsAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(RrsAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", "500");
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getOAuth2ErrorCode());
        gen.writeEndObject();
    }
}
