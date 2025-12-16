# -*- coding: utf-8 -*-
"""
测试前端与后端集成
"""
import requests
import json
import time

def test_frontend_backend_integration():
    """测试前端与后端集成"""
    base_url = "http://localhost:8003"
    frontend_url = "http://localhost:3000"

    print("=== 测试前端与后端集成 ===\n")

    # 测试后端状态
    print("1. 检查后端状态...")
    try:
        response = requests.get(f"{base_url}/api/health")
        print(f"✅ 后端运行正常: {response.json()}")
    except Exception as e:
        print(f"❌ 后端连接失败: {e}")
        return False

    # 测试前端状态
    print("\n2. 检查前端状态...")
    try:
        response = requests.get(frontend_url)
        if response.status_code == 200:
            print("✅ 前端运行正常")
        else:
            print(f"❌ 前端状态异常: {response.status_code}")
    except Exception as e:
        print(f"❌ 前端连接失败: {e}")
        return False

    # 测试API连接
    print("\n3. 测试API连接...")
    try:
        response = requests.get(f"{base_url}/api/agent/info")
        agent_info = response.json()
        print(f"✅ Agent信息获取成功: {agent_info.get('name', 'Unknown')}")
    except Exception as e:
        print(f"❌ API连接失败: {e}")
        return False

    # 测试对话功能（模拟前端请求）
    print("\n4. 测试对话功能...")
    try:
        chat_request = {
            "message": "你好，请介绍一下你自己",
            "history": [],
            "session_id": "frontend_test"
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=chat_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            chat_response = response.json()
            print(f"✅ 对话功能正常")
            print(f"   回复: {chat_response['response'][:100]}...")
        else:
            print(f"❌ 对话功能异常: {response.status_code}")
            return False

    except Exception as e:
        print(f"❌ 对话测试失败: {e}")
        return False

    print("\n=== 集成测试完成 ===")
    print("🎉 前端与后端集成正常！")
    print(f"📍 前端地址: {frontend_url}")
    print(f"📍 后端地址: {base_url}")
    print("\n你现在可以在浏览器中打开前端地址来使用MathTutor应用！")

    return True

if __name__ == "__main__":
    test_frontend_backend_integration()