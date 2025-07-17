package com.example.backendgroupgenerateur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backendgroupgenerateur.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Trouve toutes les conversations où user1 ou user2 a l'id spécifié
    List<Conversation> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);

    // Recherche une conversation entre deux utilisateurs, quel que soit l'ordre
    @Query("SELECT c FROM Conversation c WHERE (c.user1.id = :user1Id AND c.user2.id = :user2Id) OR (c.user1.id = :user2Id AND c.user2.id = :user1Id)")
    Optional<Conversation> findByUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
