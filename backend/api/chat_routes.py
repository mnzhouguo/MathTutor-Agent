"""
Simplified chat routes for MathTutor API
Refactored to provide basic conversation functionality only
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from fastapi import APIRouter, HTTPException
from models.request_models import ChatRequest, ChatResponse
from pydantic import BaseModel
from services.simple_chat_service import get_chat_service
router = APIRouter(prefix="/api", tags=["chat"])

class MathProblemRequest(BaseModel):
    """数学压轴题分析请求模型"""
    question: str

@router.get("/")
async def root():
    """API根路径"""
    return {"message": "MathTutor Chat API is running", "version": "2.0"}


@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """处理聊天请求"""
    chat_service = get_chat_service()

    # 验证消息内容
    if not request.message or not request.message.strip():
        return ChatResponse(
            response="你好！我是数学学习助手，有什么数学问题我可以帮助你的吗？",
            history=request.history,
            suggestions=["请问有什么数学问题？", "告诉我你正在学习的内容", "我可以帮你解答数学题"]
        )

    try:
        # 使用重构后的聊天服务处理
        chat_response = chat_service.chat(
            message=request.message,
            session_id=request.session_id,
            history=request.history
        )

        return ChatResponse(
            response=chat_response["response"],
            history=chat_response["history"],
            session_id=chat_response.get("session_id"),
            suggestions=["继续提问", "换一个数学问题", "总结刚才的内容"] if chat_response.get("response") else []
        )

    except Exception as e:
        # 错误处理
        print(f"Chat processing error: {e}")
        return ChatResponse(
            response="抱歉，我现在遇到了一些技术问题。请稍后再试，或者换一种方式提问。",
            history=request.history,
            suggestions=["检查网络连接", "简化你的问题", "稍后再试"]
        )


@router.get("/service/info")
async def get_service_info():
    """获取服务信息"""
    chat_service = get_chat_service()
    return chat_service.get_service_info()


@router.get("/health")
async def health_check():
    """健康检查"""
    return {
        "status": "healthy",
        "service": "MathTutor API",
        "version": "2.0",
        "features": {
            "chat": True,
            "math_help": True,
            "simple_interface": True
        }
    }


# Legacy endpoints for frontend compatibility
@router.post("/chat/agent", response_model=ChatResponse)
async def chat_with_agent(request: ChatRequest):
    """兼容性端点 - 重定向到普通聊天"""
    return await chat(request)


@router.get("/agent/info")
async def get_agent_info():
    """兼容性端点 - 获取服务信息"""
    chat_service = get_chat_service()
    info = chat_service.get_service_info()
    return {
        "name": info["name"],
        "personality": "友善、耐心、乐于助人",
        "teaching_style": "简单直接、循序渐进",
        "expertise": "初中数学",
        "description": info["description"],
        "features": info["features"]
    }


@router.post("/analyze/problem")
async def analyze_math_problem(request: MathProblemRequest):
    """分析数学压轴题"""
    chat_service = get_chat_service()

    # 验证题目内容
    if not request.question or not request.question.strip():
        raise HTTPException(status_code=400, detail="题目内容不能为空")

    try:
        # 使用数学压轴题分析功能
        analysis_result = chat_service.analyze_math_problem(request.question)

        return {
            "status": analysis_result["status"],
            "question": analysis_result["question"],
            "analysis": analysis_result["analysis"],
            "knowledge_info": analysis_result["knowledge_info"],
            "error": analysis_result.get("error")
        }

    except Exception as e:
        print(f"数学压轴题分析API错误: {e}")
        raise HTTPException(status_code=500, detail=f"分析失败: {str(e)}")


@router.post("/analyze/problem/json")
async def analyze_math_problem_json(request: MathProblemRequest):
    """分析数学压轴题并返回JSON格式的结构化结果"""
    chat_service = get_chat_service()

    # 验证题目内容
    if not request.question or not request.question.strip():
        raise HTTPException(status_code=400, detail="题目内容不能为空")

    try:
        # 使用JSON格式的数学压轴题分析功能
        analysis_result = chat_service.analyze_math_problem_json(request.question)

        # 构建响应
        response_data = {
            "status": analysis_result.status,
            "analysis_id": analysis_result.analysis_id,
            "raw_text": analysis_result.raw_text,
            "processing_time": analysis_result.processing_time,
            "error": analysis_result.error
        }

        # 如果有结构化结果，也包含在响应中
        if analysis_result.structured_result:
            response_data["structured_result"] = analysis_result.structured_result.dict()

        return response_data

    except Exception as e:
        print(f"数学压轴题JSON分析API错误: {e}")
        raise HTTPException(status_code=500, detail=f"JSON分析失败: {str(e)}")