import React, { useState, useEffect } from 'react';
import '../App.css';
import { Table, Button, Input } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import API_USERS from '../API_USERS';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

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
    },
    {
      title: 'Team',
      dataIndex: ['team', 'teamName'],
      key: 'teamName',
    },
    {
      title: 'Role',
      dataIndex: ['team', 'teamDescription'],
      key: 'teamDescription',
    },
    {
      title: 'Authority',
      dataIndex: ['authorities', 0, 'authority'],
      key: 'authority',
    },
  ];

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
        <Button type="primary" icon={<PlusOutlined />} style={{ backgroundColor: '#c6624b', borderColor: '#c6624b' }}>Invite</Button>
      </div>
      <Table columns={columns} dataSource={filteredUsers} rowKey="id" pagination={false} style={{ backgroundColor: '#272727', color: 'white' }} />
    </div>
  );
};

export default Users;