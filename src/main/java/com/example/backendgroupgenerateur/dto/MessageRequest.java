package com.example.backendgroupgenerateur.dto;

import java.time.LocalDateTime;

public class MessageRequest {
    private Long conversationId;
    private String content;

    public MessageRequest() {
    }

    public MessageRequest(Long conversationId, Long senderId, String content, LocalDateTime timestamp) {
        this.conversationId = conversationId;
        this.content = content;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
