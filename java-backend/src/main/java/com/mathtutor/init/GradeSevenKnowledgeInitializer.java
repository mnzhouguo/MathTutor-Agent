package com.mathtutor.init;

import com.mathtutor.model.KnowledgeDomain;
import com.mathtutor.model.GradeEnum;
import com.mathtutor.model.KnowledgeSystemEnum;

/**
 * 七年级知识点数据初始化器
 * 根据提供的七年级数学核心内容初始化知识点数据
 */
public class GradeSevenKnowledgeInitializer {

    /**
     * 初始化所有七年级知识点
     * @return 七年级知识点列表
     */
    public static java.util.List<KnowledgeDomain> initializeGradeSevenKnowledge() {
        java.util.List<KnowledgeDomain> knowledgeList = new java.util.ArrayList<>();

        // 第一模块：数与式的深度运算（代数思维篇）
        knowledgeList.addAll(initializeModule1());

        // 第二模块：方程与应用（建模思维篇）
        knowledgeList.addAll(initializeModule2());

        // 第三模块：数轴与动态几何（数形结合篇）
        knowledgeList.addAll(initializeModule3());

        return knowledgeList;
    }

    /**
     * 初始化第一模块：数与式的深度运算
     */
    private static java.util.List<KnowledgeDomain> initializeModule1() {
        java.util.List<KnowledgeDomain> module1 = new java.util.ArrayList<>();

        // 专题1：绝对值化简
        KnowledgeDomain absoluteValue = new KnowledgeDomain(
            "M1T001", "绝对值化简", GradeEnum.SEVENTH_GRADE,
            "处理含绝对值表达式的化简问题，核心是掌握绝对值的性质和零点分段法",
            KnowledgeSystemEnum.ALGEBRA,
            "数与式的深度运算", "绝对值化简_(专题1)",
            "主要考察学生对符号的驾驭能力，核心是由'算数'向'代数'的转变"
        );
        absoluteValue.addDetailedPoint("零点分段法：处理多个绝对值相加（如|a| + |b|）");
        absoluteValue.addDetailedPoint("绝对值的几何意义：数轴上的距离概念");
        absoluteValue.addDetailedPoint("绝对值的性质：|ab| = |a|·|b|，|a|² = a²");
        absoluteValue.setDifficultyLevel(3);
        module1.add(absoluteValue);

        // 专题10：进位制与新运算
        KnowledgeDomain numberSystem = new KnowledgeDomain(
            "M1T010", "进位制与新运算", GradeEnum.SEVENTH_GRADE,
            "理解不同进制的转换规则和新定义运算的处理方法",
            KnowledgeSystemEnum.NUMBER_THEORY,
            "数与式的深度运算", "进位制与新运算_(专题10)",
            "培养学生的符号理解和抽象思维能力"
        );
        numberSystem.addDetailedPoint("严格套用：面对a∗b = 3a - 2b等新定义，严格代入，不随意改变运算顺序");
        numberSystem.addDetailedPoint("位值原理：理解进制转换本质，如八进制123₍₈ = 1×8² + 2×8¹ + 3×8⁰");
        numberSystem.addDetailedPoint("进制转换：二进制、八进制、十六进制与十进制的互化");
        numberSystem.setDifficultyLevel(2);
        module1.add(numberSystem);

        // 专题3：代数式化简求值
        KnowledgeDomain algebraicSimplification = new KnowledgeDomain(
            "M1T003", "代数式化简求值", GradeEnum.SEVENTH_GRADE,
            "运用代数式的运算性质和已知条件进行化简求值",
            KnowledgeSystemEnum.ALGEBRA,
            "数与式的深度运算", "代数式化简求值_(专题3)",
            "培养学生的代数思维和运算技巧"
        );
        algebraicSimplification.addDetailedPoint("整体代入法：不求单字母的值，而是将x-2y=3作为一个整体代入到复杂式子中");
        algebraicSimplification.addDetailedPoint("降幂策略：利用已知等式将高次项转化为低次项（如已知x² = x+1，求x³）");
        algebraicSimplification.addDetailedPoint("配方法：将二次三项式配成完全平方形式");
        algebraicSimplification.setDifficultyLevel(2);
        module1.add(algebraicSimplification);

        // 专题4：整式无关型问题
        KnowledgeDomain polynomialIndependence = new KnowledgeDomain(
            "M1T004", "整式无关型问题", GradeEnum.SEVENTH_GRADE,
            "解决代数式的值与某个字母无关的问题，通常是求参数值",
            KnowledgeSystemEnum.ALGEBRA,
            "数与式的深度运算", "整式无关型问题_(专题4)",
            "培养学生的分析和推理能力"
        );
        polynomialIndependence.addDetailedPoint("'无关'的本质：若代数式的值与x无关，则合并同类项后，含x的项系数必为0");
        polynomialIndependence.addDetailedPoint("求参数模型：列出方程令x的系数为0，从而求出参数m, n的值");
        polynomialIndependence.addDetailedPoint("代数恒等式：通过代数恒等式的性质求解参数");
        polynomialIndependence.setDifficultyLevel(3);
        module1.add(polynomialIndependence);

        // 专题5：整式的规律探索
        KnowledgeDomain patternExploration = new KnowledgeDomain(
            "M1T005", "整式的规律探索", GradeEnum.SEVENTH_GRADE,
            "发现数列和图形中的规律，并用代数式表示",
            KnowledgeSystemEnum.ALGEBRA,
            "数与式的深度运算", "整式的规律探索_(专题5)",
            "培养学生的观察、归纳和抽象思维能力"
        );
        patternExploration.addDetailedPoint("数式规律：周期性变化（符号跳变、个位循环）、斐波那契数列变形");
        patternExploration.addDetailedPoint("图形规律：火柴棒、点阵图，核心是找到n对应的通项公式（通常是an+b或n²）");
        patternExploration.addDetailedPoint("递推关系：通过前几项推导递推公式");
        patternExploration.setDifficultyLevel(2);
        module1.add(patternExploration);

        return module1;
    }

