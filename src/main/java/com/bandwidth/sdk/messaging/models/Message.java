package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableMessage.class)
@JsonDeserialize(as = ImmutableMessage.class)
public abstract class Message {
    public abstract String getId();
    public abstract String getTime();
    public abstract String getOwner();
    public abstract Set<String> getTo();
    public abstract String getFrom();
    public abstract Optional<String> getText();
    public abstract String getApplicationId();
    public abstract Optional<List<String>> getMedia();
    public abstract Optional<String> getTag();
    public abstract String getDirection();
    public abstract Integer getSegmentCount();
    public abstract Optional<String> getPriority();
    public abstract Optional<String> getExpiration();

    @JsonIgnore
    public boolean isSms(){
        return getTo().size() == 1 && (!getMedia().isPresent() || getMedia().get().isEmpty());
    }

    @JsonIgnore
    public boolean isMms(){
        return !isSms();
    }

    @JsonIgnore
    public Set<String> getReplyNumbers(){
        Set<String> retVal = getTo()
            .stream()
            .filter(number -> !getOwner().equals(number))
            .collect(Collectors.toSet());
        retVal.add(getFrom());
        return retVal;
    }

}
