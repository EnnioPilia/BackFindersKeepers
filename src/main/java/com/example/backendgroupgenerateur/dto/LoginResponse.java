package com.example.backendgroupgenerateur.dto;

public class LoginResponse {
    private final String message;

    public LoginResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
