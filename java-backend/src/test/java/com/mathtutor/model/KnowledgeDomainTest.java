package com.mathtutor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KnowledgeDomainTest {

    @Test
    void testBasicKnowledgeDomain() {
        // 测试基础知识点创建（使用新的构造函数）
        KnowledgeDomain knowledge = new KnowledgeDomain(
            "ALG001", "一元一次方程", GradeEnum.SEVENTH_GRADE,
            "含有一个未知数且未知数次数为1的方程", KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "一元一次方程基础", "理解方程的概念和基本解法"
        );

        // 验证基本属性
        assertEquals("ALG001", knowledge.getCode());
        assertEquals("一元一次方程", knowledge.getName());
        assertEquals(GradeEnum.SEVENTH_GRADE, knowledge.getGrade());
        assertEquals("含有一个未知数且未知数次数为1的方程", knowledge.getDescription());
        assertEquals(KnowledgeSystemEnum.ALGEBRA, knowledge.getKnowledgeSystem());
        assertEquals("方程与应用", knowledge.getModule());
        assertEquals("一元一次方程基础", knowledge.getTopic());
        assertEquals("理解方程的概念和基本解法", knowledge.getAssessmentCore());
        assertEquals(2, knowledge.getDepthLevel());
        assertEquals(2, knowledge.getDifficultyLevel());

        // 验证有效性
        assertTrue(knowledge.isValid());

        // 验证完整标识符
        assertEquals("代数-ALG001", knowledge.getFullIdentifier());
        assertEquals("方程与应用-ALG001", knowledge.getModuleIdentifier());
        assertEquals("方程与应用-一元一次方程基础-ALG001", knowledge.getTopicIdentifier());

        // 验证完整信息
        assertTrue(knowledge.getFullInfo().contains("[ALG001]"));
        assertTrue(knowledge.getFullInfo().contains("方程与应用"));
        assertTrue(knowledge.getFullInfo().contains("一元一次方程基础"));

        // 验证模块和专题归属
        assertTrue(knowledge.belongsToModule("方程与应用"));
        assertTrue(knowledge.belongsToTopic("一元一次方程基础"));
    }

    @Test
    void testKnowledgeHierarchy() {
        // 创建父知识点
        KnowledgeDomain parent = new KnowledgeDomain(
            "ALG000", "方程基础", GradeEnum.SEVENTH_GRADE,
            "方程的基本概念和解法", KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "方程基础", "理解方程的基本概念和分类"
        );

        // 创建子知识点
        KnowledgeDomain child = new KnowledgeDomain(
            "ALG001", "一元一次方程", GradeEnum.SEVENTH_GRADE,
            "含有一个未知数且未知数次数为1的方程", KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "一元一次方程基础", "理解方程的概念和基本解法",
            parent
        );

        // 验证层次关系
        assertEquals(parent, child.getParentKnowledge());
        assertFalse(child.isBasicKnowledge());
        assertEquals("方程基础 > 一元一次方程", child.getFullPath());
    }

    @Test
    void testAdvancedKnowledge() {
        KnowledgeDomain advanced = new KnowledgeDomain(
            "FUNC003", "二次函数综合应用", GradeEnum.NINTH_GRADE,
            "二次函数在几何、物理等领域的综合应用", KnowledgeSystemEnum.FUNCTION,
            "函数与分析", "二次函数应用", "综合运用二次函数解决复杂问题"
        );
        advanced.setDepthLevel(3); // 高级知识点
        advanced.setDifficultyLevel(3); // 困难

        assertTrue(advanced.isAdvancedKnowledge());
        assertEquals(3, advanced.getDifficultyLevel());
    }

    @Test
    void testInvalidKnowledge() {
        // 测试无效数据
        KnowledgeDomain invalid1 = new KnowledgeDomain();
        invalid1.setName("测试知识点");
        assertFalse(invalid1.isValid()); // 缺少必要字段

        KnowledgeDomain invalid2 = new KnowledgeDomain("", "测试", GradeEnum.SEVENTH_GRADE, "说明",
            KnowledgeSystemEnum.ALGEBRA, "测试模块", "测试专题", "测试核心"
        );
        assertFalse(invalid2.isValid()); // 空编号
    }

    @Test
    void testDifferentKnowledgeSystems() {
        // 代数体系
        KnowledgeDomain algebra = new KnowledgeDomain(
            "ALG002", "二元一次方程组", GradeEnum.EIGHTH_GRADE,
            "含有两个未知数的两个一次方程组成的方程组", KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "二元一次方程组", "理解二元一次方程组的解法和应用"
        );

        // 几何体系
        KnowledgeDomain geometry = new KnowledgeDomain(
            "GEO001", "三角形内角和", GradeEnum.SEVENTH_GRADE,
            "三角形三个内角的和等于180度", KnowledgeSystemEnum.GEOMETRY,
            "几何基础", "三角形基本性质", "掌握三角形内角和定理及其应用"
        );

        assertEquals("代数-ALG002", algebra.getFullIdentifier());
        assertEquals("几何-GEO001", geometry.getFullIdentifier());
        assertNotEquals(algebra.getKnowledgeSystem(), geometry.getKnowledgeSystem());
    }

    @Test
    void testSetterMethods() {
        KnowledgeDomain knowledge = new KnowledgeDomain();

        // 测试父知识点设置
        KnowledgeDomain parent = new KnowledgeDomain("P001", "父级", GradeEnum.SEVENTH_GRADE, "说明",
            KnowledgeSystemEnum.ALGEBRA, "测试模块", "测试专题", "测试核心");
        knowledge.setParentKnowledge(parent);
        assertEquals(parent, knowledge.getParentKnowledge());

        // 测试属性设置
        knowledge.setCode("TEST001");
        knowledge.setName("测试知识点");
        knowledge.setGrade(GradeEnum.EIGHTH_GRADE);
        knowledge.setDescription("这是一个测试知识点");
        knowledge.setKnowledgeSystem(KnowledgeSystemEnum.ALGEBRA);
        knowledge.setModule("测试模块");
        knowledge.setTopic("测试专题");
        knowledge.setAssessmentCore("测试核心");

        assertTrue(knowledge.isValid());
        assertEquals("代数-TEST001", knowledge.getFullIdentifier());
    }
}