# -*- coding: utf-8 -*-
"""
测试重构后的API功能
"""
import requests
import json
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def test_refactored_api():
    """测试重构后的API"""
    base_url = "http://localhost:8003"

    print("=== 测试重构后的API功能 ===\n")

    # 1. 测试服务信息更新
    print("1. 检查服务信息更新...")
    try:
        response = requests.get(f"{base_url}/api/service/info")
        service_info = response.json()
        print(f"✅ 服务版本: {service_info.get('version')}")
        print(f"✅ 消息类: {service_info.get('message_class', 'N/A')}")
        print(f"✅ 新增功能: {'消息格式标准化' in service_info.get('features', [])}")
    except Exception as e:
        print(f"❌ 服务信息获取失败: {e}")

    # 2. 测试新对话（第一次对话）
    print("\n2. 测试新对话...")
    try:
        chat_request = {
            "message": "什么是黄金分割？",
            "history": [],  # 空历史，触发新对话
            "session_id": None
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=chat_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            chat_response = response.json()
            print(f"✅ 新对话成功")
            print(f"回复长度: {len(chat_response['response'])} 字符")
            print(f"历史记录数量: {len(chat_response['history'])}")
            print(f"会话ID: {chat_response['session_id']}")

            # 保存会话信息用于后续测试
            session_id = chat_response['session_id']
            history = chat_response['history']
        else:
            print(f"❌ 新对话失败: {response.status_code}")
            return False

    except Exception as e:
        print(f"❌ 新对话测试失败: {e}")
        return False

    # 3. 测试继续对话（有历史记录）
    print("\n3. 测试继续对话...")
    try:
        followup_request = {
            "message": "能给我一个具体的例子吗？",
            "history": history,  # 使用之前的历史记录
            "session_id": session_id
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=followup_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            chat_response = response.json()
            print(f"✅ 继续对话成功")
            print(f"回复长度: {len(chat_response['response'])} 字符")
            print(f"历史记录数量: {len(chat_response['history'])}")

            # 检查历史记录是否包含时间戳
            latest_msg = chat_response['history'][-1]
            has_timestamp = 'timestamp' in latest_msg
            print(f"包含时间戳: {has_timestamp}")
        else:
            print(f"❌ 继续对话失败: {response.status_code}")

    except Exception as e:
        print(f"❌ 继续对话测试失败: {e}")

    # 4. 测试消息格式的一致性
    print("\n4. 测试消息格式一致性...")
    try:
        # 检查历史记录格式
        for i, msg in enumerate(history[-3:]):  # 检查最后3条消息
            required_fields = ['role', 'content']
            optional_fields = ['timestamp']

            missing_required = [field for field in required_fields if field not in msg]
            has_optional = any(field in msg for field in optional_fields)

            print(f"消息 {i+1}: 角色={msg.get('role')}, 字段完整={not missing_required}, 包含扩展={has_optional}")

    except Exception as e:
        print(f"❌ 格式检查失败: {e}")

    print("\n=== API测试完成 ===")
    return True

if __name__ == "__main__":
    test_refactored_api()