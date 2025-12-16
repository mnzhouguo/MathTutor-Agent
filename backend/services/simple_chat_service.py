"""
简化的聊天服务实现
直接使用DeepSeek API进行对话
"""

import os
import sys
from services.deepseek_service import DeepSeekClient
from services.knowledge_base import MathKnowledgeBase
from services.analysis_converter import MarkdownToJSONConverter
from models.analysis_models import AnalysisResponse



class DefaultMessage:
    """自定义消息格式，对OpenAI message进行简单封装"""

    def __init__(self, role, content, timestamp=None):
        """
        初始化消息对象

        Args:
            role (str): 消息角色 ("system", "user", "assistant")
            content (str): 消息内容
            timestamp (float, optional): 时间戳，默认为当前时间
        """
        self.role = role
        self.content = content

        self.timestamp = timestamp or self._get_current_timestamp()

    def _get_current_timestamp(self):
        """获取当前时间戳"""
        import time
        return time.time()

    def to_dict(self):
        """转换为字典格式，用于API调用"""
        return {
            "role": self.role,
            "content": self.content
        }

    def to_response_dict(self):
        """转换为响应格式，包含历史记录信息"""
        return {
            "role": self.role,
            "content": self.content,
            "timestamp": str(self.timestamp)  # 转换为字符串格式
        }

    @classmethod
    def from_dict(cls, data):
        """从字典创建DefaultMessage对象"""
        if isinstance(data, dict):
            return cls(
                role=data.get("role", "user"),
                content=data.get("content", ""),
                timestamp=data.get("timestamp")
            )
        elif isinstance(data, (list, tuple)) and len(data) >= 2:
            # 兼容旧的格式 [role, content]
            return cls(role=data[0], content=data[1])
        else:
            # 默认创建用户消息
            return cls(role="user", content=str(data))

    @classmethod
    def create_system_message(cls, content):
        """创建系统消息"""
        return cls(role="system", content=content)

    @classmethod
    def create_user_message(cls, content):
        """创建用户消息"""
        return cls(role="user", content=content)

    @classmethod
    def create_assistant_message(cls, content):
        """创建助手消息"""
        return cls(role="assistant", content=content)

    def __str__(self):
        """字符串表示"""
        # 避免使用emoji以防止编码问题
        role_prefix = {
            "system": "[系统]",
            "user": "[用户]",
            "assistant": "[助手]"
        }
        prefix = role_prefix.get(self.role, "[未知]")
        return f"{prefix} {self.role}: {self.content[:50]}{'...' if len(self.content) > 50 else ''}"

    def __repr__(self):
        """详细的字符串表示"""
        return f"DefaultMessage(role='{self.role}', content='{self.content[:30]}...', timestamp={self.timestamp})"

       
