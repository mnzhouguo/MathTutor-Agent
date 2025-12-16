"""
测试SimpleChatService的analyze_math_problem方法
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from services.simple_chat_service import SimpleChatService

def test_analyze_method():
    """测试analyze_math_problem方法"""
    print("=== 测试SimpleChatService.analyze_math_problem ===\n")

    # 创建服务实例
    try:
        agent = SimpleChatService()
        print("✅ SimpleChatService实例创建成功")

        # 检查方法是否存在
        if hasattr(agent, 'analyze_math_problem'):
            print("✅ analyze_math_problem方法存在")
        else:
            print("❌ analyze_math_problem方法不存在")
            return False

    except Exception as e:
        print(f"❌ 创建SimpleChatService实例失败: {e}")
        return False

    # 测试题目
    test_problem = """25.(10分)如图,数轴上点A表示的数为a,点B表示的数为b.满足 |a+5|+(b-8)^2=0 ,机器人M从点A出发,以每秒4个单位长度的速度向右运动,1秒后,机器人N从点B出发,以每
 秒2个单位长度的速度向左运动.根据机器人程序设定,机器人 M遇到机器人N后立即降速,以原速的一半返回,与此同时,机器人N以原速折返.设机器人 M运动时间为t秒.

(1)点A与点B之间的距离是_:
(2)求两个机器人 M、 N相遇的时间t及相遇点P所表示的数:
(3)两个机器人在相遇点P折返后,是否存在某一时刻,使得机器人M到点A的距离与机器人N到点B的距离之和为10?若存在,求出此时 t的值及机器人N所在位置表示的数:若不存在,请说明理由."""

    print(f"\n📝 测试题目: {test_problem[:100]}...")

    # 调用分析方法
    try:
        print("\n🔍 开始分析...")
        result = agent.analyze_math_problem(test_problem)

        print(f"✅ 分析状态: {result['status']}")

        if result['status'] == 'success':
            print(f"📊 题目: {result['question']}")
            print(f"\n📋 分析结果:")
            print(result['analysis'])

            if result['knowledge_info']:
                print(f"\n🎯 匹配的知识点:")
                for info in result['knowledge_info']:
                    print(f"  - {info.get('module', '')}: {info.get('description', '')}")
        else:
            print(f"❌ 分析失败: {result.get('error')}")

    except Exception as e:
        print(f"❌ 调用分析方法失败: {e}")
        import traceback
        traceback.print_exc()
        return False

    print("\n=== 测试完成 ===")
    return True

if __name__ == "__main__":
    test_analyze_method()