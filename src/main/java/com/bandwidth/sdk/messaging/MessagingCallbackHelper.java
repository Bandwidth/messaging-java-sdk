package com.bandwidth.sdk.messaging;

import com.bandwidth.sdk.messaging.models.MessageEvent;
import com.bandwidth.sdk.messaging.serde.MessageSerde;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class MessagingCallbackHelper {
    private final MessageSerde messageSerde = new MessageSerde();

    /**
     * Parse callback into a a usable object
     *
     * @param callback
     * @return Deserialized MessageEvent object
     */
    public List<MessageEvent> parseCallback(String callback) {
        return messageSerde.deserialize(callback, new TypeReference<List<MessageEvent>>(){});
    }
}
