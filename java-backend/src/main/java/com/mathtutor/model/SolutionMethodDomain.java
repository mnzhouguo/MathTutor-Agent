package com.mathtutor.model;

import lombok.Data;

/**
 * 解题方法领域模型
 *
 * 结构化设计：
 * SolutionMethodDomain
 * ├── name (方法名称)
 * ├── description (方法说明)
 * ├── applicableKnowledgeSystems (适用知识体系)
 * ├── applicableGradeRange (适用年级范围)
 * ├── difficultyLevel (方法难度级别)
 * ├── methodCategory (方法分类)
 * ├── steps (解题步骤)
 * ├── examples (示例)
 * └── relatedMethods (相关方法)
 *
 * 领域概念：
 * - 解题方法：解决数学问题的系统化方法和技巧
 * - 方法分类：代数方法、几何方法、数形结合等
 * - 适用范围：方法适用的知识点和年级
 */
@Data
public class SolutionMethodDomain {

    /**
     * 方法编号
     */
    private String code;

    /**
     * 方法名称
     */
    private String name;

    /**
     * 方法说明
     */
    private String description;

    /**
     * 适用知识体系列表
     */
    private java.util.List<KnowledgeSystemEnum> applicableKnowledgeSystems;

    /**
     * 适用年级范围起始
     */
    private GradeEnum applicableGradeFrom;

    /**
     * 适用年级范围结束
     */
    private GradeEnum applicableGradeTo;

    /**
     * 方法难度级别
     * 1-基础方法，2-常用方法，3-高级方法
     */
    private Integer difficultyLevel;

    /**
     * 方法分类
     */
    private SolutionMethodCategory methodCategory;

    /**
     * 解题步骤列表
     */
    private java.util.List<String> steps;

    /**
     * 关键要点列表
     */
    private java.util.List<String> keyPoints;

    /**
     * 注意事项列表
     */
    private java.util.List<String> precautions;

    /**
     * 适用题型描述
     */
    private String applicableProblemTypes;

    /**
     * 相关方法列表
     */
    private java.util.List<SolutionMethodDomain> relatedMethods;

    /**
     * 默认构造函数
     */
    public SolutionMethodDomain() {
    }

    /**
     * 构造函数
     */
    public SolutionMethodDomain(String code, String name, String description,
                              SolutionMethodCategory methodCategory) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.methodCategory = methodCategory;
        this.difficultyLevel = 2; // 默认为常用方法
        this.steps = new java.util.ArrayList<>();
        this.keyPoints = new java.util.ArrayList<>();
        this.precautions = new java.util.ArrayList<>();
        this.applicableKnowledgeSystems = new java.util.ArrayList<>();
        this.relatedMethods = new java.util.ArrayList<>();
    }

    /**
     * 构造函数 - 包含适用范围
     */
    public SolutionMethodDomain(String code, String name, String description,
                              SolutionMethodCategory methodCategory, GradeEnum fromGrade, GradeEnum toGrade) {
        this(code, name, description, methodCategory);
        this.applicableGradeFrom = fromGrade;
        this.applicableGradeTo = toGrade;
    }

    /**
     * 添加解题步骤
     */
    public void addStep(String step) {
        if (this.steps == null) {
            this.steps = new java.util.ArrayList<>();
        }
        this.steps.add(step);
    }

    /**
     * 添加关键要点
     */
    public void addKeyPoint(String keyPoint) {
        if (this.keyPoints == null) {
            this.keyPoints = new java.util.ArrayList<>();
        }
        this.keyPoints.add(keyPoint);
    }

    /**
     * 添加注意事项
     */
    public void addPrecaution(String precaution) {
        if (this.precautions == null) {
            this.precautions = new java.util.ArrayList<>();
        }
        this.precautions.add(precaution);
    }

    /**
     * 添加适用知识体系
     */
    public void addApplicableKnowledgeSystem(KnowledgeSystemEnum knowledgeSystem) {
        if (this.applicableKnowledgeSystems == null) {
            this.applicableKnowledgeSystems = new java.util.ArrayList<>();
        }
        this.applicableKnowledgeSystems.add(knowledgeSystem);
    }

    /**
     * 添加相关方法
     */
    public void addRelatedMethod(SolutionMethodDomain relatedMethod) {
        if (this.relatedMethods == null) {
            this.relatedMethods = new java.util.ArrayList<>();
        }
        this.relatedMethods.add(relatedMethod);
    }

    /**
     * 检查方法是否适用于指定年级
     */
    public boolean isApplicableToGrade(GradeEnum grade) {
        if (applicableGradeFrom == null || applicableGradeTo == null) {
            return true; // 如果未指定范围，则认为适用
        }
        return grade.getLevel() >= applicableGradeFrom.getLevel()
            && grade.getLevel() <= applicableGradeTo.getLevel();
    }

    /**
     * 检查方法是否适用于指定知识体系
     */
    public boolean isApplicableToKnowledgeSystem(KnowledgeSystemEnum knowledgeSystem) {
        if (applicableKnowledgeSystems == null || applicableKnowledgeSystems.isEmpty()) {
            return true; // 如果未指定，则认为适用
        }
        return applicableKnowledgeSystems.contains(knowledgeSystem);
    }

    /**
     * 获取方法的完整标识符
     */
    public String getFullIdentifier() {
        return methodCategory != null ?
            methodCategory.getDisplayName() + "-" + code :
            "METHOD-" + code;
    }

    /**
     * 判断是否为基础方法
     */
    public boolean isBasicMethod() {
        return difficultyLevel != null && difficultyLevel <= 1;
    }

    /**
     * 判断是否为高级方法
     */
    public boolean isAdvancedMethod() {
        return difficultyLevel != null && difficultyLevel >= 3;
    }

    /**
     * 获取方法复杂度描述
     */
    public String getComplexityDescription() {
        if (difficultyLevel == null) return "未指定";
        switch (difficultyLevel) {
            case 1: return "基础方法，易于掌握";
            case 2: return "常用方法，需要练习";
            case 3: return "高级方法，需要深入理解";
            default: return "未知复杂度";
        }
    }

    /**
     * 验证方法数据的完整性
     */
    public boolean isValid() {
        return code != null && !code.trim().isEmpty()
            && name != null && !name.trim().isEmpty()
            && description != null && !description.trim().isEmpty()
            && methodCategory != null;
    }
}