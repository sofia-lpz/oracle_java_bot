// src/components/KanbanColumn.jsx
import React from 'react';
import { useDroppable } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import TaskCard from './TaskCard';

const KanbanColumn = ({ state, tasks, getStateColor, formatDate, deleteTask }) => {
  const { setNodeRef } = useDroppable({ id: state.id.toString() });

  return (
    <div
      ref={setNodeRef}
      style={{
        backgroundColor: '#1d1d1d',
        padding: '8px',
        borderRadius: '8px',
        height: 'calc(100vh - 250px)',
        minWidth: '300px',
        maxWidth: '300px',
        transition: 'background-color 0.2s ease',
        flexShrink: 0,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
      }}
    >
      <div style={{ padding: '8px', borderBottom: '1px solid #333', width: '100%' }}>
        <h2 style={{ color: 'white', display: 'flex', alignItems: 'center', gap: '8px' }}>
          <span style={{ backgroundColor: getStateColor(state.name), width: '8px', height: '8px', borderRadius: '50%' }}></span>
          {state.name}
        </h2>
      </div>

      <SortableContext
        items={tasks.map(task => task.id.toString())}
        strategy={verticalListSortingStrategy}
      >
        <div style={{ flex: 1, overflowY: 'auto', width: '100%', padding: '0 8px' }}>
          {tasks.map(task => (
            <TaskCard
              key={task.id.toString()}
              id={task.id.toString()}
              title={task.title}
              description={task.description}
              dueDate={formatDate(task.dueDate)}
              storyPoints={task.storyPoints}
              avatarUrl={task.assignee?.avatarUrl}
              username={task.user.name}
              estimatedHours={task.estimated_hours}
              realHours={task.real_hours}
              onDelete={() => deleteTask(task.id)}
            />
          ))}
        </div>
      </SortableContext>
    </div>
  );
};

export default KanbanColumn;