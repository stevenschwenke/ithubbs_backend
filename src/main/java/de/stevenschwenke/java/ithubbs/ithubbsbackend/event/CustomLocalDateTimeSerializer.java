package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class CustomLocalDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    protected CustomLocalDateTimeSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    protected CustomLocalDateTimeSerializer() {
        this(null);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider sp)
            throws IOException {
        gen.writeString(Long.toString(value.toEpochSecond()));
    }
}
