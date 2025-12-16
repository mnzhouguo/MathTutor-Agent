/**
 * 消息组件
 * 支持错误状态和重试功能
 */

import React from 'react';
import './Message.css';

const Message = ({ message, onRetry }) => {
  const isError = message.isError || false;
  const isUser = message.role === 'user';
  const isAssistant = message.role === 'assistant';

  
  // 渲染数学公式 (简单实现)
  const formatContent = (content) => {
    // 简单的数学公式格式化
    return content
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.*?)\*/g, '<em>$1</em>')
      .replace(/`([^`]+)`/g, '<code>$1</code>');
  };

  return (
    <div className={`message ${message.role} ${isError ? 'error' : ''}`}>
      <div className="message-avatar">
        {isUser ? '👤' : isAssistant ? '🤖' : '❓'}
      </div>

      <div className="message-content">
        <div
          className="message-text"
          dangerouslySetInnerHTML={{ __html: formatContent(message.content) }}
        />

        {/* 学习建议 */}
        {message.suggestions && message.suggestions.length > 0 && isAssistant && (
          <div className="message-suggestions">
            <div className="suggestions-title">💡 学习建议：</div>
            <ul className="suggestions-list">
              {message.suggestions.map((suggestion, index) => (
                <li key={index}>{suggestion}</li>
              ))}
            </ul>
          </div>
        )}

        {/* 知识点信息 */}
        {message.knowledgeInfo && message.knowledgeInfo.length > 0 && isAssistant && (
          <div className="knowledge-info">
            <div className="knowledge-title">📚 相关知识点：</div>
            <div className="knowledge-content">
              {message.knowledgeInfo.map((knowledge, index) => (
                <div key={index} className="knowledge-item">
                  <strong>{knowledge.module}</strong>
                  {knowledge.topic && <span> - {knowledge.topic}</span>}
                </div>
              ))}
            </div>
          </div>
        )}

        {isError && onRetry && (
          <div className="message-actions">
            <button
              className="retry-button"
              onClick={() => onRetry(message)}
              title="重新发送"
            >
              🔄 重试
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Message;