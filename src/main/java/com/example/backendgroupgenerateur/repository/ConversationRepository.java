package com.example.backendgroupgenerateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backendgroupgenerateur.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Trouve toutes les conversations où user1 ou user2 a l'id spécifié
    List<Conversation> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}
