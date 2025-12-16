"""
测试数学压轴题分析功能
"""
import requests
import json
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def test_math_problem_analysis():
    """测试数学压轴题分析"""
    base_url = "http://localhost:8003"

    print("=== 测试数学压轴题分析功能 ===\n")

    # 测试题目
    test_problems = [
        {
            "name": "绝对值最值问题",
            "question": "求 |x-2| + |x+3| 的最小值"
        },
        {
            "name": "数轴动点问题",
            "question": "数轴上点A在原点左侧2个单位，点B在原点右侧3个单位，动点P从A出发以每秒2个单位的速度向右运动，几秒后P到A的距离是P到B距离的2倍？"
        },
        {
            "name": "整式化简求值",
            "question": "已知x²+3x=5，求x⁴+6x³+7x²-12x-15的值"
        }
    ]

    for i, problem in enumerate(test_problems, 1):
        print(f"测试 {i}: {problem['name']}")
        print(f"题目: {problem['question']}")
        print("-" * 60)

        try:
            # 发送分析请求
            analysis_request = {
                "question": problem['question']
            }

            response = requests.post(
                f"{base_url}/api/analyze/problem",
                json=analysis_request,
                headers={"Content-Type": "application/json"}
            )

            if response.status_code == 200:
                result = response.json()
                print(f"分析状态: {result.get('status')}")

                if result.get('status') == 'success':
                    print(f"分析结果:")
                    print(result['analysis'])

                    # 显示匹配的知识点信息
                    knowledge_info = result.get('knowledge_info')
                    if knowledge_info:
                        print(f"\n匹配的知识点:")
                        for info in knowledge_info:
                            print(f"  - 模块: {info.get('module')}")
                            print(f"    描述: {info.get('description')}")
                else:
                    print(f"分析失败: {result.get('error')}")
            else:
                print(f"请求失败: {response.status_code}")
                print(f"错误信息: {response.text}")

        except Exception as e:
            print(f"测试错误: {e}")

        print("\n" + "="*80 + "\n")

if __name__ == "__main__":
    test_math_problem_analysis()