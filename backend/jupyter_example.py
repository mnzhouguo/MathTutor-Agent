"""
Jupyter Notebook中使用SimpleChatService的示例代码
"""

# 导入必要的模块
import sys
import os
sys.path.append('你的项目路径')  # 设置正确的项目路径

from services.simple_chat_service import SimpleChatService

# 创建SimpleChatService实例
agent = SimpleChatService()

# 测试题目（你在Jupyter中的题目）
test_problem = """25.(10分)如图,数轴上点A表示的数为a,点B表示的数为b.满足 |a+5|+(b-8)^2=0 ,机器人M从点A出发,以每秒4个单位长度的速度向右运动,1秒后,机器人N从点B出发,以每秒2个单位长度的速度向左运动.根据机器人程序设定,机器人 M遇到机器人N后立即降速,以原速的一半返回,与此同时,机器人N以原速折返.设机器人 M运动时间为t秒.

(1)点A与点B之间的距离是_:
(2)求两个机器人 M、 N相遇的时间t及相遇点P所表示的数:
(3)两个机器人在相遇点P折返后,是否存在某一时刻,使得机器人M到点A的距离与机器人N到点B的距离之和为10?若存在,求出此时 t的值及机器人N所在位置表示的数:若不存在,请说明理由."""

# 调用分析方法
try:
    result = agent.analyze_math_problem(test_problem)

    # 显示结果
    print("分析状态:", result['status'])
    print("题目:", result['question'])
    print("\n分析结果:")
    print(result['analysis'])

    if result['knowledge_info']:
        print("\n匹配的知识点:")
        for info in result['knowledge_info']:
            print(f"- {info.get('module', '')}: {info.get('description', '')}")

except Exception as e:
    print(f"错误: {e}")
    import traceback
    traceback.print_exc()