package com.bandwidth.sdk.messaging.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

public class MessageSerde {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module());

    public <T> T deserialize(String messageBody, Class<T> clazz) {
        try {
            return mapper.readValue(messageBody, clazz);
        } catch (IOException e) {
            //TODO: this is temporary until we have a standard Exception to use here
            throw new RuntimeException(e);
        }
    }

    public <T> String serialize(T objectToMap) throws IOException {
        return mapper.writeValueAsString(objectToMap);
    }
}