class SimpleChatService:
    def __init__(self):
        """初始化聊天服务"""
        self.api_key = os.getenv("DEEPSEEK_API_KEY", "sk-c22b4a521f63464fa56bfa359dc7842b")
        self.client = DeepSeekClient(api_key=self.api_key)

        # 初始化知识库
        self.knowledge_base = MathKnowledgeBase()

        # 初始化转换器
        self.converter = MarkdownToJSONConverter()

        # 使用DefaultMessage创建系统提示
        system_content = (
            "你是一位专业的数学私教老师。你的特点：1）友善耐心，善于鼓励学生；"
            "2）能够用简单易懂的方式解释数学概念；3）循序渐进地引导学生思考；"
            "4）针对初中数学知识体系。请根据学生的问题提供详细的解答和指导。"
        )
        self.system_prompt = DefaultMessage.create_system_message(system_content)

    def chat(self, message, session_id=None, history=None):
        """
        处理聊天请求，使用DefaultMessage类管理消息格式

        Args:
            message (str): 用户输入的消息
            session_id (str, optional): 会话ID
            history (list, optional): 对话历史，可以是字典或DefaultMessage对象

        Returns:
            dict: 包含回复、更新后的历史记录和会话ID的字典
        """
        try:
            # 准备消息历史列表
            message_objects = []

            # 添加系统提示（如果是新对话）
            if not history:
                message_objects.append(self.system_prompt)

            # 处理历史消息，转换为DefaultMessage对象
            if history:
                for hist_item in history:
                    if isinstance(hist_item, DefaultMessage):
                        message_objects.append(hist_item)
                    elif isinstance(hist_item, dict):
                        message_objects.append(DefaultMessage.from_dict(hist_item))
                    else:
                        # 尝试从其他格式转换
                        message_objects.append(DefaultMessage.from_dict(hist_item))

            # 创建当前用户消息
            user_message = DefaultMessage.create_user_message(message)
            message_objects.append(user_message)

            # 转换为API需要的格式
            api_messages = [msg.to_dict() for msg in message_objects]

            # 调用DeepSeek API
            response = self.client.chat(api_messages)

            # 创建助手回复消息
            assistant_message = DefaultMessage.create_assistant_message(response.content)
            message_objects.append(assistant_message)

            # 生成返回的历史记录（排除系统消息）
            response_history = []
            for msg in message_objects:
                if msg.role != "system":  # 不返回系统消息
                    response_history.append(msg.to_response_dict())

            # 返回结果
            return {
                "response": response.content,
                "history": response_history,
                "session_id": session_id or "default_session"
            }

        except Exception as e:
            print(f"Chat service error: {e}")
            import traceback
            traceback.print_exc()

            # 返回错误响应
            return {
                "response": "抱歉，我现在遇到了一些技术问题。请稍后再试，或者换一种方式提问。",
                "history": self._convert_history_to_response_format(history) if history else [],
                "session_id": session_id
            }

    def _convert_history_to_response_format(self, history):
        """将历史记录转换为响应格式"""
        if not history:
            return []

        response_history = []
        for item in history:
            if isinstance(item, DefaultMessage):
                if item.role != "system":
                    response_history.append(item.to_response_dict())
            elif isinstance(item, dict):
                role = item.get("role", "user")
                if role != "system":
                    response_history.append({
                        "role": role,
                        "content": item.get("content", ""),
                        "timestamp": item.get("timestamp")
                    })
        return response_history

    def analyze_math_problem(self, question):
        """
        分析数学压轴题，使用自定义提示词模板

        Args:
            question (str): 数学题目

        Returns:
            dict: 包含分析结果的字典
        """
        try:
     
            # 构建自定义提示词
            custom_prompt = f"""
你是一名初中数学教学专家，擅长分析压轴题的命题结构与解题思路。请针对用户提供的数学压轴题，完成以下任务：

1. **题目分析**：整体解读题目背景、难点、易错点、解决思路、考查意图。
    - **题目背景**：请说明题目的背景、题目的难易程度。
    - **考查意图**：分析题目考查的核心知识点与能力要求。
    - **难点解析**：指出题目中可能存在的难点与易错点，并给出相应的解题建议。
    
2. **分问解析**：对每一小问独立分析，包括：
   - **考点识别**：明确该问对应的核心考点，并说明属于哪个知识模块（如函数、几何、代数综合等）。
   - **知识点梳理**：列出解决该问必须掌握的概念、定理、公式、方法，并适当说明它们在该题中的应用方式。
   - **解题方案**：提供清晰的解题思路与步骤，包括关键推理环节、可能用到的转化策略或辅助线作法等，**不给出具体数值结果或最终答案**。

3. **解题建议**：总结解题过程中应注意的事项与策略，帮助学生提升解题能力。


请严格按照以下格式输出：

```
## 题目分析
### 题目背景
### 考查意图
### 难点解析


## 各问分析
### 第一问分析
**考点识别：**
 - 考点1
 - 考点2


**需要掌握的知识点：**
- 知识点1
- 知识点2
……

**解题思路与步骤：**
1. 步骤一……
2. 步骤二……
……

### 第二问分析
**考点识别：**
（指明具体考点）

**需要掌握的知识点：**
- 知识点1
- 知识点2
……

**解题思路与步骤：**
1. 步骤一……
2. 步骤二……
……
（如有更多小问，继续按相同结构补充）
```

## 解题建议
1. 建议一……
2. 建议二……

**注意：** 如果你的输出中有包含数学符号，请用LaTeX格式表示。

**输入信息：**  
数学压轴题：{question}  

请开始你的分析。
---"""

            # 创建系统消息
            system_message = DefaultMessage.create_system_message(custom_prompt)

            # 准备API调用
            messages = [system_message.to_dict()]

            # 调用DeepSeek API
            response = self.client.chat(messages)

            return {
                "analysis": response.content,
                "question": question,
                "status": "success"
            }

        except Exception as e:
            print(f"数学压轴题分析错误: {e}")
            return {
                "analysis": "抱歉，分析过程中遇到了技术问题，请稍后再试。",
                "question": question,
                "knowledge_info": None,
                "status": "error",
                "error": str(e)
            }

    def analyze_math_problem_json(self, question):
        """
        分析数学压轴题并返回JSON格式的结构化结果

        Args:
            question (str): 数学题目

        Returns:
            AnalysisResponse: 包含结构化JSON格式的分析结果
        """
        try:
            # 首先调用原来的分析方法获取Markdown格式结果
            basic_result = self.analyze_math_problem(question)

            if basic_result['status'] != 'success':
                return AnalysisResponse(
                    status="error",
                    analysis_id=basic_result.get('analysis_id', ''),
                    raw_text=basic_result.get('analysis', ''),
                    error=basic_result.get('error', 'Unknown error')
                )

            # 使用转换器将Markdown转换为JSON
            json_response = self.converter.convert_analysis_to_json(
                question=basic_result['question'],
                markdown_text=basic_result['analysis']
            )

            return json_response

        except Exception as e:
            print(f"数学压轴题JSON分析错误: {e}")
            import traceback
            traceback.print_exc()

            return AnalysisResponse(
                status="error",
                analysis_id="",
                raw_text="",
                error=f"JSON转换失败: {str(e)}"
            )

    def get_service_info(self):
        """获取服务信息"""
        return {
            "name": "MathTutor Simple Chat Service",
            "version": "2.1.0",
            "description": "基于DeepSeek API的简化数学私教聊天服务，使用DefaultMessage类管理消息格式，支持数学压轴题分析",
            "features": [
                "数学问题解答",
                "概念解释",
                "步骤指导",
                "对话历史管理",
                "消息格式标准化",
                "数学压轴题分析",
                "知识点智能匹配"
            ],
            "model": "deepseek-chat",
            "status": "active",
            "message_class": "DefaultMessage",
            "knowledge_base": "MathKnowledgeBase"
        }

# 全局服务实例
_service_instance = None

def get_chat_service():
    """获取聊天服务实例"""
    global _service_instance
    if _service_instance is None:
        _service_instance = SimpleChatService()
    return _service_instance


# 示例用法
if __name__ == "__main__":
    # 初始化服务
    chat_service = SimpleChatService()

    # 测试题目
    test_problem = """25.(10分)如图,数轴上点A表示的数为a,点B表示的数为b.满足 |a+5|+(b-8)^2=0 ,机器人M从点A出发,以每秒4个单位长度的速度向右运动,1秒后,机器人N从点B出发,以每
    秒2个单位长度的速度向左运动.根据机器人程序设定,机器人 M遇到机器人N后立即降速,以原速的一半返回,与此同时,机器人N以原速折返.设机器人 M运动时间为t秒.

    (1)点A与点B之间的距离是_:
    (2)求两个机器人 M、 N相遇的时间t及相遇点P所表示的数:
    (3)两个机器人在相遇点P折返后,是否存在某一时刻,使得机器人M到点A的距离与机器人N到点B的距离之和为10?若存在,求出此时 t的值及机器人N所在位置表示的数:若不存在,请说明理由."""

    json=chat_service.analyze_math_problem_json(test_problem)
    print

