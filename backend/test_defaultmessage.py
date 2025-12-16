# -*- coding: utf-8 -*-
"""
测试DefaultMessage类和重构后的SimpleChatService
"""
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

from services.simple_chat_service import DefaultMessage, SimpleChatService

def test_defaultmessage_class():
    """测试DefaultMessage类的各种功能"""
    print("=== 测试DefaultMessage类 ===\n")

    # 1. 测试基本创建
    print("1. 测试基本创建功能...")
    user_msg = DefaultMessage.create_user_message("什么是勾股定理？")
    assistant_msg = DefaultMessage.create_assistant_message("勾股定理是...")
    system_msg = DefaultMessage.create_system_message("你是一个数学老师")

    print(f"用户消息: {user_msg}")
    print(f"助手消息: {assistant_msg}")
    print(f"系统消息: {system_msg}")

    # 2. 测试to_dict方法
    print("\n2. 测试to_dict方法...")
    user_dict = user_msg.to_dict()
    print(f"转换为字典格式: {user_dict}")

    # 3. 测试from_dict方法
    print("\n3. 测试from_dict方法...")
    recreated_msg = DefaultMessage.from_dict(user_dict)
    print(f"从字典重建: {recreated_msg}")

    # 4. 测试to_response_dict方法
    print("\n4. 测试to_response_dict方法...")
    response_dict = user_msg.to_response_dict()
    print(f"响应格式: {response_dict}")

    # 5. 测试兼容性
    print("\n5. 测试兼容性...")
    old_format = ["user", "测试消息"]
    compatible_msg = DefaultMessage.from_dict(old_format)
    print(f"兼容旧格式: {compatible_msg}")

def test_refactored_service():
    """测试重构后的SimpleChatService"""
    print("\n=== 测试重构后的SimpleChatService ===\n")

    # 创建服务实例
    service = SimpleChatService()

    print("1. 测试服务信息...")
    info = service.get_service_info()
    print(f"服务信息: {info}")

    print("\n2. 测试无历史对话...")
    response1 = service.chat("你好，我想学习数学")
    print(f"回复1: {response1['response'][:100]}...")
    print(f"历史记录数量: {len(response1['history'])}")

    print("\n3. 测试有历史对话...")
    # 使用之前的历史记录
    response2 = service.chat(
        "一元二次方程怎么解？",
        session_id=response1['session_id'],
        history=response1['history']
    )
    print(f"回复2: {response2['response'][:100]}...")
    print(f"历史记录数量: {len(response2['history'])}")

    print("\n4. 测试消息格式...")
    for i, msg in enumerate(response2['history'][-4:]):  # 显示最后4条消息
        print(f"消息 {i+1}: {msg['role']} - {msg['content'][:50]}...")

def test_mixed_history_formats():
    """测试混合历史格式的兼容性"""
    print("\n=== 测试混合历史格式兼容性 ===\n")

    service = SimpleChatService()

    # 创建混合格式的历史记录
    mixed_history = [
        {"role": "user", "content": "你好", "timestamp": 1234567890},
        DefaultMessage.create_assistant_message("你好！我是数学老师"),
        ["user", "请解释勾股定理"]  # 旧格式
    ]

    print("1. 创建混合格式历史记录...")
    for i, item in enumerate(mixed_history):
        if isinstance(item, DefaultMessage):
            print(f"  项 {i+1}: DefaultMessage - {item}")
        else:
            print(f"  项 {i+1}: {type(item)} - {item}")

    print("\n2. 测试混合格式对话...")
    try:
        response = service.chat(
            "能给我举个具体的例子吗？",
            history=mixed_history
        )
        print(f"回复成功: {response['response'][:100]}...")
        print(f"最终历史记录数量: {len(response['history'])}")
    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    test_defaultmessage_class()
    test_refactored_service()
    test_mixed_history_formats()
    print("\n=== 所有测试完成 ===")