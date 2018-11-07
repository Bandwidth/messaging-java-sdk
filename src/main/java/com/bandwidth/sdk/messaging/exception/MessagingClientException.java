package com.bandwidth.sdk.messaging.exception;


public class MessagingClientException extends RuntimeException {

    public MessagingClientException(String message) {
        super(message);
    }

    public MessagingClientException(Throwable cause) {
        super(cause);
    }

    public MessagingClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
