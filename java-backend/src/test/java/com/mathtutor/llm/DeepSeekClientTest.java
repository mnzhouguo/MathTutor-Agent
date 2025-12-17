package com.mathtutor.llm;

import com.mathtutor.config.LlmProperties;
import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeepSeekClientTest {

    @Mock
    private LlmProperties config;

    private DeepSeekClient client;

    @BeforeEach
    void setUp() {
        when(config.getApiKey()).thenReturn("sk-c22b4a521f63464fa56bfa359dc7842b");
        when(config.getBaseUrl()).thenReturn("https://api.deepseek.com");
        when(config.getModel()).thenReturn("deepseek-chat");
        when(config.getMaxTokens()).thenReturn(4096);
        when(config.getTemperature()).thenReturn(0.7);

        client = new DeepSeekClient(config);
    }

    @Test
    void testHealthCheck() {
        assertTrue(client.healthCheck(), "Should return true with valid API key");
    }

    @Test
    void testAsyncChat() {
        ChatRequest request = new ChatRequest();
        request.setMessage("Test message");

        CompletableFuture<ChatResponse> future = client.chatAsync(request);
        assertNotNull(future);
    }


    @Test
    void testStreamHandler() {
        ChatRequest request = new ChatRequest();
        request.setMessage("hello");

        System.out.println("\n=== 开始流式测试 ===");
        System.out.println("请求消息: " + request.getMessage());
        System.out.println("API Base URL: " + config.getBaseUrl());
        System.out.println("API Key: " + config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "...");

        LlmClient.StreamHandler handler = new LlmClient.StreamHandler() {
            private int chunkCount = 0;

            @Override
            public void onContent(String content, Runnable onComplete) {
                if (content != null && !content.isEmpty()) {
                    chunkCount++;
                    System.out.print(content); // 每个数据块单独一行显示
                }
                if (onComplete != null) {
                    System.out.println("\n=== 流式结束，共收到 " + chunkCount + " 个数据块 ===");
                    onComplete.run();
                }
            }
        };

        System.out.println("发送流式请求...");
        assertDoesNotThrow(() -> client.chatStream(request, handler));
        System.out.println("流式请求已发送，等待响应...");

        // 等待更长时间让异步任务有机会完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("=== 流式测试结束 ===");
    }

    @Test
    void testSyncChat() {
        ChatRequest request = new ChatRequest();

        String promptTemplate = "你是一名初中数学教学专家，擅长分析压轴题的命题结构与解题思路。请针对用户提供的数学压轴题，完成以下任务：1.题目分析：整体解读题目背景、难点、易错点、解决思路、考查意图。题目背景：请说明题目的背景、题目的难易程度。考查意图：分析题目考查的核心知识点与能力要求。难点解析：指出题目中可能存在的难点与易错点，并给出相应的解题建议。2.分问解析：对每一小问独立分析，包括：考点识别：明确该问对应的核心考点，并说明属于哪个知识模块（如函数、几何、代数综合等）。知识点梳理：列出解决该问必须掌握的概念、定理、公式、方法，并适当说明它们在该题中的应用方式。解题方案：提供清晰的解题思路与步骤，包括关键推理环节、可能用到的转化策略或辅助线作法等，不给出具体数值结果或最终答案。3.解题建议：总结解题过程中应注意的事项与策略，帮助学生提升解题能力。请严格按照以下格式输出：## 题目分析 ### 题目背景 ### 考查意图 ### 难点解析 ## 各问分析 ### 第一问分析 **考点识别：** - 考点1 - 考点2 **需要掌握的知识点：** - 知识点1 - 知识点2 …… **解题思路与步骤：** 1. 步骤一…… 2. 步骤二…… …… ### 第二问分析 **考点识别：** （指明具体考点） **需要掌握的知识点：** - 知识点1 - 知识点2 …… **解题思路与步骤：** 1. 步骤一…… 2. 步骤二…… …… ## 解题建议 1. 建议一…… 2. 建议二…… **注意：** 如果你的输出中有包含数学符号，请用LaTeX格式表示。**输入信息：** 数学压轴题：";
        String question = "25.(10分)如图,数轴上点A表示的数为a,点B表示的数为b.满足 |a+5|+(b-8)^2=0 ,机器人M从点A出发,以每秒4个单位长度的速度向右运动,1秒后,机器人N从点B出发,以每秒2个单位长度的速度向左运动.根据机器人程序设定,机器人 M遇到机器人N后立即降速,以原速的一半返回,与此同时,机器人N以原速折返.设机器人 M运动时间为t秒.(1)点A与点B之间的距离是_:(2)求两个机器人 M、 N相遇的时间t及相遇点P所表示的数:(3)两个机器人在相遇点P折返后,是否存在某一时刻,使得机器人M到点A的距离与机器人N到点B的距离之和为10?若存在,求出此时 t的值及机器人N所在位置表示的数:若不存在,请说明理由. 请开始你的分析。";
        String customPrompt = promptTemplate + question;

        request.setMessage(customPrompt);

        System.out.println("\n=== 开始同步测试 ===");
        System.out.println("请求消息: " + request.getMessage());
        System.out.println("API Base URL: " + config.getBaseUrl());
        System.out.println("API Key: " + config.getApiKey().substring(0, Math.min(10, config.getApiKey().length())) + "...");

        ChatResponse response = client.chat(request);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getResponse(), "Response content should not be null");
        assertFalse(response.getResponse().isEmpty(), "Response content should not be empty");

        System.out.println("=== 模型响应详情 ===");
        System.out.println("响应内容: " + response.getResponse());
        System.out.println("会话ID: " + response.getSessionId());
        System.out.println("状态: " + response.getStatus());
        System.out.println("时间戳: " + response.getTimestamp());
        System.out.println("=== 同步测试结束 ===");
    }
}