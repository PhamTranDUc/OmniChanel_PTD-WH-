package com.example.omnichannelfinal.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserChatRepository extends MongoRepository<UserChat, String> {
    List<UserChat> findAllByStatus(Status status);
}
