package com.bandwidth.sdk.messaging;

import static com.bandwidth.sdk.messaging.exception.ExceptionUtils.catchAsyncClientExceptions;
import static com.bandwidth.sdk.messaging.exception.ExceptionUtils.catchClientExceptions;
import static com.bandwidth.sdk.messaging.exception.MessagingServiceException.throwIfApiError;
import static org.asynchttpclient.Dsl.asyncHttpClient;

import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.SendMessageRequest;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import org.apache.commons.io.IOUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Realm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

public class MessagingClient {
    private static String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private static String BASE_URL = "https://api.catapult.inetwork.com/v2";
    private static String MEDIA_URL = "https://api.catapult.inetwork.com/v1";

    private static final Realm blankRealm = new Realm.Builder("", "")
            .setUsePreemptiveAuth(false)
            .setScheme(Realm.AuthScheme.BASIC)
            .build();

    private final String userId;
    private final AsyncHttpClient httpClient;
    private final MessageSerde messageSerde = new MessageSerde();

    /**
     * Credentials to access Bandwidth's Messaging V2 api
     *
     * @param userId Ex: u-1a2b3c4d
     * @param token  Ex: t-1a2b3c4d
     * @param secret Ex: a3947ouilar
     */
    public MessagingClient(String userId, String token, String secret) {
        this.userId = userId;

        AsyncHttpClientConfig httpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .setRealm(new Realm.Builder(token, secret)
                        .setUsePreemptiveAuth(true)
                        .setScheme(Realm.AuthScheme.BASIC))
                .build();

        httpClient = asyncHttpClient(httpClientConfig);
    }

    MessagingClient(String userId, AsyncHttpClient httpClient) {
        this.userId = userId;
        this.httpClient = httpClient;
    }

    /**
     * Send an SMS / MMS / or group MMS and wait for the response
     *
     * @param request
     * @return The message object (if successful)
     */
    public Message sendMessage(SendMessageRequest request) {
        return sendMessageAsync(request).join();
    }

    /**
     * Send an SMS / MMS / or group MMS without waiting for the response
     *
     * @param request
     * @return A completable future that completes when the request completes, with the message object as the result
     */
    public CompletableFuture<Message> sendMessageAsync(SendMessageRequest request) {
        return catchAsyncClientExceptions(() -> {
            String url = MessageFormat.format("{0}/users/{1}/messages", BASE_URL, userId);
            return httpClient.preparePost(url)
                    .setHeader(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE_APPLICATION_JSON)
                    .setBody(messageSerde.serialize(request))
                    .execute()
                    .toCompletableFuture()
                    .thenApply((resp) -> {
                        throwIfApiError(resp);
                        String responseBodyString = resp.getResponseBody(StandardCharsets.UTF_8);
                        return messageSerde.deserialize(responseBodyString, Message.class);
                    });
        });

    }

    /**
     * Uploads MMS media content.
     *
     * @param path     Path to file to upload
     * @param fileName File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(String path, String fileName) {
        return catchClientExceptions(() -> {
            try (FileInputStream stream = new FileInputStream(path)) {
                return uploadMedia(stream, fileName);
            }
        });
    }

    /**
     * Uploads MMS media content.
     *
     * @param stream   InputStream that contains the data to be sent
     * @param fileName File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(InputStream stream, String fileName) {
        return catchClientExceptions(() -> uploadMedia(IOUtils.toByteArray(stream), fileName));
    }

    /**
     * Uploads MMS media content.
     *
     * @param byteArray byte array of the data to be sent
     * @param fileName  File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(byte[] byteArray, String fileName) {
        return uploadMediaAsync(byteArray, fileName).join();
    }

    /**
     * Uploads MMS media content.
     *
     * @param byteArray byte array of the data to be sent
     * @param fileName  File name as you would like it to be uploaded
     * @return CompletableFuture that contains URL that can be sent in an MMS
     */
    public CompletableFuture<String> uploadMediaAsync(byte[] byteArray, String fileName) {
        String url = MessageFormat.format("{0}/users/{1}/media/{2}", MEDIA_URL, userId, fileName);
        return catchAsyncClientExceptions(() ->
                httpClient.preparePut(url)
                        .setBody(byteArray)
                        .execute()
                        .toCompletableFuture()
                        .thenApply((resp) -> {
                            throwIfApiError(resp);
                            return MessageFormat.format("{0}/users/{1}/media/{2}", MEDIA_URL, userId, fileName);
                        })
        );
    }

    /**
     * Downloads MMS media content asynchronously.
     *
     * @param mediaUrl URL that you would like to download
     * @return CompletableFuture that contains byte array of the mms content
     */
    public CompletableFuture<byte[]> downloadMessageMediaAsync(String mediaUrl) {
        return catchAsyncClientExceptions(() -> {
            BoundRequestBuilder building = httpClient.prepareGet(mediaUrl);
            // Remove credentials if the media is not hosted by Bandwidth
            if (!mediaUrl.startsWith(MEDIA_URL)) {
                building.setRealm(blankRealm);
            }
            return building.execute()
                    .toCompletableFuture()
                    .thenApply((resp) -> {
                        throwIfApiError(resp);
                        return resp.getResponseBodyAsBytes();
                    });
            }
        );

    }

    /**
     * Downloads MMS media content.
     *
     * @param mediaUrl media urls to download from
     * @return byte array containing the mms content
     */
    public byte[] downloadMessageMediaAsBytes(String mediaUrl) {
        return downloadMessageMediaAsync(mediaUrl).join();
    }

    /**
     * Downloads MMS media content and stores it to disk
     *
     * @param mediaUrl media urls to download from
     * @param path     path on disk to store file to
     * @return byte array containing the mms content
     */
    public void downloadMessageMediaToFile(String mediaUrl, String path) {
        catchClientExceptions(() -> {
            File tmp = new File(path);
            try (FileOutputStream stream = new FileOutputStream(tmp)) {
                stream.write(downloadMessageMediaAsBytes(mediaUrl));
            }
            return (Void) null;
        });
    }
}
