import React, { useEffect, useState } from 'react';
import { Card, Avatar, Row, Col } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import API_LIST from '../API';

const { Meta } = Card;

const TaskCard = ({ title, description, dueDate, avatarUrl, storyPoints }) => (
  <Card
    style={{
        width: 270,
        borderRadius: '8px',
        marginTop: '16px',
        marginBottom: '16px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    }}
    actions={[
      <span>{storyPoints}</span>,
    ]}
  >
    <Meta
      avatar={<Avatar src={avatarUrl} icon={<UserOutlined />} />}
      title={title}
      description={
        <>
          <p>{description}</p>
          <p style={{ color: '#1890ff' }}>Due on {dueDate}</p>
        </>
      }
    />
  </Card>
);

const KanbanBoard = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    fetch(API_LIST)
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Something went wrong fetching tasks');
        }
      })
      .then(
        (result) => {
          setLoading(false);
          setTasks(result);
        },
        (error) => {
          setLoading(false);
          setError(error);
        });
  }, []);

  if (loading) return <p>Loading tasks...</p>;
  if (error) return <p>Error loading tasks: {error.message}</p>;

  return (
    <Row gutter={[16, 16]}>
      {tasks.map((task, index) => (
        <Col key={index} xs={24} sm={12} md={8}>
          <TaskCard {...task} />
        </Col>
      ))}
    </Row>
  );
};

export default KanbanBoard;