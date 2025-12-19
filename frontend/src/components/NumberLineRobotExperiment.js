import React, { useState, useEffect, useRef } from 'react';
import './NumberLineRobotExperiment.css';

const NumberLineRobotExperiment = ({ problemData, onStepComplete }) => {
  // 题目数据
  const A_POINT = -5;  // A点坐标
  const B_POINT = 8;   // B点坐标
  const INITIAL_DISTANCE = B_POINT - A_POINT; // 13
  const MEETING_POINT = 5; // 相遇点P
  const MEETING_TIME = 2.5; // 相遇时间t

  // 状态管理
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [speed, setSpeed] = useState(1); // 动画速度倍数
  const [phase, setPhase] = useState('initial'); // initial, meeting, returning
  const [selectedQuestion, setSelectedQuestion] = useState(1);
  const [showHint, setShowHint] = useState(false);
  const [hintLevel, setHintLevel] = useState(1);
  const [measuringMode, setMeasuringMode] = useState(false);
  const [distanceSum, setDistanceSum] = useState(0);

  const animationRef = useRef(null);
  const startTimeRef = useRef(null);

  // 机器人位置计算
  const calculateRobotPositions = (time) => {
    let mPosition, nPosition;

    if (time <= 0) {
      // 初始状态
      mPosition = A_POINT;
      nPosition = B_POINT;
    } else if (time <= MEETING_TIME) {
      // 相遇前 (0 < t ≤ 2.5)
      mPosition = A_POINT + 4 * time; // M从A向右运动，速度4
      if (time <= 1) {
        nPosition = B_POINT; // N还没出发
      } else {
        nPosition = B_POINT - 2 * (time - 1); // N从B向左运动，速度2
      }
    } else {
      // 相遇后折返 (t > 2.5)
      const t = time - MEETING_TIME;
      mPosition = MEETING_POINT - 2 * t; // M向左返回，速度2
      nPosition = MEETING_POINT + 2 * t; // N向右返回，速度2
    }

    return { mPosition, nPosition };
  };

  // 计算距离和
  const calculateDistanceSum = (time) => {
    const { mPosition, nPosition } = calculateRobotPositions(time);
    const distanceMA = Math.abs(mPosition - A_POINT);
    const distanceNB = Math.abs(nPosition - B_POINT);
    return distanceMA + distanceNB;
  };

  // 动画循环
  const animate = (timestamp) => {
    if (!startTimeRef.current) {
      startTimeRef.current = timestamp;
    }

    const elapsed = (timestamp - startTimeRef.current) / 1000 * speed; // 转换为秒，考虑速度倍数
    setCurrentTime(elapsed);

    // 更新阶段
    if (elapsed <= 0) {
      setPhase('initial');
    } else if (elapsed <= MEETING_TIME) {
      setPhase('meeting');
    } else {
      setPhase('returning');
    }

    // 更新距离和
    setDistanceSum(calculateDistanceSum(elapsed));

    // 继续动画
    if (isPlaying && elapsed < 10) { // 最多播放10秒
      animationRef.current = requestAnimationFrame(animate);
    } else if (elapsed >= 10) {
      setIsPlaying(false);
    }
  };

  // 播放/暂停
  const togglePlay = () => {
    if (isPlaying) {
      setIsPlaying(false);
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    } else {
      setIsPlaying(true);
      startTimeRef.current = null;
      animationRef.current = requestAnimationFrame(animate);
    }
  };

  // 重置
  const reset = () => {
    setIsPlaying(false);
    setCurrentTime(0);
    setPhase('initial');
    setDistanceSum(0);
    startTimeRef.current = null;
    if (animationRef.current) {
      cancelAnimationFrame(animationRef.current);
    }
  };

  // 获取当前提示内容
  const getCurrentHint = () => {
    const question = problemData.questions.find(q => q.question_index === selectedQuestion);
    if (!question || !showHint) return null;

    const step = question.logic_steps[hintLevel - 1];
    return step ? step.scaffolding[`level_${hintLevel}`] : null;
  };

  const { mPosition, nPosition } = calculateRobotPositions(currentTime);

  return (
    <div className="number-line-experiment">
      <div className="experiment-header">
        <h2>数轴机器人相遇实验</h2>
        <div className="question-selector">
          <label>选择题目：</label>
          <select value={selectedQuestion} onChange={(e) => setSelectedQuestion(Number(e.target.value))}>
            <option value={1}>问题1：计算AB距离</option>
            <option value={2}>问题2：求相遇时间和地点</option>
            <option value={3}>问题3：距离和问题</option>
          </select>
        </div>
      </div>

      {/* 数轴组件 */}
      <div className="number-line-container">
        <svg className="number-line-svg" viewBox="-10 -2 20 4" preserveAspectRatio="xMidYMid meet">
          {/* 数轴主线 */}
          <line x1="-10" y1="0" x2="10" y2="0" stroke="#333" strokeWidth="0.05" />

          {/* 刻度和标签 */}
          {Array.from({ length: 21 }, (_, i) => {
            const x = -10 + i;
            return (
              <g key={i}>
                <line x1={x} y1="-0.1" x2={x} y2="0.1" stroke="#333" strokeWidth="0.02" />
                <text x={x} y="0.3" textAnchor="middle" fontSize="0.2" fill="#666">
                  {x}
                </text>
              </g>
            );
          })}

          {/* A点和B点 */}
          <g className="fixed-points">
            <circle cx={A_POINT} cy="0" r="0.15" fill="#e74c3c" />
            <text x={A_POINT} y="-0.5" textAnchor="middle" fontSize="0.25" fill="#e74c3c" fontWeight="bold">
              A
            </text>
            <circle cx={B_POINT} cy="0" r="0.15" fill="#3498db" />
            <text x={B_POINT} y="-0.5" textAnchor="middle" fontSize="0.25" fill="#3498db" fontWeight="bold">
              B
            </text>
          </g>

          {/* 相遇点P（当到达时显示） */}
          {phase !== 'initial' && (
            <g className="meeting-point">
              <circle cx={MEETING_POINT} cy="0" r="0.1" fill="#27ae60" opacity="0.5" />
              <text x={MEETING_POINT} y="-0.3" textAnchor="middle" fontSize="0.2" fill="#27ae60">
                P
              </text>
            </g>
          )}

          {/* 机器人M */}
          <g className="robot-m" transform={`translate(${mPosition}, 0)`}>
            <circle r="0.2" fill="#e74c3c" opacity="0.8">
              <animate attributeName="r" values="0.2;0.25;0.2" dur="1s" repeatCount="indefinite" />
            </circle>
            <text y="-0.4" textAnchor="middle" fontSize="0.25" fill="#e74c3c" fontWeight="bold">
              M
            </text>
          </g>

          {/* 机器人N */}
          <g className="robot-n" transform={`translate(${nPosition}, 0)`}>
            <circle r="0.2" fill="#3498db" opacity="0.8">
              <animate attributeName="r" values="0.2;0.25;0.2" dur="1s" repeatCount="indefinite" />
            </circle>
            <text y="-0.4" textAnchor="middle" fontSize="0.25" fill="#3498db" fontWeight="bold">
              N
            </text>
          </g>

          {/* 连接线显示距离 */}
          {measuringMode && (
            <>
              {/* M到A的距离 */}
              <line x1={mPosition} y1="0.5" x2={A_POINT} y2="0.5"
                    stroke="#e74c3c" strokeWidth="0.05" strokeDasharray="0.1,0.1" />
              <text x={(mPosition + A_POINT) / 2} y="0.8" textAnchor="middle" fontSize="0.15" fill="#e74c3c">
                {Math.abs(mPosition - A_POINT).toFixed(1)}
              </text>

              {/* N到B的距离 */}
              <line x1={nPosition} y1="-0.5" x2={B_POINT} y2="-0.5"
                    stroke="#3498db" strokeWidth="0.05" strokeDasharray="0.1,0.1" />
              <text x={(nPosition + B_POINT) / 2} y="-0.8" textAnchor="middle" fontSize="0.15" fill="#3498db">
                {Math.abs(nPosition - B_POINT).toFixed(1)}
              </text>
            </>
          )}
        </svg>
      </div>

      {/* 控制面板 */}
      <div className="control-panel">
        <div className="controls">
          <button onClick={togglePlay} className={`btn ${isPlaying ? 'btn-pause' : 'btn-play'}`}>
            {isPlaying ? '⏸ 暂停' : '▶ 播放'}
          </button>
          <button onClick={reset} className="btn btn-reset">🔄 重置</button>

          <div className="speed-control">
            <label>速度：</label>
            <input type="range" min="0.5" max="3" step="0.5" value={speed}
                   onChange={(e) => setSpeed(Number(e.target.value))} />
            <span>{speed}x</span>
          </div>

          <button onClick={() => setMeasuringMode(!measuringMode)}
                  className={`btn ${measuringMode ? 'btn-active' : ''}`}>
            📏 {measuringMode ? '隐藏测量' : '显示测量'}
          </button>
        </div>

        {/* 实时数据显示 */}
        <div className="data-display">
          <div className="data-item">
            <label>时间：</label>
            <span className="value">{currentTime.toFixed(2)}秒</span>
          </div>
          <div className="data-item">
            <label>阶段：</label>
            <span className="value phase">
              {phase === 'initial' ? '初始' : phase === 'meeting' ? '接近中' : '折返'}
            </span>
          </div>
          <div className="data-item">
            <label>M位置：</label>
            <span className="value" style={{ color: '#e74c3c' }}>{mPosition.toFixed(2)}</span>
          </div>
          <div className="data-item">
            <label>N位置：</label>
            <span className="value" style={{ color: '#3498db' }}>{nPosition.toFixed(2)}</span>
          </div>
          <div className="data-item">
            <label>距离和：</label>
            <span className="value" style={{ color: '#27ae60' }}>{distanceSum.toFixed(2)}</span>
          </div>
        </div>
      </div>

      {/* 提示系统 */}
      <div className="hint-system">
        <div className="hint-controls">
          <button onClick={() => setShowHint(!showHint)}
                  className={`btn ${showHint ? 'btn-active' : ''}`}>
            💡 {showHint ? '隐藏提示' : '显示提示'}
          </button>
          {showHint && (
            <div className="hint-level-selector">
              <label>提示级别：</label>
              <select value={hintLevel} onChange={(e) => setHintLevel(Number(e.target.value))}>
                <option value={1}>级别1</option>
                <option value={2}>级别2</option>
                <option value={3}>级别3</option>
              </select>
            </div>
          )}
        </div>

        {showHint && getCurrentHint() && (
          <div className="hint-content">
            <h4>提示 {hintLevel}：</h4>
            <p>{getCurrentHint()}</p>
          </div>
        )}
      </div>

      {/* 题目信息 */}
      <div className="question-info">
        <h3>问题 {selectedQuestion}</h3>
        <p>{problemData.questions.find(q => q.question_index === selectedQuestion)?.question_text}</p>
      </div>
    </div>
  );
};

export default NumberLineRobotExperiment;