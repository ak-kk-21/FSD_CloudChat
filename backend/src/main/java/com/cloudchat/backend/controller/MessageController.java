package com.cloudchat.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.cloudchat.backend.model.Message;
import com.cloudchat.backend.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/channels")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class MessageController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    // Get message history for a channel
    @GetMapping("/{channelId}/messages")
    public ResponseEntity<?> getChannelMessages(@PathVariable("channelId") String channelId) {
        List<Message> messages = messageRepository.findByChannelIdOrderByTimestampAsc(channelId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("channelId", channelId);
        response.put("messages", messages);
        response.put("count", messages.size());
        
        return ResponseEntity.ok(response);
    }
    
    // Create a message (REST fallback)
    @PostMapping("/{channelId}/messages")
    public ResponseEntity<?> createMessage(
            @PathVariable("channelId") String channelId,
            @RequestBody Map<String, String> messageData,
            @RequestHeader("userId") String userId,
            @RequestHeader("userName") String userName) {
        
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setChannelId(channelId);
        message.setSenderId(userId);
        message.setSenderName(userName);
        message.setContent(messageData.get("content"));
        message.setTimestamp(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }
}