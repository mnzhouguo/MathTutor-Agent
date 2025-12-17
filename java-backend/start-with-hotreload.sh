#!/bin/bash

# 带热加载功能的Spring Boot启动脚本
# 使用Spring Boot DevTools实现自动重启

echo "启动MathTutor Java后端（带热加载功能）..."
echo "DevTools已启用，当您修改Java源代码时，应用会自动重启"
echo "请访问 http://localhost:8080 测试API"
echo "按 Ctrl+C 停止应用"
echo

# 使用Maven spring-boot:run命令启动，该命令会自动使用DevTools
./mvnw spring-boot:run