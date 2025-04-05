import React from 'react';
import { Menu, Avatar } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import { 
  HomeOutlined,
  MessageOutlined,
  ProjectOutlined,
  UserOutlined
} from '@ant-design/icons';

const SideBar = () => {
  const location = useLocation();

  return (
    <>
      <style>{`
        .sidebar-menu .ant-menu-item-selected {
          background-color: #272727 !important;
          border-radius: 6px !important;
          margin: 4px 8px !important;
          width: calc(100% - 16px) !important;
          position: relative !important;
        }
        .sidebar-menu .ant-menu-item-selected::before {
          content: '';
          position: absolute;
          left: 0;
          top: 0;
          bottom: 0;
          width: 3px;
          background-color: #c6624b;
          border-top-left-radius: 6px;
          border-bottom-left-radius: 6px;
        }
      `}</style>
      <div style={{ 
        backgroundColor: '#1a1a1a', 
        height: '100vh',
        width: '250px',
        display: 'flex',
        flexDirection: 'column',
        padding: '16px'
      }}>
        {/* Header */}
        <div style={{ 
          display: 'flex', 
          alignItems: 'center', 
          padding: '8px 16px',
          marginBottom: '24px',
          flexDirection: 'column'
        }}>
          <img
            src="/oracle_O.png"
            alt="Oracle Logo"
            style={{
              width: '100px',
              height: '100px',
              marginBottom: '12px'
            }}
          />
          <span style={{ color: 'white', fontSize: '18px', fontWeight: 'bold' }}>
            My To-Do
          </span>
          <span style={{ 
            color: '#666', 
            fontSize: '12px' 
          }}>
            v3.0
          </span>
        </div>

        {/* Main Navigation */}
        <div style={{ marginBottom: '24px' }}>
          <div style={{ 
            color: '#666',
            fontSize: '12px',
            padding: '8px 16px',
            fontWeight: 'bold'
          }}>
            NAVIGATION
          </div>
          <Menu
            mode="inline"
            selectedKeys={[location.pathname]}
            style={{ 
              backgroundColor: 'transparent',
              border: 'none'
            }}
            theme="dark"
            className="sidebar-menu"
          >
            <Menu.Item key="/home">
              <Link to="/home">
                <HomeOutlined />
                <span>Home</span>
              </Link>
            </Menu.Item>
            <Menu.Item key="/chatbot">
              <Link to="/chatbot">
                <MessageOutlined />
                <span>Chatbot</span>
              </Link>
            </Menu.Item>
            <Menu.Item key="/task">
              <Link to="/task">
                <ProjectOutlined />
                <span>Kanban</span>
              </Link>
            </Menu.Item>
            <Menu.Item key="/users">
              <Link to="/users">
                <UserOutlined />
                <span>Users</span>
              </Link>
            </Menu.Item>
          </Menu>
        </div>

        {/* User Profile */}
        <div style={{ 
          marginTop: 'auto',
          padding: '12px',
          borderTop: '1px solid #333',
          display: 'flex',
          alignItems: 'center',
          gap: '12px'
        }}>
          <Avatar size={32} icon={<UserOutlined />} />
          <div style={{ flex: 1 }}>
            <div style={{ color: 'white', fontSize: '14px' }}>Admin User</div>
            <div style={{ color: '#666', fontSize: '12px' }}>admin@agileorganizer.com</div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SideBar;