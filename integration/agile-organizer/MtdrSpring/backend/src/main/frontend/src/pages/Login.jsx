import React from 'react';
import { Form, Input, Button, Typography, Card } from 'antd';

const { Title } = Typography;

const Login = ({ setIsAuthenticated }) => {
  const onFinish = (values) => {
    console.log('Login data:', values);
    setIsAuthenticated(true);
  };

  return (
    <div className="login-container">
      <Card className="login-card">
        <img
          src="/oracle_O.png"
          alt="Logo"
          className="login-logo"
        />
        <Title level={2} className="login-title">Login</Title>
        <Form
          name="login"
          layout="vertical"
          onFinish={onFinish}
        >
          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true, message: 'Please enter your email' }]}
          >
            <Input className="login-input" placeholder="Enter your email" />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: 'Please enter your password' }]}
          >
            <Input.Password className="login-input" placeholder="Enter your password" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block className="login-button">
              Log In
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;