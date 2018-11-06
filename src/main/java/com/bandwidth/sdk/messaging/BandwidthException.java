package com.bandwidth.sdk;


public class BandwidthException extends RuntimeException {

    private static final String MESSAGE_ERROR_TYPE = "message-failed";
    
    private int errorCode;
    private MessageEvent message;


    public BandwidthException(String message) {
        super(message);
    }

    public BandwidthException(Throwable cause) {
        super(cause);
    }

    public BandwidthException(String message, Throwable cause) {
        super(message, cause);
    }

    public BandwidthException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BandwidthException(String message, MessageEvent messageEvent, Throwable cause) {
        super(message, cause);
        this.messageEvent = messageEvent;
        if (messageEvent.getErrorCode().isPresent()) { //no reason this should not be the case
            this.errorCode = messageEvent;
        }
    }

    /**
     * Specifics for error codes can be found here https://dev.bandwidth.com/v2-messaging/codes.html
     */
    public MessageErrorTypes getErrorType(){
        return getErrorCode().map(errorCode -> {
            if (4000 <= errorCode && errorCode < 5000)
                return MessageErrorTypes.CLIENT;
            else if (5000 <= errorCode && errorCode < 6000)
                return MessageErrorTypes.SERVER;
            else return MessageErrorTypes.UNKNOWN;
        });
    }
}
