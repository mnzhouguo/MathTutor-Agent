import React, { useState, useEffect, useRef } from 'react';
import {
  Play, Pause, RotateCcw, BrainCircuit,
  Activity, ArrowLeftRight, Timer, Target, Sword, Radio, Rocket, Anchor, ShieldCheck,
  Binary, Waves, Zap, Crosshair, Radar
} from 'lucide-react';
import './ModernMathExperiment.css';

// ==========================================
// 1. 类型定义
// ==========================================

const NumberLineTool = {
  id: 'number_line',
  range: [-15, 25],
  points: [
    {
      id: 'M',
      label: '猎潜舰 M',
      color: '#0ea5e9',
      startPos: -6,
      velocity: 2.5,
      icon: 'ship'
    },
    {
      id: 'N',
      label: '渗透潜艇 N',
      color: '#f43f5e',
      startPos: 9,
      velocity: -0.5,
      icon: 'sub'
    }
  ],
  condition: {
    formula: "坐标锁定: M(t) = N(t)",
    targets: [5]
  }
};

const AbsoluteValueTool = {
  id: 'abs_optimization',
  anchors: [
    { label: '补给 A', pos: -8, color: '#10b981' },
    { label: '补给 B', pos: 2, color: '#10b981' },
    { label: '补给 C', pos: 12, color: '#10b981' }
  ],
  scene: 'ocean'
};

// ==========================================
// 2. 模拟真实战场关卡设计
// ==========================================

const MATH_LAB_EXAMS = [
  {
    title: "代号：红海拦截",
    point: "追及相遇与拦截坐标",
    description: "【战术分析】我方猎潜舰 M 发现敌方渗透潜艇 N。我们需要通过计算精确的 T 时刻，使两舰坐标重合实施包围。这是决定胜负的数学博弈。",
    config: NumberLineTool
  },
  {
    title: "代号：黄金锚点",
    point: "绝对值之和的最值问题",
    description: "【战术分析】三艘损坏的友军补给舰散布在航道上。旗舰 P 的能量场必须覆盖所有单位。求航程和最小化的位置，确保救援效率最大化。",
    config: AbsoluteValueTool
  }
];

// ==========================================
// 3. 核心渲染组件 (加入雷达特效)
// ==========================================

const RadarBackground = (ctx, width, height, time) => {
  // 绘制雷达扫描圈
  ctx.save();
  ctx.strokeStyle = 'rgba(16, 185, 129, 0.1)';
  ctx.lineWidth = 1;
  const centerX = width / 2;
  const centerY = height / 2;

  for(let r = 50; r < width; r += 100) {
    ctx.beginPath();
    ctx.arc(centerX, centerY, r, 0, Math.PI * 2);
    ctx.stroke();
  }

  // 扫描线
  const angle = (time % 4) * Math.PI / 2;
  const gradient = ctx.createRadialGradient(centerX, centerY, 0, centerX, centerY, width/2);
  gradient.addColorStop(0, 'rgba(16, 185, 129, 0)');
  gradient.addColorStop(1, 'rgba(16, 185, 129, 0.05)');

  ctx.beginPath();
  ctx.moveTo(centerX, centerY);
  ctx.arc(centerX, centerY, width, angle, angle + 0.2);
  ctx.fillStyle = gradient;
  ctx.fill();
  ctx.restore();
};

