import React, { useState } from 'react';
import './Header.css';

const Header = ({ agentInfo, useAgent, onToggleAgent, onGetSummary, sessionId }) => {
  const [showInfo, setShowInfo] = useState(false);

  return (
    <div className="header">
      <div className="header-content">
        <div className="header-title">
          <h1>MathTutor</h1>
          <span className="subtitle">智能数学学习助手</span>
        </div>

        {/* Agent模式切换 */}
        <div className="header-controls">
          <div className="agent-toggle">
            <label className="toggle-switch">
              <input
                type="checkbox"
                checked={useAgent}
                onChange={onToggleAgent}
              />
              <span className="slider"></span>
            </label>
            <span className="toggle-label">
              {useAgent ? '数学私教模式' : '普通聊天模式'}
            </span>
          </div>

          {/* Agent信息按钮 */}
          {useAgent && (
            <button
              className="info-button"
              onClick={() => setShowInfo(!showInfo)}
              title="查看Agent信息"
            >
              ℹ️
            </button>
          )}

          {/* 会话总结按钮 */}
          {useAgent && sessionId && (
            <button
              className="summary-button"
              onClick={onGetSummary}
              title="获取学习总结"
            >
              📊
            </button>
          )}
        </div>
      </div>

      {/* Agent信息卡片 */}
      {showInfo && agentInfo && (
        <div className="agent-info-card">
          <div className="info-header">
            <h3>🤖 {agentInfo.name}</h3>
            <button
              className="close-button"
              onClick={() => setShowInfo(false)}
            >
              ×
            </button>
          </div>
          <div className="info-content">
            <p><strong>教学风格:</strong> {agentInfo.teaching_style}</p>
            <p><strong>专业领域:</strong> {agentInfo.expertise}</p>
            <p><strong>性格特点:</strong> {agentInfo.personality}</p>
            <p className="description">{agentInfo.description}</p>
            <div className="features">
              <strong>核心功能:</strong>
              <ul>
                {agentInfo.features?.map((feature, index) => (
                  <li key={index}>{feature}</li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Header;