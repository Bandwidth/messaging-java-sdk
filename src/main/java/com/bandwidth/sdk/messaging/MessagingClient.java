package com.bandwidth.sdk.messaging;

import com.bandwidth.sdk.messaging.models.Message;
import com.bandwidth.sdk.messaging.models.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Realm;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class MessagingClient {

    private static String BASE_URL = "https://api.catapult.inetwork.com/v2";

    /**
     * Credentials to access Bandwidth's Messaging V2 api
     * @param userId Ex: u-1a2b3c4d
     * @param token Ex: t-1a2b3c4d
     * @param secret Ex: a3947ouilar
     */

    private final String userId;
    private final AsyncHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module());

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
     * @param request
     *
     * @return The message object (if successful)
     */
    public Message sendMessage(SendMessageRequest request) throws ExecutionException, InterruptedException, IOException {
        String url = MessageFormat.format("{0}/users/{1}/messages", BASE_URL, userId);
        Response response = httpClient.preparePost(url)
                .setBody(objectMapper.writeValueAsString(request))
                .execute().toCompletableFuture().get();

        //TODO: currently assuming the request is successful here
        String responseBodyString = response.getResponseBody(StandardCharsets.UTF_8);
        return objectMapper.readValue(responseBodyString, Message.class);
    }

}