const MotionLabRenderer = ({ t, config }) => {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const { width, height } = canvas;
    ctx.clearRect(0, 0, width, height);

    RadarBackground(ctx, width, height, t);

    const midY = height / 2 + 20;
    const mapX = (v) => ((v - config.range[0]) / (config.range[1] - config.range[0])) * (width - 120) + 60;

    // 绘制电子数轴
    ctx.beginPath();
    ctx.moveTo(30, midY);
    ctx.lineTo(width - 30, midY);
    ctx.strokeStyle = 'rgba(56, 189, 248, 0.3)';
    ctx.lineWidth = 2;
    ctx.stroke();

    // 绘制刻度
    for(let i = config.range[0]; i <= config.range[1]; i += 5) {
      const sx = mapX(i);
      ctx.beginPath();
      ctx.moveTo(sx, midY - 5);
      ctx.lineTo(sx, midY + 5);
      ctx.strokeStyle = 'rgba(56, 189, 248, 0.5)';
      ctx.stroke();
      ctx.fillStyle = 'rgba(56, 189, 248, 0.5)';
      ctx.font = '8px monospace';
      ctx.fillText(i.toString(), sx - 5, midY + 15);
    }

    // 绘制单位
    config.points.forEach((p) => {
      const currentPos = p.startPos + p.velocity * t;
      const x = mapX(currentPos);

      // 船只阴影
      ctx.save();
      ctx.shadowBlur = 20;
      ctx.shadowColor = p.color;

      // 绘制战术几何图形替代图片，更具黑客/雷达感
      ctx.fillStyle = p.color;
      ctx.beginPath();
      if(p.id === 'M') {
        ctx.moveTo(x - 15, midY - 10);
        ctx.lineTo(x + 20, midY);
        ctx.lineTo(x - 15, midY + 10);
        ctx.closePath();
        ctx.fill();
      } else {
        ctx.arc(x, midY, 8, 0, Math.PI * 2);
        ctx.fill();
      }
      ctx.restore();

      // 标签面板
      ctx.fillStyle = '#fff';
      ctx.font = 'bold 11px sans-serif';
      ctx.textAlign = 'center';
      ctx.fillText(p.label, x, midY - 25);

      ctx.fillStyle = 'rgba(0,0,0,0.6)';
      ctx.fillRect(x - 30, midY + 22, 60, 14);
      ctx.fillStyle = p.color;
      ctx.font = '9px monospace';
      ctx.fillText(`POS: ${currentPos.toFixed(1)}`, x, midY + 32);
    });
  }, [t, config]);

  return (
    <canvas
      ref={canvasRef}
      width={700}
      height={220}
      className="motion-canvas"
    />
  );
};

