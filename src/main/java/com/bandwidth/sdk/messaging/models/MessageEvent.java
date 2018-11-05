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
    public abstract String getType();
    public abstract String getTime();
    public abstract String getDescription();
    public abstract Message getMessage();
    public abstract Optional<Integer> getErrorCode();
    public abstract Optional<String> getTo();


    @JsonIgnore
    public boolean isIncomingMessage(){
        return getTo().isPresent();
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
        Optional<MessageErrorTypes> errorType = Optional.empty();

        if (getErrorCode().isPresent()){
            Integer errorCode = getErrorCode().get();
            if (4000 <= errorCode && errorCode < 5000)
                errorType = Optional.of(MessageErrorTypes.CLIENT);
            else if (5000 <= errorCode && errorCode < 6000)
                errorType = Optional.of(MessageErrorTypes.SERVER);
            else errorType = Optional.of(MessageErrorTypes.UNKNOWN);
        }
        return errorType;
    }

    @JsonIgnore
    public Set<String> getReplyNumbers(){
        return getMessage().getReplyNumbers();
    }
}
