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
        icon={<DeleteOutlined style={{ color: 'white' }} />}
        onClick={(e) => {
          e.stopPropagation();
          showDeleteModal();
        }}
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
              <p style={{color: '#ffffff'}}>{description}</p>
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
        title="Edit Task"
        open={isEditModalOpen}
        onCancel={handleEditCancel}
        footer={null}
      >
        <p style={{color: 'white'}}>Title: {title}</p>
        <p style={{color: 'white'}}>Description: {description}</p>
        <p style={{color: 'white'}}>Due Date: {dueDate}</p>
        <p style={{color: 'white'}}>Story Points: {storyPoints}</p>
        <p style={{color: 'white'}}>Estimated Hours: {estimatedHours}</p>
        <p style={{color: 'white'}}>Real Hours: {realHours}</p>
      </Modal>  
    </Card>
  );
};

export default TaskCard;