package com.bandwidth.sdk.messaging.exception;

import com.bandwidth.sdk.messaging.MessagingClient;
import com.bandwidth.sdk.messaging.models.ImmutableMessageApiError;
import com.bandwidth.sdk.messaging.models.ImmutableMessageApiFieldError;
import com.bandwidth.sdk.messaging.serde.MessageSerde;
import org.asynchttpclient.Response;
import org.junit.Test;

//import static org.junit.Assert.assertThat;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessagingServiceExceptionTest {

    private static final String ERROR_RESPONSE =
            "{\"type\":\"request-validation\",\"description\":\"Your request could not be accepted\",\"fieldE" +
                    "rrors\":[{\"fieldName\":\"to\",\"description\":\"'+19198674339sbb' must be replaced wi" +
                    "th a valid E164 formatted telephone number\"}]}\n}";
    private final Response response = mock(Response.class);

    public MessagingServiceExceptionTest() {
        when(response.getStatusCode()).thenReturn(200);
    }

    @Test
    public void testSuccessResponse() {
        MessagingServiceException.throwIfApiError(response);
        //asserting no exception thrown
    }

    @Test
    public void testErrorResponse() {
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseBody()).thenReturn(ERROR_RESPONSE);
        assertThatExceptionOfType(MessagingServiceException.class)
                .isThrownBy(() -> MessagingServiceException.throwIfApiError(response));
    }

    @Test
    public void testErrorParsingResponse() {
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseBody()).thenReturn("definitely {}not [valid JSON]");
        assertThatExceptionOfType(MessagingClientException.class)
                .isThrownBy(() -> MessagingServiceException.throwIfApiError(response));
    }
}
