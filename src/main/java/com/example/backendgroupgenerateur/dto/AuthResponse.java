package com.example.backendgroupgenerateur.dto;

public class AuthResponse {
    private String token;

    public AuthResponse() {
        // constructeur par défaut
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    // getter
    public String getToken() {
        return token;
    }

    // setter
    public void setToken(String token) {
        this.token = token;
    }
}
