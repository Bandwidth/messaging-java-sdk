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
        if (4000 <= messagingCode && messagingCode < 5000)
            return MessageErrorType.CLIENT;
        if (5000 <= messagingCode && messagingCode < 6000)
            return MessageErrorType.SERVER;
        return MessageErrorType.UNKNOWN;
    }

    public String getErrorType(){
        return errorType;
    }
}
