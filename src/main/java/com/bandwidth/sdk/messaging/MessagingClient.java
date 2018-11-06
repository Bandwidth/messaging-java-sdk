package com.bandwidth.sdk.messaging;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import com.google.common.io.ByteStreams;

import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.MessageErrorType;
import com.bandwidth.sdk.messaging.models.SendMessageRequest;
import com.bandwidth.sdk.messaging.serde.MessageSerde;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Realm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

public class MessagingClient {

    private static String BASE_URL = "https://api.catapult.inetwork.com/v2";
    private static String MEDIA_URL = "https://api.catapult.inetwork.com/v1";

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
        try {
            String url = MessageFormat.format("{0}/users/{1}/messages", BASE_URL, userId);
            return httpClient.preparePost(url)
                    .setBody(messageSerde.serialize(request))
                    .execute()
                    .toCompletableFuture()
                    .thenApply((resp) -> {
                        if (isSuccessfulStatusCode(resp.getStatusCode()))
                            throw new MessagingException(MessageErrorType.fromStatusCode(resp.getStatusCode()).toString());
                        String responseBodyString = resp.getResponseBody(StandardCharsets.UTF_8);
                        return messageSerde.deserialize(responseBodyString, Message.class);
                    });
        } catch (Exception e) {
            CompletableFuture<Message> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Uploads MMS media content.
     *
     * @param path Path to file to upload
     * @param fileName File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(String path, String fileName) throws FileNotFoundException, IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
            return uploadMedia(stream, fileName);
        }
        finally{
            if (stream != null)
                stream.close();
        }
    }

    /**
     * Uploads MMS media content.
     *
     * @param stream InputStream that contains the data to be sent
     * @param fileName File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(InputStream stream, String fileName) throws IOException {
        byte[] byteArray = ByteStreams.toByteArray(stream);
        return uploadMedia(byteArray, fileName);
    }

    /**
     * Uploads MMS media content.
     *
     * @param byteArray byte array of the data to be sent
     * @param fileName File name as you would like it to be uploaded
     * @return URL that can be sent in an MMS
     */
    public String uploadMedia(byte[] byteArray, String fileName) {
        return uploadMediaAsync(byteArray, fileName).join();
    }

    /**
     * Uploads MMS media content.
     *
     * @param byteArray byte array of the data to be sent
     * @param fileName File name as you would like it to be uploaded
     * @return CompletableFuture that contains URL that can be sent in an MMS
     */
    public CompletableFuture<String> uploadMediaAsync(byte[] byteArray, String fileName){
        String url = MessageFormat.format("{0}/users/{1}/media", MEDIA_URL, userId);
        try{
            return httpClient.preparePut(url)
                    .setBody(byteArray)
                    .execute()
                    .toCompletableFuture()
                    .thenApply((resp) -> {
                        if (isSuccessfulStatusCode(resp.getStatusCode()))
                            throw new MessagingException(MessageErrorType.fromStatusCode(resp.getStatusCode()).toString());
                        return MessageFormat.format("{0}/users/{1}/media/{2}", MEDIA_URL, userId, fileName);
                    });
        } catch (MessagingException e) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Downloads MMS media content asynchronously .
     *
     * @param mediaUrl URL that you would like to download
     * @return CompletableFuture that contains byte array of the mms content
     */
    public CompletableFuture<byte[]> downloadMessageMediaAsync(String mediaUrl){
        try{
            return httpClient.prepareGet(mediaUrl)
                    .execute()
                    .toCompletableFuture()
                    .thenApply((resp) -> {
                        if (isSuccessfulStatusCode(resp.getStatusCode()))
                            throw new MessagingException(MessageErrorType.fromStatusCode(resp.getStatusCode()).toString());
                        return resp.getResponseBodyAsBytes();
                    });
        } catch (MessagingException e) {
            CompletableFuture<byte[]> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Downloads MMS media content.
     *
     * @param mediaUrl media urls to download from
     * @return byte array containing the mms content
     */
    public byte[] downloadMessageMediaAsBytes(String mediaUrl){
        return downloadMessageMediaAsync(mediaUrl).join();
    }

    /**
     * Downloads MMS media content and stores it to disk
     *
     * @param mediaUrl media urls to download from
     * @param path path on disk to store file to
     * @return byte array containing the mms content
     */
    public void downloadMessageMediaToFile(String mediaUrl, String path) throws IOException {
        FileOutputStream stream = new FileOutputStream(path);
        try {
            stream.write(downloadMessageMediaAsBytes(mediaUrl));
        }
        finally {
            if (stream != null)
                stream.close();
        }
    }

    private boolean isSuccessfulStatusCode(Integer statusCode){
        while (statusCode > 10)
            statusCode /= 10;
        return statusCode == 2;
    }
}
