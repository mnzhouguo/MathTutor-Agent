"""
测试DeepSeek聊天功能的脚本
"""
import asyncio
import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from services.deepseek_service import DeepSeekClient

async def test_deepseek_conversation():
    """测试DeepSeek对话功能"""
    print("开始测试DeepSeek对话功能...")

    # 初始化客户端
    api_key = "sk-c22b4a521f63464fa56bfa359dc7842b"
    client = DeepSeekClient(api_key=api_key)

    # 模拟数学私教的对话
    messages = [
        {
            "role": "system",
            "content": "你是一位专业的数学私教老师。你的特点：1）友善耐心，善于鼓励学生；2）能够用简单易懂的方式解释数学概念；3）循序渐进地引导学生思考；4）针对初中数学知识体系。请根据学生的问题提供详细的解答和指导。"
        }
    ]

    # 第一轮对话
    print("\n=== 第一轮对话 ===")
    user_message = "你好，我想学习一元二次方程"
    messages.append({"role": "user", "content": user_message})

    response = client.chat(messages)
    messages.append(response)

    print(f"用户: {user_message}")
    print(f"助教: {response.content}")

    # 第二轮对话
    print("\n=== 第二轮对话 ===")
    user_message = "你能给我举一个具体的例子吗？"
    messages.append({"role": "user", "content": user_message})

    response = client.chat(messages)
    messages.append(response)

    print(f"用户: {user_message}")
    print(f"助教: {response.content}")

    # 第三轮对话
    print("\n=== 第三轮对话 ===")
    user_message = "如果x² + 5x + 6 = 0，那x等于多少？"
    messages.append({"role": "user", "content": user_message})

    response = client.chat(messages)
    messages.append(response)

    print(f"用户: {user_message}")
    print(f"助教: {response.content}")

    print("\n=== 测试完成 ===")
    print(f"总共进行了 {len([msg for msg in messages if msg['role'] == 'user'])} 轮对话")

if __name__ == "__main__":
    asyncio.run(test_deepseek_conversation())