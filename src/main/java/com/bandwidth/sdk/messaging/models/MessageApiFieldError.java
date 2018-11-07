package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageApiFieldError.class)
@JsonDeserialize(as = ImmutableMessageApiFieldError.class)
public abstract class MessageApiFieldError {
    public abstract String getFieldName();
    public abstract String getDescription();

}
