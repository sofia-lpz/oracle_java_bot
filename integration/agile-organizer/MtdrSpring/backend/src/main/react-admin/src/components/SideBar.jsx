import React, { useState, useEffect } from 'react';
import { Menu, Avatar, Popover } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import { 
  HomeOutlined,
  MessageOutlined,
  ProjectOutlined,
  UserOutlined,
  BarChartOutlined,
  LogoutOutlined
} from '@ant-design/icons';

const SideBar = () => {
  const location = useLocation();
  const [isCollapsed, setIsCollapsed] = useState(window.innerWidth <= 767);
  const [popoverVisible, setPopoverVisible] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      setIsCollapsed(window.innerWidth <= 767);
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const handleVisibleChange = (visible) => {
    setPopoverVisible(visible);
  };

  return (
    <>
      <style>{`
        .sidebar {
          transition: width 0.3s ease;
          width: ${isCollapsed ? '100px' : '250px'};
          display: flex;
          flex-direction: column;
          align-items: center;
        }

        .sidebar img {
          width: ${isCollapsed ? '40px' : '100px'};
          height: ${isCollapsed ? '40px' : '100px'};
        }

        .sidebar span {
          display: ${isCollapsed ? 'inline' : 'inline'};
        }

        .sidebar-menu .ant-menu-item {
          justify-content: center;
        }

        .ant-popover-inner {
          background-color: #272727 !important;
        }
      `}</style>
      <div className="sidebar" style={{ 
        backgroundColor: '#1a1a1a', 
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
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
              width: isCollapsed ? '40px' : '100px',
              height: isCollapsed ? '40px' : '100px',
              marginBottom: '12px'
            }}
          />
          {!isCollapsed && (
            <span style={{ color: 'white', fontSize: '18px', fontWeight: 'bold' }}>
              My To-Do
            </span>
          )}
          <span style={{ 
            color: '#666', 
            fontSize: '12px' 
          }}>
            v3.0
          </span>
        </div>

        {/* Main Navigation */}
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          style={{ 
            backgroundColor: 'transparent',
            border: 'none',
            width: '100%'
          }}
          theme="dark"
          className="sidebar-menu"
        >
          <Menu.Item key="/dashboard">
            <Link to="/dashboard">
              <HomeOutlined />
              <span>Dashboard</span>
            </Link>
          </Menu.Item>
          <Menu.Item key="/charts">
            <Link to="/charts">
              <BarChartOutlined />
              <span>Charts</span>
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

        {/* User Profile */}
        <div style={{ 
          marginTop: 'auto',
          padding: '12px',
          borderTop: '1px solid #333',
          display: 'flex',
          alignItems: 'center',
          gap: '12px',
          width: '100%',
          justifyContent: 'center'
        }}>
          <Popover
            content={<div style={{ backgroundColor: '#272727', padding: '8px', borderRadius: '4px' }}>
              <button style={{ 
                backgroundColor: '#ff4d4f', 
                color: 'white', 
                border: 'none', 
                padding: '8px', 
                borderRadius: '4px', 
                cursor: 'pointer' 
              }} 
                onClick={() => {
                  localStorage.removeItem('token');
                  window.location.pathname = '/';
                }}> 
                <LogoutOutlined style={{ fontSize: '16px' }} /> 
              </button>
            </div>}
            trigger="click"
            visible={popoverVisible}
            onVisibleChange={handleVisibleChange}
          >
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              gap: '12px', 
              width: '100%', 
              justifyContent: 'center' 
            }}>
              <Avatar size={32} icon={<UserOutlined />} />
              {!isCollapsed && (
                <div style={{ flex: 1, textAlign: 'center' }}>
                  <div style={{ color: 'white', fontSize: '14px' }}>Admin User</div>
                  <div style={{ color: '#666', fontSize: '12px' }}>admin@agileorganizer.com</div>
                </div>
              )}
            </div>
          </Popover>
        </div>
      </div>
    </>
  );
};

export default SideBar;