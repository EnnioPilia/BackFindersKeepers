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

import com.example.backendgroupgenerateur.dto.MessageRequest;
import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.Message;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.repository.MessageRepository;
import com.example.backendgroupgenerateur.service.ConversationAccessService;


@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationAccessService accessService;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public List<Conversation> getConversations(Principal principal) {
        User current = accessService.getCurrentUser(principal);
        List<Conversation> conversations = conversationRepository.findByUser1IdOrUser2Id(current.getId(),
                current.getId());
        System.out.println("▶▶ getConversations user=" + current.getNom() + " -> " + conversations.size() + " convs");
        return conversations;
    }

    @PostMapping
    public ResponseEntity<?> createMessage(Principal principal, @RequestBody MessageRequest dto) {
        if (dto.getConversationId() == null) {
            return ResponseEntity.badRequest().body("Le champ 'conversationId' est requis");
        }

        User current = accessService.getCurrentUser(principal);
        Conversation conv = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));

        Message message = new Message();
        message.setContenu(dto.getContent());
        message.setConversation(conv);
        message.setSender(current);

        return ResponseEntity.ok(messageRepository.save(message));
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
