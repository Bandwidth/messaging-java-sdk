package com.bandwidth.sdk.messaging.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

public class MessageSerde {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new GuavaModule())
            .registerModule(new Jdk8Module());

    public <T> T deserialize(String messageBody, Class<T> clazz) throws IOException {
        return mapper.readValue(messageBody, clazz);
    }

    public <T> String serialize(T objectToMap) throws IOException {
        return mapper.writeValueAsString(objectToMap);
    }
}
