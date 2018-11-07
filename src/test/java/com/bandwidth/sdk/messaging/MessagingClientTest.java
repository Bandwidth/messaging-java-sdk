package com.bandwidth.sdk.messaging;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.ArgumentMatchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MessagingClientTest {


    private final AsyncHttpClient mockClient = mock(AsyncHttpClient.class);
    private final BoundRequestBuilder boundRequestBuilder = mock(BoundRequestBuilder.class);
    private final ListenableFuture<Response> listenableFuture = mock(ListenableFuture.class);
    private final Response response = mock(Response.class);
    private final FileInputStream inputStream = mock(FileInputStream.class);

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
    public void testMessagingClient() throws IOException {
        when(mockClient.preparePost(ArgumentMatchers.anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(ArgumentMatchers.anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(returnMessage));

        assertThat(returnMessage).isEqualTo(client.sendMessage(smr));
    }

    @Test
    public void testdownloadMessageMediaAsBytes() throws IOException {
        when(mockClient.prepareGet(ArgumentMatchers.anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBodyAsBytes()).thenReturn("asdf".getBytes());

        client.downloadMessageMediaToFile("url","./.tmp");
    }

    @Test
    public void testMediaUploadFromStream() throws IOException {
        when(mockClient.preparePut(ArgumentMatchers.anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(ArgumentMatchers.any(byte[].class))).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));

        client.uploadMedia("./.tmp","fileName.txt");
    }

}
