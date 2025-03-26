import React from 'react';
import { Layout, Menu } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import { CheckCircleOutlined, MessageOutlined, UserOutlined } from '@ant-design/icons';

const { Sider } = Layout;

const SideBar = () => {
  const location = useLocation(); // Obtener la ruta actual

  return (
    <Sider width={220} height={220} style={{ background: '#181818' }}>
      <div style={{ textAlign: 'center', padding: '20px' }}>
        <img 
          src="/logo.png" // Asegúrate de que la imagen esté en `/public/`
          alt="Oracle"
          style={{ width: '100px', height: 'auto' }} 
        />
      </div>

      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[location.pathname]}
        style={{ background: '#181818' }}
      >
        <Menu.Item
          key="/"
          icon={<MessageOutlined />}
          style={{
            background: location.pathname === "/" ? "#b9503c" : "transparent", // Color de selección
            color: location.pathname === "/" ? "white" : "inherit",
          }}
        >
          <Link to="/" style={{ color: 'white' }}>ChatBot</Link>
        </Menu.Item>

        <Menu.Item
          key="/Task"
          icon={<CheckCircleOutlined />}
          style={{
            background: location.pathname === "/Task" ? "#b9503c" : "transparent",
            color: location.pathname === "/Task" ? "white" : "inherit",
          }}
        >
          <Link to="/Task" style={{ color: 'white' }}>Task</Link>
        </Menu.Item>

        <Menu.Item
          key="/Users"
          icon={<UserOutlined />}
          style={{
            background: location.pathname === "/Users" ? "#b9503c" : "transparent",
            color: location.pathname === "/Users" ? "white" : "inherit",
          }}
        >
          <Link to="/Users" style={{ color: 'white' }}>Users</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default SideBar;