package com.bandwidth.sdk.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.ImmutableMessageEvent;
import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.MessageEvent;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        List<MessageEvent> returnedEvent = callbackHelper.parseCallback(serde.serialize(Arrays.asList(messageEvent)));
        assertThat(returnedEvent.get(0)).isEqualTo(messageEvent);
    }
}
