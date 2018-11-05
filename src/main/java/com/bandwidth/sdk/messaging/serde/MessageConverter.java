package com.bandwidth.sdk.messaging.serde;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

public class MessageConverter {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module());

    public Message deserialize(String messageBody) throws IOException {
        return mapper.readValue(messageBody, ImmutableMessage.class);
    }
    public String serialize(ImmutableMessage messageEvent) throws IOException {
        String tmp = mapper.writeValueAsString(messageEvent);
        return tmp;
    }

}
