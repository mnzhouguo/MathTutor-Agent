package com.mathtutor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolutionMethodDomainTest {

    @Test
    void testBasicSolutionMethodDomain() {
        // 测试基础解题方法创建
        SolutionMethodDomain method = new SolutionMethodDomain(
            "ALG001", "配方法", "通过配方将二次项转换为完全平方形式的代数方法",
            SolutionMethodCategory.ALGEBRA_METHOD
        );

        // 验证基本属性
        assertEquals("ALG001", method.getCode());
        assertEquals("配方法", method.getName());
        assertEquals("通过配方将二次项转换为完全平方形式的代数方法", method.getDescription());
        assertEquals(SolutionMethodCategory.ALGEBRA_METHOD, method.getMethodCategory());
        assertEquals(2, method.getDifficultyLevel());
        assertTrue(method.isValid());

        // 验证方法复杂度
        assertFalse(method.isBasicMethod());
        assertFalse(method.isAdvancedMethod());
        assertEquals("常用方法，需要练习", method.getComplexityDescription());

        // 验证完整标识符
        assertEquals("代数方法-ALG001", method.getFullIdentifier());
    }

    @Test
    void testSolutionMethodWithGradeRange() {
        // 测试带年级范围的解题方法
        SolutionMethodDomain method = new SolutionMethodDomain(
            "GEO001", "辅助线法", "通过添加辅助线解决几何问题的方法",
            SolutionMethodCategory.GEOMETRY_METHOD,
            GradeEnum.SEVENTH_GRADE,
            GradeEnum.NINTH_GRADE
        );

        // 验证年级适用性
        assertTrue(method.isApplicableToGrade(GradeEnum.SEVENTH_GRADE));
        assertTrue(method.isApplicableToGrade(GradeEnum.EIGHTH_GRADE));
        assertTrue(method.isApplicableToGrade(GradeEnum.NINTH_GRADE));
        assertFalse(method.isApplicableToGrade(GradeEnum.SIXTH_GRADE));

        assertEquals(GradeEnum.SEVENTH_GRADE, method.getApplicableGradeFrom());
        assertEquals(GradeEnum.NINTH_GRADE, method.getApplicableGradeTo());
    }

    @Test
    void testSolutionMethodWithStepsAndKeyPoints() {
        SolutionMethodDomain method = new SolutionMethodDomain(
            "NS001", "数形结合法", "通过数与形的对应关系解决问题的方法",
            SolutionMethodCategory.NUMBER_SHAPE_COMBINATION
        );

        // 添加解题步骤
        method.addStep("第一步：分析问题的数学特征");
        method.addStep("第二步：构造对应的图形模型");
        method.addStep("第三步：通过图形直观理解数学关系");
        method.addStep("第四步：将图形结论转化为数学解答");

        // 添加关键要点
        method.addKeyPoint("要善于发现数量关系的几何意义");
        method.addKeyPoint("图形的选择要简单直观");
        method.addKeyPoint("注意数与形的等价转换");

        // 添加注意事项
        method.addPrecaution("图形要准确反映数量关系");
        method.addPrecaution("避免过度依赖图形而忽略严谨性");

        // 验证步骤和要点
        assertEquals(4, method.getSteps().size());
        assertEquals(3, method.getKeyPoints().size());
        assertEquals(2, method.getPrecautions().size());

        assertTrue(method.getSteps().contains("第一步：分析问题的数学特征"));
        assertTrue(method.getKeyPoints().contains("要善于发现数量关系的几何意义"));
    }

    @Test
    void testSolutionMethodWithKnowledgeSystems() {
        SolutionMethodDomain method = new SolutionMethodDomain(
            "SUB001", "换元法", "通过变量替换简化复杂表达式的方法",
            SolutionMethodCategory.SUBSTITUTION
        );

        // 添加适用知识体系
        method.addApplicableKnowledgeSystem(KnowledgeSystemEnum.ALGEBRA);
        method.addApplicableKnowledgeSystem(KnowledgeSystemEnum.FUNCTION);

        // 验证知识体系适用性
        assertTrue(method.isApplicableToKnowledgeSystem(KnowledgeSystemEnum.ALGEBRA));
        assertTrue(method.isApplicableToKnowledgeSystem(KnowledgeSystemEnum.FUNCTION));
        assertFalse(method.isApplicableToKnowledgeSystem(KnowledgeSystemEnum.GEOMETRY));

        assertEquals(2, method.getApplicableKnowledgeSystems().size());
        assertTrue(method.getApplicableKnowledgeSystems().contains(KnowledgeSystemEnum.ALGEBRA));
    }

    @Test
    void testRelatedMethods() {
        SolutionMethodDomain mainMethod = new SolutionMethodDomain(
            "CLASS001", "分类讨论法", "根据问题的不同情况分别讨论的方法",
            SolutionMethodCategory.CLASSIFICATION
        );

        SolutionMethodDomain relatedMethod1 = new SolutionMethodDomain(
            "INDUCTION001", "归纳法", "从特殊情况归纳一般规律的方法",
            SolutionMethodCategory.INDUCTION
        );

        SolutionMethodDomain relatedMethod2 = new SolutionMethodDomain(
            "CONSTRUCTION001", "构造法", "构造特定对象解决问题的方法",
            SolutionMethodCategory.CONSTRUCTION
        );

        // 添加相关方法
        mainMethod.addRelatedMethod(relatedMethod1);
        mainMethod.addRelatedMethod(relatedMethod2);

        // 验证相关方法
        assertEquals(2, mainMethod.getRelatedMethods().size());
        assertTrue(mainMethod.getRelatedMethods().contains(relatedMethod1));
        assertTrue(mainMethod.getRelatedMethods().contains(relatedMethod2));
    }

    @Test
    void testDifferentMethodCategories() {
        // 测试不同分类的方法
        SolutionMethodCategory[] categories = {
            SolutionMethodCategory.ALGEBRA_METHOD,
            SolutionMethodCategory.GEOMETRY_METHOD,
            SolutionMethodCategory.NUMBER_SHAPE_COMBINATION,
            SolutionMethodCategory.PROOF_BY_CONTRADICTION,
            SolutionMethodCategory.MATHEMATICAL_MODELING
        };

        String[] expectedDescriptions = {
            "代数方法-ALG001",
            "几何方法-GEO001",
            "数形结合-NS001",
            "反证法-CONTRA001",
            "数学建模-MODEL001"
        };

        String[] codes = {
            "ALG001",
            "GEO001",
            "NS001",
            "CONTRA001",
            "MODEL001"
        };

        for (int i = 0; i < categories.length; i++) {
            SolutionMethodDomain method = new SolutionMethodDomain(
                codes[i],
                "测试方法",
                "测试描述",
                categories[i]
            );
            assertEquals(expectedDescriptions[i], method.getFullIdentifier());
            assertEquals(categories[i], method.getMethodCategory());
        }
    }

    @Test
    void testBasicAndAdvancedMethods() {
        // 测试基础方法
        SolutionMethodDomain basicMethod = new SolutionMethodDomain(
            "BASIC001", "基础代入法", "基础代入求解",
            SolutionMethodCategory.SUBSTITUTION
        );
        basicMethod.setDifficultyLevel(1);

        assertTrue(basicMethod.isBasicMethod());
        assertFalse(basicMethod.isAdvancedMethod());
        assertEquals("基础方法，易于掌握", basicMethod.getComplexityDescription());

        // 测试高级方法
        SolutionMethodDomain advancedMethod = new SolutionMethodDomain(
            "ADV001", "高级构造法", "高级构造技巧",
            SolutionMethodCategory.CONSTRUCTION
        );
        advancedMethod.setDifficultyLevel(3);

        assertFalse(advancedMethod.isBasicMethod());
        assertTrue(advancedMethod.isAdvancedMethod());
        assertEquals("高级方法，需要深入理解", advancedMethod.getComplexityDescription());
    }

    @Test
    void testInvalidMethod() {
        // 测试无效数据
        SolutionMethodDomain invalid1 = new SolutionMethodDomain();
        invalid1.setName("测试方法");
        assertFalse(invalid1.isValid()); // 缺少必要字段

        SolutionMethodDomain invalid2 = new SolutionMethodDomain(
            "", "测试方法", "测试描述", null
        );
        assertFalse(invalid2.isValid()); // 空编号
    }

    @Test
    void testSetterMethods() {
        SolutionMethodDomain method = new SolutionMethodDomain();

        // 测试基本属性设置
        method.setCode("TEST001");
        method.setName("测试解题方法");
        method.setDescription("这是一个测试方法");
        method.setMethodCategory(SolutionMethodCategory.ANALYSIS_SYNTHESIS);
        method.setDifficultyLevel(2);
        method.setApplicableProblemTypes("代数方程、几何证明题");

        // 验证设置结果
        assertTrue(method.isValid());
        assertEquals("TEST001", method.getCode());
        assertEquals("测试解题方法", method.getName());
        assertEquals(SolutionMethodCategory.ANALYSIS_SYNTHESIS, method.getMethodCategory());
        assertEquals(2, method.getDifficultyLevel());
        assertEquals("代数方程、几何证明题", method.getApplicableProblemTypes());
    }
}