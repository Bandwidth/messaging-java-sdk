package com.bandwidth.sdk.messaging.exception;

public interface ThrowableSupplier<T> {
    T get() throws Throwable;
}