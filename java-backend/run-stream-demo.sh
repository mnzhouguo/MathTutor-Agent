#!/bin/bash

echo "🚀 启动流式响应演示"
echo "=========================="

echo "正在启动应用，请稍候..."

# 启动应用并运行流式演示
./mvnw spring-boot:run -Dspring-boot.run.arguments="--demo.stream=true"