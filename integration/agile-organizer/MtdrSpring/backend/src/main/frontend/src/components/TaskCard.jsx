import React from 'react';
import { Card, Avatar, Button, Modal, Space, Tag } from 'antd';
import { UserOutlined, DeleteOutlined, EditOutlined, ClockCircleOutlined, FieldTimeOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

const { Meta } = Card;

//Black #1d1d1d
//Gray #272727
//Oracle #c6624b

/* Cambios */

const TaskCard = ({ task, onDelete }) => {
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging
  } = useSortable({
    id: task.id.toString(),
    data: {
      type: 'task',
      task: task
    }
  });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging ? 0.5 : 1,
    cursor: 'grab',
    position: 'relative',
    zIndex: isDragging ? 1000 : 1,
    touchAction: 'none'
  };

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
    <div 
      ref={setNodeRef} 
      style={style} 
      {...attributes} 
      {...listeners}
    >
      <Card
        className="task-card"
        style={{
          background: '#1d1d1d',
          border: 'none',
          borderRadius: '8px',
          marginBottom: '12px',
          transition: 'all 0.2s ease',
          userSelect: 'none'
        }}
        hoverable
        onClick={() => setIsModalOpen(true)}
      >
        <Meta
          avatar={
            <Avatar 
              src={task.assignee?.avatarUrl} 
              icon={<UserOutlined />}
              style={{ backgroundColor: '#c6624b' }}
            />
          }
          title={
            <div style={{ 
              display: 'flex', 
              justifyContent: 'space-between',
              alignItems: 'center',
              marginBottom: '8px'
            }}>
              <span style={{ color: 'white', fontSize: '16px', fontWeight: '500' }}>
                {task.title.length > 13 ? `${task.title.substring(0, 13)}...` : task.title}
              </span>
              <Button
                type="text"
                icon={<DeleteOutlined style={{ color: '#c6624b' }} />}
                onClick={(e) => {
                  e.stopPropagation();
                  showDeleteModal();
                }}
                style={{ padding: '4px' }}
              />
            </div>
          }
          description={
            <div style={{ color: '#8c8c8c' }}>
              <p style={{ marginBottom: '8px' }}>{task.description}</p>
              <Space direction="vertical" size="small" style={{ width: '100%' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <ClockCircleOutlined style={{ color: '#c6624b' }} />
                  <span>Vence: {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'No definido'}</span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <FieldTimeOutlined style={{ color: '#c6624b' }} />
                  <span>Estimado: {task.estimated_hours || 'No definido'} horas</span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <CheckCircleOutlined style={{ color: '#c6624b' }} />
                  <span>Real: {task.real_hours || 'No definido'} horas</span>
                </div>
                {task.storyPoints && (
                  <Tag color="#c6624b" style={{ marginTop: '8px' }}>
                    {task.storyPoints} Story Points
                  </Tag>
                )}
              </Space>
            </div>
          }
        />
      </Card>

      <Modal
        title={
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            gap: '12px',
            color: 'white'
          }}>
            <Avatar 
              src={task.assignee?.avatarUrl} 
              icon={<UserOutlined />}
              style={{ backgroundColor: '#c6624b' }}
            />
            <span>{task.title}</span>
          </div>
        }
        open={isModalOpen}
        onCancel={handleCancel}
        onOk={handleCancel}
        footer={[
          <Button key="delete" type="primary" danger onClick={handleOk}>
            Eliminar
          </Button>,
          <Button key="close" onClick={handleCancel}>
            Cerrar
          </Button>
        ]}
        width={600}
        style={{ top: 20 }}
        bodyStyle={{ 
          backgroundColor: '#1d1d1d',
          padding: '24px',
          borderRadius: '8px'
        }}
      >
        <div style={{ color: 'white' }}>
          <div style={{ marginBottom: '24px' }}>
            <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Descripción</h3>
            <p>{task.description || 'No hay descripción disponible'}</p>
          </div>

          <div style={{ 
            display: 'grid', 
            gridTemplateColumns: 'repeat(2, 1fr)',
            gap: '16px',
            marginBottom: '24px'
          }}>
            <div>
              <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Fecha de vencimiento</h3>
              <p>{task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'No definido'}</p>
            </div>
            <div>
              <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Story Points</h3>
              <p>{task.storyPoints || 'No definido'}</p>
            </div>
            <div>
              <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Horas estimadas</h3>
              <p>{task.estimated_hours || 'No definido'}</p>
            </div>
            <div>
              <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Horas reales</h3>
              <p>{task.real_hours || 'No definido'}</p>
            </div>
          </div>

          <div>
            <h3 style={{ color: '#c6624b', marginBottom: '8px' }}>Asignado a</h3>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              gap: '8px'
            }}>
              <Avatar 
                src={task.assignee?.avatarUrl} 
                icon={<UserOutlined />}
                style={{ backgroundColor: '#c6624b' }}
              />
              <span>{task.user?.name || 'No asignado'}</span>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default TaskCard;