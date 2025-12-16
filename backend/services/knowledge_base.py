"""
数学知识库模块
包含七年级上册数学的知识点、考点和教学策略
"""

class MathKnowledgeBase:
    """数学知识库类"""

    def __init__(self):
        self.knowledge_data = {
            "数与式的深度运算": {
                "description": "主要考察学生对符号的驾驭能力，核心是去绝对值和整体代入",
                "topics": {
                    "绝对值化简与最值": {
                        "description": "期中期末必考难点，函数思维雏形",
                        "key_points": [
                            "零点分段法：处理绝对值问题的关键方法",
                            "分类讨论：根据绝对值内表达式的正负性去掉绝对值符号",
                            "最值问题：通过分析绝对值的几何意义求最值"
                        ],
                        "teaching_focus": [
                            "引导学生理解绝对值的几何意义：数轴上点到原点的距离",
                            "培养学生的分类讨论思想",
                            "通过具体例子让学生掌握零点分段法的步骤"
                        ]
                    },
                    "代数式化简求值": {
                        "description": "代数式的恒等变形和求值技巧",
                        "key_points": [
                            "整体代入法：不求单字母的值，而是将x² + 3x = 5作为一个整体代入",
                            "降次策略：利用已知等式将高次项转化为低次项",
                            "主元思想：在多字母式子中，选定一个字母为主元，重新排列观察结构"
                        ],
                        "teaching_focus": [
                            "培养学生观察代数式结构的能力",
                            "引导学生发现整体代换的契机",
                            "通过对比练习让学生感受不同方法的优劣"
                        ]
                    },
                    "整式的加减无关型问题": {
                        "description": "代数式与某字母无关的问题",
                        "key_points": [
                            "无关的本质：若代数式的值与x无关，则合并同类项后，含x的项系数必为0",
                            "求参数模型：列出方程令对应项系数为0，求出参数"
                        ],
                        "teaching_focus": [
                            "让学生理解'无关'的数学含义",
                            "培养学生根据条件建立方程的能力",
                            "通过变式练习巩固解题方法"
                        ]
                    },
                    "整式的规律探索": {
                        "description": "数字和图形规律问题的探索",
                        "key_points": [
                            "数式规律：周期性变化、斐波那契数列变形",
                            "图形规律：火柴棒问题、地砖铺设问题",
                            "运算规律：观察算式的结构，如裂项相消、平方差公式的初级应用"
                        ],
                        "teaching_focus": [
                            "培养学生观察、归纳、猜想的能力",
                            "引导学生从具体到抽象的思维方式",
                            "鼓励学生用多种方法解决同一问题"
                        ]
                    }
                }
            },
            "方程与应用": {
                "description": "方程是解决问题的工具，压轴题往往在解的特殊性和场景的复杂性上做文章",
                "topics": {
                    "一元一次方程特殊解": {
                        "description": "含参数方程的特殊解问题",
                        "key_points": [
                            "整数解问题：求出x的表达式（含参数k），根据x是整数，利用整除特性反求k",
                            "同解方程：两个方程解相同，或解互为相反数/倒数",
                            "错解还原：看错系数、看错符号导致结果错误，倒推原方程系数"
                        ],
                        "teaching_focus": [
                            "培养学生分析方程解的性质的能力",
                            "引导学生理解同解方程的数学原理",
                            "通过典型例题让学生掌握解题思路"
                        ]
                    },
                    "一元一次方程应用(复杂背景)": {
                        "description": "实际应用问题的建模与求解",
                        "key_points": [
                            "经济利润问题：打折销售、分段计费、方案优化",
                            "工程与调配：人多活少、配套问题，关键是抓不变量",
                            "不定方程思想：利用实际意义确定唯一解"
                        ],
                        "teaching_focus": [
                            "培养学生分析实际问题、建立数学模型的能力",
                            "引导学生识别问题中的数量关系和等量关系",
                            "通过多样化的应用场景提高解题兴趣"
                        ]
                    }
                }
            },
            "数轴与动态几何": {
                "description": "这是七上最难、区分度最大的板块，动点、动角是初中动态几何的起点",
                "topics": {
                    "数轴动点问题": {
                        "description": "数轴上点的运动问题",
                        "key_points": [
                            "路程与坐标互化：P点坐标x = 起点 ± 速度 × t",
                            "追及与相遇：利用相对速度或距离差列方程",
                            "中点公式的应用：x_M = (x_A + x_B)/2",
                            "存在性问题：需要分类讨论P点的位置"
                        ],
                        "teaching_focus": [
                            "培养学生的数形结合思想",
                            "引导学生用方程思想解决几何问题",
                            "通过动画演示帮助学生理解点的运动过程"
                        ]
                    },
                    "线段动点问题": {
                        "description": "几何图形中的动点问题",
                        "key_points": [
                            "双中点模型：不管C点怎么动，MN = 1/2 AB",
                            "折返运动：动点在两点之间往返，需分段计算路程",
                            "线段的和差倍分：结合方程思想解决问题"
                        ],
                        "teaching_focus": [
                            "培养学生的空间想象能力",
                            "引导学生寻找不变量",
                            "通过实物演示加深理解"
                        ]
                    },
                    "动角问题": {
                        "description": "角度的旋转与变化问题",
                        "key_points": [
                            "旋转角速度：角度 = 速度 × t",
                            "角平分线夹角模型：寻找角之间的倍数关系",
                            "不变量：两边同时旋转时，角度差可能保持不变",
                            "分类讨论：射线的位置和旋转方向"
                        ],
                        "teaching_focus": [
                            "培养学生观察图形变化的能力",
                            "引导学生识别旋转中的不变量",
                            "通过动手操作加深理解"
                        ]
                    }
                }
            }
        }

        # 关键词映射表，用于快速匹配学生问题到知识点
        self.keyword_mapping = {
            "绝对值": ["数与式的深度运算", "绝对值化简与最值"],
            "absolute": ["数与式的深度运算", "绝对值化简与最值"],
            "最值": ["数与式的深度运算", "绝对值化简与最值"],
            "化简": ["数与式的深度运算", "代数式化简求值"],
            "simplify": ["数与式的深度运算", "代数式化简求值"],
            "求值": ["数与式的深度运算", "代数式化简求值"],
            "无关": ["数与式的深度运算", "整式的加减无关型问题"],
            "规律": ["数与式的深度运算", "整式的规律探索"],
            "pattern": ["数与式的深度运算", "整式的规律探索"],
            "方程": ["方程与应用"],
            "equation": ["方程与应用"],
            "应用题": ["方程与应用", "一元一次方程应用(复杂背景)"],
            "经济": ["方程与应用", "一元一次方程应用(复杂背景)"],
            "利润": ["方程与应用", "一元一次方程应用(复杂背景)"],
            "数轴": ["数轴与动态几何", "数轴动点问题"],
            "number line": ["数轴与动态几何", "数轴动点问题"],
            "动点": ["数轴与动态几何", "数轴动点问题"],
            "线段": ["数轴与动态几何", "线段动点问题"],
            "角度": ["数轴与动态几何", "动角问题"],
            "旋转": ["数轴与动态几何", "动角问题"]
        }

    def query_knowledge(self, question):
        """
        根据学生问题查询相关知识点

        Args:
            question (str): 学生的问题

        Returns:
            dict: 相关的知识点信息
        """
        question_lower = question.lower()

        # 通过关键词匹配找到相关知识点
        matched_knowledge = []
        for keyword, knowledge_path in self.keyword_mapping.items():
            if keyword in question_lower:
                if len(knowledge_path) == 1:
                    module_name = knowledge_path[0]
                    topic_name = None
                else:
                    module_name, topic_name = knowledge_path

                if module_name in self.knowledge_data:
                    module_info = self.knowledge_data[module_name]
                    if topic_name and topic_name in module_info["topics"]:
                        topic_info = module_info["topics"][topic_name]
                        matched_knowledge.append({
                            "module": module_name,
                            "topic": topic_name,
                            "description": topic_info["description"],
                            "key_points": topic_info["key_points"],
                            "teaching_focus": topic_info["teaching_focus"]
                        })
                    else:
                        matched_knowledge.append({
                            "module": module_name,
                            "description": module_info["description"],
                            "topics": list(module_info["topics"].keys())
                        })

        return matched_knowledge if matched_knowledge else None

    def get_teaching_suggestions(self, module_name, topic_name=None):
        """
        获取教学建议

        Args:
            module_name (str): 模块名称
            topic_name (str, optional): 专题名称

        Returns:
            dict: 教学建议
        """
        if module_name not in self.knowledge_data:
            return None

        module_info = self.knowledge_data[module_name]

        if topic_name and topic_name in module_info["topics"]:
            return module_info["topics"][topic_name]["teaching_focus"]
        else:
            # 返回整个模块的教学建议
            suggestions = []
            for topic in module_info["topics"].values():
                suggestions.extend(topic.get("teaching_focus", []))
            return suggestions


# 全局知识库实例
knowledge_base = MathKnowledgeBase()