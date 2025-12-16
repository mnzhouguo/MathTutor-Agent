"""
测试多轮对话功能
"""
import requests
import json

def test_multi_turn_conversation():
    """测试多轮对话"""
    base_url = "http://localhost:8003"

    print("=== 测试多轮对话 ===\n")

    session_id = "multi_turn_test"
    history = []

    questions = [
        "什么是勾股定理？",
        "能给我一个具体的例子吗？",
        "如果直角边是3和4，斜边是多少？"
    ]

    for i, question in enumerate(questions, 1):
        print(f"第{i}轮对话:")
        print(f"问题: {question}")

        chat_request = {
            "message": question,
            "history": history,
            "session_id": session_id
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=chat_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            result = response.json()
            print(f"回复: {result['response'][:100]}...")
            print(f"历史记录数: {len(result['history'])}")

            # 更新历史记录
            history = result['history']

        else:
            print(f"错误: {response.status_code}")
            break

        print("-" * 50)

    print(f"\n总共进行了 {len(history)} 条消息的对话")
    return True

if __name__ == "__main__":
    test_multi_turn_conversation()