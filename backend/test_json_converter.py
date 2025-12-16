# -*- coding: utf-8 -*-
"""
测试数学压轴题分析JSON转换功能
"""
import sys
import io
import json
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from services.simple_chat_service import SimpleChatService
from services.analysis_converter import MarkdownToJSONConverter

def test_json_conversion():
    """测试JSON转换功能"""
    print("=== 测试数学压轴题JSON转换功能 ===\n")

    # 创建服务实例
    try:
        agent = SimpleChatService()
        converter = MarkdownToJSONConverter()
        print("✅ 服务实例创建成功")
    except Exception as e:
        print(f"❌ 创建实例失败: {e}")
        return False

    # 测试题目
    test_problem = """求 |x-1| + |x+2| 的最小值"""

    print(f"📝 测试题目: {test_problem}")

    # 1. 首先测试直接JSON分析
    print("\n🔍 测试JSON分析方法...")
    try:
        result = agent.analyze_math_problem_json(test_problem)
        print(f"✅ JSON分析状态: {result.status}")

        if result.status == 'success' and result.structured_result:
            structured = result.structured_result
            print(f"📊 分析ID: {structured.analysis_id}")
            print(f"⏱️ 处理时间: {result.processing_time:.2f}秒")
            print(f"📈 小问总数: {structured.total_subquestions}")
            print(f"📚 知识点总数: {structured.total_knowledge_points}")
            print(f"🔧 解题步骤总数: {structured.total_solution_steps}")

            # 显示题目分析
            qa = structured.question_analysis
            print(f"\n📋 题目背景: {qa.background[:100]}...")

            # 显示小问分析
            if structured.sub_questions:
                for i, sq in enumerate(structured.sub_questions, 1):
                    print(f"\n📌 第{i}问:")
                    print(f"   考点: {', '.join(sq.key_points[:3])}")
                    print(f"   知识点数: {len(sq.knowledge_points)}")
                    print(f"   解题步骤数: {len(sq.solution_steps)}")

        else:
            print(f"❌ JSON分析失败: {result.error}")

    except Exception as e:
        print(f"❌ JSON分析测试失败: {e}")
        import traceback
        traceback.print_exc()

    # 2. 测试单独的转换器
    print("\n🔍 测试单独的Markdown转换器...")
    try:
        sample_markdown = """## 题目分析
### 题目背景
这是一个绝对值最值问题
### 考查意图
考查学生对绝对值几何意义的理解
### 难点解析
需要理解绝对值的几何意义

## 各问分析
### 第一问分析
**考点识别：**
- 绝对值的几何意义
- 数轴上的距离

**需要掌握的知识点：**
- |x-a|表示x到a的距离
- 距离的最小值求解

**解题思路与步骤：**
1. 将绝对值转化为几何意义
2. 在数轴上表示距离
3. 分析最值情况

## 解题建议
1. 理解绝对值的几何意义
2. 通过数轴辅助理解"""

        conversion_result = converter.convert_analysis_to_json(test_problem, sample_markdown)
        print(f"✅ 转换状态: {conversion_result.status}")

        if conversion_result.status == 'success':
            print(f"📊 转换处理时间: {conversion_result.processing_time:.2f}秒")

    except Exception as e:
        print(f"❌ 转换器测试失败: {e}")

    print("\n=== 测试完成 ===")
    return True

def test_api_json_endpoint():
    """测试API JSON端点"""
    import requests

    base_url = "http://localhost:8003"

    print("=== 测试API JSON端点 ===\n")

    test_problem = "求|x-2| + |x+3|的最小值"

    try:
        response = requests.post(
            f"{base_url}/api/analyze/problem/json",
            json={"question": test_problem},
            headers={"Content-Type": "application/json"}
        )

        if response.status_code == 200:
            result = response.json()
            print(f"✅ API调用成功")
            print(f"📊 状态: {result.get('status')}")
            print(f"📈 分析ID: {result.get('analysis_id')}")
            print(f"⏱️ 处理时间: {result.get('processing_time', 0):.2f}秒")

            if result.get('structured_result'):
                structured = result['structured_result']
                print(f"📚 总知识点数: {structured.get('total_knowledge_points')}")
                print(f"🔧 总步骤数: {structured.get('total_solution_steps')}")

        else:
            print(f"❌ API调用失败: {response.status_code}")
            print(f"错误信息: {response.text}")

    except Exception as e:
        print(f"❌ API测试失败: {e}")

if __name__ == "__main__":
    test_json_conversion()
    test_api_json_endpoint()