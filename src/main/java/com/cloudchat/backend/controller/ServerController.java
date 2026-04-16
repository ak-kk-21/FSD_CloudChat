package com.cloudchat.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.cloudchat.backend.model.Server;
import com.cloudchat.backend.repository.ServerRepository;
import com.cloudchat.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/servers")
@CrossOrigin(origins = "*")
public class ServerController {
    
    @Autowired
    private ServerRepository serverRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all servers for a user
    @GetMapping
    public ResponseEntity<?> getUserServers(@RequestHeader("userId") String userId) {
        List<Server> servers = serverRepository.findByMembersContaining(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("servers", servers);
        response.put("count", servers.size());
        
        return ResponseEntity.ok(response);
    }
    
    // Create a new server
    @PostMapping
    public ResponseEntity<?> createServer(@RequestBody Server server, @RequestHeader("userId") String userId) {
        if (server.getName() == null || server.getName().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Server name is required");
            return ResponseEntity.badRequest().body(error);
        }
        
        server.setOwnerId(userId);
        server.setCreatedAt(LocalDateTime.now());
        
        if (server.getMembers() == null) {
            server.setMembers(new ArrayList<>());
        }
        if (!server.getMembers().contains(userId)) {
            server.getMembers().add(userId);
        }
        
        if (server.getChannels() == null) {
            server.setChannels(new ArrayList<>());
        }
        if (server.getChannels().isEmpty()) {
            Server.Channel generalChannel = new Server.Channel();
            generalChannel.setId(UUID.randomUUID().toString());
            generalChannel.setName("general");
            generalChannel.setType("text");
            server.getChannels().add(generalChannel);
        }
        
        Server savedServer = serverRepository.save(server);
        return ResponseEntity.ok(savedServer);
    }
    
    // Get channels in a server
    @GetMapping("/{serverId}/channels")
    public ResponseEntity<?> getServerChannels(@PathVariable("serverId") String serverId) {
        Optional<Server> serverOpt = serverRepository.findById(serverId);
        
        if (serverOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Server not found");
            return ResponseEntity.status(404).body(error);
        }
        
        Server server = serverOpt.get();
        return ResponseEntity.ok(server.getChannels());
    }
    
    // Create a channel in a server
    @PostMapping("/{serverId}/channels")
    public ResponseEntity<?> createChannel(
            @PathVariable("serverId") String serverId,
            @RequestBody Map<String, String> channelData,
            @RequestHeader("userId") String userId) {
        
        Optional<Server> serverOpt = serverRepository.findById(serverId);
        
        if (serverOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Server not found");
            return ResponseEntity.status(404).body(error);
        }
        
        Server server = serverOpt.get();
        
        if (!server.getMembers().contains(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "You are not a member of this server");
            return ResponseEntity.status(403).body(error);
        }
        
        Server.Channel newChannel = new Server.Channel();
        newChannel.setId(UUID.randomUUID().toString());
        newChannel.setName(channelData.get("name"));
        newChannel.setType(channelData.getOrDefault("type", "text"));
        
        server.getChannels().add(newChannel);
        serverRepository.save(server);
        
        return ResponseEntity.ok(newChannel);
    }
    
    // Join a server
    @PostMapping("/{serverId}/join")
    public ResponseEntity<?> joinServer(
            @PathVariable("serverId") String serverId, 
            @RequestHeader("userId") String userId) {
        
        Optional<Server> serverOpt = serverRepository.findById(serverId);
        
        if (serverOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Server not found");
            return ResponseEntity.status(404).body(error);
        }
        
        Server server = serverOpt.get();
        
        if (server.getMembers().contains(userId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Already a member");
            return ResponseEntity.badRequest().body(error);
        }
        
        server.getMembers().add(userId);
        serverRepository.save(server);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Joined server successfully");
        response.put("serverId", serverId);
        
        return ResponseEntity.ok(response);
    }
}