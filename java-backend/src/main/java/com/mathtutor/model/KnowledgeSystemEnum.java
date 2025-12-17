package com.mathtutor.model;

/**
 * 数学知识体系枚举
 * 定义初中数学的主要知识体系分类
 */
public enum KnowledgeSystemEnum {
    /**
     * 代数体系
     */
    ALGEBRA("代数", "包括方程、不等式、函数等代数基础知识"),

    /**
     * 几何体系
     */
    GEOMETRY("几何", "包括平面几何、立体几何等空间图形知识"),

    /**
     * 数与代数基础
     */
    NUMBER_THEORY("数与代数", "包括实数、有理数、整式等基础概念"),

    /**
     * 统计与概率
     */
    STATISTICS("统计与概率", "包括数据收集、分析、概率计算等"),

    /**
     * 函数与分析
     */
    FUNCTION("函数", "包括一次函数、二次函数、反比例函数等"),

    /**
     * 图形与变换
     */
    TRANSFORMATION("图形与变换", "包括平移、旋转、对称等几何变换"),

    /**
     * 解题方法
     */
    PROBLEM_SOLVING("解题方法", "包括数学思想方法、解题技巧等");

    private final String displayName;
    private final String description;

    KnowledgeSystemEnum(String displayName, String description) {
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
     * 根据中文名称查找对应的知识体系
     */
    public static KnowledgeSystemEnum findByDisplayName(String displayName) {
        for (KnowledgeSystemEnum system : values()) {
            if (system.displayName.equals(displayName)) {
                return system;
            }
        }
        throw new IllegalArgumentException("未找到知识体系: " + displayName);
    }

    /**
     * 获取所有知识体系的显示名称列表
     */
    public static String[] getAllDisplayNames() {
        KnowledgeSystemEnum[] systems = values();
        String[] names = new String[systems.length];
        for (int i = 0; i < systems.length; i++) {
            names[i] = systems[i].displayName;
        }
        return names;
    }
}