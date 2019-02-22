package com.bandwidth.sdk.messaging.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableMedia.class)
@JsonDeserialize(as = ImmutableMedia.class)
public abstract class Media {
    public abstract String getMediaName();
    public abstract String getContent();
    public abstract Integer getContentLength();
}
