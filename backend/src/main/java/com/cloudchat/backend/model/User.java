package com.cloudchat.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private String avatarUrl;
    
    // No-args constructor (REQUIRED by Spring)
    public User() {}
    
    // All-args constructor
    public User(String id, String username, String email, String password, LocalDateTime createdAt, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
    }
    
    // Getters
    public String getId() { 
        return id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public String getAvatarUrl() { 
        return avatarUrl; 
    }
    
    // Setters
    public void setId(String id) { 
        this.id = id; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public void setAvatarUrl(String avatarUrl) { 
        this.avatarUrl = avatarUrl; 
    }
}