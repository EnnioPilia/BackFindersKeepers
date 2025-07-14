package com.example.backendgroupgenerateur.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.PasswordReset;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.PasswordResetRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;

@Service
public class PasswordResetService {

    private final PasswordResetRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;  // injecté

    public PasswordResetService(PasswordResetRepository tokenRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Étape 1 - Générer et enregistrer un token, puis envoyer mail
    public String createPasswordResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Aucun utilisateur trouvé avec cet email.";
        }

        User user = optionalUser.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(30);

        PasswordReset resetToken = new PasswordReset(token, user, now, expiresAt);
        tokenRepository.save(resetToken);

        // Envoi du mail avec le token
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return "Un lien de réinitialisation a été envoyé à votre adresse email.";
    }

    // Étape 2 - Réinitialiser le mot de passe
    public String resetPassword(String token, String newPassword) {
        Optional<PasswordReset> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return "Token invalide.";
        }

        PasswordReset resetToken = optionalToken.get();

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Le token a expiré.";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Supprimer le token après usage
        tokenRepository.delete(resetToken);

        return "Mot de passe réinitialisé avec succès.";
    }
}
