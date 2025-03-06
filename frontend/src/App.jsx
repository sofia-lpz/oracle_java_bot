import React from 'react';
import { Layout, theme } from 'antd';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SideBar from './components/SideBar';
// import RightSidebar from './components/RightSidebar';
import ChatBot from './pages/ChatBot';
import Task from './pages/Task';
import Users from './pages/Users';

const { Content, Footer } = Layout;

const App = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const siderWidth = 0; // Ancho fijo de ambas Sidebars

  return (
    <Router>
      <Layout style={{ minHeight: '100vh', minWidth: '150vh', display: 'flex' }}>
        
        {/* Sidebar izquierda */}
        <SideBar />

        {/* Contenido centrado entre ambas Sidebars */}
        <Content style={{ padding: '24px', flex: 1, background: '#1f1f1f'}}>
          <div
            style={{
              minHeight: 640,
              minWidth: 730,
              background: "#1f1f1f",
              borderRadius: borderRadiusLG,
              padding: 24,
              color: 'white',
              fontSize: '8px',
            }}
          >
            <Routes>
              <Route path="/ChatBot" element={<ChatBot />} />
              <Route path="/Task" element={<Task />} />
              <Route path="/Users" element={<Users />} />
              <Route path="/" element={<h2>Bienvenido</h2>} />
            </Routes>
          </div>
        </Content>

        {/* Sidebar derecha vacía */}
        {/* <RightSidebar /> */}

      </Layout>
    </Router>
  );
};

export default App;