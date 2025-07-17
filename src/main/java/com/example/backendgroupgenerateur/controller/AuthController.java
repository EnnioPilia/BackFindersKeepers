package com.example.backendgroupgenerateur.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.config.JWTUtils;
import com.example.backendgroupgenerateur.dto.LoginRequest;
import com.example.backendgroupgenerateur.dto.LoginResponse;
import com.example.backendgroupgenerateur.dto.RegisterRequest;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.service.EmailService;
import com.example.backendgroupgenerateur.service.UserService;
import com.example.backendgroupgenerateur.service.VerificationService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User();

            if (request.getName() != null && !request.getName().isBlank()) {
                String[] parts = request.getName().trim().split(" ", 2);
                user.setNom(parts[0]);
                user.setPrenom(parts.length > 1 ? parts[1] : "");
            } else {
                user.setNom("");
                user.setPrenom("");
            }

            user.setEmail(request.getEmail().toLowerCase());
            user.setPassword(request.getPassword());
            user.setAge(request.getAge());
            user.setRole(request.getRole() == null ? "USER" : request.getRole().toUpperCase());
            user.setActif(true);  // ATTENTIONNNNNNNN FALSEEEEE POUR AUTH FINAL !!!!!!!!!!

            User savedUser = userService.register(user);

            // Générer token et envoyer email via VerificationService
            // String verificationToken = verificationService.createVerificationToken(savedUser);
            // emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

            return ResponseEntity.ok("Utilisateur enregistré avec succès, veuillez vérifier votre email");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword())
            );

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .orElse("ROLE_USER");

            String cleanRole = role.startsWith("ROLE_") ? role.substring(5).toLowerCase() : role.toLowerCase();

            String token = jwtUtils.generateToken(request.getEmail().toLowerCase(), cleanRole);

            // Création du cookie sécurisé
            // ResponseCookie cookie = ResponseCookie.from("adminToken", token)
            //         .httpOnly(true)
            //         .secure(false) // mettre true en production avec HTTPS
            //         .path("/")
            //         .maxAge(24 * 60 * 60)
            //         .sameSite("Strict")
            //         .build();

            // response.addHeader("Set-Cookie", cookie.toString());

            // Renvoie le token dans la réponse JSON
            return ResponseEntity.ok(new LoginResponse("Connexion réussie !", token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Identifiants invalides"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token) {
        try {
            String result = verificationService.confirmToken(token);
            if (result.contains("succès")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
