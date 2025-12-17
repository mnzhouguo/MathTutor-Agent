package com.mathtutor.model;

import lombok.Data;

/**
 * 聊天响应模型
 */
@Data
public class ChatResponse {

    private String response;
    private String sessionId;
    private String status;
    private long timestamp;

    public ChatResponse(String response, String sessionId) {
        this.response = response;
        this.sessionId = sessionId;
        this.status = "success";
        this.timestamp = System.currentTimeMillis();
    }

    public static ChatResponse success(String response, String sessionId) {
        return new ChatResponse(response, sessionId);
    }

    public static ChatResponse error(String error, String sessionId) {
        ChatResponse chatResponse = new ChatResponse(error, sessionId);
        chatResponse.setStatus("error");
        return chatResponse;
    }
}