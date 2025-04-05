import React from 'react';
import { Layout, Menu } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import { CheckCircleOutlined, MessageOutlined, UserOutlined } from '@ant-design/icons';

const { Sider } = Layout;

const SideBar = () => {
  const location = useLocation(); // Obtener la ruta actual

  return (
    <Sider width={250} style={{ background: '#1f1f1f', height: '100vh', padding: '20px 15px 20px 20px' }}>
      <div style={{ textAlign: 'center', padding: '20px 0' }}>
        <img 
          src="/logo.png" // Asegúrate de que la imagen esté en `/public/`
          alt="Oracle"
          style={{ width: '120px', height: 'auto', marginBottom: '20px' }} 
        />
      </div>

      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[location.pathname]}
        style={{ background: '#1f1f1f', borderRight: 'none' }}
      >
        <Menu.Item
          key="/"
          icon={<MessageOutlined />}
          style={{
            background: location.pathname === "/" ? "#c6624b" : "transparent",
            color: location.pathname === "/" ? "white" : "#aaa",
            marginBottom: '10px',
            padding: '10px 20px',
            borderRadius: '8px',
          }}
        >
          <Link to="/" style={{ color: 'inherit' }}>ChatBot</Link>
        </Menu.Item>

        <Menu.Item
          key="/Task"
          icon={<CheckCircleOutlined />}
          style={{
            background: location.pathname === "/Task" ? "#c6624b" : "transparent",
            color: location.pathname === "/Task" ? "white" : "#aaa",
            marginBottom: '10px',
            padding: '10px 20px',
            borderRadius: '8px',
          }}
        >
          <Link to="/Task" style={{ color: 'inherit' }}>Task</Link>
        </Menu.Item>

        <Menu.Item
          key="/Users"
          icon={<UserOutlined />}
          style={{
            background: location.pathname === "/Users" ? "#c6624b" : "transparent",
            color: location.pathname === "/Users" ? "white" : "#aaa",
            marginBottom: '10px',
            padding: '10px 20px',
            borderRadius: '8px',
          }}
        >
          <Link to="/Users" style={{ color: 'inherit' }}>Users</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default SideBar;