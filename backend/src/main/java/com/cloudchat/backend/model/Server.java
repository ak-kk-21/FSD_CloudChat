package com.cloudchat.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "servers")
public class Server {
    @Id
    private String id;
    private String name;
    private String ownerId;
    private String iconUrl;
    private boolean isPublic = true;
    private String inviteCode;
    private List<String> members = new ArrayList<>();
    private List<Channel> channels = new ArrayList<>();
    private LocalDateTime createdAt;
    
    public Server() {}
    
    public Server(String id, String name, String ownerId, String iconUrl, boolean isPublic, String inviteCode, List<String> members, List<Channel> channels, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.iconUrl = iconUrl;
        this.isPublic = isPublic;
        this.inviteCode = inviteCode;
        this.members = members != null ? members : new ArrayList<>();
        this.channels = channels != null ? channels : new ArrayList<>();
        this.createdAt = createdAt;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getOwnerId() { return ownerId; }
    public String getIconUrl() { return iconUrl; }
    public boolean isPublic() { return isPublic; }
    public String getInviteCode() { return inviteCode; }
    public List<String> getMembers() { return members; }
    public List<Channel> getChannels() { return channels; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
    public void setMembers(List<String> members) { this.members = members; }
    public void setChannels(List<Channel> channels) { this.channels = channels; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Inner Channel class
    public static class Channel {
        private String id;
        private String name;
        private String type; // "text" or "voice"
        
        public Channel() {}
        
        public Channel(String id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setType(String type) { this.type = type; }
    }
}