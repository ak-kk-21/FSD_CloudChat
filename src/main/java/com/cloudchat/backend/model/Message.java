package com.cloudchat.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String channelId;
    private String senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
}