import React from 'react';
import { Layout } from 'antd';

const { Sider } = Layout;

const RightSidebar = () => {
  return (
    <Sider width={210} style={{ background: '#001529', height: '100vh', position: 'fixed', right: 0 }} />
  );
};

export default RightSidebar;