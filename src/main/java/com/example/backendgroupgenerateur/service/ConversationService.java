package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.repository.ConversationRepository;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation creerConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public Optional<Conversation> getConversationParId(Long id) {
        return conversationRepository.findById(id);
    }

    public List<Conversation> getConversationsByUserId(Long userId) {
        // Remplacement ici
        return conversationRepository.findByUser1IdOrUser2Id(userId, userId);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }
}
