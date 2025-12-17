package com.mathtutor.llm;

import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;
import java.util.concurrent.CompletableFuture;

public interface LlmClient {
    ChatResponse chat(ChatRequest request);
    CompletableFuture<ChatResponse> chatAsync(ChatRequest request);
    void chatStream(ChatRequest request, StreamHandler handler);
    boolean healthCheck();

    @FunctionalInterface
    interface StreamHandler {
        void onContent(String content, Runnable onComplete);
    }
}