import React from 'react';
import { DndContext, closestCenter } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import { Card, Avatar, Button, Modal } from 'antd';
import { UserOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

const { Meta } = Card;

//Black #1d1d1d
//Gray #272727
//Oracle #c6624b

const TaskCard = ({ 
  id,
  title, 
  description, 
  dueDate, 
  avatarUrl,
  username,
  storyPoints, 
  estimatedHours, 
  realHours, 
  onDelete
}) => {
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = React.useState(false);
  
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

  const showEditModal = () => {
    setIsEditModalOpen(true);
  };

  const handleEditCancel = () => {
    setIsEditModalOpen(false);
  };

  const { attributes, listeners, setNodeRef, transform, transition } = useSortable({ id: id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    borderRadius: '8px',
    marginTop: '4px',
    marginBottom: '4px',
    // boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', //Shadow area drag and drop
    background: '#1d1d1d',
    border: '2px solid #1d1d1d',
    userSelect: 'none',
    zIndex: transform ? 1050 : 10,
  };

  return (
    <Card
      className="custom-task-card"
      style={style}
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
        icon={<EditOutlined style={{ color: 'white' }} />}
        onClick={(e) => {
          e.stopPropagation();
          showEditModal();
        }}
        aria-label="Edit task"
        style={{
          position: 'absolute',
          top: '8px',
          right: '40px',
          backgroundColor: 'transparent',
          border: 'none',
          color: '#c6624b',
          zIndex: 1,
        }}
      />
      <div ref={setNodeRef} {...attributes} {...listeners} style={style}>
        <Meta
          avatar={<Avatar src={avatarUrl} icon={<UserOutlined />} />}
          title={<span style={{color: '#ffffff'}}>{title}</span>}
          description={
            <>
              <p style={{fontWeight: 'bold' ,color: '#c6624b' }}>Due on {dueDate}</p>
            </>
          }
        />
      </div>

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

      <Modal
        title={<div style={{ fontSize: '20px', fontWeight: '600', color: '#fff' }}>{title}</div>}
        open={isEditModalOpen}
        onCancel={handleEditCancel}
        centered
        width={600}
        bodyStyle={{ 
          backgroundColor: '#1d1d1d',
          padding: '24px',
          borderTop: '2px solid #333',
          borderBottom: '2px solid #333'
        }}
        style={{
          top: 20
        }}
        modalRender={(modal) => (
          <div style={{ 
            border: '3px solid #333',
            borderRadius: '8px',
            overflow: 'hidden'
          }}>
            {modal}
          </div>
        )}
        footer={
          <div style={{ 
            display: 'flex', 
            justifyContent: 'space-between',
            padding: '12px 24px',
            backgroundColor: '#1d1d1d',
            borderTop: '2px solid #333'
          }}>
            <Button
              type="primary"
              danger
              onClick={() => {
                handleEditCancel();
                showDeleteModal();
              }}
            >
              Delete
            </Button>
          </div>
        }
      >
        <div style={{ display: 'grid', gridTemplateColumns: '120px 1fr', rowGap: '16px' }}>
          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Description:</div>
          <div style={{ color: '#fff' }}>{description}</div>
          
          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Due Date:</div>
          <div style={{ color: '#fff' }}>{dueDate}</div>
          
          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Story Points:</div>
          <div style={{ color: '#fff' }}>{storyPoints || 'Not set'}</div>
          
          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Estimated Hours:</div>
          <div style={{ color: '#fff' }}>{estimatedHours || 'Not set'}</div>
          
          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Real Hours:</div>
          <div style={{ color: '#fff' }}>{realHours || 'Not set'}</div>

          <div style={{ color: '#8c8c8c', fontWeight: '500' }}>Assigned to:</div>
          <div style={{ color: '#fff' }}>{username || 'Not set'}</div>
        </div>
      </Modal>
    </Card>
  );
};

export default TaskCard;