package com.bandwidth.sdk.messaging.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableSendMessageRequest.class)
@JsonDeserialize(as = ImmutableSendMessageRequest.class)
public abstract class SendMessageRequest {
    public abstract List<String> getTo();

    public abstract String getFrom();

    public abstract Optional<String> getText();

    public abstract String getApplicationId();

    public abstract Optional<String> getTag();

    public abstract List<String> getMedia();

    public abstract Optional<String> getPriority();

    public abstract Optional<String> getExpiration();

    public static ImmutableSendMessageRequest.Builder builder() {
        return ImmutableSendMessageRequest.builder();
    }
}
