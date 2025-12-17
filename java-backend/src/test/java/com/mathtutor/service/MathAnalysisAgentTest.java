package com.mathtutor.service;

import com.mathtutor.llm.LlmClient;
import com.mathtutor.model.ProblemAnalysisDomain;
import com.mathtutor.model.SubQuestionAnalysis;
import com.mathtutor.service.impl.MathAnalysisAgentImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MathAnalysisAgentTest {

    @Mock
    private LlmClient llmClient;

    private MathAnalysisAgent mathAnalysisAgent;

    @BeforeEach
    void setUp() {
        mathAnalysisAgent = new MathAnalysisAgentImpl(llmClient);
    }

    @Test
    void testAnalyzeMathProblem() {
        // 使用实际的数学题目进行测试
        String question = "25.(10分)如图,数轴上点A表示的数为a,点B表示的数为b.满足 |a+5|+(b-8)^2=0 ,机器人\n" + //
                        "M从点A出发,以每秒4个单位长度的速度向右运动,1秒后,机器人N从点B出发,以每\n" + //
                        "秒2个单位长度的速度向左运动.根据机器人程序设定,机器人 M遇到机器人N后立即降速,\n" + //
                        "以原速的一半返回,与此同时,机器人N以原速折返.设机器人 M运动时间为t秒.\n" + //
                        "(1)点A与点B之间的距离是_:\n" + //
                        "(2)求两个机器人 M、 N相遇的时间t及相遇点P所表示的数:\n" + //
                        "(3)两个机器人在相遇点P折返后,是否存在某一时刻,使得机器人M到点A的距离与机\n" + //
                        "器人N到点B的距离之和为10?若存在,求出此时 t的值及机器人N所在位置表示的数:\n" + //
                        "若不存在,请说明理由.";

        System.out.println("=== 开始实际大模型调用测试 ===");
        System.out.println("输入题目: " + question);
        System.out.println("开始分析...");

        ProblemAnalysisDomain response = mathAnalysisAgent.analyzeMathProblem(question);

        assertNotNull(response);
        assertNotNull(response.getProblemAnalysis());
        assertNotNull(response.getSessionId());
        assertTrue(response.getSessionId().startsWith("math_analysis_"));

        // 打印大模型分析结果
        System.out.println("\n=== 大模型分析结果 ===");
        System.out.println(response.getProblemAnalysis());

        // 打印会话ID和分析结果长度
        System.out.println("\n=== 测试结果 ===");
        System.out.println("会话ID: " + response.getSessionId());
        System.out.println("分析结果长度: " + response.getProblemAnalysis().length());
        System.out.println("=== 测试结束 ===\n");
    }

    @Test
    void testAnalyzeMathProblemWithContext() {
        String mockAnalysis = "## 题目分析\n这是一道几何与代数结合的综合题...";
        com.mathtutor.model.ChatResponse mockChatResponse = new com.mathtutor.model.ChatResponse(mockAnalysis, "test_session_789");

        when(llmClient.chat(any())).thenReturn(mockChatResponse);

        String question = "在△ABC中，AB=AC，∠BAC=40°，D是BC边上一点";
        String context = "考点：等腰三角形性质、角度计算；难度：中等偏上";

        ProblemAnalysisDomain response = mathAnalysisAgent.analyzeMathProblemWithContext(question, context);

        assertNotNull(response);
        assertEquals(mockAnalysis, response.getProblemAnalysis());
    }

    @Test
    void testAnalyzeMathProblemError() {
        when(llmClient.chat(any())).thenThrow(new RuntimeException("API调用失败"));

        String question = "测试题目";

        ProblemAnalysisDomain response = mathAnalysisAgent.analyzeMathProblem(question);

        assertNotNull(response);
        assertTrue(response.getProblemAnalysis().contains("分析过程中发生错误"));
    }

    @Test
    void testGenerateSessionId() {
        String sessionId = mathAnalysisAgent.generateSessionId();

        assertNotNull(sessionId);
        assertTrue(sessionId.startsWith("math_analysis_"));
        assertFalse(sessionId.contains("-"));
    }

    @Test
    void testStructuredMathAnalysisResponse() {
        // 测试结构化响应的创建和访问
        String mockAnalysis = "## 题目分析\n### 题目背景\n这是一道数轴运动题目...";
        com.mathtutor.model.ChatResponse mockChatResponse = new com.mathtutor.model.ChatResponse(mockAnalysis, "test_session_structured");

        when(llmClient.chat(any())).thenReturn(mockChatResponse);

        // 创建结构化数据
        List<String> backgrounds = Arrays.asList("数轴运动背景", "初中数学压轴题");
        List<String> intents = Arrays.asList("考查数轴概念和运动分析", "提升学生逻辑思维能力");
        List<String> difficulties = Arrays.asList("绝对值应用难点", "运动过程分析复杂");
        String overallOriginalMarkdown = "## 题目分析\n### 题目背景\n这是一道数轴运动题目...";

        SubQuestionAnalysis subQuestion1 = new SubQuestionAnalysis("第一问",
            Arrays.asList("数轴概念", "距离计算"),
            Arrays.asList("数轴上点的坐标", "两点间距离公式"),
            Arrays.asList("确定A、B坐标", "计算距离"));

        SubQuestionAnalysis subQuestion2 = new SubQuestionAnalysis("第二问",
            Arrays.asList("运动分析", "相遇问题"),
            Arrays.asList("相对运动", "时间计算"),
            Arrays.asList("建立运动方程", "求解相遇时间"));

        List<String> suggestions = Arrays.asList("注意坐标系转换", "验证计算结果");

        String question = "简单的数轴运动题目";
        ProblemAnalysisDomain response = mathAnalysisAgent.analyzeMathProblem(question);

        assertNotNull(response);
        assertNotNull(response.getProblemAnalysis());

        // 如果需要测试结构化数据，可以手动创建响应对象
        ProblemAnalysisDomain structuredResponse = new ProblemAnalysisDomain();
        structuredResponse.setProblemAnalysis(mockAnalysis);
        structuredResponse.setBackgrounds(backgrounds);
        structuredResponse.setIntents(intents);
        structuredResponse.setDifficulties(difficulties);
        structuredResponse.setOverallOriginalMarkdown(overallOriginalMarkdown);
        structuredResponse.setSubQuestions(Arrays.asList(subQuestion1, subQuestion2));
        structuredResponse.setSuggestions(suggestions);
        structuredResponse.setSessionId("test_structured");

        assertEquals(2, structuredResponse.getSubQuestionCount());
        assertNotNull(structuredResponse.getSubQuestion("第一问"));
        assertNotNull(structuredResponse.getSubQuestion("第二问"));
        assertNull(structuredResponse.getSubQuestion("第三问"));
    }

    @Test
    void testToJSONMethods() {
        // 测试JSON转换方法
        String mockAnalysis = "## 题目分析\n测试JSON转换...";

        List<String> backgrounds = Arrays.asList("测试背景");
        List<String> intents = Arrays.asList("测试意图");
        List<String> difficulties = Arrays.asList("测试难点");
        String overallOriginalMarkdown = "## 测试题目分析";

        SubQuestionAnalysis subQuestion = new SubQuestionAnalysis("第一问",
            Arrays.asList("测试考点"),
            Arrays.asList("测试知识点"),
            Arrays.asList("测试步骤"));

        List<String> suggestions = Arrays.asList("测试建议");

        ProblemAnalysisDomain response = new ProblemAnalysisDomain();
        response.setProblemAnalysis(mockAnalysis);
        response.setBackgrounds(backgrounds);
        response.setIntents(intents);
        response.setDifficulties(difficulties);
        response.setOverallOriginalMarkdown(overallOriginalMarkdown);
        response.setSubQuestions(Arrays.asList(subQuestion));
        response.setSuggestions(suggestions);
        response.setSessionId("test_json");

        // 验证响应对象的基本属性
        assertEquals("test_json", response.getSessionId());
        assertEquals(mockAnalysis, response.getProblemAnalysis());
        assertEquals(backgrounds, response.getBackgrounds());
        assertEquals(intents, response.getIntents());
        assertEquals(difficulties, response.getDifficulties());
        assertEquals(overallOriginalMarkdown, response.getOverallOriginalMarkdown());
        assertEquals(1, response.getSubQuestionCount());
        assertEquals(suggestions, response.getSuggestions());
    }
}