package com.bandwidth.sdk.messaging.models;

public enum MessageErrorTypes {
    SERVER("server"),
    CLIENT("client"),
    UNKNOWN("unknown");

    private final String errorType;

    MessageErrorTypes(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType(){
        return errorType;
    }
}
