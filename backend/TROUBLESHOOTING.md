# SimpleChatService AttributeError 解决方案

## 问题描述
在Jupyter Notebook中使用 `SimpleChatService` 时遇到 `AttributeError: 'SimpleChatService' object has no attribute 'analyze_math_problem'`

## 原因分析
这个错误通常是因为Jupyter Notebook中加载的模块版本不是最新的，没有包含 `analyze_math_problem` 方法。

## 解决方案

### 方案1: 重新加载模块（推荐）

在Jupyter Notebook中运行以下代码：

```python
# 重新导入模块
import importlib
import sys
from services.simple_chat_service import SimpleChatService

# 如果已经导入过，重新加载
if 'services.simple_chat_service' in sys.modules:
    importlib.reload(sys.modules['services.simple_chat_service'])

# 重新创建实例
agent = SimpleChatService()

# 现在可以正常使用
result = agent.analyze_math_problem(your_problem_text)
```

### 方案2: 重启Jupyter Kernel

1. 在Jupyter Notebook中，选择 Kernel → Restart
2. 重新运行所有的导入代码
3. 创建新的SimpleChatService实例

### 方案3: 直接通过API调用

如果本地调用仍有问题，可以通过API接口使用：

```python
import requests

def analyze_via_api(question):
    url = "http://localhost:8003/api/analyze/problem"
    response = requests.post(url, json={"question": question})
    return response.json()

# 使用
result = analyze_via_api(your_problem_text)
print(result['analysis'])
```

### 方案4: 完整的Jupyter代码示例

```python
# 设置路径
import sys
import os
sys.path.append('d:/code/MathTutor-Agent/backend')  # 修改为你的实际路径

# 重新加载模块
import importlib
if 'services.simple_chat_service' in sys.modules:
    importlib.reload(sys.modules['services.simple_chat_service'])

from services.simple_chat_service import SimpleChatService

# 创建实例
agent = SimpleChatService()

# 测试
test_problem = """你的数学题目"""
result = agent.analyze_math_problem(test_problem)

# 显示结果
print(result['analysis'])
```

## 验证方法

运行以下代码验证方法是否可用：

```python
# 检查方法是否存在
agent = SimpleChatService()
print(hasattr(agent, 'analyze_math_problem'))  # 应该返回 True

# 检查方法签名
import inspect
print(inspect.signature(agent.analyze_math_problem))
```

## 常见问题

1. **路径问题**：确保 `sys.path` 包含正确的工作目录
2. **模块缓存**：Jupyter会缓存已导入的模块，需要使用 `importlib.reload()`
3. **版本问题**：确保使用的是最新的代码版本
4. **服务器状态**：如果使用API，确保后端服务器正在运行

## 技术细节

`analyze_math_problem` 方法是最近添加的功能，具有以下特点：
- 接受一个字符串参数（数学题目）
- 返回包含分析结果的字典
- 自动匹配知识点
- 使用自定义提示词模板
- 通过DeepSeek API生成分析