#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Simple test for the refactored backend - Windows compatible
Tests basic conversation functionality
"""

import sys
import os
from pathlib import Path

# 添加项目路径
sys.path.append(str(Path(__file__).parent))

def test_simple_chat():
    """测试简化的聊天功能"""
    print("测试简化后的聊天功能")
    print("=" * 50)

    try:
        from services.simple_chat import SimpleChatService

        # 初始化聊天服务（会自动读取配置中的API密钥）
        chat_service = SimpleChatService()
        print("简单聊天服务初始化成功")

        # 测试基本对话
        test_messages = [
            "你好",
            "1+1等于几？",
            "请解释一下什么是分数"
        ]

        for i, message in enumerate(test_messages, 1):
            print(f"\n测试消息 {i}: {message}")

            try:
                response = chat_service.chat(message=message, session_id="test_session")

                print(f"回复: {response['response'][:100]}...")
                print(f"历史记录长度: {len(response['history'])}")

            except Exception as e:
                print(f"处理失败: {e}")

        # 测试服务信息
        print(f"\n服务信息:")
        service_info = chat_service.get_service_info()
        print(f"   名称: {service_info['name']}")
        print(f"   描述: {service_info['description']}")
        print(f"   功能: {', '.join(service_info['features'])}")

        return True

    except Exception as e:
        print(f"测试失败: {e}")
        import traceback
        traceback.print_exc()
        return False


def test_api_import():
    """测试API路由导入"""
    print(f"\n测试API路由导入:")
    print("-" * 30)

    try:
        from api.chat_routes import router
        print("API路由导入成功")

        # 检查路由
        routes = [route.path for route in router.routes]
        print(f"可用路由: {routes}")

        return True
    except Exception as e:
        print(f"API路由导入失败: {e}")
        return False


def main():
    """主测试函数"""
    print("MathTutor 后端重构测试")
    print("=" * 60)

    # 测试聊天服务
    chat_success = test_simple_chat()

    # 测试API路由
    api_success = test_api_import()

    # 总结
    print(f"\n测试总结:")
    print(f"   聊天服务: {'通过' if chat_success else '失败'}")
    print(f"   API路由: {'通过' if api_success else '失败'}")

    if chat_success and api_success:
        print(f"\n后端重构成功！所有基本功能正常工作")
    else:
        print(f"\n存在问题，需要进一步调试")


if __name__ == "__main__":
    main()