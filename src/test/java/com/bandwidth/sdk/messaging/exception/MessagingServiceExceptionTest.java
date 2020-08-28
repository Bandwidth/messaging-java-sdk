package com.bandwidth.sdk.messaging.exception;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.asynchttpclient.Response;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class MessagingServiceExceptionTest {

    private static final String ERROR_RESPONSE =
            "{\"type\":\"request-validation\",\"description\":\"Your request could not be accepted\",\"fieldE" +
                    "rrors\":[{\"fieldName\":\"to\",\"description\":\"'+1invalidnumber' must be replaced wi" +
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
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(ERROR_RESPONSE);
        assertThatExceptionOfType(MessagingServiceException.class)
                .isThrownBy(() -> MessagingServiceException.throwIfApiError(response));
    }

    @Test
    public void testErrorParsingResponse() {
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn("definitely {}not [valid JSON]");
        assertThatExceptionOfType(MessagingClientException.class)
                .isThrownBy(() -> MessagingServiceException.throwIfApiError(response));
    }

    @Test
    public void testMessagingClientExceptionHasStatusCode() {
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn("definitely {}not [valid JSON]");
        assertThatExceptionOfType(MessagingClientException.class)
                .isThrownBy(() -> MessagingServiceException.throwIfApiError(response))
                .satisfies(exception -> assertThat(exception.getStatusCode().isPresent())
                        .isTrue());
    }
}
