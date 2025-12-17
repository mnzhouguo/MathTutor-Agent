#!/bin/bash

# Java后端启动脚本

echo "正在启动MathTutor Java后端服务..."

# 检查Java版本
java -version

# 设置环境变量
export DEEPSEEK_API_KEY=${DEEPSEEK_API_KEY:-sk-c22b4a521f63464fa56bfa359dc7842b}

# 启动应用
mvn spring-boot:run