package com.example.backendgroupgenerateur.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.config.JWTUtils;
import com.example.backendgroupgenerateur.dto.LoginRequest;
import com.example.backendgroupgenerateur.dto.LoginResponse;
import com.example.backendgroupgenerateur.dto.RegisterRequest;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserService userService;
@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
        User user = new User();

        // Split le nom complet en nom et prénom
        if (request.getName() != null) {
            String[] parts = request.getName().split(" ", 2);
            user.setNom(parts[0]);
            user.setPrenom(parts.length > 1 ? parts[1] : "");
        } else {
            user.setNom("");
            user.setPrenom("");
        }

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassWord()); // mot de passe encodé en service UserService
        user.setAge(request.getAge());
        user.setRole(request.getRole() == null ? "USER" : request.getRole().toUpperCase());

        userService.register(user);
        return ResponseEntity.ok("Utilisateur enregistré");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

@PostMapping(value = "/login", produces = "application/json")
public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    try {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("ROLE_USER");

        String cleanRole = role.startsWith("ROLE_") ? role.substring(5).toLowerCase() : role.toLowerCase();

        String token = jwtUtils.generateToken(request.getEmail(), cleanRole);

        ResponseCookie cookie = ResponseCookie.from("adminToken", token)
                .httpOnly(true)
                .secure(false) // true en prod HTTPS
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        // Ici on renvoie uniquement un message ET le token (pas de password !)
return ResponseEntity.ok(new LoginResponse("Connexion réussie !"));
    } catch (BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Identifiants invalides"));
    }
}



}
