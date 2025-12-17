package com.mathtutor.model;

import lombok.Data;
import java.util.List;

/**
 * 知识点领域模型
 *
 * 结构化设计：
 * KnowledgeDomain
 * ├── name (知识点名称)
 * ├── grade (所属年级)
 * ├── description (知识点说明)
 * ├── knowledgeSystem (所属知识点体系)
 * ├── code (编号)
 * ├── module (模块)
 * ├── topic (专题)
 * ├── assessmentCore (考核核心)
 * ├── detailedPoints (详细考点列表)
 * ├── parentKnowledge (父知识点)
 * └── difficultyLevel (难度级别)
 *
 * 领域概念：
 * - 知识点：数学学习的基本单元
 * - 模块：教学中的大主题分组
 * - 专题：模块下的具体专题
 * - 考核核心：该知识点在考试中的核心考察点
 * - 详细考点：具体的考核内容和要求
 */
@Data
public class KnowledgeDomain {

    /**
     * 知识点编号
     */
    private String code;

    /**
     * 知识点名称
     */
    private String name;

    /**
     * 所属年级
     */
    private GradeEnum grade;

    /**
     * 知识点说明
     */
    private String description;

    /**
     * 所属知识点体系
     */
    private KnowledgeSystemEnum knowledgeSystem;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 所属专题
     */
    private String topic;

    /**
     * 考核核心
     */
    private String assessmentCore;

    /**
     * 详细考点列表
     */
    private List<String> detailedPoints;

    /**
     * 父知识点（支持知识点层次结构）
     */
    private KnowledgeDomain parentKnowledge;

    /**
     * 知识点深度级别
     * 1-基础概念，2-核心知识点，3-综合应用
     */
    private Integer depthLevel;

    /**
     * 知识点掌握难度
     * 1-容易，2-中等，3-困难
     */
    private Integer difficultyLevel;

    /**
     * 默认构造函数
     */
    public KnowledgeDomain() {
    }

    /**
     * 构造函数
     */
    public KnowledgeDomain(String code, String name, GradeEnum grade, String description,
                          KnowledgeSystemEnum knowledgeSystem, String module, String topic,
                          String assessmentCore) {
        this.code = code;
        this.name = name;
        this.grade = grade;
        this.description = description;
        this.knowledgeSystem = knowledgeSystem;
        this.module = module;
        this.topic = topic;
        this.assessmentCore = assessmentCore;
        this.detailedPoints = new java.util.ArrayList<>();
        this.depthLevel = 2; // 默认为核心知识点
        this.difficultyLevel = 2; // 默认为中等难度
    }

    /**
     * 构造函数 - 包含父知识点
     */
    public KnowledgeDomain(String code, String name, GradeEnum grade, String description,
                          KnowledgeSystemEnum knowledgeSystem, String module, String topic,
                          String assessmentCore, KnowledgeDomain parentKnowledge) {
        this(code, name, grade, description, knowledgeSystem, module, topic, assessmentCore);
        this.parentKnowledge = parentKnowledge;
    }

    /**
     * 构造函数 - 包含详细考点
     */
    public KnowledgeDomain(String code, String name, GradeEnum grade, String description,
                          KnowledgeSystemEnum knowledgeSystem, String module, String topic,
                          String assessmentCore, List<String> detailedPoints) {
        this(code, name, grade, description, knowledgeSystem, module, topic, assessmentCore);
        this.detailedPoints = detailedPoints != null ? detailedPoints : new java.util.ArrayList<>();
    }

    /**
     * 设置父知识点
     */
    public void setParentKnowledge(KnowledgeDomain parentKnowledge) {
        this.parentKnowledge = parentKnowledge;
    }

    /**
     * 获取完整知识点路径（包含父知识点）
     */
    public String getFullPath() {
        if (parentKnowledge == null) {
            return name;
        }
        return parentKnowledge.getFullPath() + " > " + name;
    }

    /**
     * 检查是否为基础知识点（无父知识点）
     */
    public boolean isBasicKnowledge() {
        return parentKnowledge == null;
    }

    /**
     * 检查是否为高级知识点（有子知识点）
     */
    public boolean isAdvancedKnowledge() {
        return depthLevel != null && depthLevel >= 3;
    }

    /**
     * 获取知识点的完整标识符
     */
    public String getFullIdentifier() {
        return knowledgeSystem.getDisplayName() + "-" + code;
    }

    /**
     * 添加详细考点
     */
    public void addDetailedPoint(String detailedPoint) {
        if (this.detailedPoints == null) {
            this.detailedPoints = new java.util.ArrayList<>();
        }
        this.detailedPoints.add(detailedPoint);
    }

    /**
     * 获取详细考点数量
     */
    public int getDetailedPointCount() {
        return detailedPoints != null ? detailedPoints.size() : 0;
    }

    /**
     * 获取模块完整标识符
     */
    public String getModuleIdentifier() {
        return module + "-" + code;
    }

    /**
     * 获取专题完整标识符
     */
    public String getTopicIdentifier() {
        return module + "-" + topic + "-" + code;
    }

    /**
     * 检查是否属于指定模块
     */
    public boolean belongsToModule(String moduleName) {
        return module != null && module.equals(moduleName);
    }

    /**
     * 检查是否属于指定专题
     */
    public boolean belongsToTopic(String topicName) {
        return topic != null && topic.equals(topicName);
    }

    /**
     * 获取知识点完整信息
     */
    public String getFullInfo() {
        return String.format("[%s] %s.%s - %s", code, module, topic, name);
    }

    /**
     * 验证知识点数据的完整性
     */
    public boolean isValid() {
        return code != null && !code.trim().isEmpty()
            && name != null && !name.trim().isEmpty()
            && grade != null
            && knowledgeSystem != null
            && module != null && !module.trim().isEmpty()
            && topic != null && !topic.trim().isEmpty()
            && assessmentCore != null && !assessmentCore.trim().isEmpty();
    }
}