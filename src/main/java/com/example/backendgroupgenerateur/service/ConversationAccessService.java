package com.example.backendgroupgenerateur.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;

@Service
public class ConversationAccessService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    /**
     * 🔐 Récupère l'utilisateur connecté à partir du principal.
     */
    public User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + principal.getName()));
    }

    /**
     * 🔐 Vérifie que la conversation appartient à l'utilisateur connecté (user1 ou user2).
     */
    public Conversation getOwnedConversation(Long conversationId, User currentUser) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée : " + conversationId));
        
        // Vérifier si currentUser est user1 ou user2
        boolean isParticipant = (conversation.getUser1() != null && conversation.getUser1().getId().equals(currentUser.getId()))
                            || (conversation.getUser2() != null && conversation.getUser2().getId().equals(currentUser.getId()));

        if (!isParticipant) {
            throw new RuntimeException("Accès refusé à la conversation");
        }

        return conversation;
    }
}
