package com.cloudchat.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.cloudchat.backend.model.User;
import com.cloudchat.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email already exists");
            return ResponseEntity.badRequest().body(error);
        }
        
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(error);
        }
        
        // Save new user
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        
        // Return user info including ID (without password)
        Map<String, Object> response = new HashMap<>();
        response.put("userId", savedUser.getId());
        response.put("username", savedUser.getUsername());
        response.put("email", savedUser.getEmail());
        response.put("createdAt", savedUser.getCreatedAt());
        response.put("message", "Registration successful");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(error);
        }
        
        User user = userOpt.get();
        
        // Check password
        if (!user.getPassword().equals(password)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(error);
        }
        
        // Return user info including ID
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("message", "Login successful");
        
        return ResponseEntity.ok(response);
    }
    
    // DEBUG: Get all users (remove in production)
    @GetMapping("/debug/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}