package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Enregistre un nouvel utilisateur avec rôle USER par défaut
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActif(true);
        return userRepository.save(user);
    }

    // Récupère tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Création manuelle d'un utilisateur (ex: par un admin) avec rôle USER
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setActif(true);
        return userRepository.save(user);
    }

    // Création d'un administrateur avec rôle ADMIN
    public User createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN");
        user.setActif(true);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Chargement de l'utilisateur pour Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec email : " + email));

        if (!user.isActif()) {
            throw new UsernameNotFoundException("Utilisateur non actif");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                // Ici, Spring Security attend un ou plusieurs rôles sans "ROLE_" devant.
                // Donc on met user.getRole() en majuscules et ça créera un rôle "ROLE_USER" ou "ROLE_ADMIN" automatiquement.
                .roles(user.getRole())
                .build();
    }
    public boolean deleteUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
        userRepository.deleteById(id);
        return true;
    }
    return false;
}

}
