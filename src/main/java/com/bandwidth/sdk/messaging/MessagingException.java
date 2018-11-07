package com.bandwidth.sdk.messaging;


public class MessagingException extends RuntimeException {
    

    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(Throwable cause) {
        super(cause);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
