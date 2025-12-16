# -*- coding: utf-8 -*-
"""
测试API聊天功能
"""
import requests
import json
import sys
import io

# 设置标准输出编码为UTF-8
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def test_chat_api():
    """测试聊天API"""
    base_url = "http://localhost:8003"

    print("=== 测试MathTutor聊天API ===\n")

    # 测试健康检查
    print("1. 健康检查...")
    try:
        response = requests.get(f"{base_url}/health")
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.json()}")
    except Exception as e:
        print(f"健康检查失败: {e}")
        return False

    # 测试服务信息
    print("\n2. 获取服务信息...")
    try:
        response = requests.get(f"{base_url}/api/service/info")
        print(f"状态码: {response.status_code}")
        print(f"服务信息: {response.json()}")
    except Exception as e:
        print(f"获取服务信息失败: {e}")

    # 测试对话功能
    print("\n3. 测试对话功能...")

    test_messages = [
        "你好，我想学习一元二次方程",
        "你能给我举一个具体的例子吗？",
        "如果x² + 5x + 6 = 0，那x等于多少？"
    ]

    history = []

    for i, message in enumerate(test_messages, 1):
        print(f"\n--- 对话轮次 {i} ---")
        print(f"用户: {message}")

        try:
            # 发送聊天请求
            chat_request = {
                "message": message,
                "history": history,
                "session_id": "test_session"
            }

            response = requests.post(
                f"{base_url}/api/chat",
                json=chat_request,
                headers={"Content-Type": "application/json"}
            )

            if response.status_code == 200:
                chat_response = response.json()
                print(f"助教: {chat_response['response']}")
                print(f"建议: {chat_response.get('suggestions', [])}")

                # 更新历史记录
                history = chat_response['history']

            else:
                print(f"请求失败，状态码: {response.status_code}")
                print(f"错误信息: {response.text}")

        except Exception as e:
            print(f"对话请求失败: {e}")

    print("\n=== 测试完成 ===")
    return True

if __name__ == "__main__":
    test_chat_api()