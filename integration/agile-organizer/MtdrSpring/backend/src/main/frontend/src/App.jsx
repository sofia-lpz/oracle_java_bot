import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from 'antd';
import SideBar from './components/SideBar';
import Task from './pages/Task';
import ChatBot from './pages/Chatbot';
import Users from './pages/Users';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import { isAuthenticated } from './utils/authUtils';
import './App.css';

const { Content } = Layout;

function App() {
  // Check if user is authenticated on initial load
  const [isAuth, setIsAuth] = useState(isAuthenticated());

  // Effect to check authentication status changes
  useEffect(() => {
    // Check if the token exists in localStorage
    setIsAuth(isAuthenticated());
    
    // Listen for storage events (if user logs out in another tab)
    const handleStorageChange = () => {
      setIsAuth(isAuthenticated());
    };
    
    window.addEventListener('storage', handleStorageChange);
    
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  const handleLogin = (token) => {
    localStorage.setItem('token', token);
    setIsAuth(true);
  };

  const PrivateRoute = ({ children }) => {
    return isAuth ? children : <Navigate to="/login" />;
  };

  return (
    <Router future={{ v7_startTransition: true }}>
      <Layout style={{ minHeight: '100vh', background: '#1d1d1d' }}>
        {isAuth && <SideBar />}
        <Layout>
          <Content style={{ 
            margin: '24px 16px', 
            padding: 24, 
            background: '#1d1d1d',
            minHeight: 280,
            overflow: 'auto'
          }}>
            <Routes>
              <Route path="/login" element={
                isAuth ? <Navigate to="/dashboard" /> : <Login onLogin={handleLogin} />
              } />
              <Route path="/" element={<PrivateRoute><Home /></PrivateRoute>} />
              <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
              <Route path="/task" element={<PrivateRoute><Task /></PrivateRoute>} />
              <Route path="/chatbot" element={<PrivateRoute><ChatBot /></PrivateRoute>} />
              <Route path="/users" element={<PrivateRoute><Users /></PrivateRoute>} />
            </Routes>
          </Content>
        </Layout>
      </Layout>
    </Router>
  );
}

export default App;