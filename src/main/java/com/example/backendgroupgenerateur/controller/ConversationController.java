package com.example.backendgroupgenerateur.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.service.ConversationAccessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationAccessService accessService;

    @GetMapping
    public List<Conversation> getConversations(Principal principal) {
        User current = accessService.getCurrentUser(principal);
        List<Conversation> conversations = conversationRepository.findByUser1IdOrUser2Id(current.getId(), current.getId());
        System.out.println("▶▶ getConversations user=" + current.getNom() + " -> " + conversations.size() + " convs");
        return conversations;
    }

    @PostMapping
    public ResponseEntity<Conversation> createConversation(
            Principal principal,
            @RequestBody @Valid Conversation conversation) {
        
        User current = accessService.getCurrentUser(principal);

        // On met l'initiateur comme user1
        conversation.setUser1(current);

        // user2 doit être fourni dans le JSON
        if (conversation.getUser2() == null) {
            return ResponseEntity.badRequest().build();
        }

        Conversation saved = conversationRepository.save(conversation);
        System.out.println("▶▶ createConversation created id=" + saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(
            Principal principal,
            @PathVariable Long id) {
        
        User current = accessService.getCurrentUser(principal);
        Conversation conversation = accessService.getOwnedConversation(id, current);
        conversationRepository.delete(conversation);
        System.out.println("▶▶ deleteConversation id=" + id);
        return ResponseEntity.noContent().build();
    }
}
