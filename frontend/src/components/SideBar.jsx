import React from 'react';
import { Layout, Menu } from 'antd';
import { Link } from 'react-router-dom';
import { CheckCircleOutlined, MessageOutlined, UserOutlined } from '@ant-design/icons';

const { Sider } = Layout;

const SideBar = () => {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider>
        <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']}>
          <Menu.Item key="1" icon={<MessageOutlined />}>
            <Link to="/">Chatbot</Link>
          </Menu.Item>
          <Menu.Item key="2" icon={<CheckCircleOutlined />}>
            <Link to="/chat">Task</Link>
          </Menu.Item>
          <Menu.Item key="3" icon={<UserOutlined />}>
            <Link to="/settings">Users</Link>
          </Menu.Item>
        </Menu>
      </Sider>
    </Layout>
  );
};

export default SideBar;