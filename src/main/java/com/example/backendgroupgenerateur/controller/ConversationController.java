package com.example.backendgroupgenerateur.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backendgroupgenerateur.dto.ConversationRequest;
import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;
import com.example.backendgroupgenerateur.service.ConversationAccessService;
import com.example.backendgroupgenerateur.service.ConversationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationAccessService accessService;

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id, Principal principal) {
        User current = accessService.getCurrentUser(principal);

        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conversation non trouvée avec id : " + id));

        if (!conversation.getUser1().getId().equals(current.getId()) &&
                !conversation.getUser2().getId().equals(current.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllConversations() {
        return ResponseEntity.ok(conversationRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Conversation> createConversation(
            Principal principal,
            @RequestBody @Valid Conversation conversation) {

        User current = accessService.getCurrentUser(principal);

        // On met l'initiateur comme user1
        conversation.setUser1(current);

        if (conversation.getUser2() == null || conversation.getUser2().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Charger user2 depuis la base
        User user2 = userRepository.findById(conversation.getUser2().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user2 inconnu"));

        conversation.setUser2(user2);

        Conversation saved = conversationRepository.save(conversation);
        System.out.println("▶▶ createConversation created id=" + saved.getId());
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/conversation/getOrCreate")
    public ResponseEntity<Conversation> getOrCreateConversation(
            Principal principal,
            @RequestBody ConversationRequest request) {

        User currentUser = accessService.getCurrentUser(principal);
        Long user1Id = currentUser.getId(); // ID de l'utilisateur connecté (via token)
        Long user2Id = request.getUser2Id(); // ID du destinataire

        Conversation conversation = conversationService.getOrCreateConversation(user1Id, user2Id);
        return ResponseEntity.ok(conversation);
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
