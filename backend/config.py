"""
Simple configuration for MathTutor backend
"""

import os
from typing import Optional

def get_api_key() -> str:
    """Get DeepSeek API key from environment variables"""
    api_key = os.getenv("DEEPSEEK_API_KEY")
    if not api_key:
        # Fallback to hardcoded key for testing
        api_key = "sk-c22b4a521f63464fa56bfa359dc7842b"

    return api_key

def get_service_info() -> dict:
    """Get service information"""
    return {
        "name": "数学学习助手",
        "description": "简单的数学问题解答和学习支持",
        "version": "2.0",
        "features": [
            "数学问题解答",
            "基础学习指导",
            "友善对话交流"
        ]
    }