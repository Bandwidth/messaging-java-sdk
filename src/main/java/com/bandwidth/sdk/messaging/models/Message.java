package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;

import java.util.List;
import java.util.Optional;

@Immutable
@JsonSerialize(as = ImmutableMessage.class)
@JsonDeserialize(as = ImmutableMessage.class)
public abstract class Message {
    public abstract String getId();
    public abstract String getTime();
    public abstract String getOwner();
    public abstract List<String> getTo();
    public abstract String getFrom();
    public abstract String getText();
    public abstract String getApplicationId();
    public abstract Optional<List<String>> getMedia();
    public abstract Optional<String> getTag();
    public abstract String getDirection();
    public abstract Integer getSegmentCount();
}
