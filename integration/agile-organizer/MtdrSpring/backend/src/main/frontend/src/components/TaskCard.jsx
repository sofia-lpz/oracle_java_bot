import React from 'react';
import { Card, Avatar, Button, Modal } from 'antd';
import { UserOutlined, DeleteOutlined } from '@ant-design/icons';

const { Meta } = Card;

//Black #1d1d1d
//Gray #272727
//Oracle #c6624b

const TaskCard = ({ 
  title, 
  description, 
  dueDate, 
  avatarUrl, 
  storyPoints, 
  estimatedHours, 
  realHours, 
  onDelete
}) => {
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  
  const showDeleteModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
    onDelete();
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <Card
      className="custom-task-card"
      style={{
        borderRadius: '8px',
        marginTop: '4px',
        marginBottom: '4px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        background: '#1d1d1d',
        border: '2px solid #1d1d1d',
        userSelect: 'none',
      }}
      actions={[
        <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', backgroundColor: '#1d1d1d' }}>
          <p style={{fontWeight: 'bold', color: '#c6624b'}}>
          Story Points <br /> {storyPoints ?? 'N/A'}
          </p>
          <p style={{fontWeight: 'bold', color: '#c6624b'}}>Estimated <br /> {estimatedHours}</p>
          <p style={{fontWeight: 'bold', color: '#c6624b'}}>Real <br />{realHours}</p>
        </div>
      ]}
    >
      <Button
        type="text"
        icon={<DeleteOutlined style={{ color: 'white' }} />}
        onClick={showDeleteModal}
        aria-label="Delete task"
        style={{
          position: 'absolute',
          top: '8px',
          right: '8px',
          backgroundColor: 'transparent',
          border: 'none',
          color: '#c6624b',
          zIndex: 1,
        }}
      />
      <Meta
        avatar={<Avatar src={avatarUrl} icon={<UserOutlined />} />}
        title={<span style={{color: '#ffffff'}}>{title}</span>}
        description={
          <>
            <p style={{color: '#ffffff'}}>{description}</p>
            <p style={{fontWeight: 'bold' ,color: '#c6624b' }}>Due on {dueDate}</p>
          </>
        }
      />

      <Modal
        title="Confirm Deletion"
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Delete"
        cancelText="Cancel"
        okButtonProps={{ danger: true }}
      >
        <p style={{color: 'white'}}>Are you sure you want to delete this task?</p>
      </Modal>  
    </Card>
  );
};

export default TaskCard;