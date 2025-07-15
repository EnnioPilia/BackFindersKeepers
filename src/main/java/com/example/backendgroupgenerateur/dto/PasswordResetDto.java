package com.example.backendgroupgenerateur.dto;

public class PasswordResetDto {
    private String token;
    private String newPassword;

    public PasswordResetDto() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
