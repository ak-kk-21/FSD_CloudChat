package com.cloudchat.backend.repository;

import com.cloudchat.backend.model.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ServerRepository extends MongoRepository<Server, String> {
    List<Server> findByMembersContaining(String userId);
    List<Server> findByOwnerId(String ownerId);
}