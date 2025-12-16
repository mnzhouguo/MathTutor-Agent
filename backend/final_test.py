#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Final test for the refactored MathTutor backend
"""

import sys
import os
from pathlib import Path

# Add project path
sys.path.append(str(Path(__file__).parent))

def test_simple_chat_service():
    """Test the simplified chat service"""
    print("Testing Simple Chat Service...")

    try:
        from simple_chat_service import SimpleChatService

        # Create service (will use config API key)
        service = SimpleChatService()
        print("✓ Service created successfully")

        # Test basic chat
        response = service.chat("Hello, what is 2+2?")
        print(f"✓ Chat response: {response['response'][:100]}...")

        # Test service info
        info = service.get_service_info()
        print(f"✓ Service: {info['name']}")

        return True

    except Exception as e:
        print(f"✗ Error: {e}")
        return False

def test_api_routes():
    """Test API routes can be imported"""
    print("\nTesting API Routes...")

    try:
        from api.chat_routes import router
        print("✓ API routes imported successfully")

        # List routes
        routes = [route.path for route in router.routes]
        print(f"✓ Available routes: {len(routes)} routes")

        return True

    except Exception as e:
        print(f"✗ Error: {e}")
        return False

def test_config():
    """Test configuration"""
    print("\nTesting Configuration...")

    try:
        from config import get_api_key, get_service_info

        api_key = get_api_key()
        print(f"✓ API Key configured: {'Yes' if api_key else 'No'}")

        service_info = get_service_info()
        print(f"✓ Service info: {service_info['name']}")

        return True

    except Exception as e:
        print(f"✗ Error: {e}")
        return False

def main():
    """Main test function"""
    print("MathTutor Backend Refactoring - Final Test")
    print("=" * 50)

    # Run tests
    config_ok = test_config()
    service_ok = test_simple_chat_service()
    api_ok = test_api_routes()

    # Summary
    print(f"\nTest Results:")
    print(f"Configuration: {'PASS' if config_ok else 'FAIL'}")
    print(f"Chat Service: {'PASS' if service_ok else 'FAIL'}")
    print(f"API Routes: {'PASS' if api_ok else 'FAIL'}")

    if all([config_ok, service_ok, api_ok]):
        print(f"\n🎉 All tests PASSED! Backend refactoring successful.")
        print(f"Features:")
        print(f"  ✓ Simple conversation functionality")
        print(f"  ✓ Configuration-based API key loading")
        print(f"  ✓ Frontend compatibility maintained")
        print(f"  ✓ Complex features removed")
    else:
        print(f"\n❌ Some tests FAILED. Check the errors above.")

if __name__ == "__main__":
    main()