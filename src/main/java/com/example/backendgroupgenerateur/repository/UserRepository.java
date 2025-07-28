package com.example.backendgroupgenerateur.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backendgroupgenerateur.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
@Query("SELECT COUNT(u) FROM User u WHERE u.actif = true")
long countActiveUsers();
}
