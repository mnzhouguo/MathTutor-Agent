package com.mathtutor.service;

import com.mathtutor.model.ProblemAnalysisDomain;

/**
 * 数学题目分析Agent服务接口
 */
public interface MathAnalysisAgent {

    /**
     * 分析数学压轴题
     * @param question 数学题目
     * @return 分析响应
     */
    ProblemAnalysisDomain analyzeMathProblem(String question);

    /**
     * 分析数学压轴题（带上下文）
     * @param question 数学题目
     * @param context 上下文信息（如考点、难度等）
     * @return 分析响应
     */
    ProblemAnalysisDomain analyzeMathProblemWithContext(String question, String context);

    /**
     * 生成会话ID
     * @return 会话ID
     */
    String generateSessionId();
}