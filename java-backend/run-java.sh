#!/bin/bash

# 简化的Java应用启动脚本
echo "正在启动MathTutor Java后端服务..."

# 设置环境变量
export DEEPSEEK_API_KEY=${DEEPSEEK_API_KEY:-sk-c22b4a521f63464fa56bfa359dc7842b}

# 创建lib目录来存储依赖
mkdir -p lib

# 下载必要的依赖（简化版本）
echo "检查依赖..."

# 编译Java代码
echo "编译Java代码..."
javac -cp ".:lib/*" -d target/classes $(find src/main/java -name "*.java") 2>/dev/null || {
    echo "编译失败，可能需要下载依赖"
    echo "请安装Maven或使用IDE运行此项目"
    exit 1
}

# 运行应用
echo "启动应用..."
java -cp ".:target/classes:lib/*" com.mathtutor.MathTutorApplication