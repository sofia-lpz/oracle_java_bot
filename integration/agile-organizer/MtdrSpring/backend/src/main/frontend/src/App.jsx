import React, { useState, useEffect } from 'react';
import { Layout, theme } from 'antd';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SideBar from './components/SideBar';
// import RightSidebar from './components/RightSidebar';
import ChatBot from './pages/Chatbot';
import Task from './pages/Task';
import Users from './pages/Users';
import API_LIST from './API';
import Login from './pages/Login';

const { Content, Footer } = Layout;

function App() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  useEffect(() => {
    setLoading(true);
    fetch(API_LIST)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          setLoading(false);
          setItems(result);
        },
        (error) => {
          setLoading(false);
          setError(error);
        });
  }, []);

  return isAuthenticated ? (
    <Router>
      <Layout style={{ minHeight: '100vh', minWidth: '100vw', display: 'flex' }}>
        
        <SideBar />
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
              <Route path="/" element={<ChatBot />} />
              <Route path="/Task" element={<Task />} />
              <Route path="/Users" element={<Users />} />
            </Routes>
          </div>
        </Content>

        {/* Sidebar derecha vacía */}
        {/* <RightSidebar /> */}

      </Layout>
    </Router>
  ) : (
    <Login setIsAuthenticated={setIsAuthenticated} />
  );
};

export default App;