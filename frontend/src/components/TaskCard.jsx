import React, { useEffect, useState } from 'react';
import { Card, Avatar, Row, Col } from 'antd';
import { UserOutlined } from '@ant-design/icons';

const { Meta } = Card;

const TaskCard = ({ title, description, dueDate, avatarUrl, storyPoints }) => (
  <Card
    style={{
        width: 450,
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
    // tasks hardcoded, cambiar para integracion
  const tasks = [
    {
      title: 'ER to SQL OCI',
      description: 'Design and implement SQL scripts in Oracle Cloud Infrastructure (OCI) based on an Entity-Relationship (ER) model.',
      dueDate: '29/02/25',
      avatarUrl: 'https://via.placeholder.com/32',
      storyPoints: '0/3',
    },
  ];

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
