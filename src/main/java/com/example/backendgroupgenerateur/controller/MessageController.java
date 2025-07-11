package com.example.backendgroupgenerateur.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.model.Message;
import com.example.backendgroupgenerateur.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 🔹 Créer un nouveau message dans une conversation
    @PostMapping("/send")
    public ResponseEntity<Message> envoyerMessage(
            @RequestParam Long conversationId,
            @RequestParam Long senderId,
            @RequestParam String contenu) {

        Message message = messageService.envoyerMessage(conversationId, senderId, contenu);
        return ResponseEntity.ok(message);
    }

    // 🔹 Récupérer tous les messages d'une conversation
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessagesParConversation(
            @PathVariable Long conversationId) {

        List<Message> messages = messageService.getMessagesParConversation(conversationId);
        return ResponseEntity.ok(messages);
    }
}
