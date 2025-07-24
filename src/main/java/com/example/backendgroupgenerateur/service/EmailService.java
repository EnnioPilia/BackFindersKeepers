package com.example.backendgroupgenerateur.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private byte[] generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            System.err.println("Erreur génération QR code : " + e.getMessage());
            return null;
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "Réinitialisation de votre mot de passe";
        String resetUrl = "exp://192.168.1.26:8081/--/auth/reset-password?token=" + token;

        byte[] qrCodeImage = generateQRCodeImage(resetUrl, 250, 250);
        if (qrCodeImage == null) {
            System.err.println("QR code non généré, mail non envoyé.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlMsg = "<p>Pour réinitialiser votre mot de passe, cliquez sur ce lien :</p>"
                    + "<a href=\"" + resetUrl + "\">" + resetUrl + "</a>"
                    + "<p>Ou scannez ce QR code :</p>"
                    + "<img src='cid:qrCodeImage' />";

            helper.setText(htmlMsg, true);
            helper.addInline("qrCodeImage", new ByteArrayResource(qrCodeImage), "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Erreur envoi mail réinitialisation : " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Vérification de votre compte";
        String verificationUrl = "exp://192.168.1.26:8081/--/verify?token=" + token;

        byte[] qrCodeImage = generateQRCodeImage(verificationUrl, 250, 250);
        if (qrCodeImage == null) {
            System.err.println("QR code non généré, mail non envoyé.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlMsg = "<p>Merci de cliquer sur ce lien pour activer votre compte :</p>"
                    + "<a href=\"" + verificationUrl + "\">" + verificationUrl + "</a>"
                    + "<p>Ou scannez ce QR code :</p>"
                    + "<img src='cid:qrCodeImage' />";

            helper.setText(htmlMsg, true);
            helper.addInline("qrCodeImage", new ByteArrayResource(qrCodeImage), "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Erreur envoi mail vérification : " + e.getMessage());
        }
    }
    
    public void sendPasswordResetEmailAdmin(String toEmail, String token) {
        String subject = "Réinitialisation de votre mot de passe";
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;
        String message = "Pour réinitialiser votre mot de passe, cliquez sur ce lien : " + resetUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

}
