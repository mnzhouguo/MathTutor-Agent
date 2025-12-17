@echo off
echo ========================================
echo MathTutor Java Backend - VS Code Launch
echo ========================================
echo.

REM 设置环境变量
set DEEPSEEK_API_KEY=sk-c22b4a521f63464fa56bfa359dc7842b
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java版本:
java -version
echo.

echo 检查Maven...
mvn --version
if %ERRORLEVEL% NEQ 0 (
    echo Maven未安装，尝试使用其他方式...
    goto :MANUAL_COMPILE
)

echo.
echo 使用Maven启动Spring Boot应用...
echo 服务将在 http://localhost:8080 启动
echo.

mvn spring-boot:run
goto :END

:MANUAL_COMPILE
echo.
echo 尝试手动编译和运行...
echo.

REM 创建目录
if not exist target mkdir target
if not exist target\classes mkdir target\classes

echo 编译Java代码...
javac -cp ".;lib\*" -d target\classes src\main\java\com\mathtutor\*.java src\main\java\com\mathtutor\*\*.java src\main\java\com\mathtutor\*\*\*.java 2>nul

if %ERRORLEVEL% EQU 0 (
    echo 编译成功！启动应用...
    java -cp "target\classes;lib\*" com.mathtutor.MathTutorApplication
) else (
    echo 编译失败，请检查Java环境和依赖
    echo.
    echo 建议解决方案：
    echo 1. 安装Maven: https://maven.apache.org/download.cgi
    echo 2. 使用IntelliJ IDEA打开项目
    echo 3. 检查Java版本是否为17+
)

:END
echo.
pause