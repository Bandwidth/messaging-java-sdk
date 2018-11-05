package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Immutable
@JsonSerialize(as = ImmutableMessage.class)
@JsonDeserialize(as = ImmutableMessage.class)
public abstract class Message {
    public abstract String getId();
    public abstract String getTime();
    public abstract String getOwner();
    public abstract Set<String> getTo();
    public abstract String getFrom();
    public abstract String getText();
    public abstract String getApplicationId();
    public abstract Optional<Collection<String>> getMedia();
    public abstract Optional<String> getTag();
    public abstract String getDirection();
    public abstract Integer getSegmentCount();

    @JsonIgnore
    public boolean isSms(){
        return getTo().size() == 1 && !getMedia().isPresent();
    }

    @JsonIgnore
    public boolean isMms(){
        return !isSms();
    }

}
