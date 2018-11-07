package com.bandwidth.sdk.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.SendMessageRequest;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MessagingClientTest {
    private final String MEDIA_URL = "https://api.catapult.inetwork.com/v1";

    private final AsyncHttpClient mockClient = mock(AsyncHttpClient.class);
    private final BoundRequestBuilder boundRequestBuilder = mock(BoundRequestBuilder.class);
    private final ListenableFuture<Response> listenableFuture = mock(ListenableFuture.class);
    private final Response response = mock(Response.class);

    private final String userId = "u-abcde123456";

    private final MessagingClient client = new MessagingClient(userId, mockClient);

    private final MessageSerde messageSerde = new MessageSerde();

    private final SendMessageRequest smr = SendMessageRequest.builder()
            .addTo("1")
            .from("2")
            .tag("test tag")
            .text("test message")
            .addMedia("http://example.com/my.jpg")
            .applicationId("a-abcde123456")
            .build();

    private final Message returnMessage = ImmutableMessage.builder()
            .addTo("1")
            .from("2")
            .tag("test tag")
            .text("test message")
            .media(Arrays.asList("http://example.com/my.jpg"))
            .applicationId("a-abcde123456")
            .direction("out")
            .id("abcde123456")
            .owner("2")
            .segmentCount(1)
            .time("timestamp")
            .build();


    @Test
    public void testMessagingClient() {
        when(mockClient.preparePost(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(boundRequestBuilder.setHeader(anyString(), anyString() )).thenReturn(boundRequestBuilder);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(returnMessage));
        when(response.getStatusCode()).thenReturn(200);
        assertThat(returnMessage).isEqualTo(client.sendMessage(smr));
    }

    @Test
    public void testdownloadMessageMediaAsBytes() {
        when(mockClient.prepareGet(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBodyAsBytes()).thenReturn("asdf".getBytes());
        when(response.getStatusCode()).thenReturn(200);
        File tmpFile = new File("./.tmp");
        tmpFile.delete();
        assertThat(tmpFile).doesNotExist();
        client.downloadMessageMediaToFile("url","./.tmp");
        assertThat(tmpFile).exists();
    }

    @Test
    public void testMediaUploadFromStream() {
        when(mockClient.preparePut(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(any(byte[].class))).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getStatusCode()).thenReturn(200);

        String testUrl = client.uploadMedia("./.tmp","fileName.txt");
        assertThat(testUrl).isEqualTo(MessageFormat.format("{0}/users/{1}/media/{2}", MEDIA_URL, userId, "fileName.txt"));
    }

}
