@echo off
echo 正在启动MathTutor Java后端服务...
echo.

REM 设置Java路径
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM 检查Java
java -version
if %ERRORLEVEL% NEQ 0 (
    echo 错误: Java未安装或路径不正确
    echo 请安装Java 17或更新版本
    pause
    exit /b 1
)

REM 设置环境变量
set DEEPSEEK_API_KEY=sk-c22b4a521f63464fa56bfa359dc7842b

echo 检查Maven...
mvn --version
if %ERRORLEVEL% NEQ 0 (
    echo 警告: Maven未安装
    echo 请通过以下方式之一启动服务:
    echo 1. 安装Maven: https://maven.apache.org/download.cgi
    echo 2. 使用IDE（如IntelliJ IDEA或Eclipse）打开项目
    echo 3. 使用Docker: docker-compose up
    echo.
    echo 如果已安装Maven，请确保它在PATH中
    pause
    exit /b 1
)

REM 启动服务
echo 正在使用Maven启动服务...
mvn spring-boot:run

pause