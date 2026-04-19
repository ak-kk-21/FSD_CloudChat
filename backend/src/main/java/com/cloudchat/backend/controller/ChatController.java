package com.cloudchat.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import com.cloudchat.backend.model.ChatMessage;
import com.cloudchat.backend.model.Message;
import com.cloudchat.backend.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ChatController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Set timestamp if not set
        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(LocalDateTime.now());
        }
        
        // Set ID if not set
        if (chatMessage.getId() == null) {
            chatMessage.setId(UUID.randomUUID().toString());
        }
        
        // Save message to MongoDB (for persistence)
        Message savedMessage = new Message();
        savedMessage.setId(chatMessage.getId());
        savedMessage.setChannelId(chatMessage.getChannelId());
        savedMessage.setSenderId(chatMessage.getSenderId());
        savedMessage.setSenderName(chatMessage.getSenderName());
        savedMessage.setContent(chatMessage.getContent());
        savedMessage.setTimestamp(chatMessage.getTimestamp());
        
        messageRepository.save(savedMessage);
        
        // Return the message to broadcast to all subscribers
        return chatMessage;
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username to WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());
        headerAccessor.getSessionAttributes().put("userId", chatMessage.getSenderId());
        
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setContent(chatMessage.getSenderName() + " joined the channel!");
        
        return chatMessage;
    }
    
    @MessageMapping("/chat.typing")
    @SendTo("/topic/public")
    public ChatMessage typing(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.TYPING);
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessage;
    }
}