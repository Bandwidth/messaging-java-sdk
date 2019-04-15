package com.bandwidth.sdk.messaging.models;

import com.bandwidth.sdk.messaging.MessagingClient;

import org.immutables.value.Value.Immutable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Immutable
public abstract class ListMediaResponse {
    public abstract MessagingClient getClient();
    public abstract List<Media> getMedia();
    public abstract Optional<String> getContinuationToken();

    public boolean isAdditionalMediaAvailable() {
        return getContinuationToken().isPresent();
    }

    public final ListMediaResponse next() {
        return nextAsync().join();
    }

    public final CompletableFuture<ListMediaResponse> nextAsync() {
        if (!isAdditionalMediaAvailable()) {
            return null;
        }
        return getClient().listMediaAsync(getContinuationToken().get());
    }
}
