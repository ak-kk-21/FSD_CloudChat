package com.cloudchat.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String channelId;    // ← This must exist and match exactly
    private String senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
    
    // No-args constructor (REQUIRED)
    public Message() {}
    
    // Constructor with all fields
    public Message(String id, String channelId, String senderId, String senderName, String content, LocalDateTime timestamp) {
        this.id = id;
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getId() { return id; }
    public String getChannelId() { return channelId; }
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}