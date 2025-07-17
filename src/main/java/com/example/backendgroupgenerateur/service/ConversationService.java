package com.example.backendgroupgenerateur.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    // Création simple d'une conversation
    public Conversation creerConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    // Récupérer une conversation par son ID
    public Optional<Conversation> getConversationParId(Long id) {
        return conversationRepository.findById(id);
    }

    // Récupère une conversation entre deux utilisateurs, ou en crée une nouvelle si aucune n'existe
    public Conversation getOrCreateConversation(Long user1Id, Long user2Id) {
        // Chargement des utilisateurs
        User user1 = userRepository.findById(user1Id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur 1 non trouvé"));
        User user2 = userRepository.findById(user2Id)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur 2 non trouvé"));

        // Recherche d'une conversation existante (ordre indifférent)
        Optional<Conversation> existingConversation = conversationRepository.findByUsers(user1Id, user2Id);

        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        // Création d'une nouvelle conversation si aucune n'existe
        Conversation conversation = new Conversation();
        conversation.setUser1(user1);
        conversation.setUser2(user2);
        conversation.setNom("Conversation entre " + user1Id + " et " + user2Id);
        conversation.setPartagee(false);

        return conversationRepository.save(conversation);
    }
}
