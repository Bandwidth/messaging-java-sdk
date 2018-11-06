package com.bandwidth.sdk.messaging;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.ImmutableMessageEvent;
import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.MessageEvent;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MessagingCallbackHelperTest {

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

    private final MessageEvent messageEvent = ImmutableMessageEvent.builder()
            .message(message)
            .description("message-delivered")
            .time("1")
            .type("Message delivered to carrier")
            .build();

    private final MessagingCallbackHelper callbackHelper = new MessagingCallbackHelper();
    private final MessageSerde serde = new MessageSerde();


    @Test
    public void testParseCallback() throws IOException {
        MessageEvent returnedEvent = callbackHelper.parseCallback(serde.serialize(messageEvent));
        Assert.assertEquals(returnedEvent,messageEvent);
    }
}
