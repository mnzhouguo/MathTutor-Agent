package com.mathtutor.init;

import com.mathtutor.model.GradeEnum;
import com.mathtutor.model.KnowledgeSystemEnum;
import com.mathtutor.model.KnowledgeDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeSevenKnowledgeInitializerTest {

    @Test
    void testInitializeAllKnowledge() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();

        // 验证总数量
        assertEquals(10, allKnowledge.size());

        // 验证所有知识点都是七年级
        allKnowledge.forEach(knowledge -> {
            assertEquals(GradeEnum.SEVENTH_GRADE, knowledge.getGrade());
            assertTrue(knowledge.isValid());
        });
    }

    @Test
    void testModule1Knowledge() {
        var module1 = GradeSevenKnowledgeInitializer.getModule1Knowledge();

        // 验证模块1包含5个专题
        assertEquals(5, module1.size());

        // 验证模块名称
        module1.forEach(knowledge -> {
            assertEquals("数与式的深度运算", knowledge.getModule());
        });

        // 验证具体的专题
        var topics = module1.stream().map(KnowledgeDomain::getTopic).toList();
        assertTrue(topics.contains("绝对值化简_(专题1)"));
        assertTrue(topics.contains("进位制与新运算_(专题10)"));
        assertTrue(topics.contains("代数式化简求值_(专题3)"));
        assertTrue(topics.contains("整式无关型问题_(专题4)"));
        assertTrue(topics.contains("整式的规律探索_(专题5)"));
    }

    @Test
    void testModule2Knowledge() {
        var module2 = GradeSevenKnowledgeInitializer.getModule2Knowledge();

        // 验证模块2包含2个专题
        assertEquals(2, module2.size());

        // 验证模块名称
        module2.forEach(knowledge -> {
            assertEquals("方程与应用", knowledge.getModule());
        });

        // 验证专题名称
        var topics = module2.stream().map(KnowledgeDomain::getTopic).toList();
        assertTrue(topics.contains("一元一次方程特殊解_(专题6)"));
        assertTrue(topics.contains("一元一次方程应用_(专题7)"));
    }

    @Test
    void testModule3Knowledge() {
        var module3 = GradeSevenKnowledgeInitializer.getModule3Knowledge();

        // 验证模块3包含3个专题
        assertEquals(3, module3.size());

        // 验证模块名称
        module3.forEach(knowledge -> {
            assertEquals("数轴与动态几何", knowledge.getModule());
        });

        // 验证专题名称
        var topics = module3.stream().map(KnowledgeDomain::getTopic).toList();
        assertTrue(topics.contains("数轴动点问题_(专题8)"));
        assertTrue(topics.contains("线段动点问题_(专题9)"));
        assertTrue(topics.contains("动角问题_(专题10)"));
    }

    @Test
    void testKnowledgeStructure() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();

        // 验证第一个知识点（绝对值化简）
        KnowledgeDomain absoluteValue = allKnowledge.get(0);
        assertEquals("M1T001", absoluteValue.getCode());
        assertEquals("绝对值化简", absoluteValue.getName());
        assertEquals("数与式的深度运算", absoluteValue.getModule());
        assertEquals("绝对值化简_(专题1)", absoluteValue.getTopic());
        assertTrue(absoluteValue.getAssessmentCore().contains("符号的驾驭能力"));
        assertTrue(absoluteValue.getAssessmentCore().contains("代数"));

        // 验证详细考点数量
        assertTrue(absoluteValue.getDetailedPointCount() > 0);
        assertTrue(absoluteValue.getDetailedPoints().stream().anyMatch(point -> point.contains("零点分段法")));

        // 验证难度设置
        assertEquals(3, absoluteValue.getDifficultyLevel());
    }

    @Test
    void testKnowledgeByModule() {
        var algebraModule = GradeSevenKnowledgeInitializer.getKnowledgeByModule("数与式的深度运算");
        var geometryModule = GradeSevenKnowledgeInitializer.getKnowledgeByModule("数轴与动态几何");
        var equationModule = GradeSevenKnowledgeInitializer.getKnowledgeByModule("方程与应用");

        assertEquals(5, algebraModule.size());
        assertEquals(3, geometryModule.size());
        assertEquals(2, equationModule.size());
    }

    @Test
    void testKnowledgeByTopic() {
        var absoluteValueTopic = GradeSevenKnowledgeInitializer.getKnowledgeByTopic("绝对值化简_(专题1)");
        var dynamicPointsTopic = GradeSevenKnowledgeInitializer.getKnowledgeByTopic("数轴动点问题_(专题8)");

        assertEquals(1, absoluteValueTopic.size());
        assertEquals(1, dynamicPointsTopic.size());

        assertEquals("M1T001", absoluteValueTopic.get(0).getCode());
        assertEquals("M3T008", dynamicPointsTopic.get(0).getCode());
    }

    @Test
    void testKnowledgeIdentifiers() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();

        // 验证模块标识符
        KnowledgeDomain knowledge = allKnowledge.get(0);
        assertEquals("数与式的深度运算-M1T001", knowledge.getModuleIdentifier());

        // 验证专题标识符
        assertEquals("数与式的深度运算-绝对值化简_(专题1)-M1T001", knowledge.getTopicIdentifier());

        // 验证完整信息
        assertTrue(knowledge.getFullInfo().contains("[M1T001]"));
        assertTrue(knowledge.getFullInfo().contains("数与式的深度运算"));
        assertTrue(knowledge.getFullInfo().contains("绝对值化简_(专题1)"));
    }

    @Test
    void testKnowledgeBelongsTo() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();
        KnowledgeDomain knowledge = allKnowledge.get(0);

        assertTrue(knowledge.belongsToModule("数与式的深度运算"));
        assertFalse(knowledge.belongsToModule("方程与应用"));

        assertTrue(knowledge.belongsToTopic("绝对值化简_(专题1)"));
        assertFalse(knowledge.belongsToTopic("一元一次方程应用_(专题7)"));
    }

    @Test
    void testKnowledgeSystemDistribution() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();

        // 统计不同知识体系的数量
        long algebraCount = allKnowledge.stream()
                .filter(k -> k.getKnowledgeSystem() == KnowledgeSystemEnum.ALGEBRA)
                .count();
        long numberTheoryCount = allKnowledge.stream()
                .filter(k -> k.getKnowledgeSystem() == KnowledgeSystemEnum.NUMBER_THEORY)
                .count();
        long geometryCount = allKnowledge.stream()
                .filter(k -> k.getKnowledgeSystem() == KnowledgeSystemEnum.GEOMETRY)
                .count();

        assertTrue(algebraCount > 0);
        assertTrue(numberTheoryCount > 0);
        assertTrue(geometryCount > 0);

        // 验证总数
        assertEquals(allKnowledge.size(), algebraCount + numberTheoryCount + geometryCount);
    }

    @Test
    void testDifficultyLevelDistribution() {
        var allKnowledge = GradeSevenKnowledgeInitializer.initializeGradeSevenKnowledge();

        // 统计不同难度级别的数量
        long easyCount = allKnowledge.stream()
                .filter(k -> k.getDifficultyLevel() != null && k.getDifficultyLevel() == 1)
                .count();
        long mediumCount = allKnowledge.stream()
                .filter(k -> k.getDifficultyLevel() != null && k.getDifficultyLevel() == 2)
                .count();
        long hardCount = allKnowledge.stream()
                .filter(k -> k.getDifficultyLevel() != null && k.getDifficultyLevel() == 3)
                .count();

        System.out.println("七年级知识点难度分布：");
        System.out.println("简单 (1级): " + easyCount);
        System.out.println("中等 (2级): " + mediumCount);
        System.out.println("困难 (3级): " + hardCount);

        // 验证难度分布合理
        assertTrue(mediumCount > 0);
        assertTrue(hardCount > 0); // 应该有高难度的专题
    }
}