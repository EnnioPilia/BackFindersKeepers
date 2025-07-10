package com.example.backendgroupgenerateur.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role = "USER";  // par défaut en majuscules

    private boolean actif = false;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateAcceptationCGU;

    public User() {}

    // getters & setters

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role.toUpperCase(); // forcer majuscule pour cohérence
    }
    
    // autres getters/setters...


    // Getters et setters (tu peux générer avec ton IDE)

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActif() {
        return actif;
    }
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public LocalDateTime getDateAcceptationCGU() {
        return dateAcceptationCGU;
    }
    public void setDateAcceptationCGU(LocalDateTime dateAcceptationCGU) {
        this.dateAcceptationCGU = dateAcceptationCGU;
    }
    private Integer age;

public Integer getAge() {
    return age;
}

public void setAge(Integer age) {
    this.age = age;
}

}
