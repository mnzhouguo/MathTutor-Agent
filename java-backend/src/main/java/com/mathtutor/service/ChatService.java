package com.mathtutor.service;

import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;

/**
 * 聊天服务接口
 */
public interface ChatService {

    /**
     * 处理聊天请求
     * @param request 聊天请求
     * @return 聊天响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 生成会话ID
     * @return 会话ID
     */
    String generateSessionId();
}