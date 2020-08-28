package com.bandwidth.sdk.messaging.exception;


import com.bandwidth.sdk.messaging.models.MessageApiError;
import com.bandwidth.sdk.messaging.serde.MessageSerde;
import org.asynchttpclient.Response;

public class MessagingServiceException extends RuntimeException {

    private MessageApiError error;

    public MessagingServiceException(MessageApiError error) {
        super(error.toString());
        this.error = error;
    }

    public MessageApiError getError() {
        return error;
    }

    public static void throwIfApiError(Response apiResponse) {
        int statusCode = apiResponse.getStatusCode();
        if (!isSuccessfulHttpStatusCode(statusCode)) {
            MessageApiError apiError;
            try {
                apiError = new MessageSerde().deserialize(apiResponse, MessageApiError.class);
            } catch (Exception e) {
                throw new MessagingClientException("Unknown error response from API: " + apiResponse, statusCode);
            }
            throw new MessagingServiceException(apiError);
        }
    }

    static boolean isSuccessfulHttpStatusCode(int statusCode) {
        return (statusCode / 100) == 2;
    }
}
