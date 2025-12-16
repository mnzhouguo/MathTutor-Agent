"""
启动后端服务的脚本
"""

import subprocess
import sys
import os

def start_backend():
    """启动 FastAPI 后端"""
    print("正在启动 FastAPI 后端...")
    print(f"Python 路径: {sys.executable}")
    print(f"工作目录: {os.getcwd()}")

    # 确保在 backend 目录下运行
    backend_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(backend_dir)

    # 启动主应用
    subprocess.run([sys.executable, "main.py"])

if __name__ == "__main__":
    start_backend()