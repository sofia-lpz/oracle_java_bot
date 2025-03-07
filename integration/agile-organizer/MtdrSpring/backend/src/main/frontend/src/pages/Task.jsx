import React from 'react';
import TaskCard from '../components/TaskCard';
import '../App.css'

const Task = () => {
  return (
    <div>
      <h1>Tasks</h1>
      <div className="kanban">
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#3376cd' }}></span> To Do</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            <TaskCard/>
          </div>
        </div>
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#ffbc05' }}></span> In Progress</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            <TaskCard/>
          </div>
        </div>
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#2ECC71' }}></span> Completed</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            <TaskCard/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Task;