const AbsOptimizationRenderer = ({ xPos, config }) => {
  const canvasRef = useRef(null);
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const { width, height } = canvas;
    ctx.clearRect(0, 0, width, height);

    const range = [-20, 25];
    const axisY = height - 50;
    const graphBaseY = height - 80;
    const mapX = (v) => ((v - range[0]) / (range[1] - range[0])) * (width - 100) + 50;
    const mapY = (val) => graphBaseY - val * 6;
    const getSum = (x) => config.anchors.reduce((acc, a) => acc + Math.abs(x - a.pos), 0);

    // 绘制网格背景
    ctx.strokeStyle = 'rgba(15, 23, 42, 0.5)';
    ctx.lineWidth = 1;
    for(let i = 0; i < width; i += 30) {
      ctx.beginPath();
      ctx.moveTo(i, 0);
      ctx.lineTo(i, height);
      ctx.stroke();
    }

    // 能量特征曲线
    ctx.beginPath();
    ctx.strokeStyle = '#22d3ee';
    ctx.lineWidth = 3;
    ctx.shadowBlur = 15;
    ctx.shadowColor = '#0891b2';
    for (let x = range[0]; x <= range[1]; x += 0.2) {
      const cx = mapX(x);
      const cy = mapY(getSum(x));
      if (x === range[0]) ctx.moveTo(cx, cy);
      else ctx.lineTo(cx, cy);
    }
    ctx.stroke();
    ctx.shadowBlur = 0;

    // 航道
    ctx.beginPath();
    ctx.moveTo(20, axisY);
    ctx.lineTo(width-20, axisY);
    ctx.strokeStyle = 'rgba(148, 163, 184, 0.2)';
    ctx.stroke();

    const px = mapX(xPos);
    const py = mapY(getSum(xPos));

    // 补给舰
    config.anchors.forEach((a, index) => {
      const ax = mapX(a.pos);
      ctx.fillStyle = '#10b981';
      ctx.beginPath();
      ctx.arc(ax, axisY, 5, 0, Math.PI*2);
      ctx.fill();

      ctx.fillStyle = '#10b981';
      ctx.font = 'bold 10px monospace';
      ctx.textAlign = 'center';
      ctx.fillText(a.label, ax, axisY + 15);
      ctx.fillStyle = 'rgba(16, 185, 129, 0.5)';
      ctx.fillText(`${a.pos}`, ax, axisY + 28);

      // 动态拦截线
      const dist = Math.abs(xPos - a.pos);
      ctx.beginPath();
      ctx.setLineDash([4, 4]);
      ctx.strokeStyle = 'rgba(34, 211, 238, 0.2)';
      ctx.moveTo(px, axisY - 5);
      ctx.quadraticCurveTo((px + ax) / 2, axisY - 40 - index * 10, ax, axisY - 5);
      ctx.stroke();
      ctx.setLineDash([]);

      ctx.fillStyle = '#06b6d4';
      ctx.font = '9px monospace';
      ctx.fillText(`${dist.toFixed(1)}`, (px + ax) / 2, axisY - 25 - index * 10);
    });

    // 旗舰 P 信号
    ctx.beginPath();
    ctx.strokeStyle = 'rgba(234, 179, 8, 0.4)';
    ctx.moveTo(px, axisY);
    ctx.lineTo(px, py);
    ctx.stroke();

    ctx.fillStyle = '#facc15';
    ctx.beginPath();
    ctx.arc(px, axisY, 8, 0, Math.PI*2);
    ctx.fill();
    ctx.strokeStyle = '#fff';
    ctx.lineWidth = 1;
    ctx.stroke();

    ctx.fillStyle = '#facc15';
    ctx.font = 'bold 11px sans-serif';
    ctx.fillText("旗舰 P", px, axisY - 15);

    // UI 数据输出
    ctx.fillStyle = '#22d3ee';
    ctx.textAlign = 'right';
    ctx.font = 'bold 14px monospace';
    ctx.fillText(`TOTAL_ENERGY: ${getSum(xPos).toFixed(1)}`, width - 30, 30);
  }, [xPos, config]);

  return (
    <canvas
      ref={canvasRef}
      width={700}
      height={320}
      className="abs-canvas"
    />
  );
};

// ==========================================
// 4. 主程序界面
// ==========================================

