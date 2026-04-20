package com.cloudchat.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.cloudchat.backend.model.User;
import com.cloudchat.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TestController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/hello")
    public String hello() {
        return "CloudChat Backend is running!";
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "Service is running");
        return status;
    }
    
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}