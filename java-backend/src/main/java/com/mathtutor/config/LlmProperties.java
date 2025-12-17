package com.mathtutor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "mathtutor.llm")
public class LlmProperties {
    private String provider = "deepseek";
    private String apiKey= "sk-c22b4a521f63464fa56bfa359dc7842b";
    private String baseUrl = "https://api.deepseek.com";
    private String model = "deepseek-chat";
    private Integer maxTokens = 4096;
    private Double temperature = 0.7;
    private String timeout = "30s";
}