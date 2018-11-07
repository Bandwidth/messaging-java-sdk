package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageApiError.class)
@JsonDeserialize(as = ImmutableMessageApiError.class)
public abstract class MessageApiError {
    public abstract String getType();
    public abstract String getDescription();
    public abstract List<MessageApiFieldError> getFieldErrors();
}
