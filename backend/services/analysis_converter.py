"""
数学压轴题分析结果转换器
将Markdown格式的输出转换为结构化JSON数据
"""

import re
import json
import uuid
from datetime import datetime
from typing import Dict, List, Any, Optional, Tuple

from models.analysis_models import (
    MathProblemAnalysisResult,
    QuestionAnalysis,
    AnalysisObjective,
    DifficultyAnalysis,
    SubQuestionAnalysis,
    KnowledgePoint,
    SolutionStep,
    GeneralSuggestion,
    DifficultyLevel,
    KnowledgeModule,
    AnalysisResponse
)

class MarkdownToJSONConverter:
    """Markdown到JSON转换器"""

    def __init__(self):
        # 正则表达式模式
        self.patterns = {
            'main_title': r'##\s*(.+)',
            'sub_title': r'###\s*(.+)',
            'bold_section': r'\*\*(.+?)\*\*',
            'list_item': r'^\s*[-*]\s+(.+)$',
            'numbered_list': r'^\s*(\d+)\.\s+(.+)$',
            'code_block': r'```[\s\S]*?```',
            'inline_code': r'`([^`]+)`',
        }

    def convert_analysis_to_json(self, question: str, markdown_text: str) -> AnalysisResponse:
        """
        将Markdown格式的分析结果转换为结构化JSON

        Args:
            question: 原始题目
            markdown_text: Markdown格式的分析结果

        Returns:
            AnalysisResponse: 包含结构化结果的响应对象
        """
        try:
            start_time = datetime.now()
            analysis_id = str(uuid.uuid4())

            # 预处理文本
            cleaned_text = self._preprocess_text(markdown_text)

            # 解析各个部分
            question_analysis = self._parse_question_analysis(cleaned_text)
            sub_questions = self._parse_sub_questions(cleaned_text)
            general_suggestions = self._parse_general_suggestions(cleaned_text)

            # 构建完整结果
            structured_result = MathProblemAnalysisResult(
                question=question,
                analysis_id=analysis_id,
                timestamp=start_time.isoformat(),
                version="1.0",
                question_analysis=question_analysis,
                sub_questions=sub_questions,
                general_suggestions=general_suggestions,
                total_subquestions=len(sub_questions),
                total_knowledge_points=sum(len(sq.knowledge_points) for sq in sub_questions),
                total_solution_steps=sum(len(sq.solution_steps) for sq in sub_questions)
            )

            # 计算处理时间
            processing_time = (datetime.now() - start_time).total_seconds()

            return AnalysisResponse(
                status="success",
                analysis_id=analysis_id,
                raw_text=markdown_text,
                structured_result=structured_result,
                processing_time=processing_time
            )

        except Exception as e:
            return AnalysisResponse(
                status="error",
                analysis_id=str(uuid.uuid4()),
                raw_text=markdown_text,
                error=str(e)
            )

    def _preprocess_text(self, text: str) -> str:
        """预处理文本，统一格式"""
        # 移除代码块标记
        text = re.sub(r'```', '', text)
        # 统一换行符
        text = text.replace('\r\n', '\n').replace('\r', '\n')
        # 移除多余空行
        text = re.sub(r'\n{3,}', '\n\n', text)
        return text.strip()

    def _parse_question_analysis(self, text: str) -> QuestionAnalysis:
        """解析题目分析部分"""
        # 提取题目分析部分
        analysis_section = self._extract_section(text, "## 题目分析", "## 各问分析")
        if not analysis_section:
            return self._create_default_question_analysis()

        # 解析各个子部分
        background = self._extract_subsection(analysis_section, "### 题目背景")
        intent = self._extract_subsection(analysis_section, "### 考查意图")
        difficulty = self._extract_subsection(analysis_section, "### 难点解析")

        return QuestionAnalysis(
            background=background or "暂无背景信息",
            difficulty=self._determine_difficulty(text),
            objectives=AnalysisObjective(
                knowledge_points=self._extract_list_items(intent),
                skill_requirements=[],
                thinking_methods=[]
            ),
            difficulty_analysis=DifficultyAnalysis(
                difficult_points=self._extract_list_items(difficulty),
                common_errors=[],
                solving_strategies=[]
            ),
            overall_approach="根据题目要求逐步求解"
        )

    def _parse_sub_questions(self, text: str) -> List[SubQuestionAnalysis]:
        """解析各问分析部分"""
        sub_questions = []

        # 提取各问分析部分
        sub_questions_section = self._extract_section(text, "## 各问分析", "## 解题建议")
        if not sub_questions_section:
            return sub_questions

        # 查找所有小问
        sub_question_patterns = [
            r'### 第([一二三四五六七八九十\d]+)问分析',
            r'### 第(\d+)问分析',
            r'### (\d+)、',
            r'### （(\d+)）',
            r'### 第(\d+)小问',
        ]

        for pattern in sub_question_patterns:
            matches = list(re.finditer(pattern, sub_questions_section))
            for match in matches:
                question_num = self._chinese_to_number(match.group(1)) if match.group(1).isdigit() == False else int(match.group(1))

                # 提取这个小问的完整内容
                start_pos = match.start()
                end_pos = len(sub_questions_section)

                # 查找下一个小问的开始位置
                next_match = re.search(pattern, sub_questions_section[match.end():])
                if next_match:
                    end_pos = match.end() + next_match.start()

                sub_q_text = sub_questions_section[start_pos:end_pos]

                # 解析小问内容
                sub_question = self._parse_single_sub_question(question_num, sub_q_text)
                sub_questions.append(sub_question)

        return sub_questions

    def _parse_single_sub_question(self, question_num: int, text: str) -> SubQuestionAnalysis:
        """解析单个小问"""
        # 提取各个部分
        key_points_text = self._extract_bold_section(text, "考点识别")
        knowledge_points_text = self._extract_bold_section(text, "需要掌握的知识点")
        solution_steps_text = self._extract_bold_section(text, "解题思路与步骤")

        # 解析知识点
        knowledge_points = []
        for kp_text in self._extract_list_items(knowledge_points_text):
            kp = KnowledgePoint(
                name=self._extract_knowledge_point_name(kp_text),
                description=kp_text,
                application="在解题过程中应用",
                module=self._determine_knowledge_module(kp_text)
            )
            knowledge_points.append(kp)

        # 解析解题步骤
        solution_steps = []
        step_number = 1
        for line in solution_steps_text.split('\n'):
            line = line.strip()
            if line and (line[0].isdigit() or line.startswith('-')):
                step = SolutionStep(
                    step_number=step_number,
                    description=line,
                    reasoning="根据题目要求进行分析",
                    key_points=[]
                )
                solution_steps.append(step)
                step_number += 1

        return SubQuestionAnalysis(
            question_number=question_num,
            question_text=f"第{question_num}问",
            key_points=self._extract_list_items(key_points_text),
            knowledge_points=knowledge_points,
            solution_steps=solution_steps,
            alternative_methods=[]
        )

    def _parse_general_suggestions(self, text: str) -> List[GeneralSuggestion]:
        """解析解题建议部分"""
        suggestions = []
        suggestions_section = self._extract_section(text, "## 解题建议", None)

        if suggestions_section:
            for i, suggestion in enumerate(self._extract_list_items(suggestions_section), 1):
                gs = GeneralSuggestion(
                    type="解题建议",
                    content=suggestion,
                    priority=i
                )
                suggestions.append(gs)

        return suggestions

    def _extract_section(self, text: str, start_marker: str, end_marker: Optional[str] = None) -> str:
        """提取指定标记之间的文本"""
        start_pos = text.find(start_marker)
        if start_pos == -1:
            return ""

        start_pos += len(start_marker)

        if end_marker:
            end_pos = text.find(end_marker, start_pos)
            if end_pos == -1:
                return text[start_pos:].strip()
        else:
            end_pos = len(text)

        return text[start_pos:end_pos].strip()

    def _extract_subsection(self, text: str, subsection_marker: str) -> str:
        """提取子部分内容"""
        lines = text.split('\n')
        result = []
        capture = False

        for line in lines:
            line = line.strip()
            if line.startswith(subsection_marker):
                capture = True
                continue
            elif capture and (line.startswith('###') or line.startswith('##')):
                break
            elif capture and line:
                result.append(line)

        return '\n'.join(result)

    def _extract_bold_section(self, text: str, section_name: str) -> str:
        """提取加粗标记的部分"""
        pattern = rf'\*\*{section_name}：\*\*\s*\n((?:.*\n)*?)(?=\*\*|\n\n|\Z)'
        match = re.search(pattern, text, re.MULTILINE | re.DOTALL)
        return match.group(1).strip() if match else ""

    def _extract_list_items(self, text: str) -> List[str]:
        """提取列表项"""
        items = []
        for line in text.split('\n'):
            line = line.strip()
            if line.startswith(('-', '*', '•', '1.', '2.', '3.', '4.', '5.', '6.', '7.', '8.', '9.')):
                # 移除列表标记
                clean_line = re.sub(r'^[-*•]\s*', '', line)
                clean_line = re.sub(r'^\d+\.\s*', '', clean_line)
                if clean_line:
                    items.append(clean_line)
        return items

    def _extract_knowledge_point_name(self, text: str) -> str:
        """提取知识点名称"""
        # 简单提取，提取第一句话或前20个字符
        if '：' in text:
            return text.split('：')[0].strip()
        if ':' in text:
            return text.split(':')[0].strip()
        return text[:20].strip()

    def _determine_knowledge_module(self, text: str) -> KnowledgeModule:
        """根据文本确定知识模块"""
        module_keywords = {
            KnowledgeModule.ALGEBRA: ['代数', '方程', '不等式', '函数', '多项式'],
            KnowledgeModule.GEOMETRY: ['几何', '三角形', '圆', '角度', '平行', '垂直'],
            KnowledgeModule.COORDINATE_GEOMETRY: ['坐标', '解析几何', '数轴'],
            KnowledgeModule.FUNCTION: ['函数', '图像', '定义域', '值域'],
            KnowledgeModule.STATISTICS: ['统计', '概率', '数据'],
            KnowledgeModule.NUMBER_THEORY: ['数论', '整数', '质数', '因数'],
            KnowledgeModule.COMPREHENSIVE: ['综合', '应用', '实际']
        }

        for module, keywords in module_keywords.items():
            if any(keyword in text for keyword in keywords):
                return module

        return KnowledgeModule.COMPREHENSIVE

    def _determine_difficulty(self, text: str) -> DifficultyLevel:
        """根据文本确定难度级别"""
        difficulty_keywords = {
            DifficultyLevel.EASY: ['基础', '简单', '容易'],
            DifficultyLevel.MEDIUM: ['中等', '一般', '适中'],
            DifficultyLevel.HARD: ['困难', '复杂', '挑战'],
            DifficultyLevel.ADVANCED: ['压轴', '高级', '综合']
        }

        for level, keywords in difficulty_keywords.items():
            if any(keyword in text for keyword in keywords):
                return level

        return DifficultyLevel.MEDIUM

    def _chinese_to_number(self, text: str) -> int:
        """中文数字转换为阿拉伯数字"""
        chinese_numbers = {
            '一': 1, '二': 2, '三': 3, '四': 4, '五': 5,
            '六': 6, '七': 7, '八': 8, '九': 9, '十': 10,
            '1': 1, '2': 2, '3': 3, '4': 4, '5': 5,
            '6': 6, '7': 7, '8': 8, '9': 9, '10': 10
        }
        return chinese_numbers.get(text, 1)

    def _create_default_question_analysis(self) -> QuestionAnalysis:
        """创建默认的题目分析"""
        return QuestionAnalysis(
            background="暂无背景信息",
            difficulty=DifficultyLevel.MEDIUM,
            objectives=AnalysisObjective(
                knowledge_points=[],
                skill_requirements=[],
                thinking_methods=[]
            ),
            difficulty_analysis=DifficultyAnalysis(
                difficult_points=[],
                common_errors=[],
                solving_strategies=[]
            ),
            overall_approach="根据题目要求逐步求解"
        )

# 使用示例
if __name__ == "__main__":
    converter = MarkdownToJSONConverter()

    # 示例使用
    sample_question = "求 |x-2| + |x+3| 的最小值"
    sample_markdown = """
## 题目分析
### 题目背景
这是一个绝对值最值问题
### 考查意图
考查学生对绝对值几何意义的理解
### 难点解析
需要理解绝对值的几何意义

## 各问分析
### 第一问分析
**考点识别：**
- 绝对值的几何意义
- 数轴上的距离

**需要掌握的知识点：**
- |x-a|表示x到a的距离
- 距离的最小值求解

**解题思路与步骤：**
1. 将绝对值转化为几何意义
2. 在数轴上表示距离
3. 分析最值情况

## 解题建议
1. 理解绝对值的几何意义
2. 通过数轴辅助理解
    """

    result = converter.convert_analysis_to_json(sample_question, sample_markdown)
    print(json.dumps(result.structured_result.dict(), indent=2, ensure_ascii=False))