    /**
     * 初始化第二模块：方程与应用
     */
    private static java.util.List<KnowledgeDomain> initializeModule2() {
        java.util.List<KnowledgeDomain> module2 = new java.util.ArrayList<>();

        // 专题6：一元一次方程特殊解
        KnowledgeDomain equationSpecialSolution = new KnowledgeDomain(
            "M2T006", "一元一次方程特殊解", GradeEnum.SEVENTH_GRADE,
            "解决具有特殊解的条件的一元一次方程问题",
            KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "一元一次方程特殊解_(专题6)",
            "压轴题往往在'解的特殊性'（整数解）和'场景的复杂性'（最优化方案）上做文章"
        );
        equationSpecialSolution.addDetailedPoint("整数解问题：分离参数，将x表示为含k的式子（如x=12/k），利用整除特性反求k");
        equationSpecialSolution.addDetailedPoint("同解方程：两个方程解相同，或者解互为相反数/倒数，互相代入求解");
        equationSpecialSolution.addDetailedPoint("错解还原：看错系数导致结果错误，通过错误结果倒推原方程系数");
        equationSpecialSolution.setDifficultyLevel(3);
        module2.add(equationSpecialSolution);

        // 专题7：一元一次方程应用
        KnowledgeDomain equationApplication = new KnowledgeDomain(
            "M2T007", "一元一次方程应用", GradeEnum.SEVENTH_GRADE,
            "运用一元一次方程解决实际生活中的各种应用问题",
            KnowledgeSystemEnum.ALGEBRA,
            "方程与应用", "一元一次方程应用_(专题7)",
            "方程是解决实际问题的工具"
        );
        equationApplication.addDetailedPoint("方案选择：比较两种套餐（如话费、租车）哪个更划算，通常需寻找'费用相等'的临界点");
        equationApplication.addDetailedPoint("分段计费：水费、电费、出租车的阶梯计价，需准确列出分段方程");
        equationApplication.addDetailedPoint("销售利润：熟练运用售价 = 进价 × (1+利润率)及打折公式");
        equationApplication.addDetailedPoint("行程问题：利用路程=速度×时间解决相遇、追及等问题");
        equationApplication.setDifficultyLevel(2);
        module2.add(equationApplication);

        return module2;
    }

