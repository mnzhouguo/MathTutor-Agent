@echo off
REM Java后端启动脚本 (Windows)

echo 正在启动MathTutor Java后端服务...

REM 检查Java版本
java -version

REM 设置环境变量
set DEEPSEEK_API_KEY=sk-c22b4a521f63464fa56bfa359dc7842b

REM 启动应用
mvn spring-boot:run