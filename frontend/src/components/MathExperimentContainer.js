import React, { useState } from 'react';
import NumberLineRobotExperiment from './NumberLineRobotExperiment';
import ModernMathExperiment from './ModernMathExperiment';
import './MathExperimentContainer.css';

const MathExperimentContainer = () => {
  const [selectedExperiment, setSelectedExperiment] = useState(null);
  const [activeExperiment, setActiveExperiment] = useState(null);
  const [useModernInterface, setUseModernInterface] = useState(true); // 默认使用现代化界面

  // 模拟问题数据
  const mockProblemData = {
    questions: [
      {
        question_index: 1,
        question_text: "计算数轴上点A(-5)和点B(8)之间的距离",
        logic_steps: [
          {
            step_id: 1,
            scaffolding: {
              level_1: "使用绝对值公式：|x2 - x1|",
              level_2: "具体计算：|8 - (-5)| = |13| = 13",
              level_3: "A点坐标是-5，B点坐标是8，距离是13个单位长度"
            }
          }
        ]
      },
      {
        question_index: 2,
        question_text: "机器人M从A点以速度4单位/秒向右运动，机器人N从B点以速度2单位/秒向左运动，求相遇时间和地点",
        logic_steps: [
          {
            step_id: 1,
            scaffolding: {
              level_1: "设相遇时间为t秒，建立方程",
              level_2: "-5 + 4t = 8 - 2t，解这个方程",
              level_3: "6t = 13，t = 13/6 ≈ 2.17秒，相遇点坐标约为3.67"
            }
          }
        ]
      }
    ]
  };

  // 实验数据
  const experiments = [
    {
      id: 'robot-movement',
      title: '数轴机器人相遇实验',
      icon: '🤖',
      description: '通过两个机器人在数轴上的运动，直观理解相遇问题、距离计算和运动规律。',
      difficulty: '中等',
      duration: '15-20分钟',
      tags: ['数轴', '相遇问题', '距离计算', '运动规律'],
      color: '#3498db'
    },
    {
      id: 'coordinate-system',
      title: '坐标系探索实验',
      icon: '📊',
      description: '在平面坐标系中进行图形变换，理解平移、旋转、缩放等几何变换概念。',
      difficulty: '简单',
      duration: '10-15分钟',
      tags: ['坐标系', '几何变换', '图形', '可视化'],
      color: '#27ae60'
    },
    {
      id: 'function-graph',
      title: '函数图像实验',
      icon: '📈',
      description: '动态探索一次函数、二次函数的图像特征，理解参数变化对图像的影响。',
      difficulty: '中等',
      duration: '20-25分钟',
      tags: ['函数', '图像', '参数', '可视化'],
      color: '#e74c3c'
    },
    {
      id: 'geometry-construction',
      title: '几何构造实验',
      icon: '📐',
      description: '使用圆规、直尺等工具进行几何作图，掌握基本几何图形的构造方法。',
      difficulty: '困难',
      duration: '25-30分钟',
      tags: ['几何', '构造', '圆规', '直尺'],
      color: '#f39c12'
    },
    {
      id: 'probability-simulation',
      title: '概率统计模拟',
      icon: '🎲',
      description: '通过模拟抛硬币、掷骰子等随机实验，理解概率论的基本概念。',
      difficulty: '简单',
      duration: '15-20分钟',
      tags: ['概率', '统计', '随机', '模拟'],
      color: '#9b59b6'
    },
    {
      id: 'algebra-manipulation',
      title: '代数运算可视化',
      icon: '➕',
      description: '将抽象的代数运算转化为直观的图形表示，帮助理解运算的几何意义。',
      difficulty: '中等',
      duration: '20-25分钟',
      tags: ['代数', '运算', '可视化', '几何意义'],
      color: '#1abc9c'
    }
  ];

  const handleExperimentSelect = (experimentId) => {
    setSelectedExperiment(experimentId);
    // 这里可以添加加载具体实验内容的逻辑
  };

  const handleStartExperiment = () => {
    const experiment = experiments.find(e => e.id === selectedExperiment);
    if (experiment) {
      console.log(`开始实验: ${experiment.title}`);
      if (experiment.id === 'robot-movement') {
        // 机器人实验使用现代化界面
        setUseModernInterface(true);
        setActiveExperiment('modern-interface');
      } else {
        setActiveExperiment(selectedExperiment);
      }
    }
  };

  const handleViewTutorial = () => {
    const experiment = experiments.find(e => e.id === selectedExperiment);
    if (experiment) {
      console.log(`查看教程: ${experiment.title}`);
      // 这里可以添加打开教程的逻辑
      alert(`"${experiment.title}" 教程\n\n${experiment.description}\n\n教程功能开发中...`);
    }
  };

  const handleStepComplete = (questionIndex, stepId, isCorrect) => {
    console.log(`问题 ${questionIndex}, 步骤 ${stepId} 完成. 正确: ${isCorrect}`);
  };

  return (
    <div className="experiment-container">
      {/* 头部导航 */}
      <header className="experiment-header">
        <div className="header-content">
          <div className="header-text">
            <h1>
              <span className="icon">🧪</span>
              数学实验工具
            </h1>
            <p>通过互动实验，深入理解数学概念</p>
          </div>
          <div className="header-stats">
            <div className="stat-item">
              <span className="stat-number">{experiments.length}</span>
              <span className="stat-label">实验项目</span>
            </div>
            <div className="stat-item">
              <span className="stat-number">∞</span>
              <span className="stat-label">探索可能</span>
            </div>
          </div>
        </div>
      </header>

      {/* 主要内容区 */}
      <main className="experiment-main">
        {/* 实验选择网格 */}
        <section className="experiments-grid-section">
          <div className="section-header">
            <h2>选择实验项目</h2>
            <p>点击下方卡片开始你的数学探索之旅</p>
          </div>

          <div className="experiments-grid">
            {experiments.map((experiment) => (
              <div
                key={experiment.id}
                className={`experiment-card ${selectedExperiment === experiment.id ? 'selected' : ''}`}
                onClick={() => handleExperimentSelect(experiment.id)}
                style={{ '--card-color': experiment.color }}
              >
                <div className="card-header">
                  <div className="card-icon">{experiment.icon}</div>
                  <div className="card-meta">
                    <span className="difficulty">{experiment.difficulty}</span>
                    <span className="duration">{experiment.duration}</span>
                  </div>
                </div>

                <div className="card-content">
                  <h3>{experiment.title}</h3>
                  <p>{experiment.description}</p>
                </div>

                <div className="card-footer">
                  <div className="tags">
                    {experiment.tags.map((tag, index) => (
                      <span key={index} className="tag">{tag}</span>
                    ))}
                  </div>
                  <div className="card-action">
                    <span>开始探索 →</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* 当前实验展示区 */}
        {selectedExperiment && (
          <section className="current-experiment-section">
            <div className="experiment-display">
              {(() => {
                const experiment = experiments.find(e => e.id === selectedExperiment);
                return (
                  <>
                    <div className="experiment-header-bar">
                      <div className="experiment-info">
                        <span className="experiment-icon">{experiment.icon}</span>
                        <h3>{experiment.title}</h3>
                      </div>
                      <button
                        className="btn-close"
                        onClick={() => {
                          setSelectedExperiment(null);
                          setActiveExperiment(null);
                          setUseModernInterface(true);
                        }}
                      >
                        ✕
                      </button>
                    </div>

                    <div className="experiment-content-area">
                      {activeExperiment === 'modern-interface' ? (
                        <ModernMathExperiment />
                      ) : activeExperiment === 'robot-movement' ? (
                        <NumberLineRobotExperiment
                          problemData={mockProblemData}
                          onStepComplete={handleStepComplete}
                        />
                      ) : (
                        <div className="experiment-placeholder">
                          <div className="placeholder-icon">{experiment.icon}</div>
                          <h4>准备就绪</h4>
                          <p>即将开始 "{experiment.title}" 实验</p>
                          <div className="experiment-actions">
                            <button
                              className="btn-primary"
                              onClick={handleStartExperiment}
                            >
                              🚀 开始实验
                            </button>
                            <button
                              className="btn-secondary"
                              onClick={handleViewTutorial}
                            >
                              📖 查看教程
                            </button>
                          </div>
                        </div>
                      )}
                    </div>
                  </>
                );
              })()}
            </div>
          </section>
        )}

        {/* 学习目标区域 */}
        <section className="learning-section">
          <div className="section-header">
            <h2>学习目标</h2>
            <p>通过实验探索，你将掌握这些重要的数学技能</p>
          </div>

          <div className="objectives-grid">
            <div className="objective-card">
              <div className="objective-icon">🎯</div>
              <h3>概念理解</h3>
              <p>将抽象的数学概念通过可视化方式呈现，加深理解</p>
            </div>

            <div className="objective-card">
              <div className="objective-icon">🔍</div>
              <h3>探索发现</h3>
              <p>通过动手实验，主动发现数学规律和性质</p>
            </div>

            <div className="objective-card">
              <div className="objective-icon">💡</div>
              <h3>思维培养</h3>
              <p>培养数学思维，提高问题分析和解决能力</p>
            </div>

            <div className="objective-card">
              <div className="objective-icon">🎨</div>
              <h3>创意应用</h3>
              <p>将数学知识应用到实际问题中，激发创造力</p>
            </div>
          </div>
        </section>
      </main>

      {/* 底部信息 */}
      <footer className="experiment-footer">
        <div className="footer-content">
          <div className="usage-guide">
            <h4>使用指南</h4>
            <ul>
              <li>选择感兴趣的实验项目，点击开始探索</li>
              <li>按照实验指导进行操作，观察现象</li>
              <li>记录实验过程中的发现和疑问</li>
              <li>与老师同学分享讨论，深化理解</li>
            </ul>
          </div>

          <div className="tips">
            <h4>💡 学习小贴士</h4>
            <ul>
              <li>实验前先思考可能的结果</li>
              <li>尝试不同的参数设置，观察变化</li>
              <li>将实验现象与数学理论联系起来</li>
              <li>多动手实践，加深记忆理解</li>
            </ul>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default MathExperimentContainer;