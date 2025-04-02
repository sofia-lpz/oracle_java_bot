import React from 'react';
import { Form, Input, Button, Typography, Checkbox } from 'antd';

const { Title } = Typography;

const Login = ({ setIsAuthenticated }) => {
  const onFinish = (values) => {
    console.log('Login data:', values);
    setIsAuthenticated(true);
  };

  return (
    <div className="login-wrapper">
      <div className="login-form-side">
      <img
          src="/oracle_O.png"
          alt="Logo"
          className="login-logo-mini"
        />
        <Title level={2} className="login-title">Sign In</Title>

        <Form
          name="login"
          layout="vertical"
          onFinish={onFinish}
          className="custom-login-form"
        >
          <Form.Item
            label="Username"
            name="username"
            rules={[{ required: true, message: 'Please enter your username' }]}
          >
            <Input placeholder="Username" />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: 'Please enter your password' }]}
          >
            <Input.Password placeholder="Password" />
          </Form.Item>

          <Form.Item>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Checkbox>Remember Me</Checkbox>
              <a className="forgot-password" href="#">Forgot Password</a>
            </div>
          </Form.Item>
          <Button type="primary" htmlType="submit" className="login-btn">Sign In</Button>
        </Form>
      </div>

      <div className="login-welcome-side">
        <img
          src="/logo.png"
          alt="Logo"
          className="login-logo"
        />
        <Title level={2} classname="login-title-white">Welcome to login</Title>
        <p>Don't have an account?</p>
        <Button type="default" className="signup-btn">Sign Up</Button>
      </div>
    </div>
  );
};

export default Login;