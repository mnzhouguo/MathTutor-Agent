from openai import OpenAI


class DeepSeekClient:
    def __init__(self, api_key, base_url="https://api.deepseek.com"):
        """初始化DeepSeek客户端"""
        self.client = OpenAI(api_key=api_key, base_url=base_url)
        self.model = "deepseek-chat"

    def chat(self, messages):
        """发送聊天请求并返回响应"""
        response = self.client.chat.completions.create(
            model=self.model,
            messages=messages
        )
        return response.choices[0].message


# 示例用法
if __name__ == "__main__":
    # 初始化客户端
    deepseek = DeepSeekClient(api_key="sk-c22b4a521f63464fa56bfa359dc7842b")

    # 第一轮对话
    messages = [{"role": "user", "content": "你是谁？"}]
    response = deepseek.chat(messages)
    messages.append(response)
    
    print(f"Messages Round 1: {messages}")

    # 第二轮对话
    messages.append({"role": "user", "content": "你爸爸是谁?"})
    response = deepseek.chat(messages)
    messages.append(response)
    print(f"Messages Round 2: {messages}")