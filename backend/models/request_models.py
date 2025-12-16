from pydantic import BaseModel
from typing import List, Dict, Optional, Any


class ChatRequest(BaseModel):
    """聊天请求模型"""
    message: str
    history: List[Dict[str, str]] = []
    session_id: Optional[str] = None  # 会话ID，用于Agent状态管理
    use_agent: bool = False  # 是否使用数学私教Agent


class ChatResponse(BaseModel):
    """聊天响应模型"""
    response: str
    history: List[Dict[str, str]]
    session_id: Optional[str] = None  # 返回会话ID
    suggestions: List[str] = []  # 学习建议
    knowledge_info: Optional[List[Dict[str, Any]]] = None  # 相关知识点信息


class SessionSummaryRequest(BaseModel):
    """会话总结请求模型"""
    session_id: str


class SessionSummaryResponse(BaseModel):
    """会话总结响应模型"""
    session_id: str
    duration: str
    question_count: int
    topics_covered: List[str]
    learning_progress: str
    recommendations: List[str]