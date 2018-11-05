package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;

import java.util.Optional;
import java.util.Set;

@Immutable
@JsonSerialize(as = ImmutableMessageEvent.class)
@JsonDeserialize(as = ImmutableMessageEvent.class)
public abstract class MessageEvent {

    private static final String INCOMING_MESSAGE_TYPE = "message-received";

    public abstract String getType();
    public abstract String getTime();
    public abstract String getDescription();
    public abstract Message getMessage();
    public abstract Optional<Integer> getErrorCode();
    public abstract Optional<String> getTo();


    @JsonIgnore
    public boolean isIncomingMessage(){
        return INCOMING_MESSAGE_TYPE.equals(getType());
    }

    @JsonIgnore
    public boolean isDeliveryReceipt(){
        return !isIncomingMessage();
    }

    @JsonIgnore
    public boolean isSms(){
        return getMessage().isSms();
    }

    @JsonIgnore
    public boolean isMms(){
        return !isSms();
    }

    @JsonIgnore
    public boolean isError(){
        return getErrorCode().isPresent();
    }

    @JsonIgnore
    public Optional<MessageErrorTypes> getErrorType(){
        return getErrorCode().map(errorCode -> {
            if (4000 <= errorCode && errorCode < 5000)
                return MessageErrorTypes.CLIENT;
            else if (5000 <= errorCode && errorCode < 6000)
                return MessageErrorTypes.SERVER;
            else return MessageErrorTypes.UNKNOWN;
        });
    }

    @JsonIgnore
    public Set<String> getReplyNumbers(){
        return getMessage().getReplyNumbers();
    }
}
