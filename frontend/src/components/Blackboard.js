/**
 * 黑板组件
 * 模拟老师的教学板书
 */

import React, { useState, useRef, useEffect } from 'react';
import './Blackboard.css';

const Blackboard = ({ agentMessages = [], currentMessage = null }) => {
  const [isDrawing, setIsDrawing] = useState(false);
  const [currentPath, setCurrentPath] = useState([]);
  const [paths, setPaths] = useState([]);
  const canvasRef = useRef(null);
  const [eraserMode, setEraserMode] = useState(false);

  // 从AI消息中提取数学内容和步骤
  const extractMathContent = (message) => {
    if (!message) return [];

    const mathSteps = [];

    // 提取公式（$...$ 或 $$...$$ 格式）
    const formulaMatches = message.match(/\$\$([^$]+)\$\$|\$([^$]+)\$/g);
    if (formulaMatches) {
      formulaMatches.forEach(formula => {
        mathSteps.push({
          type: 'formula',
          content: formula.replace(/\$/g, ''),
          timestamp: Date.now()
        });
      });
    }

    // 提取步骤（数字序号或-开头的行）
    const stepMatches = message.match(/\d+\.\s*[^.!\n]+[.!\n]|-\s*[^.!\n]+[.!\n]/g);
    if (stepMatches) {
      stepMatches.forEach((step, index) => {
        mathSteps.push({
          type: 'step',
          content: step.trim(),
          order: index + 1,
          timestamp: Date.now() + index
        });
      });
    }

    // 提取关键词（粗体标记）
    const keywordMatches = message.match(/\*\*([^*]+)\*\*/g);
    if (keywordMatches) {
      keywordMatches.forEach(keyword => {
        mathSteps.push({
          type: 'keyword',
          content: keyword.replace(/\*\*/g, ''),
          timestamp: Date.now()
        });
      });
    }

    return mathSteps.sort((a, b) => a.timestamp - b.timestamp);
  };

  const mathContent = extractMathContent(currentMessage?.content || '');
  const allMathContent = agentMessages.flatMap(msg => extractMathContent(msg.content || ''));

  // Canvas 绘图功能
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';
    ctx.lineWidth = 2;
    ctx.strokeStyle = eraserMode ? '#2c3e50' : '#ffffff';
  }, [eraserMode]);

  const startDrawing = (e) => {
    setIsDrawing(true);
    const rect = canvasRef.current.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    setCurrentPath([[x, y]]);
  };

  const draw = (e) => {
    if (!isDrawing) return;

    const rect = canvasRef.current.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');

    if (eraserMode) {
      // 橡皮擦模式 - 恢复黑板背景
      ctx.globalCompositeOperation = 'destination-out';
      ctx.lineWidth = 20;
    } else {
      // 绘图模式
      ctx.globalCompositeOperation = 'source-over';
      ctx.lineWidth = 2;
    }

    ctx.beginPath();
    if (currentPath.length > 0) {
      const lastPoint = currentPath[currentPath.length - 1];
      ctx.moveTo(lastPoint[0], lastPoint[1]);
    }
    ctx.lineTo(x, y);
    ctx.stroke();

    setCurrentPath([...currentPath, [x, y]]);
  };

  const stopDrawing = () => {
    if (isDrawing && currentPath.length > 0) {
      setPaths([...paths, currentPath]);
    }
    setIsDrawing(false);
    setCurrentPath([]);
  };

  const clearBlackboard = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    setPaths([]);
  };

  const downloadBlackboard = () => {
    const canvas = canvasRef.current;
    const link = document.createElement('a');
    link.download = 'blackboard.png';
    link.href = canvas.toDataURL();
    link.click();
  };

  return (
    <div className="blackboard-container">
      <div className="blackboard-header">
        <h3>📝 教学黑板</h3>
        <div className="blackboard-controls">
          <button
            className={`control-btn ${eraserMode ? 'active' : ''}`}
            onClick={() => setEraserMode(!eraserMode)}
            title={eraserMode ? '切换到粉笔' : '切换到橡皮擦'}
          >
            {eraserMode ? '✏️' : '🧹'}
          </button>
          <button
            className="control-btn"
            onClick={clearBlackboard}
            title="清空黑板"
          >
            🗑️
          </button>
          <button
            className="control-btn"
            onClick={downloadBlackboard}
            title="保存黑板"
          >
            💾
          </button>
        </div>
      </div>

      <div className="blackboard-content">
        {/* 数学内容展示区 */}
        <div className="math-content-area">
          {mathContent.map((item, index) => (
            <div key={index} className={`math-item math-${item.type}`}>
              {item.type === 'formula' && (
                <div className="formula">
                  {item.content}
                </div>
              )}
              {item.type === 'step' && (
                <div className="step">
                  <span className="step-number">{item.order}.</span>
                  {item.content}
                </div>
              )}
              {item.type === 'keyword' && (
                <div className="keyword">
                  🔑 {item.content}
                </div>
              )}
            </div>
          ))}
        </div>

        {/* 手绘画板 */}
        <div className="drawing-area">
          <canvas
            ref={canvasRef}
            width={400}
            height={300}
            className="blackboard-canvas"
            onMouseDown={startDrawing}
            onMouseMove={draw}
            onMouseUp={stopDrawing}
            onMouseLeave={stopDrawing}
          />
        </div>

        {/* 历史内容 */}
        {allMathContent.length > 0 && (
          <div className="history-content">
            <h4>📚 历史要点</h4>
            <div className="history-items">
              {allMathContent.slice(-5).map((item, index) => (
                <div key={index} className={`history-item history-${item.type}`}>
                  {item.type === 'formula' && `📐 ${item.content}`}
                  {item.type === 'step' && `✓ ${item.content}`}
                  {item.type === 'keyword' && `★ ${item.content}`}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      <div className="blackboard-footer">
        <div className="status-info">
          <span>粉笔模式: {eraserMode ? '橡皮擦' : '绘图'}</span>
          <span>笔画数: {paths.length}</span>
        </div>
      </div>
    </div>
  );
};

export default Blackboard;