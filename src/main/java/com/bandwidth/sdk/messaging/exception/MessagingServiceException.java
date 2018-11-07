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
        if (!isSuccessfulHttpStatusCode(apiResponse.getStatusCode())) {
            try {
                throw new MessagingServiceException(
                        new MessageSerde().deserialize(apiResponse.getResponseBody(), MessageApiError.class)
                );
            } catch (Exception e) {
                throw new MessagingClientException("Unknown error response from API: " + apiResponse);
            }
        }
    }

    static boolean isSuccessfulHttpStatusCode(int statusCode) {
        return (statusCode / 100) == 2;
    }
}
