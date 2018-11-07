package com.bandwidth.sdk.messaging.models;

public enum MessageErrorType {
    SERVER("server"),
    CLIENT("client"),
    UNKNOWN("unknown");

    private final String errorType;

    MessageErrorType(String errorType) {
        this.errorType = errorType;
    }

    public static MessageErrorType fromStatusCode(Integer httpStatusCode) {
        if (400 <= httpStatusCode && httpStatusCode < 500)
            return MessageErrorType.CLIENT;
        if (500 <= httpStatusCode && httpStatusCode < 600)
            return MessageErrorType.SERVER;
        return MessageErrorType.UNKNOWN;
    }

    public static MessageErrorType fromMessagingCode(Integer messagingCode) {
        return fromStatusCode(messagingCode/10);
    }

    public String getErrorType(){
        return errorType;
    }
}
