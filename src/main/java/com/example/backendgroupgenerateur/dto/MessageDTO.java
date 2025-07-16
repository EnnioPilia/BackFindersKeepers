package com.example.backendgroupgenerateur.dto;

public class MessageDTO {
    private Long senderId;
    private String contenu;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}