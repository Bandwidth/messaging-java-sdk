package com.bandwidth.sdk.messaging.serde;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.Message;


import org.junit.Test;

public class MessageSerdeTest {

    private ImmutableMessage message = ImmutableMessage.builder()
            .addTo("1")
            .applicationId("1")
            .id("1")
            .owner("1")
            .segmentCount(1)
            .text("1")
            .time("1")
            .from("1")
            .direction("in")
            .build();


    @Test
    public void testMessageSerde() {
        MessageSerde tmp = new MessageSerde();
        try {
            String test = tmp.serialize(message);
            tmp.deserialize(test, Message.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
