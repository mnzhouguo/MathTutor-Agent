# MathTutor Backend

数学导师对话系统的后端服务，使用 FastAPI 框架构建。

## 项目结构

```
backend/
├── api/                    # API 路由层
│   ├── __init__.py
│   └── chat_routes.py     # 聊天相关路由
├── models/                 # 数据模型
│   ├── __init__.py
│   └── request_models.py  # 请求和响应模型
├── services/               # 业务逻辑层
│   ├── __init__.py
│   └── deepseek_service.py # DeepSeek API 服务
├── utils/                  # 工具函数
│   └── __init__.py
├── main.py                 # 应用入口
├── start_backend.py        # 启动脚本
├── requirements.txt        # 依赖包
└── README.md              # 说明文档
```

## 功能特点

- **模块化设计**: 清晰的分层架构
- **API路由**: RESTful API 设计
- **数据验证**: 使用 Pydantic 进行请求验证
- **CORS支持**: 跨域资源共享配置
- **健康检查**: 提供服务状态检查端点

## API 端点

### 1. 健康检查
```
GET /health
```
返回服务健康状态。

### 2. API根路径
```
GET /api/
```
返回API基本信息。

### 3. 聊天接口
```
POST /api/chat
```
发送聊天消息，接收AI回复。

**请求体**:
```json
{
  "message": "用户消息",
  "history": [
    {"role": "user", "content": "历史消息1"},
    {"role": "assistant", "content": "历史回复1"}
  ]
}
```

**响应体**:
```json
{
  "response": "AI回复",
  "history": [
    {"role": "user", "content": "历史消息1"},
    {"role": "assistant", "content": "历史回复1"},
    {"role": "user", "content": "用户消息"},
    {"role": "assistant", "content": "AI回复"}
  ]
}
```

## 安装和运行

### 1. 安装依赖
```bash
cd backend
pip install -r requirements.txt
```

### 2. 运行服务
```bash
# 方法1: 使用主程序
python main.py

# 方法2: 使用启动脚本
python start_backend.py
```

服务将在 http://localhost:8003 启动。

### 3. API文档
启动服务后，可以访问以下地址查看API文档：
- Swagger UI: http://localhost:8003/docs
- ReDoc: http://localhost:8003/redoc

## 环境变量

可以通过环境变量配置 DeepSeek API Key：
```bash
export DEEPSEEK_API_KEY="your-api-key"
```

## 开发说明

1. **添加新的API路由**: 在 `api/` 目录下创建新的路由文件
2. **数据模型**: 在 `models/` 目录下定义新的请求和响应模型
3. **业务逻辑**: 在 `services/` 目录下实现业务逻辑
4. **工具函数**: 在 `utils/` 目录下添加通用工具函数

## 部署建议

1. 生产环境中应设置具体的 CORS 允许域名
2. 使用环境变量管理敏感信息
3. 添加日志记录和错误处理
4. 考虑使用 Docker 容器化部署