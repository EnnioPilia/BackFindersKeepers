package com.example.backendgroupgenerateur.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendgroupgenerateur.dto.RegisterRequest;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.service.AdminService;
import com.example.backendgroupgenerateur.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
@PostMapping("/register-direct")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    System.out.println(">>> register-direct called");
    System.out.println("Received password: " + request.getPassword());
        System.out.println("Received password: " + request.getPassword());
    System.out.println("Received request: " + request);
    System.out.println("Received password: " + request.getPassword());

    if (request.getPassword() == null || request.getPassword().isEmpty()) {
        System.out.println("Password is missing or empty!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Le mot de passe est requis."));
    }

    try {
        User user = new User();

        user.setNom(request.getNom() != null ? request.getNom() : "");
        user.setPrenom(request.getPrenom() != null ? request.getPrenom() : "");
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setRole(request.getRole() == null ? "USER" : request.getRole().toUpperCase());
        user.setActif(true);
        user.setEnabled(true);

        User savedUser = userService.save(user);

        return ResponseEntity.ok("Utilisateur enregistré et activé avec succès.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
    }
    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody(required = false) Map<String, Object> body) {
        return ResponseEntity.ok("Test endpoint reached!");
    }
}