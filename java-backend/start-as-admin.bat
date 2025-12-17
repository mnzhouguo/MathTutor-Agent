@echo off
echo ========================================
echo MathTutor Java后端启动脚本（80端口）
echo ========================================
echo.
echo 重要提示：
echo 1. 80端口是HTTP标准端口，需要管理员权限
echo 2. 如果80端口被其他服务占用（如IIS），请先停止该服务
echo 3. 确保没有其他Web服务器运行在80端口
echo.
echo 正在检查管理员权限...
net session >nul 2>&1
if %errorLevel% == 0 (
    echo [√] 检测到管理员权限，正在启动应用...
    echo.
    echo 启动MathTutor后端服务（端口80）...
    echo 访问地址: http://localhost
    echo 按 Ctrl+C 停止服务
    echo.
    call mvnw.cmd spring-boot:run
) else (
    echo [×] 未检测到管理员权限！
    echo.
    echo 80端口需要管理员权限，请：
    echo 1. 右键点击此文件
    echo 2. 选择"以管理员身份运行"
    echo 3. 或者在管理员命令提示符中运行此脚本
    echo.
    echo 按任意键退出...
    pause >nul
)