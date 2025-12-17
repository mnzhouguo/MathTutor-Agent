package com.mathtutor.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathtutor.config.LlmProperties;
import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class DeepSeekClient implements LlmClient {

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final LlmProperties config;

    public DeepSeekClient(LlmProperties config) {
        this.config = config;
        this.mapper = new ObjectMapper();
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        try {
            RequestBody body = buildRequestBody(request, false);
            Request httpRequest = new Request.Builder()
                .url(config.getBaseUrl() + "/chat/completions")
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Content-Type", "application/json")
                .post(body)
                .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    String errorMsg = String.format("同步API调用失败 - HTTP %d: %s", response.code(), errorBody);
                    log.error("Chat API Error: {}", errorMsg);
                    throw new RuntimeException(errorMsg);
                }

                Map<String, Object> responseBody = mapper.readValue(response.body().string(), Map.class);
                Map<String, Object> choices = (Map<String, Object>) ((java.util.List<?>) responseBody.get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choices.get("message");

                String content = (String) message.get("content");
                String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();

                return ChatResponse.success(content, sessionId);
            }
        } catch (IOException e) {
            log.error("同步API调用IO异常 - URL: {}, Key: {}, Message: {}",
                config.getBaseUrl(),
                config.getApiKey() != null ? config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "..." : "null",
                e.getMessage(), e);
            return ChatResponse.error("API调用失败: " + e.getMessage(), request.getSessionId());
        } catch (Exception e) {
            log.error("同步API调用异常 - URL: {}, Key: {}",
                config.getBaseUrl(),
                config.getApiKey() != null ? config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "..." : "null", e);
            return ChatResponse.error("API调用异常: " + e.getMessage(), request.getSessionId());
        }
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(request));
    }

    @Override
    public void chatStream(ChatRequest request, StreamHandler handler) {
        try {
            RequestBody body = buildRequestBody(request, true);
            Request httpRequest = new Request.Builder()
                .url(config.getBaseUrl() + "/chat/completions")
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Content-Type", "application/json")
                .post(body)
                .build();

            log.info("开始流式请求 - URL: {}, Model: {}", config.getBaseUrl(), config.getModel());
            CompletableFuture.runAsync(() -> {
                try (Response response = client.newCall(httpRequest).execute()) {
                    if (!response.isSuccessful()) {
                        String errorBody = response.body() != null ? response.body().string() : "No response body";
                        String errorMsg = String.format("流式API请求失败 - HTTP %d: %s", response.code(), errorBody);
                        log.error("Stream API Error: {}", errorMsg);
                        // 通知处理器请求失败
                        handler.onContent("", null);
                        return;
                    }

                    log.info("流式响应建立成功，开始读取数据");
                    BufferedReader reader = new BufferedReader(response.body().charStream());
                    String line;
                    int lineCount = 0;
                    int dataChunkCount = 0;

                    while ((line = reader.readLine()) != null) {
                        lineCount++;
                        if (line.startsWith("data: ")) {
                            String data = line.substring(6);
                            log.debug("收到流式数据第{}行: {}", lineCount, data);

                            if (data.equals("[DONE]")) {
                                log.info("流式数据传输完成，共接收{}个数据块", dataChunkCount);
                                handler.onContent("", null);
                                break;
                            }

                            try {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> json = mapper.readValue(data, Map.class);
                                java.util.List<Map<String, Object>> choices =
                                    (java.util.List<Map<String, Object>>) json.get("choices");
                                if (choices != null && !choices.isEmpty()) {
                                    Map<String, Object> delta =
                                        (Map<String, Object>) choices.get(0).get("delta");
                                    if (delta != null && delta.containsKey("content")) {
                                        String content = (String) delta.get("content");
                                        if (content != null && !content.isEmpty()) {
                                            dataChunkCount++;
                                            log.debug("发送第{}个内容块: '{}'", dataChunkCount, content);
                                            handler.onContent(content, null);
                                        }
                                    }
                                } else {
                                    log.debug("收到数据但不包含choices字段: {}", data);
                                }
                            } catch (Exception e) {
                                log.error("解析流式数据失败 - 第{}行: {}", lineCount, data, e);
                            }
                        } else if (!line.trim().isEmpty()) {
                            log.debug("非数据行: {}", line);
                        }
                    }
                    log.debug("流式数据读取结束，共处理{}行，其中{}个数据块", lineCount, dataChunkCount);
                } catch (IOException e) {
                    log.error("流式请求IO异常 - URL: {}, Key: {}, Message: {}",
                        config.getBaseUrl(),
                        config.getApiKey() != null ? config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "..." : "null",
                        e.getMessage(), e);
                    handler.onContent("", null);
                } catch (Exception e) {
                    log.error("流式请求处理异常 - URL: {}", config.getBaseUrl(), e);
                    handler.onContent("", null);
                }
            });

        } catch (Exception e) {
            log.error("启动流式请求失败 - URL: {}, Key: {}, Message: {}",
                config.getBaseUrl(),
                config.getApiKey() != null ? config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "..." : "null",
                e.getMessage(), e);
            handler.onContent("", null);
        }
    }

    @Override
    public boolean healthCheck() {
        try {
            Request request = new Request.Builder()
                .url(config.getBaseUrl() + "/models")
                .header("Authorization", "Bearer " + config.getApiKey())
                .get()
                .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            log.warn("健康检查失败 - URL: {}, Key: {}, Message: {}",
                config.getBaseUrl(),
                config.getApiKey() != null ? config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "..." : "null",
                e.getMessage());
            return false;
        }
    }

    private RequestBody buildRequestBody(ChatRequest request, boolean stream) throws IOException {
        Map<String, Object> body = Map.of(
            "model", config.getModel(),
            "messages", new Object[]{
                Map.of("role", "system", "content", "你是一个专业的数学老师，请用简洁明了的语言回答问题"),
                Map.of("role", "user", "content", request.getMessage())
            },
            "max_tokens", config.getMaxTokens(),
            "temperature", config.getTemperature(),
            "stream", stream
        );

        return RequestBody.create(
            mapper.writeValueAsString(body),
            MediaType.parse("application/json")
        );
    }
}