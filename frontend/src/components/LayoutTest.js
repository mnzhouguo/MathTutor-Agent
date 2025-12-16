/**
 * 布局测试组件
 * 用于验证左右布局是否正确
 */

import React from 'react';
import './LayoutTest.css';

const LayoutTest = () => {
  return (
    <div className="layout-test">
      <div className="test-header">
        <h2>🎯 布局测试页面</h2>
        <p>验证左右布局：对话框 + 黑板</p>
      </div>
      <div className="test-content">
        <div className="left-panel">
          <h3>左侧：对话框</h3>
          <div className="demo-chat">
            <div className="demo-message user">
              <strong>用户：</strong> 如何解绝对值不等式？
            </div>
            <div className="demo-message assistant">
              <strong>老师：</strong> 很好的问题！让我们一步步来思考。
              **步骤1**：先找到绝对值的零点
              **步骤2**：根据区间去掉绝对值符号
            </div>
          </div>
        </div>
        <div className="right-panel">
          <h3>右侧：黑板</h3>
          <div className="demo-blackboard">
            <div className="demo-formula">
              |2x - 1| < 3
            </div>
            <div className="demo-steps">
              <div className="demo-step">
                <span className="step-number">1</span>
                零点：2x - 1 = 0 → x = 0.5
              </div>
              <div className="demo-step">
                <span className="step-number">2</span>
                分类讨论：x < 0.5 和 x > 0.5
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LayoutTest;