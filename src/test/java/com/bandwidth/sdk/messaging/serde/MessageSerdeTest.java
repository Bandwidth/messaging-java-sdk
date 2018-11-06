package com.bandwidth.sdk.messaging.serde;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.Message;


import org.junit.Assert;
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
        Assert.assertEquals(deserialized, message);
    }
}
