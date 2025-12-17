package com.mathtutor.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 聊天请求模型
 */
@Data
public class ChatRequest {

    @NotBlank(message = "用户消息不能为空")
    private String message;

    private String sessionId;
    private String context;
}