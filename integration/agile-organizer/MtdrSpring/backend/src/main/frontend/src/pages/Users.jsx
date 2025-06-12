import React, { useState, useEffect } from 'react';
import '../App.css';
import { Table, Button, Input, Modal, Form, message, Select } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import { API_USERS, API_SIGNUP } from '../API';

import { authenticatedFetch } from '../utils/authUtils';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        const response = await authenticatedFetch(API_USERS);

        if (!response.ok) {
          throw new Error(`Error del servidor: ${response.status}`);
        }

        const usersData = await response.json();
        setUsers(usersData);
        setError(null);
      } catch (error) {
        console.error('Error fetching users:', error);
        setError(new Error('No se pudo conectar al servidor.'));
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  const handleSearch = (event) => {
    setSearchTerm(event.target.value);
  };

  const filteredUsers = users.filter(user =>
    user.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (name) => name || 'No name provided'
    },
    {
      title: 'Team',
      dataIndex: ['team', 'teamName'],
      key: 'teamName',
      render: (teamName, record) => (record.team?.teamName || 'No team')
    },
    {
      title: 'Role',
      dataIndex: ['role'],
      key: 'role',
      render: (role) => role || 'No role'
    }
  ];

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
  };

  const handleSubmit = async (values) => {
    try {
      setLoading(true);
      const body = {
        phoneNumber: values.phoneNumber,
        password: values.password,
        name: values.name,
        role: values.rol
      };
      const response = await authenticatedFetch(API_SIGNUP, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Error del servidor: ${response.status}`);
      }

      message.success('Usuario registrado exitosamente');
      setIsModalVisible(false);
      form.resetFields();
      // Opcional: recargar usuarios
      const usersResponse = await authenticatedFetch(API_USERS);
      if (usersResponse.ok) {
        const usersData = await usersResponse.json();
        setUsers(usersData);
      }
    } catch (error) {
      console.error('Error al registrar usuario:', error);
      message.error(error.message || 'Error al registrar usuario');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>{error.message}</div>;

  return (
    <div style={{ padding: '20px', backgroundColor: '#1d1d1d', minHeight: '100vh' }}>
      <h1 style={{ color: 'white' }}>Team <span style={{ color: '#c6624b', fontSize: 'medium' }}>{users.length} users</span></h1>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
        <Input 
          placeholder="Filter by: All" 
          prefix={<SearchOutlined />} 
          style={{ 
            width: '100%', 
            maxWidth: '200px', 
            backgroundColor: '#272727', 
            color: 'white', 
            borderColor: '#444', 
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.2)',
transition: 'border-color 0.3s, box-shadow 0.3s',
          }} 
          value={searchTerm}
          onChange={handleSearch}
          onFocus={(e) => e.target.style.borderColor = '#c6624b'}
          onBlur={(e) => e.target.style.borderColor = '#444'}
        />
        <Button 
          type="primary" 
          icon={<PlusOutlined />} 
          style={{ backgroundColor: '#c6624b', borderColor: '#c6624b' }}
          onClick={showModal}
        >
          Agregar Usuario
        </Button>
      </div>
      <Table columns={columns} dataSource={filteredUsers} rowKey="id" pagination={false} style={{ backgroundColor: '#272727', color: 'white' }} />

      <Modal
        title="Agregar Nuevo Usuario"
        open={isModalVisible}
        onCancel={handleCancel}
        footer={null}
        style={{ backgroundColor: '#272727' }}
      >
        <Form
          form={form}
          onFinish={handleSubmit}
          layout="vertical"
          style={{ color: 'white' }}
        >
          <Form.Item
            name="name"
            label="Nombre"
            rules={[{ required: true, message: 'Por favor ingrese el nombre' }]}
          >
            <Input style={{ backgroundColor: '#1d1d1d', color: 'white', borderColor: '#444' }} />
          </Form.Item>

          <Form.Item
            name="phoneNumber"
            label="Número de Teléfono"
            rules={[{ required: true, message: 'Por favor ingrese el número de teléfono' }]}
          >
            <Input style={{ backgroundColor: '#1d1d1d', color: 'white', borderColor: '#444' }} />
          </Form.Item>

          <Form.Item
            name="password"
            label="Contraseña"
            rules={[{ required: true, message: 'Por favor ingrese la contraseña' }, { min: 6, message: 'La contraseña debe tener al menos 6 caracteres' }]}
          >
            <Input.Password style={{ backgroundColor: '#1d1d1d', color: 'white', borderColor: '#444' }} />
          </Form.Item>

          <Form.Item
            name="rol"
            label="Rol"
            rules={[{ required: true, message: 'Por favor seleccione un rol' }]}
          >
            <Select
              style={{ backgroundColor: '#1d1d1d', color: 'white' }}
              options={[
                { value: 'USER', label: 'Usuario' },
                { value: 'ADMIN', label: 'Administrador' }
              ]}
            />
          </Form.Item>

          <Form.Item>
            <Button 
              type="primary" 
              htmlType="submit" 
              loading={loading}
              style={{ backgroundColor: '#c6624b', borderColor: '#c6624b', marginRight: '8px' }}
            >
              Guardar
            </Button>
            <Button onClick={handleCancel}>
              Cancelar
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Users;

<style>{`
  .users-container {
    padding: 20px;
    box-sizing: border-box;
  }

  .users-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .users-header input {
    width: 100%;
    margin-bottom: 10px;
  }

    .ant-input-password .ant-input-suffix .anticon {
    color: white !important;
  }

  .ant-input-password .ant-input-suffix .anticon:hover {
    color: #c6624b !important;
  }

  @media (min-width: 768px) {
    .users-header {
      flex-direction: row;
      align-items: center;
    }

    .users-header input {
      width: auto;
      margin-bottom: 0;
    }
  }
`}</style>