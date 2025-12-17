package com.mathtutor.service;

import com.mathtutor.llm.LlmClient;
import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;
import com.mathtutor.service.impl.ChatServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private LlmClient llmClient;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    void testGenerateSessionId() {
        String sessionId = chatService.generateSessionId();
        assertNotNull(sessionId, "Session ID should not be null");
        assertFalse(sessionId.isEmpty(), "Session ID should not be empty");
        assertEquals(32, sessionId.length(), "Session ID should be 32 characters long");
    }

    @Test
    void testChatWithSuccessResponse() {
        ChatRequest request = new ChatRequest();
        request.setMessage("What is 2+2?");
        request.setSessionId("test-session");

        ChatResponse expectedResponse = ChatResponse.success("2+2 = 4", "test-session");
        when(llmClient.chat(any(ChatRequest.class))).thenReturn(expectedResponse);

        ChatResponse actualResponse = chatService.chat(request);

        assertNotNull(actualResponse, "Response should not be null");
        assertEquals("2+2 = 4", actualResponse.getResponse(), "Response content should match");
        assertEquals("test-session", actualResponse.getSessionId(), "Session ID should match");
        assertEquals("success", actualResponse.getStatus(), "Status should be success");
    }

    @Test
    void testChatWithNullSessionId() {
        ChatRequest request = new ChatRequest();
        request.setMessage("What is 2+2?");
        request.setSessionId(null);

        ChatResponse expectedResponse = ChatResponse.success("2+2 = 4", "generated-session");
        when(llmClient.chat(any(ChatRequest.class))).thenReturn(expectedResponse);

        ChatResponse actualResponse = chatService.chat(request);

        assertNotNull(actualResponse, "Response should not be null");
        assertNotNull(request.getSessionId(), "Session ID should be generated");
    }

    @Test
    void testChatWithException() {
        ChatRequest request = new ChatRequest();
        request.setMessage("What is 2+2?");
        request.setSessionId("test-session");

        when(llmClient.chat(any(ChatRequest.class)))
            .thenThrow(new RuntimeException("API error"));

        ChatResponse actualResponse = chatService.chat(request);

        assertNotNull(actualResponse, "Response should not be null");
        assertEquals("error", actualResponse.getStatus(), "Status should be error");
        assertTrue(actualResponse.getResponse().contains("unavailable"),
                  "Error message should indicate service unavailability");
    }
}