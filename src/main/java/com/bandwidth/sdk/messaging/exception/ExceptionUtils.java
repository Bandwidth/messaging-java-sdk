package com.bandwidth.sdk.messaging.exception;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class ExceptionUtils {

    public static <T> CompletableFuture<T> catchAsyncClientExceptions(Callable<CompletableFuture<T>> supplier) {
        try {
            return supplier.call();
        } catch (Throwable e) {
            CompletableFuture<T> future = new CompletableFuture<>();
            future.completeExceptionally(new MessagingClientException(e));
            return future;
        }
    }

    public static <T> T catchClientExceptions(Callable<T> supplier) {
        try {
            return supplier.call();
        } catch (Throwable e) {
            throw new MessagingClientException(e);
        }
    }

}
