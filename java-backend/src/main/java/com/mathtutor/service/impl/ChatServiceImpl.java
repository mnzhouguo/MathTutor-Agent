package com.mathtutor.service.impl;

import com.mathtutor.llm.LlmClient;
import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;
import com.mathtutor.service.ChatService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final LlmClient llmClient;

    public ChatServiceImpl(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            log.debug("Processing chat request: {}", request.getMessage());

            if (request.getSessionId() == null || request.getSessionId().isEmpty()) {
                request.setSessionId(generateSessionId());
            }

            return llmClient.chat(request);
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return ChatResponse.error("Service temporarily unavailable", request.getSessionId());
        }
    }

    @Override
    public String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}