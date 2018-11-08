package com.bandwidth.sdk.messaging.serde;

import static com.bandwidth.sdk.messaging.exception.ExceptionUtils.catchClientExceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class MessageSerde {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module());

    public <T> T deserialize(String messageBody, TypeReference<T> clazz) {
        return catchClientExceptions(() -> mapper.readValue(messageBody, clazz));
    }

    public <T> T deserialize(String messageBody, Class<T> clazz) {
        return catchClientExceptions(() -> mapper.readValue(messageBody, clazz));
    }

    public <T> String serialize(T objectToMap) {
        return catchClientExceptions(() -> mapper.writeValueAsString(objectToMap));
    }
}
