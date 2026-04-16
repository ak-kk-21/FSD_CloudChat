package com.cloudchat.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker
        // Messages sent to destinations starting with /topic will be broadcast to all subscribers
        config.enableSimpleBroker("/topic");
        
        // Messages sent to /app are routed to @MessageMapping methods in controllers
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint that clients will connect to
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Allow all origins (for development)
                .withSockJS();  // Enable SockJS fallback for browsers that don't support WebSocket
    }
}