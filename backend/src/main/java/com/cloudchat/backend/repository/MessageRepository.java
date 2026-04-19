package com.cloudchat.backend.repository;

import com.cloudchat.backend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChannelIdOrderByTimestampAsc(String channelId);
}