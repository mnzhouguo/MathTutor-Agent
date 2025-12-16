/**
 * 聊天容器组件
 * 重构后的简洁设计
 */

import React from 'react';
import Message from './Message';
import './ChatContainer.css';

const ChatContainer = ({ messages, loading, messagesEndRef, onRetry }) => {
  return (
    <div className="chat-container">
      {/* 消息列表 */}
      <div className="messages-list">
        {messages.map((message) => (
          <Message
            key={message.id}
            message={message}
            onRetry={onRetry}
          />
        ))}

        {/* 加载状态 */}
        {loading && (
          <div className="message assistant loading">
            <div className="message-avatar">🤖</div>
            <div className="message-content">
              <div className="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
              <div className="message-text">正在思考中...</div>
            </div>
          </div>
        )}

        {/* 滚动锚点 */}
        <div ref={messagesEndRef} />
      </div>
    </div>
  );
};

export default ChatContainer;