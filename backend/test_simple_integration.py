"""
简单的前后端集成测试
"""
import requests
import json

def test_integration():
    """测试前后端集成"""
    base_url = "http://localhost:8003"
    frontend_url = "http://localhost:3000"

    print("=== Frontend-Backend Integration Test ===\n")

    # 测试后端
    print("1. Testing backend...")
    try:
        response = requests.get(f"{base_url}/api/health")
        print(f"Backend OK: {response.json()}")
    except Exception as e:
        print(f"Backend failed: {e}")
        return False

    # 测试前端
    print("\n2. Testing frontend...")
    try:
        response = requests.get(frontend_url)
        print(f"Frontend OK: Status {response.status_code}")
    except Exception as e:
        print(f"Frontend failed: {e}")
        return False

    # 测试对话
    print("\n3. Testing chat...")
    try:
        chat_request = {
            "message": "Hello, please introduce yourself",
            "history": [],
            "session_id": "test_session"
        }

        response = requests.post(
            f"{base_url}/api/chat",
            json=chat_request,
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            chat_response = response.json()
            print("Chat OK: Response received")
            print(f"Assistant: {chat_response['response'][:100]}...")
        else:
            print(f"Chat failed: {response.status_code}")
            return False

    except Exception as e:
        print(f"Chat test failed: {e}")
        return False

    print("\n=== Integration Test Complete ===")
    print("Success! Frontend and backend are working together.")
    print(f"Frontend URL: {frontend_url}")
    print(f"Backend URL: {base_url}")
    print("\nYou can now open the frontend URL in your browser!")

    return True

if __name__ == "__main__":
    test_integration()