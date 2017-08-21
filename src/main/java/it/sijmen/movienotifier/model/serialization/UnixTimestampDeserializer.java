package it.sijmen.movienotifier.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UnixTimestampDeserializer extends JsonDeserializer<Long> {

    private static final SimpleDateFormat PATHEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            return PATHEFORMAT.parse(p.getText().trim()).getTime();
        } catch (Exception e) {
            return -1L;
        }
    }
}
