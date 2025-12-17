package com.mathtutor.model;

/**
 * 解题方法分类枚举
 * 定义数学解题方法的主要分类
 */
public enum SolutionMethodCategory {
    /**
     * 代数方法
     */
    ALGEBRA_METHOD("代数方法", "主要运用代数知识和技巧的解题方法"),

    /**
     * 几何方法
     */
    GEOMETRY_METHOD("几何方法", "主要运用几何知识和图形分析的解题方法"),

    /**
     * 数形结合方法
     */
    NUMBER_SHAPE_COMBINATION("数形结合", "通过数与形的相互关系解决问题的方法"),

    /**
     * 分类讨论方法
     */
    CLASSIFICATION("分类讨论", "根据问题的不同情况分别讨论的方法"),

    /**
     * 分析综合方法
     */
    ANALYSIS_SYNTHESIS("分析综合", "通过分析条件、综合推理解决问题的方法"),

    /**
     * 归纳推理方法
     */
    INDUCTION("归纳推理", "从特殊情况出发归纳一般规律的方法"),

    /**
     * 演绎推理方法
     */
    DEDUCTION("演绎推理", "从一般原理出发推导特殊情况的方法"),

    /**
     * 反证法
     */
    PROOF_BY_CONTRADICTION("反证法", "通过否定结论来证明结论正确的方法"),

    /**
     * 构造法
     */
    CONSTRUCTION("构造法", "通过构造特定图形或方程来解决问题的方法"),

    /**
     * 参数法
     */
    PARAMETER_METHOD("参数法", "引入参数简化问题求解的方法"),

    /**
     * 换元法
     */
    SUBSTITUTION("换元法", "通过变量替换简化问题的方法"),

    /**
     * 数学建模方法
     */
    MATHEMATICAL_MODELING("数学建模", "将实际问题转化为数学模型的方法");

    private final String displayName;
    private final String description;

    SolutionMethodCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据中文名称查找对应的方法分类
     */
    public static SolutionMethodCategory findByDisplayName(String displayName) {
        for (SolutionMethodCategory category : values()) {
            if (category.displayName.equals(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("未找到方法分类: " + displayName);
    }

    /**
     * 获取所有方法分类的显示名称列表
     */
    public static String[] getAllDisplayNames() {
        SolutionMethodCategory[] categories = values();
        String[] names = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            names[i] = categories[i].displayName;
        }
        return names;
    }

    /**
     * 判断是否为基础方法分类
     */
    public boolean isBasicCategory() {
        return this == ALGEBRA_METHOD || this == GEOMETRY_METHOD;
    }

    /**
     * 判断是否为高级推理方法分类
     */
    public boolean isAdvancedReasoningCategory() {
        return this == INDUCTION || this == DEDUCTION || this == PROOF_BY_CONTRADICTION;
    }

    /**
     * 判断是否为技巧性方法分类
     */
    public boolean isTechniqueCategory() {
        return this == CONSTRUCTION || this == PARAMETER_METHOD || this == SUBSTITUTION;
    }
}