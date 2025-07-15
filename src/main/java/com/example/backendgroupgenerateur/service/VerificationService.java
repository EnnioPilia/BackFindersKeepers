package com.example.backendgroupgenerateur.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.model.VerificationToken;
import com.example.backendgroupgenerateur.repository.VerificationTokenRepository;

@Service
public class VerificationService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        );
        tokenRepository.save(verificationToken);
        return token;
    }

    public String confirmToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token invalide"));

        if (verificationToken.getConfirmedAt() != null) {
            return "Ce compte a déjà été activé.";
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Le token a expiré.";
        }

        verificationToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(verificationToken);

        User user = verificationToken.getUser();
        user.setActif(true);
        userService.save(user);

        return "Votre compte a été activé avec succès.";
    }
}