    /**
     * 初始化第三模块：数轴与动态几何
     */
    private static java.util.List<KnowledgeDomain> initializeModule3() {
        java.util.List<KnowledgeDomain> module3 = new java.util.ArrayList<>();

        // 专题8：数轴动点问题
        KnowledgeDomain numberLineDynamicPoints = new KnowledgeDomain(
            "M3T008", "数轴动点问题", GradeEnum.SEVENTH_GRADE,
            "在数轴上分析点的运动规律，解决相遇、追及等问题",
            KnowledgeSystemEnum.NUMBER_THEORY,
            "数轴与动态几何", "数轴动点问题_(专题8)",
            "这是七上最难、区分度最大的板块，必须掌握'分类讨论'思想"
        );
        numberLineDynamicPoints.addDetailedPoint("路程与坐标互化：P点坐标xt = 起点 ± 速度 × t");
        numberLineDynamicPoints.addDetailedPoint("追及与相遇：数轴上的行程问题，利用相对速度或距离差列方程");
        numberLineDynamicPoints.addDetailedPoint("中点公式：若M是AB中点，则xM = (xA + xB)/2，这是解决复杂动点问题的神器");
        numberLineDynamicPoints.addDetailedPoint("分类讨论：动点在定点的左侧还是右侧？相遇前还是相遇后？必须画图讨论");
        numberLineDynamicPoints.setDifficultyLevel(3);
        module3.add(numberLineDynamicPoints);

        // 专题9：线段动点问题
        KnowledgeDomain segmentDynamicPoints = new KnowledgeDomain(
            "M3T009", "线段动点问题", GradeEnum.SEVENTH_GRADE,
            "在几何图形中分析线段长度的变化规律",
            KnowledgeSystemEnum.GEOMETRY,
            "数轴与动态几何", "线段动点问题_(专题9)",
            "将数轴动点问题推广到平面几何中"
        );
        segmentDynamicPoints.addDetailedPoint("双中点模型：M是AC中点，N是BC中点，不管C点怎么动，MN = 1/2AB（长度定值）");
        segmentDynamicPoints.addDetailedPoint("折返运动：动点在两点之间往返，需分段计算路程，注意'转折点'的时间");
        segmentDynamicPoints.addDetailedPoint("方程思想：设最小线段为x，用x表示其他线段列方程求解");
        segmentDynamicPoints.addDetailedPoint("相似三角形：利用相似比例关系求解线段长度");
        segmentDynamicPoints.setDifficultyLevel(3);
        module3.add(segmentDynamicPoints);

        // 专题10：动角问题
        KnowledgeDomain dynamicAngle = new KnowledgeDomain(
            "M3T010", "动角问题", GradeEnum.SEVENTH_GRADE,
            "分析角度的动态变化规律，解决与角度相关的几何问题",
            KnowledgeSystemEnum.GEOMETRY,
            "数轴与动态几何", "动角问题_(专题10)",
            "将线段运动的概念推广到角度运动"
        );
        dynamicAngle.addDetailedPoint("旋转角速度：类似于追及问题，角度 = 角速度 × t");
        dynamicAngle.addDetailedPoint("角平分线夹角模型：如'双角平分线'，两边角平分线夹角往往是定值（总角的一半）");
        dynamicAngle.addDetailedPoint("三角板叠合：利用三角板的固定角度（30°、45°、60°、90°）建立等量关系");
        dynamicAngle.addDetailedPoint("分类讨论：射线在角的内部还是外部？顺时针还是逆时针？漏解是最大扣分点");
        dynamicAngle.setDifficultyLevel(3);
        module3.add(dynamicAngle);

        return module3;
    }

    /**
     * 获取第一模块的知识点
     */
    public static java.util.List<KnowledgeDomain> getModule1Knowledge() {
        return initializeModule1();
    }

    /**
     * 获取第二模块的知识点
     */
    public static java.util.List<KnowledgeDomain> getModule2Knowledge() {
        return initializeModule2();
    }

    /**
     * 获取第三模块的知识点
     */
    public static java.util.List<KnowledgeDomain> getModule3Knowledge() {
        return initializeModule3();
    }

    /**
     * 根据模块名称获取知识点
     */
    public static java.util.List<KnowledgeDomain> getKnowledgeByModule(String moduleName) {
        switch (moduleName) {
            case "数与式的深度运算":
                return initializeModule1();
            case "方程与应用":
                return initializeModule2();
            case "数轴与动态几何":
                return initializeModule3();
            default:
                return new java.util.ArrayList<>();
        }
    }

    /**
     * 根据专题名称获取知识点
     */
    public static java.util.List<KnowledgeDomain> getKnowledgeByTopic(String topicName) {
        java.util.List<KnowledgeDomain> allKnowledge = initializeGradeSevenKnowledge();
        return allKnowledge.stream()
                .filter(k -> k.belongsToTopic(topicName))
                .collect(java.util.stream.Collectors.toList());
    }
}