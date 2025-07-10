package com.example.backendgroupgenerateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByDateEnvoiAsc(Conversation conversation);
}
