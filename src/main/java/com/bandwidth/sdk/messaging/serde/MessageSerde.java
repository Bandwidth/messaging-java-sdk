package com.bandwidth.sdk.messaging.serde;

import static com.bandwidth.sdk.messaging.exception.ExceptionUtils.catchClientExceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.asynchttpclient.Response;

import java.nio.charset.StandardCharsets;

public class MessageSerde {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module());

    public <T> T deserialize(String messageBody, TypeReference<T> clazz) {
        return catchClientExceptions(() -> mapper.readValue(messageBody, clazz));
    }

    public <T> T deserialize(Response response, TypeReference<T> clazz) {
        return deserialize(response.getResponseBody(StandardCharsets.UTF_8), clazz);
    }

    public <T> T deserialize(String messageBody, Class<T> clazz) {
        return catchClientExceptions(() -> mapper.readValue(messageBody, clazz));
    }

    public <T> T deserialize(Response response, Class<T> clazz) {
        return deserialize(response.getResponseBody(StandardCharsets.UTF_8), clazz);
    }

    public <T> String serialize(T objectToMap) {
        return catchClientExceptions(() -> mapper.writeValueAsString(objectToMap));
    }
}
