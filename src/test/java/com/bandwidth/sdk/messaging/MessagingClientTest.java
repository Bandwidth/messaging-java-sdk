package com.bandwidth.sdk.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.bandwidth.sdk.messaging.models.ImmutableMedia;
import com.bandwidth.sdk.messaging.models.ImmutableMessage;
import com.bandwidth.sdk.messaging.models.ListMediaResponse;
import com.bandwidth.sdk.messaging.models.Media;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.mockito.internal.matchers.StartsWith;

public class MessagingClientTest {
    private final String BASE_URL = "https://messaging.bandwidth.com/api/v2";
    private static String CONTINUATION_HEADER = "Continuation-Token";

    private final AsyncHttpClient mockClient = mock(AsyncHttpClient.class);
    private final BoundRequestBuilder boundRequestBuilder = mock(BoundRequestBuilder.class);
    private final ListenableFuture<Response> listenableFuture = mock(ListenableFuture.class);
    private final Response response = mock(Response.class);

    private final String userId = "u-abcde123456";

    private final MessagingClient client = new MessagingClient(userId, mockClient, null);

    private final MessageSerde messageSerde = new MessageSerde();

    private final SendMessageRequest request = SendMessageRequest.builder()
            .addTo("1")
            .from("2")
            .text("test message")
            .addMedia("http://example.com/my.jpg")
            .applicationId("a-abcde123456")
            .build();

    private final SendMessageRequest requestWithOptionalFields = SendMessageRequest.builder()
            .addTo("1")
            .from("2")
            .text("test message")
            .addMedia("http://example.com/my.jpg")
            .applicationId("a-abcde123456")
            .tag("test tag")
            .priority("default")
            .expiration("2040-12-01T00:00:00Z")
            .build();

    private final Message returnMessage = ImmutableMessage.builder()
            .addTo("1")
            .from("2")
            .text("test message")
            .media(Arrays.asList("http://example.com/my.jpg"))
            .applicationId("a-abcde123456")
            .direction("out")
            .id("abcde123456")
            .owner("2")
            .segmentCount(1)
            .time("timestamp")
            .build();

    private final Message returnMessageWithOptionalFields = ImmutableMessage.builder()
            .addTo("1")
            .from("2")
            .text("test message")
            .media(Arrays.asList("http://example.com/my.jpg"))
            .applicationId("a-abcde123456")
            .direction("out")
            .id("abcde123456")
            .owner("2")
            .segmentCount(1)
            .time("timestamp")
            .tag("test tag")
            .priority("default")
            .expiration("2040-12-01T00:00:00Z")
            .build();

    private final Media returnMedia1 = ImmutableMedia.builder()
            .mediaName("image1.jpg")
            .content("http://example.com/api/image1.jpg")
            .contentLength(561276)
            .build();

    private final Media returnMedia2 = ImmutableMedia.builder()
            .mediaName("image2.jpg")
            .content("http://example.com/api/image2.jpg")
            .contentLength(2703360)
            .build();

    private final List<Media> returnMediaList = new ArrayList<>(
            Arrays.asList(returnMedia1, returnMedia2)
    );


    @Test
    public void testValidBuilderPattern() {
        MessagingClient testClient = MessagingClient.builder()
                .userId("u-xxx")
                .token("t-xxx")
                .secret("xxx")
                .build();
        assertThat(testClient).isNotNull();
    }

    @Test
    public void testInvalidBuilderPatternWithoutUserId() {
        MessagingClient.Builder testClient = MessagingClient.builder()
                .token("t-xxx")
                .secret("xxx");
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> testClient.build());
    }

    @Test
    public void testInvalidBuilderPatternWithoutToken() {
        MessagingClient.Builder testClient = MessagingClient.builder()
                .userId("u-xxx")
                .secret("xxx");
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> testClient.build());
    }

    @Test
    public void testInvalidBuilderPatternWithoutSecret() {
        MessagingClient.Builder testClient = MessagingClient.builder()
                .userId("t-xxx")
                .token("t-xxx");
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> testClient.build());
    }

    @Test
    public void testMessagingClient() {
        when(mockClient.preparePost(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(
                returnMessageWithOptionalFields));
        when(response.getStatusCode()).thenReturn(200);
        assertThat(returnMessageWithOptionalFields).isEqualTo(client.sendMessage(request));
    }

    @Test
    public void testMessagingClientOptionalFields() {
        when(mockClient.preparePost(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBody(StandardCharsets.UTF_8))
                .thenReturn(messageSerde.serialize(returnMessageWithOptionalFields));
        when(response.getStatusCode()).thenReturn(200);
        assertThat(returnMessageWithOptionalFields).isEqualTo(client.sendMessage(requestWithOptionalFields));
    }

    @Test
    public void testdownloadMessageMediaAsBytes() {
        when(mockClient.prepareGet(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
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
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(any(byte[].class))).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getStatusCode()).thenReturn(200);

        String testUrl = client.uploadMedia("./.tmp","fileName.txt");
        assertThat(testUrl).isEqualTo(MessageFormat.format("{0}/users/{1}/media/{2}", BASE_URL, userId, "fileName.txt"));
    }

    @Test
    public void testListMedia() throws InterruptedException, ExecutionException {
        when(mockClient.prepareGet(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.get()).thenReturn(response);
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(returnMediaList));
        when(response.getStatusCode()).thenReturn(200);
        when(response.getHeader(CONTINUATION_HEADER)).thenReturn(null);

        ListMediaResponse media = client.listMedia();
        assertThat(media.getMedia().size()).isEqualTo(returnMediaList.size());
    }

    @Test
    public void testListMediaContinuation() throws InterruptedException, ExecutionException {
        when(mockClient.prepareGet(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.get()).thenReturn(response);
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(returnMediaList));
        when(response.getStatusCode()).thenReturn(200);
        when(response.getHeader(CONTINUATION_HEADER)).thenReturn("TokenABC", null);

        ListMediaResponse media = client.listMedia();
        assertThat(media.getMedia().size()).isEqualTo(returnMediaList.size());
    }

    @Test
    public void testDeleteMedia() {
        when(mockClient.prepareDelete(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getStatusCode()).thenReturn(200);

        client.deleteMedia("image.jpg");
    }

    @Test
    public void testMultipleClientsWithDifferentBaseUrls() {
        String mockUrl = "https://fake.bandwidth.com";
        MessagingClient client2 = new MessagingClient(userId, mockClient, mockUrl);
        when(mockClient.preparePost(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setHeader(anyString(), anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.setBody(anyString())).thenReturn(boundRequestBuilder);
        when(boundRequestBuilder.execute()).thenReturn(listenableFuture);
        when(listenableFuture.toCompletableFuture()).thenReturn(CompletableFuture.completedFuture(response));
        when(response.getResponseBody(StandardCharsets.UTF_8)).thenReturn(messageSerde.serialize(
                returnMessageWithOptionalFields));
        when(response.getStatusCode()).thenReturn(200);
        assertThat(returnMessageWithOptionalFields).isEqualTo(client.sendMessage(request));
        verify(mockClient, never()).preparePost(argThat(new StartsWith(mockUrl)));
        assertThat(returnMessageWithOptionalFields).isEqualTo(client2.sendMessage(request));
        verify(mockClient).preparePost(argThat(new StartsWith(mockUrl)));
    }
}
