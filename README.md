# Bandwidth Messaging V2 Java SDK

Java SDK for [Bandwidth's V2 Messaging Platform](https://dev.bandwidth.com/v2-messaging/)

[![Build Status](https://travis-ci.org/Bandwidth/messaging-java-sdk.svg?branch=master)](https://travis-ci.org/Bandwidth/messaging-java-sdk)
[![MIT license](https://img.shields.io/crates/l/pubsub.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.bandwidth.sdk/messaging.svg)](https://search.maven.org/search?q=g:com.bandwidth.sdk.messaging)

[Javadoc API Documentation](https://dev.bandwidth.com/messaging-java-sdk/)

## Dependency

### Maven
```xml
<dependency>
    <groupId>com.bandwidth.sdk</groupId>
    <artifactId>messaging</artifactId>
    <version>(put desired version here)</version>
</dependency>
```
### Gradle
```
compile 'com.bandwidth.sdk:messaging:(put desired version here)'
```


## Quick Start
### Important Links
* [Bandwidth Dashboard](https://dashboard.bandwidth.com/portal/report/#login:)
* [Bandwidth Application](https://app.bandwidth.com/login)
* [Bandwidth Developer Homepage](https://dev.bandwidth.com/)

```java
public class MyAwesomeBandwidthMessagingApp{
    
    public static void main(String[] args){
        
        // Create an instance of the Messaging Client
        MessagingClient client = new MessagingClient(
                "u-aeawj73oafil", // your UserID
                "t-ayu44kfjhbf", // your api token
                "soykuhkfalkjdf" // your api secret
        );        
        // Upload a local file that can be used for MMS 
        String myUploadedMedia = client.uploadMedia("/path/to/file.jpg","media_file_name.jpg");
        
        // Sending a group MMS with the uploaded media. You can also use pass a url to any publicly accessible media file.
        Message message = client.sendMessage(
            SendMessageRequest.builder()
                .from("+12223334444")
                .addTo("+13334445555")
                .addTo("+14445556666") // you can add multiple recipients (will be sent as group MMS)
                .applicationId("a-a7o34uhflaifadsf")
                .text("This is a test group MMS message")
                .addMedia("http://example.com/MyMedia.jpg") // adding media is optional (will be sent as MMS)
                .addMedia(myUploadedMedia)
                .tag("An arbitrary value I will receive in associated callbacks")
                .build()
        );
        
        // You can get information from the sent message
        String messageId = message.getId();
    }
    
    // Example of handling callbacks
    public void parseCallbacks(String callbackString){
        MessagingCallbackHelper helper = new MessagingCallbackHelper();
        
        List<MessageEvent> callbacks = parseCallback(callbackString);
        
        for (MessageEvent messageEvent : callbacks) {
            // Handle an error that occurred while trying to send the message
            if (messageEvent.isError()){
                logger.error("There was a messaging error {} {}", 
                        messageEvent.getErrorType().get(), 
                        messageEvent.getMessage().getDescription());
                doSomethingWithErrors(messageEvent);
            }
            // If the message is incoming from an end user to your phone number
            else if (messageEvent.isIncomingMessage()){
                logger.info("User with phone number: {} sent a message with text: {}", 
                        messageEvent.getMessage().getFrom(),
                        messageEvent.getMessage().getText());
                doSomethingWithIncomingMessage(messageEvent.getReplyNumbers(),messageEvent.getMessage().getText());
            }
            // Manage incoming SMS delivery receipts from end users
            else {
                logger.info("User with phone number: {} received message with ID: {}", 
                        Arrays.toString(messageEvent.getMessage().getTo()),
                        messageEvent.getMessage().getId());
                doSomethingWithReceipts(messageEvent.getMessage().getId());
            }
        }
    }
    
}

```
