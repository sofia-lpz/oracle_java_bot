// src/components/KanbanColumn.jsx
import React from 'react';
import { Card } from 'antd';
import { useDroppable } from '@dnd-kit/core';

const KanbanColumn = ({ state, tasks, getStateColor, formatDate, deleteTask, children }) => {
  const { setNodeRef, isOver } = useDroppable({
    id: state.id.toString(),
    data: {
      type: 'column',
      state: state
    }
  });

  return (
    <div
      ref={setNodeRef}
      className="kanban-column"
      style={{
        backgroundColor: '#272727',
        borderRadius: '12px',
        padding: '16px',
        width: '350px',
        height: 'calc(100vh - 120px)',
        display: 'flex',
        flexDirection: 'column',
        flexShrink: 0,
        margin: '0',
        border: isOver ? '2px dashed #c6624b' : 'none',
        transition: 'all 0.2s ease',
        position: 'relative',
        minHeight: '200px',
        overflow: 'hidden'
      }}
    >
      <div className="kanban-column-header" style={{ 
        padding: '12px',
        marginBottom: '16px',
        borderBottom: '2px solid #333',
        display: 'flex',
        alignItems: 'center',
        gap: '8px',
        flexShrink: 0
      }}>
        <div style={{ 
          width: '12px',
          height: '12px',
          borderRadius: '50%',
          backgroundColor: getStateColor(state.name)
        }} />
        <h2 style={{ 
          color: 'white',
          margin: 0,
          fontSize: '18px',
          fontWeight: '600'
        }}>
          {state.name}
        </h2>
        <span style={{ 
          color: '#666',
          marginLeft: 'auto',
          fontSize: '14px'
        }}>
          {tasks.length} tareas
        </span>
      </div>

      <div className="kanban-column-content" style={{
        flex: 1,
        overflowY: 'auto',
        padding: '8px',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
        minHeight: '100px',
        '&::-webkit-scrollbar': {
          width: '8px',
        },
        '&::-webkit-scrollbar-track': {
          background: '#1d1d1d',
          borderRadius: '4px',
        },
        '&::-webkit-scrollbar-thumb': {
          background: '#c6624b',
          borderRadius: '4px',
        },
        '&::-webkit-scrollbar-thumb:hover': {
          background: '#a84832',
        }
      }}>
        {children}
      </div>
    </div>
  );
};

export default KanbanColumn;