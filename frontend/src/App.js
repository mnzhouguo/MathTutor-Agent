/**
 * MathTutor 主应用组件
 * 重构后的简洁架构
 */

import React, { useState, useEffect, useRef, useCallback } from 'react';
import { chatAPI } from './services/api';
import ChatContainer from './components/ChatContainer';
import MessageInput from './components/MessageInput';
import Header from './components/Header';
import Blackboard from './components/BlackboardSimple';
import './App.css';

function App() {
  // 状态管理
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [useAgent, setUseAgent] = useState(true); // 默认启用数学私教Agent
  const [sessionId, setSessionId] = useState(null);
  const [agentInfo, setAgentInfo] = useState(null);
  const messagesEndRef = useRef(null);

  // 滚动到底部
  const scrollToBottom = useCallback(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, []);

  // 初始化欢迎消息和Agent信息
  useEffect(() => {
    // 获取Agent信息
    const loadAgentInfo = async () => {
      try {
        const info = await chatAPI.getAgentInfo();
        setAgentInfo(info);
      } catch (err) {
        console.warn('获取Agent信息失败:', err);
      }
    };

    loadAgentInfo();

    // 设置欢迎消息
    setMessages([{
      id: 'welcome',
      role: 'assistant',
      content: useAgent
        ? '你好！我是你的专属数学小老师🎯 采用苏格拉底式教学法，通过提问引导你思考。有什么数学难题想挑战吗？'
        : '您好！我是数学导师，有什么数学问题可以帮助您吗？',
      timestamp: Date.now()
    }]);
  }, [useAgent]);

  // 自动滚动
  useEffect(() => {
    scrollToBottom();
  }, [messages, scrollToBottom]);

  // 发送消息处理
  const handleSendMessage = useCallback(async (message) => {
    if (!message?.trim() || loading) return;

    const userMessage = {
      id: `user-${Date.now()}`,
      role: 'user',
      content: message.trim(),
      timestamp: Date.now()
    };

    // 添加用户消息
    setMessages(prev => [...prev, userMessage]);
    setLoading(true);
    setError(null);

    try {
      // 获取历史记录
      const history = messages
        .filter(msg => msg.id !== 'welcome')
        .map(({ role, content }) => ({ role, content }));

      let response;

      // 根据是否启用Agent选择不同的API
      if (useAgent) {
        response = await chatAPI.sendAgentMessage(message.trim(), history, sessionId);
        // 更新会话ID（如果是新的会话）
        if (response.session_id && !sessionId) {
          setSessionId(response.session_id);
        }
      } else {
        response = await chatAPI.sendMessage(message.trim(), history, false, sessionId);
      }

      // 添加AI回复
      const assistantMessage = {
        id: `assistant-${Date.now()}`,
        role: 'assistant',
        content: response.response,
        timestamp: Date.now(),
        // 如果有学习建议，添加到消息中
        suggestions: response.suggestions || [],
        knowledgeInfo: response.knowledge_info || null
      };

      setMessages(prev => [...prev, assistantMessage]);

    } catch (err) {
      console.error('发送消息失败:', err);
      setError(err.message || '发送消息失败，请稍后重试');

      // 添加错误消息
      const errorMessage = {
        id: `error-${Date.now()}`,
        role: 'assistant',
        content: `抱歉，${err.message || '出现了错误，请稍后再试'}`,
        timestamp: Date.now(),
        isError: true
      };

      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setLoading(false);
    }
  }, [loading, messages, useAgent, sessionId]);

  // 重试错误的消息
  const handleRetry = useCallback((failedMessage) => {
    let userMessageToRetry = null;

    // 移除错误消息和对应的用户消息
    setMessages(prev => {
      const failedIndex = prev.findIndex(msg => msg.id === failedMessage.id);
      if (failedIndex > 0) {
        userMessageToRetry = prev[failedIndex - 1];
        return prev.slice(0, failedIndex - 1);
      }
      return prev.filter(msg => msg.id !== failedMessage.id);
    });

    // 重新发送
    if (userMessageToRetry?.role === 'user') {
      handleSendMessage(userMessageToRetry.content);
    }
  }, [handleSendMessage]);

  // 清除错误
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  // 切换Agent模式
  const toggleAgentMode = useCallback(() => {
    setUseAgent(prev => {
      const newMode = !prev;
      // 如果切换Agent模式，清除会话ID
      if (!newMode) {
        setSessionId(null);
      }
      return newMode;
    });
  }, []);

  // 获取会话总结
  const handleGetSessionSummary = useCallback(async () => {
    if (!sessionId) return;

    try {
      const summary = await chatAPI.getSessionSummary(sessionId);
      console.log('会话总结:', summary);
      // 这里可以显示总结信息或做其他处理
    } catch (err) {
      console.error('获取会话总结失败:', err);
    }
  }, [sessionId]);

  return (
    <div className="app">
      {/* 全局Header */}
      <div className="global-header">
        <Header
          agentInfo={agentInfo}
          useAgent={useAgent}
          onToggleAgent={toggleAgentMode}
          onGetSummary={handleGetSessionSummary}
          sessionId={sessionId}
        />
      </div>

      {/* 主要内容区：左右布局 */}
      <div className="main-content">
        {/* 左侧：对话框 */}
        <div className="chat-section">
          <ChatContainer
            messages={messages}
            loading={loading}
            messagesEndRef={messagesEndRef}
            onRetry={handleRetry}
          />

          <MessageInput
            onSendMessage={handleSendMessage}
            disabled={loading}
            placeholder={useAgent ? "请输入数学问题，让小老师引导你思考..." : "请输入您的数学问题..."}
          />
        </div>

        {/* 右侧：黑板 */}
        <div className="blackboard-section">
          <Blackboard
            agentMessages={messages.filter(msg => msg.role === 'assistant')}
            currentMessage={messages[messages.length - 1]}
          />
        </div>
      </div>

      {/* 错误提示 */}
      {error && (
        <div className="error-toast">
          <span>{error}</span>
          <button onClick={clearError}>×</button>
        </div>
      )}
    </div>
  );
}

export default App;