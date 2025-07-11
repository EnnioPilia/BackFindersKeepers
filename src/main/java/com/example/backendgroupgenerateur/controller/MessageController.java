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

import com.example.backendgroupgenerateur.model.Message;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.service.ConversationAccessService;
import com.example.backendgroupgenerateur.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationAccessService accessService; // pour récupérer l'utilisateur courant

    // Créer un nouveau message dans une conversation
    @PostMapping("/send/{conversationId}")
    public ResponseEntity<Message> envoyerMessage(
            @PathVariable Long conversationId,
            @RequestBody MessageDTO messageDto) {

        Message message = messageService.envoyerMessage(
                conversationId,
                messageDto.getSenderId(),
                messageDto.getContenu());
        return ResponseEntity.ok(message);
    }

    // Récupérer tous les messages d'une conversation
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessagesParConversation(
            @PathVariable Long conversationId) {

        List<Message> messages = messageService.getMessagesParConversation(conversationId);
        return ResponseEntity.ok(messages);
    }

    // Supprimer un message par son id (avec contrôle d'accès)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);
        messageService.deleteMessage(id, currentUser);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // DTO pour recevoir le corps JSON avec senderId et contenu
    public static class MessageDTO {
        private Long senderId;
        private String contenu;

        public Long getSenderId() {
            return senderId;
        }

        public void setSenderId(Long senderId) {
            this.senderId = senderId;
        }

        public String getContenu() {
            return contenu;
        }

        public void setContenu(String contenu) {
            this.contenu = contenu;
        }
    }
}
