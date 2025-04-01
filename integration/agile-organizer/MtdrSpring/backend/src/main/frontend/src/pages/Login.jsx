import React from 'react';
import { Form, Input, Button, Typography, Card } from 'antd';

const { Title } = Typography;

const Login = ({ setIsAuthenticated }) => {
  const onFinish = (values) => {
    console.log('Login data:', values);
    setIsAuthenticated(true);
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      width: '100vw',
      backgroundColor: '#272727'
    }}>
      <Card style={{ width: 600, backgroundColor: '#272727', border: '1px solid #272727' }}>
        <img
          src="/oracle_O.png"
          alt="Logo"
          style={{
            display: 'block',
            margin: '0 auto 2px auto',
            width: '100px',
            height: '100px',
            borderRadius: '50%',
            objectFit: 'cover'
          }}
        />
        <Title level={2} style={{ textAlign: 'center', color: 'white' }}>Login</Title>
        <style>
          {`
            .ant-form-item-label > label {
              color: white !important;
            }
          `}
        </style>
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
            <Input placeholder="Enter your email" style={{ backgroundColor: 'white', color: 'black' }} />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: 'Please enter your password' }]}
          >
            <Input.Password placeholder="Enter your password" style={{ backgroundColor: 'white', color: 'black' }} />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block style={{ backgroundColor: '#c6624b', borderColor: '#c6624b' }}>
              Log In
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;