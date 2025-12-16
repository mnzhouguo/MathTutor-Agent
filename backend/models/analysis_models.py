"""
数学压轴题分析结果的JSON数据模型
"""

from typing import List, Dict, Any, Optional
from pydantic import BaseModel, Field
from enum import Enum

class DifficultyLevel(str, Enum):
    """难度级别枚举"""
    EASY = "easy"
    MEDIUM = "medium"
    HARD = "hard"
    ADVANCED = "advanced"

class KnowledgeModule(str, Enum):
    """知识模块枚举"""
    ALGEBRA = "algebra"
    GEOMETRY = "geometry"
    FUNCTION = "function"
    STATISTICS = "statistics"
    NUMBER_THEORY = "number_theory"
    COMPREHENSIVE = "comprehensive"
    COORDINATE_GEOMETRY = "coordinate_geometry"
    TRIGONOMETRY = "trigonometry"

class AnalysisObjective(BaseModel):
    """考查意图模型"""
    knowledge_points: List[str] = Field(description="考查的核心知识点")
    skill_requirements: List[str] = Field(description="需要的能力要求")
    thinking_methods: List[str] = Field(description="涉及的思维方法")

class DifficultyAnalysis(BaseModel):
    """难点解析模型"""
    difficult_points: List[str] = Field(description="难点列表")
    common_errors: List[str] = Field(description="易错点")
    solving_strategies: List[str] = Field(description="解题策略建议")

class QuestionAnalysis(BaseModel):
    """题目分析模型"""
    background: str = Field(description="题目背景描述")
    difficulty: DifficultyLevel = Field(description="难度级别")
    objectives: AnalysisObjective = Field(description="考查意图")
    difficulty_analysis: DifficultyAnalysis = Field(description="难点解析")
    overall_approach: str = Field(description="整体解题思路")

class KnowledgePoint(BaseModel):
    """知识点模型"""
    name: str = Field(description="知识点名称")
    description: str = Field(description="知识点描述")
    application: str = Field(description="在本题中的应用方式")
    module: KnowledgeModule = Field(description="所属知识模块")

class SolutionStep(BaseModel):
    """解题步骤模型"""
    step_number: int = Field(description="步骤序号")
    description: str = Field(description="步骤描述")
    reasoning: str = Field(description="推理过程")
    key_points: List[str] = Field(description="关键要点")

class SubQuestionAnalysis(BaseModel):
    """小问分析模型"""
    question_number: int = Field(description="小问序号")
    question_text: str = Field(description="小问内容")
    key_points: List[str] = Field(description="考点识别")
    knowledge_points: List[KnowledgePoint] = Field(description="需要掌握的知识点")
    solution_steps: List[SolutionStep] = Field(description="解题思路与步骤")
    alternative_methods: Optional[List[str]] = Field(default=None, description="可选的解法")

class GeneralSuggestion(BaseModel):
    """通用建议模型"""
    type: str = Field(description="建议类型")
    content: str = Field(description="建议内容")
    priority: int = Field(description="优先级(1-5)")

class MathProblemAnalysisResult(BaseModel):
    """数学压轴题分析结果的标准JSON格式"""
    # 基本信息
    question: str = Field(description="原始题目")
    analysis_id: str = Field(description="分析ID")
    timestamp: str = Field(description="分析时间")
    version: str = Field(default="1.0", description="格式版本")

    # 分析结果
    question_analysis: QuestionAnalysis = Field(description="题目分析")
    sub_questions: List[SubQuestionAnalysis] = Field(description="各问分析")
    general_suggestions: List[GeneralSuggestion] = Field(description="解题建议")

    # 元数据
    total_score: Optional[int] = Field(default=None, description="题目总分")
    question_type: Optional[str] = Field(default=None, description="题目类型")
    subject: str = Field(default="数学", description="学科")
    grade_level: str = Field(default="初中", description="年级")

    # 统计信息
    total_subquestions: int = Field(description="小问总数")
    total_knowledge_points: int = Field(description="总知识点数")
    total_solution_steps: int = Field(description="总解题步骤数")

    class Config:
        json_encoders = {
            # 枚举类型的JSON编码
            DifficultyLevel: lambda v: v.value,
            KnowledgeModule: lambda v: v.value,
        }

# 响应模型（用于API）
class AnalysisResponse(BaseModel):
    """分析响应模型"""
    status: str = Field(description="分析状态")
    analysis_id: str = Field(description="分析ID")
    raw_text: str = Field(description="原始文本输出")
    structured_result: Optional[MathProblemAnalysisResult] = Field(default=None, description="结构化分析结果")
    processing_time: Optional[float] = Field(default=None, description="处理时间(秒)")
    error: Optional[str] = Field(default=None, description="错误信息")

# 示例JSON结构
EXAMPLE_JSON_STRUCTURE = {
    "question": "数学题目文本",
    "analysis_id": "unique_id",
    "timestamp": "2024-01-01T12:00:00Z",
    "version": "1.0",
    "question_analysis": {
        "background": "题目背景描述",
        "difficulty": "medium",
        "objectives": {
            "knowledge_points": ["考点1", "考点2"],
            "skill_requirements": ["能力1", "能力2"],
            "thinking_methods": ["方法1", "方法2"]
        },
        "difficulty_analysis": {
            "difficult_points": ["难点1", "难点2"],
            "common_errors": ["易错点1", "易错点2"],
            "solving_strategies": ["策略1", "策略2"]
        },
        "overall_approach": "整体解题思路"
    },
    "sub_questions": [
        {
            "question_number": 1,
            "question_text": "第一问内容",
            "key_points": ["考点1", "考点2"],
            "knowledge_points": [
                {
                    "name": "知识点名称",
                    "description": "知识点描述",
                    "application": "应用方式",
                    "module": "algebra"
                }
            ],
            "solution_steps": [
                {
                    "step_number": 1,
                    "description": "步骤描述",
                    "reasoning": "推理过程",
                    "key_points": ["要点1", "要点2"]
                }
            ]
        }
    ],
    "general_suggestions": [
        {
            "type": "解题策略",
            "content": "建议内容",
            "priority": 1
        }
    ],
    "total_subquestions": 3,
    "total_knowledge_points": 8,
    "total_solution_steps": 12
}