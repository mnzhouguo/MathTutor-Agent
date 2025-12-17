package com.mathtutor.service.impl;

import com.mathtutor.llm.LlmClient;
import com.mathtutor.model.ChatRequest;
import com.mathtutor.model.ProblemAnalysisDomain;
import com.mathtutor.service.MathAnalysisAgent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 数学题目分析Agent服务实现类
 */
@Slf4j
@Service
public class MathAnalysisAgentImpl implements MathAnalysisAgent {

    private final LlmClient llmClient;

    // 题目分析提示词模板
    private static final String ANALYSIS_PROMPT_TEMPLATE =
        "你是一名初中数学教学专家，擅长分析压轴题的命题结构与解题思路。请针对用户提供的数学压轴题，完成以下任务：" +
        "1.题目分析：整体解读题目背景、难点、易错点、解决思路、考查意图。" +
        "题目背景：请说明题目的背景、题目的难易程度。" +
        "考查意图：分析题目考查的核心知识点与能力要求。" +
        "难点解析：指出题目中可能存在的难点与易错点，并给出相应的解题建议。" +
        "2.分问解析：对每一小问独立分析，包括：" +
        "考点识别：明确该问对应的核心考点，并说明属于哪个知识模块（如函数、几何、代数综合等）。" +
        "知识点梳理：列出解决该问必须掌握的概念、定理、公式、方法，并适当说明它们在该题中的应用方式。" +
        "解题方案：提供清晰的解题思路与步骤，包括关键推理环节、可能用到的转化策略或辅助线作法等，不给出具体数值结果或最终答案。" +
        "3.解题建议：总结解题过程中应注意的事项与策略，帮助学生提升解题能力。" +
        "请严格按照以下格式输出：" +
        "## 题目分析 ### 题目背景 ### 考查意图 ### 难点解析 " +
        "## 各问分析 ### 第一问分析 **考点识别：** - 考点1 - 考点2 **需要掌握的知识点：** - 知识点1 - 知识点2 …… **解题思路与步骤：** 1. 步骤一…… 2. 步骤二…… …… " +
        "### 第二问分析 **考点识别：** （指明具体考点） **需要掌握的知识点：** - 知识点1 - 知识点2 …… **解题思路与步骤：** 1. 步骤一…… 2. 步骤二…… …… " +
        "## 解题建议 1. 建议一…… 2. 建议二…… " +
        "**注意：** 如果你的输出中有包含数学符号，请用LaTeX格式表示。" +
        "**输入信息：** 数学压轴题：";

    public MathAnalysisAgentImpl(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @Override
    public ProblemAnalysisDomain analyzeMathProblem(String question) {
        return analyzeMathProblemWithContext(question, "");
    }

    @Override
    public ProblemAnalysisDomain analyzeMathProblemWithContext(String question, String context) {
        try {
            log.debug("开始分析数学题目，题目长度: {}, 上下文长度: {}", question.length(), context.length());

            // 构建完整的提示词
            StringBuilder promptBuilder = new StringBuilder(ANALYSIS_PROMPT_TEMPLATE);

            // 添加题目
            promptBuilder.append(question);

            // 如果有上下文信息，添加到提示词中
            if (context != null && !context.trim().isEmpty()) {
                promptBuilder.append(" 附加信息：").append(context);
            }

            promptBuilder.append(" 请开始你的分析。");

            // 创建聊天请求
            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setMessage(promptBuilder.toString());
            chatRequest.setSessionId(generateSessionId());
            chatRequest.setContext("math_analysis");

            log.debug("发送分析请求到LLM服务");

            // 调用LLM服务进行分析
            String problemAnalysis = llmClient.chat(chatRequest).getResponse();

            log.info("数学题目分析完成，结果长度: {}", problemAnalysis != null ? problemAnalysis.length() : 0);

            ProblemAnalysisDomain domain = new ProblemAnalysisDomain();
            domain.setProblemAnalysis(problemAnalysis);
            domain.setSessionId(chatRequest.getSessionId());
            return domain;

        } catch (Exception e) {
            log.error("数学题目分析过程中发生错误", e);
            String sessionId = generateSessionId();
            ProblemAnalysisDomain domain = new ProblemAnalysisDomain();
            domain.setProblemAnalysis("抱歉，分析过程中发生了错误，请稍后重试。错误信息：" + e.getMessage());
            domain.setSessionId(sessionId);
            return domain;
        }
    }

    @Override
    public String generateSessionId() {
        return "math_analysis_" + UUID.randomUUID().toString().replace("-", "");
    }
}