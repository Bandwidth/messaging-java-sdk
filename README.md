# Bandwidth Messaging V2 Java SDK

Java SDK for [Bandwidth's V2 Messaging Platform](https://dev.bandwidth.com/v2-messaging/)

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
                "u-aeawj73oafil", // your user id
                "t-ayu44kfjhbf", // your api token
                "soykuhkfalkjdf" // your api secret
        );

        Message message = client.sendMessage(SendMessageRequest.builder()
                .from("+12223334444")
                .addTo("+13334445555")
                .addTo("+14445556666")
                .applicationId("a-a7o34uhflaifadsf")
                .text("This is a test group MMS message")
                .addMedia("http://example.com/MyMedia.jpg")
                .addMedia("http://example.com/OtherMedia.png")
                .tag("An arbitrary value I will receive in associated callbacks")
                .build()
        );
        
        //you can get information from the sent message
        String messageId = message.getId();
    }
    
}

```