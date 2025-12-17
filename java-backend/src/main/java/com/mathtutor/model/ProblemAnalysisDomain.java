package com.mathtutor.model;

import lombok.Data;
import java.util.List;

/**
 * 数学题目分析领域模型
 *
 * 结构化设计：
 * ProblemAnalysisDomain
 * ├── backgrounds (题目背景列表)
 * ├── intents (考查意图列表)
 * ├── difficulties (难点解析列表)
 * ├── overallOriginalMarkdown (整体分析原始markdown文本)
 * ├── subQuestions (各小问分析列表)
 * │   ├── SubQuestionAnalysis
 * │   │   ├── questionNumber (小问序号)
 * │   │   ├── keyTopics (考点识别列表)
 * │   │   ├── knowledgePoints (需要掌握的知识点列表)
 * │   │   └── solutionSteps (解题思路与步骤列表)
 * └── suggestions (解题建议列表)
 *
 * 优点：
 * - 支持多小问结构化存储
 * - 支持多背景、多意图、多难点的灵活存储
 * - 清晰的层次结构，便于前端展示
 * - 保持原格式，同时提供结构化数据
 * - 支持任意数量的小问扩展
 * - 扁平化设计，减少嵌套复杂度
 */
@Data
public class ProblemAnalysisDomain {

    /**
     * 问题分析内容（完整markdown文本）
     */
    private String problemAnalysis;

    /**
     * 题目背景列表
     */
    private List<String> backgrounds;

    /**
     * 考查意图列表
     */
    private List<String> intents;

    /**
     * 难点解析列表
     */
    private List<String> difficulties;

    /**
     * 整体分析的原始markdown文本
     */
    private String overallOriginalMarkdown;

    /**
     * 各小问分析列表
     */
    private List<SubQuestionAnalysis> subQuestions;

    /**
     * 解题建议列表
     */
    private List<String> suggestions;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 获取小问数量
     */
    public int getSubQuestionCount() {
        return subQuestions != null ? subQuestions.size() : 0;
    }

    /**
     * 根据序号获取小问分析
     */
    public SubQuestionAnalysis getSubQuestion(String questionNumber) {
        if (subQuestions == null) {
            return null;
        }
        return subQuestions.stream()
                .filter(sq -> questionNumber.equals(sq.getQuestionNumber()))
                .findFirst()
                .orElse(null);
    }
}