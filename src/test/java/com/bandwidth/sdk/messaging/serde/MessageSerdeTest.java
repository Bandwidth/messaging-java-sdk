package com.bandwidth.sdk.messaging.serde;

import static org.assertj.core.api.Assertions.assertThat;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import java.io.IOException;

public class MessageSerdeTest {

    private final Message message = ImmutableMessage.builder()
            .addTo("1")
            .addTo("2")
            .applicationId("1")
            .id("1")
            .owner("3")
            .segmentCount(1)
            .text("1")
            .time("1")
            .from("1")
            .direction("in")
            .build();

    private final MessageSerde serde = new MessageSerde();


    @Test
    public void testMessageSerde() throws IOException {
        String test = serde.serialize(message);
        Message deserialized = serde.deserialize(test, Message.class);
        assertThat(deserialized).isEqualTo(message);
    }

    @Test
    public void shouldIgnoreUnknownFields() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = serde.serialize(message);

        String valueWithUnexpected = objectMapper.readValue(value, ObjectNode.class)
                .put("unexpectedValue", "true")
                .toString();

        Message deserialized = serde.deserialize(valueWithUnexpected, Message.class);
        assertThat(deserialized).isEqualTo(message);
    }
}
