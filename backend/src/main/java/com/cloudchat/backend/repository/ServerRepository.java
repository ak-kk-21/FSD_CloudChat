package com.cloudchat.backend.repository;

import com.cloudchat.backend.model.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ServerRepository extends MongoRepository<Server, String> {
    List<Server> findByMembersContaining(String userId);
    List<Server> findByOwnerId(String ownerId);
    List<Server> findByIsPublicTrue();
    Optional<Server> findByInviteCode(String inviteCode);
}