package com.mathtutor.model;

import lombok.Data;
import java.util.List;

/**
 * 小问分析模型
 * 用于结构化存储每个小问的分析结果
 */
@Data
public class SubQuestionAnalysis {

    /**
     * 小问序号（如：第一问、第二问等）
     */
    private String questionNumber;

    /**
     * 考点识别列表
     */
    private List<String> keyTopics;

    /**
     * 需要掌握的知识点列表
     */
    private List<String> knowledgePoints;

    /**
     * 解题思路与步骤列表
     */
    private List<String> solutionSteps;

    /**
     * 该小问的原始markdown文本
     */
    private String originalMarkdown;

    public SubQuestionAnalysis() {
    }

    public SubQuestionAnalysis(String questionNumber, List<String> keyTopics,
                             List<String> knowledgePoints, List<String> solutionSteps) {
        this.questionNumber = questionNumber;
        this.keyTopics = keyTopics;
        this.knowledgePoints = knowledgePoints;
        this.solutionSteps = solutionSteps;
    }

    public SubQuestionAnalysis(String questionNumber, String originalMarkdown) {
        this.questionNumber = questionNumber;
        this.originalMarkdown = originalMarkdown;
    }
}