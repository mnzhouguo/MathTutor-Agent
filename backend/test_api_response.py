"""
测试API响应内容
"""
import requests
import json

def test_api_responses():
    """测试API的具体响应内容"""
    base_url = "http://localhost:8003"

    print("=== 测试API响应 ===\n")

    # 1. 测试服务信息
    print("1. 服务信息:")
    try:
        response = requests.get(f"{base_url}/api/service/info")
        info = response.json()
        print(json.dumps(info, indent=2, ensure_ascii=False))
    except Exception as e:
        print(f"错误: {e}")

    # 2. 测试简单对话
    print("\n2. 简单对话测试:")
    try:
        chat_request = {
            "message": "你好",
            "history": [],
            "session_id": "test123"
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=chat_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            result = response.json()
            print("响应结构:")
            print(f"  response: {result.get('response', 'N/A')}")
            print(f"  history length: {len(result.get('history', []))}")
            print(f"  session_id: {result.get('session_id', 'N/A')}")

            if result.get('history'):
                print("  历史记录:")
                for i, msg in enumerate(result['history']):
                    print(f"    {i+1}: {msg}")
        else:
            print(f"错误: {response.status_code} - {response.text}")

    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    test_api_responses()