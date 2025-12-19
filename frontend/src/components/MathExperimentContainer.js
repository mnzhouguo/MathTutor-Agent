import React, { useState, useEffect } from 'react';
import NumberLineRobotExperiment from './NumberLineRobotExperiment';
import './MathExperimentContainer.css';

const MathExperimentContainer = () => {
  const [problemData, setProblemData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // 加载题目数据
    const loadProblemData = async () => {
      try {
        setLoading(true);

        // 尝试从本地JSON文件加载
        const response = await fetch('/math_problem_data.json');
        if (!response.ok) {
          throw new Error('Failed to load problem data');
        }

        const data = await response.json();
        setProblemData(data);
      } catch (err) {
        console.error('Error loading problem data:', err);
        setError('无法加载题目数据，请稍后重试');
      } finally {
        setLoading(false);
      }
    };

    loadProblemData();
  }, []);

  // 步骤完成回调
  const handleStepComplete = (questionIndex, stepId, isCorrect) => {
    console.log(`Question ${questionIndex}, Step ${stepId} completed. Correct: ${isCorrect}`);
    // 这里可以添加进度保存、成绩统计等逻辑
  };

  if (loading) {
    return (
      <div className="experiment-container loading">
        <div className="loading-spinner"></div>
        <p>正在加载实验数据...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="experiment-container error">
        <div className="error-icon">⚠️</div>
        <h3>加载失败</h3>
        <p>{error}</p>
        <button onClick={() => window.location.reload()} className="btn btn-primary">
          重新加载
        </button>
      </div>
    );
  }

  return (
    <div className="experiment-container">
      <div className="experiment-header">
        <h1>数学实验工具</h1>
        <p>通过交互式动画深入理解数学概念</p>
      </div>

      <div className="experiment-content">
        <NumberLineRobotExperiment
          problemData={problemData}
          onStepComplete={handleStepComplete}
        />
      </div>

      <div className="experiment-footer">
        <div className="experiment-info">
          <h3>使用指南</h3>
          <ul>
            <li>选择要练习的题目，观察机器人运动规律</li>
            <li>使用控制面板调整动画速度，暂停观察关键时刻</li>
            <li>开启测量模式查看实时距离计算</li>
            <li>遇到困难时可以使用分级提示功能</li>
            <li>通过动画理解相遇、折返等数学概念</li>
          </ul>
        </div>

        <div className="learning-objectives">
          <h3>学习目标</h3>
          <div className="objectives-grid">
            <div className="objective-card">
              <h4>📍 理解数轴概念</h4>
              <p>掌握数轴上点的位置关系和距离计算</p>
            </div>
            <div className="objective-card">
              <h4>🤖 相遇问题建模</h4>
              <p>学习如何将实际问题转化为数学方程</p>
            </div>
            <div className="objective-card">
              <h4>📊 运动过程分析</h4>
              <p>理解速度、时间、位移之间的关系</p>
            </div>
            <div className="objective-card">
              <h4>🧮 绝对值应用</h4>
              <p>掌握绝对值在距离计算中的运用</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MathExperimentContainer;