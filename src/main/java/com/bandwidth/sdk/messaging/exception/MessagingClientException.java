package com.bandwidth.sdk.messaging.exception;


import java.util.Optional;

public class MessagingClientException extends RuntimeException {
    private final Optional<Integer> statusCode;

    public MessagingClientException(String message) {
        super(message);
        statusCode = Optional.empty();
    }

    public MessagingClientException(Throwable cause) {
        super(cause);
        statusCode = Optional.of(cause)
                .filter(throwable -> throwable instanceof MessagingClientException)
                .flatMap(throwable -> ((MessagingClientException) throwable).getStatusCode());
    }

    public MessagingClientException(String message, Throwable cause) {
        super(message, cause);
        statusCode = Optional.empty();
    }

    MessagingClientException(String message, int statusCode) {
        super(message);
        this.statusCode = Optional.of(statusCode);
    }

    public Optional<Integer> getStatusCode() {
        return statusCode;
    }
}
