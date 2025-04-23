import React, { useState, useEffect } from 'react';
import '../App.css';
import { Table, Button, Input } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import API_USERS from '../API_USERS';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        const response = await fetch(API_USERS);

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

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Email address',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
    },
    {
      title: 'Permission',
      dataIndex: 'permission',
      key: 'permission',
    },
  ];

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>{error.message}</div>;

  return (
    <div style={{ padding: '20px', backgroundColor: '#1d1d1d', minHeight: '100vh' }}>
      <h1 style={{ color: 'white' }}>Team <span style={{ color: '#c6624b' }}>{users.length} users</span></h1>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
        <Input placeholder="Filter by: All" prefix={<SearchOutlined />} style={{ width: '200px', backgroundColor: '#272727', color: 'white', borderColor: '#444' }} />
        <Button type="primary" icon={<PlusOutlined />} style={{ backgroundColor: '#c6624b', borderColor: '#c6624b' }}>Invite</Button>
      </div>
      <Table columns={columns} dataSource={users} rowKey="id" pagination={false} style={{ backgroundColor: '#272727', color: 'white' }} />
    </div>
  );
};

export default Users;