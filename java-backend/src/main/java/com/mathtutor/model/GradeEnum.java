package com.mathtutor.model;

/**
 * 年级枚举
 * 定义初中数学教学的主要年级
 */
public enum GradeEnum {
    /**
     * 六年级
     */
    SIXTH_GRADE("六年级", 6),

    /**
     * 七年级
     */
    SEVENTH_GRADE("七年级", 7),

    /**
     * 八年级
     */
    EIGHTH_GRADE("八年级", 8),

    /**
     * 九年级
     */
    NINTH_GRADE("九年级", 9);

    private final String displayName;
    private final int level;

    GradeEnum(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    /**
     * 根据中文名称查找对应的年级
     */
    public static GradeEnum findByDisplayName(String displayName) {
        for (GradeEnum grade : values()) {
            if (grade.displayName.equals(displayName)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("未找到年级: " + displayName);
    }

    /**
     * 根据级别查找对应的年级
     */
    public static GradeEnum findByLevel(int level) {
        for (GradeEnum grade : values()) {
            if (grade.level == level) {
                return grade;
            }
        }
        throw new IllegalArgumentException("未找到年级: " + level);
    }

    /**
     * 获取所有年级的显示名称列表
     */
    public static String[] getAllDisplayNames() {
        GradeEnum[] grades = values();
        String[] names = new String[grades.length];
        for (int i = 0; i < grades.length; i++) {
            names[i] = grades[i].displayName;
        }
        return names;
    }

    /**
     * 判断是否为低年级（六年级）
     */
    public boolean isLowerGrade() {
        return this == SIXTH_GRADE;
    }

    /**
     * 判断是否为高年级（八、九年级）
     */
    public boolean isHigherGrade() {
        return this == EIGHTH_GRADE || this == NINTH_GRADE;
    }

    /**
     * 判断是否为毕业年级
     */
    public boolean isGraduationGrade() {
        return this == NINTH_GRADE;
    }
}