package com.example.omnichannelfinal.repository;

import com.example.omnichannelfinal.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
}
