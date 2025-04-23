import React from 'react';
import { Form, Input, Button, Typography, Checkbox, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import API_LOGIN from '../API_LOGIN';

const { Title, Text } = Typography;

const Login = ({ onLogin }) => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const onFinish = async (values) => {
    try {
      console.log('Intentando login con:', values);
      
      const response = await fetch(API_LOGIN, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: values.username,
          password: values.password
        }),
      });

      if (!response.ok) {
        const errorData = await response.text();
        console.error('Error en la respuesta:', errorData);
        throw new Error(`Error en la autenticación: ${response.status}`);
      }

      const data = await response.json();
      
      if (data.token) {
        onLogin(data.token);
        navigate('/dashboard');
        message.success('Inicio de sesión exitoso');
      } else {
        throw new Error('No se recibió token en la respuesta');
      }
    } catch (error) {
      console.error('Error completo en el login:', error);
      message.error('Error al iniciar sesión. Por favor, verifica tus credenciales.');
    }
  };

  return (
    <div className="login-container">
      <div className="login-left-panel">
        <img 
          src="/logo.png" 
          alt="Oracle Logo" 
          className="oracle-logo" 
        />
        <img 
          src="/oracle_O.png" 
          alt="Oracle O" 
          className="oracle-o" 
        />
        <div className="login-icons">
          <div className="icon-circle thinking">🤔</div>
          <div className="icon-circle writing">✍️</div>
          <div className="icon-circle picture">🖼️</div>
        </div>
        <Title level={2} className="design-title">Welcome to<br/>Oracle Cloud</Title>
        <Text className="design-subtitle">
          Access your Oracle Cloud Infrastructure and manage your resources efficiently.
        </Text>
        <div className="navigation-dots">
          <span className="dot active"></span>
          <span className="dot"></span>
          <span className="dot"></span>
        </div>
      </div>

      <div className="login-right-panel">
        <Title level={2} className="login-title">Sign in to Oracle</Title>

        <Form
          form={form}
          name="login"
          layout="vertical"
          onFinish={onFinish}
          className="login-form"
        >
          <Form.Item
            label="Oracle Account"
            name="username"
            rules={[{ required: true, message: 'Por favor ingresa tu cuenta de Oracle' }]}
          >
            <Input placeholder="Enter your Oracle Account" />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: 'Por favor ingresa tu contraseña' }]}
            extra="Must be 8 characters at least"
          >
            <Input type="password" placeholder="Enter your password" />
          </Form.Item>

          <Form.Item>
            <div className="login-options">
              <Checkbox>Remember me</Checkbox>
              <a className="forgot-password" href="#">Forgot Password?</a>
            </div>
          </Form.Item>

          <Button type="primary" htmlType="submit" className="login-btn">
            Sign in
          </Button>
        </Form>

        <div className="login-footer">
          ©2025 Oracle Corporation. All rights reserved.
        </div>
      </div>
    </div>
  );
};

export default Login;

<style>{`
  ::placeholder {
    color: #ffffff; /* Color blanco para el placeholder */
    opacity: 1; /* Asegura que el color se aplique completamente */
  }
`}</style>