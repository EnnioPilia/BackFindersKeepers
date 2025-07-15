package com.example.backendgroupgenerateur.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "Réinitialisation de votre mot de passe";
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        String message = "Pour réinitialiser votre mot de passe, cliquez sur ce lien : " + resetUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Vérification de votre compte";
        String verificationUrl = "http://localhost:8081/verify?token=" + token;
        String message = "Merci de cliquer sur ce lien pour activer votre compte : " + verificationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
