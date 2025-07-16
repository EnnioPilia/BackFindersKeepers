package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Conversation;
import com.example.backendgroupgenerateur.model.Message;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ConversationRepository;
import com.example.backendgroupgenerateur.repository.MessageRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Message envoyerMessage(Long conversationId, Long senderId, String contenu) {
    Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
    Optional<User> userOpt = userRepository.findById(senderId);

    if (convOpt.isEmpty() || userOpt.isEmpty()) {
        throw new RuntimeException("Conversation ou utilisateur introuvable");
    }

    Conversation conversation = convOpt.get();
    User sender = userOpt.get();

    // ✅ Vérification que le sender fait partie de la conversation
    if (!conversation.getUser1().getId().equals(sender.getId()) &&
        !conversation.getUser2().getId().equals(sender.getId())) {
        throw new AccessDeniedException("Vous n'êtes pas autorisé à envoyer un message dans cette conversation");
    }

    Message message = new Message();
    message.setConversation(conversation);
    message.setSender(sender);
    message.setContenu(contenu);

    return messageRepository.save(message);
}

    public List<Message> getMessagesParConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));

        // Vérifie que l'utilisateur fait partie de la conversation
        if (!isUserInConversation(userId, conversationId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à voir les messages de cette conversation");
        }

        return messageRepository.findByConversationOrderByDateEnvoiAsc(conversation);
    }

    public void deleteMessage(Long messageId, User currentUser) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message non trouvé"));

        if (!message.getSender().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer ce message");
        }

        messageRepository.deleteById(messageId);
    }

    // Méthode corrigée pour vérifier si l'utilisateur est user1 ou user2 dans la conversation
    public boolean isUserInConversation(Long userId, Long conversationId) {
        Optional<Conversation> convOpt = conversationRepository.findById(conversationId);
        if (convOpt.isEmpty()) {
            throw new RuntimeException("Conversation introuvable");
        }
        Conversation conversation = convOpt.get();

        return (conversation.getUser1() != null && conversation.getUser1().getId().equals(userId))
            || (conversation.getUser2() != null && conversation.getUser2().getId().equals(userId));
    }
}