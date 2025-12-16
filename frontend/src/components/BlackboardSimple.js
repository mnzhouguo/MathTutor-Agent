/**
 * 简化版黑板组件
 * 专注于展示数学教学内容
 */

import React, { useState } from 'react';
import './Blackboard.css';

const Blackboard = ({ agentMessages = [], currentMessage = null }) => {
  const [showHistory, setShowHistory] = useState(false);

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

  return (
    <div className="blackboard-container">
      <div className="blackboard-header">
        <h3>📝 教学黑板</h3>
        <div className="blackboard-controls">
          <button
            className="control-btn"
            onClick={() => setShowHistory(!showHistory)}
            title={showHistory ? '隐藏历史' : '显示历史'}
          >
            {showHistory ? '📋' : '📚'}
          </button>
        </div>
      </div>

      <div className="blackboard-content">
        {/* 当前内容展示区 */}
        <div className="current-content-area">
          {mathContent.length > 0 ? (
            mathContent.map((item, index) => (
              <div key={index} className={`math-item math-${item.type}`}>
                {item.type === 'formula' && (
                  <div className="formula">
                    📐 {item.content}
                  </div>
                )}
                {item.type === 'step' && (
                  <div className="step">
                    <span className="step-number">{item.order}</span>
                    {item.content}
                  </div>
                )}
                {item.type === 'keyword' && (
                  <div className="keyword">
                    🔑 {item.content}
                  </div>
                )}
              </div>
            ))
          ) : (
            <div className="empty-state">
              <div className="empty-icon">📐</div>
              <p>等待数学私教老师开始板书...</p>
              <small>Agent回复后会在这里显示相关的数学内容</small>
            </div>
          )}
        </div>

        {/* 模拟黑板背景装饰 */}
        <div className="blackboard-decoration">
          <div className="chalk-dust chalk-dust-1"></div>
          <div className="chalk-dust chalk-dust-2"></div>
          <div className="chalk-dust chalk-dust-3"></div>
        </div>

        {/* 历史内容 */}
        {showHistory && allMathContent.length > 0 && (
          <div className="history-content">
            <h4>📚 历史要点</h4>
            <div className="history-items">
              {allMathContent.slice(-10).map((item, index) => (
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
          <span>当前内容: {mathContent.length} 项</span>
          <span>历史记录: {allMathContent.length} 项</span>
        </div>
      </div>
    </div>
  );
};

export default Blackboard;