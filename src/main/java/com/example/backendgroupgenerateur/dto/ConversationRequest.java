package com.example.backendgroupgenerateur.dto;

public class ConversationRequest {
    private Long user1Id;
    private Long user2Id;

    public ConversationRequest() {}

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }
}
