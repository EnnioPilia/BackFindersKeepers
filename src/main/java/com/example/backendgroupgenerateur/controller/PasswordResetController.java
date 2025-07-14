package com.example.backendgroupgenerateur.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.dto.PasswordResetDto;
import com.example.backendgroupgenerateur.dto.PasswordResetRequestDto;
import com.example.backendgroupgenerateur.service.PasswordResetService;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Étape 1 : Demande de reset (envoi du mail avec token)
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequestDto request) {  // <- ici, bien PasswordResetRequestDto
        String email = request.getEmail();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email est requis");
        }
        String result = passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok(result);
    }

    // Étape 2 : Réinitialisation du mot de passe avec token et nouveau mdp
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDto dto) {
        String token = dto.getToken();
        String newPassword = dto.getNewPassword();

        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token et nouveau mot de passe sont requis");
        }
        String result = passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Void> redirectToFrontendWithToken(@RequestParam String token) {
        String redirectUrl = "http://localhost:8081/reset-password?token=" + token;
        System.out.println("Redirecting with token: " + token);
        return ResponseEntity.status(302).header("Location", redirectUrl).build();
    }

    @GetMapping("/test-redirect")
    public ResponseEntity<String> testRedirect() {
        System.out.println("🔥 test-redirect endpoint hit");  // <- très important pour debug
        return ResponseEntity.ok("OK");
    }
}
