# Bandwidth Messaging V2 Java SDK

Java SDK for [Bandwidth's V2 Messaging Platform](https://dev.bandwidth.com/v2-messaging/)

[![Build Status](https://travis-ci.org/Bandwidth/messaging-java-sdk.svg?branch=master)](https://travis-ci.org/Bandwidth/messaging-java-sdk)
[![MIT license](https://img.shields.io/crates/l/pubsub.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.bandwidth.sdk/messaging.svg)](https://search.maven.org/search?q=g:com.bandwidth.sdk.messaging)
===

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
        MessagingClient client = new MessagingClient(
                "u-aeawj73oafil", // your UserID
                "t-ayu44kfjhbf", // your api token
                "soykuhkfalkjdf" // your api secret
        );

        Message message = client.sendMessage(SendMessageRequest.builder()
                .from("+12223334444")
                .addTo("+13334445555")
                .addTo("+14445556666") // you can add multiple recipients (will be sent as group MMS)
                .applicationId("a-a7o34uhflaifadsf")
                .text("This is a test group MMS message")
                .addMedia("http://example.com/MyMedia.jpg") // adding media is optional (will be sent as MMS)
                .addMedia("http://example.com/OtherMedia.png")
                .tag("An arbitrary value I will receive in associated callbacks")
                .build()
        );
        
        //you can get information from the sent message
        String messageId = message.getId();
    }
    
}

```