export default function ModernMathExperiment() {
  const [examIdx, setExamIdx] = useState(0);
  const currentExam = MATH_LAB_EXAMS[examIdx];
  const [time, setTime] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [xPos, setXPos] = useState(0);

  useEffect(() => {
    let timer;
    if (isPlaying) {
      timer = setInterval(() => setTime(prev => (prev < 15 ? prev + 0.05 : 0)), 50);
    }
    return () => clearInterval(timer);
  }, [isPlaying]);

  return (
    <div className="modern-math-experiment">
      {/* 侧边指挥终端 */}
      <div className="command-terminal">
        <div className="terminal-header">
          <div className="status-indicator"></div>
          <div className="terminal-info">
            <div className="terminal-icon">
              <Radar size={24} className="animate-pulse" />
            </div>
            <div>
              <h1 className="terminal-title">Deep Sea Command</h1>
              <div className="terminal-version">VERSION 4.0.21-MATH</div>
            </div>
          </div>
        </div>
        <p className="terminal-description">
          坐标系已同步至北斗导航卫星。当前海域天气：晴。请下达推演指令。
        </p>

        <div className="exam-list">
          {MATH_LAB_EXAMS.map((exam, i) => (
            <button
              key={i}
              onClick={() => {
                setExamIdx(i);
                setTime(0);
                setIsPlaying(false);
                setXPos(0);
              }}
              className={`exam-button ${examIdx === i ? 'active' : ''}`}
            >
              <div className="exam-button-content">
                <div className={`exam-icon ${examIdx === i ? 'active' : ''}`}>
                  {i === 0 ? <Crosshair size={16} /> : <Zap size={16} />}
                </div>
                <div className="exam-text">
                  <h3>{exam.title}</h3>
                  <p>{exam.point}</p>
                </div>
              </div>
            </button>
          ))}
        </div>

        {/* 手册面板 */}
        <div className="manual-panel">
          <div className="manual-header">
            <Binary size={14} /> 战术参数说明
          </div>
          <div className="manual-grid">
            <div className="manual-item">
              <div className="manual-label">拦截时刻</div>
              <div className="manual-value">ΔD / ΔV</div>
            </div>
            <div className="manual-item">
              <div className="manual-label">能量最优</div>
              <div className="manual-value">Median(x)</div>
            </div>
          </div>
        </div>
      </div>

      {/* 主推演大屏 */}
      <div className="main-screen">
        <div className="mission-header">
          <div className="mission-info">
            <div className="mission-objective">
              <Activity size={10} /> Mission Objective
            </div>
            <h2 className="mission-title">{currentExam.title}</h2>
          </div>
          <div className="time-display">
            <div className="time-label">Elapsed Time</div>
            <div className="time-value">
              {time.toFixed(2)}<span className="time-unit">S</span>
            </div>
          </div>
        </div>
        <p className="mission-description">{currentExam.description}</p>

        <div className="experiment-canvas">
          {/* 装饰性背景层 */}
          <div className="canvas-background"></div>

          <div className="canvas-content">
            {currentExam.config.id === 'number_line' ? (
              <MotionLabRenderer t={time} config={currentExam.config} />
            ) : (
              <AbsOptimizationRenderer xPos={xPos} config={currentExam.config} />
            )}
          </div>

          {/* 底部推演控制台 */}
          <div className="control-panel">
            <div className="control-content">
              {currentExam.config.id === 'number_line' ? (
                <>
                  <button
                    onClick={() => setIsPlaying(!isPlaying)}
                    className={`play-button ${isPlaying ? 'playing' : ''}`}
                  >
                    {isPlaying ? <Pause size={28} fill="white" /> : <Play size={28} fill="white" className="ml-1" />}
                  </button>
                  <div className="time-control">
                    <div className="control-header">
                      <Timer size={12}/> Time Simulation
                      {currentExam.config.condition && (
                        <div className="solution-buttons">
                           {currentExam.config.condition.targets.map((targetT, idx) => (
                             <button
                               key={idx}
                               onClick={() => {
                                 setTime(targetT);
                                 setIsPlaying(false);
                               }}
                               className="solution-button"
                             >
                               计算解: {targetT}s
                             </button>
                           ))}
                        </div>
                      )}
                    </div>
                    <input
                      type="range"
                      min="0"
                      max="15"
                      step="0.01"
                      value={time}
                      onChange={(e) => {
                        setTime(parseFloat(e.target.value));
                        setIsPlaying(false);
                      }}
                      className="time-slider"
                    />
                  </div>
                </>
              ) : (
                <div className="position-control">
                  <div className="position-icon">
                    <Zap size={28} />
                  </div>
                  <div className="position-slider">
                    <div className="control-header">
                      调整旗舰 P 战术锚点
                      <span className="coord-display">
                        COORD: {xPos.toFixed(1)}
                      </span>
                    </div>
                    <input
                      type="range"
                      min="-15"
                      max="20"
                      step="0.1"
                      value={xPos}
                      onChange={(e) => setXPos(parseFloat(e.target.value))}
                      className="position-slider-input"
                    />
                  </div>
                </div>
              )}
              <button
                onClick={() => {
                  setTime(0);
                  setXPos(0);
                  setIsPlaying(false);
                }}
                className="reset-button"
              >
                <RotateCcw size={24} />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}