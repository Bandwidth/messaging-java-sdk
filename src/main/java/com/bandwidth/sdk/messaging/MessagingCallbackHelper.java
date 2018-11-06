package com.bandwidth.sdk.messaging;

import com.bandwidth.sdk.messaging.models.MessageEvent;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import java.io.IOException;

public class MessagingCallbackHelper {
    private final MessageSerde messageSerde = new MessageSerde();

    /**
     * Parse callback into a a usable object
     *
     * @param callback
     * @return Deserialized MessageEvent object
     */
    public MessageEvent parseCallback(String callback) throws IOException {
        return messageSerde.deserialize(callback, MessageEvent.class);
    }
}
