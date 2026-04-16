package com.cloudchat.backend.model;

import java.time.LocalDateTime;

public class ChatMessage {
    private String id;
    private String channelId;
    private String senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
    private MessageType type;
    
    public enum MessageType {
        CHAT,        // Regular chat message
        JOIN,        // User joined channel
        LEAVE,       // User left channel
        TYPING       // User is typing
    }
    
    public ChatMessage() {}
    
    public ChatMessage(String id, String channelId, String senderId, String senderName, 
                       String content, LocalDateTime timestamp, MessageType type) {
        this.id = id;
        this.channelId = channelId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
}