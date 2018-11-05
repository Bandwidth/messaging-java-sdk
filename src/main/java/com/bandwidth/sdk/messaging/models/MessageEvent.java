package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;

import java.util.Optional;

@Immutable
@JsonSerialize(as = ImmutableMessageEvent.class)
@JsonDeserialize(as = ImmutableMessageEvent.class)
public abstract class MessageEvent {
    public abstract String getType();
    public abstract String getTime();
    public abstract String getDescription();
    public abstract Message getMessage();
    public abstract Optional<Integer> getErrorCode();
    public abstract Optional<String> getTo();

    @JsonIgnore
    public Boolean isSms(){
        return getMessage().isSms();
    }

    @JsonIgnore
    public boolean isMms(){
        return !isSms();
    }

}
