import React from 'react';
import TaskCard from '../components/TaskCard';
import '../App.css'

const Task = () => {
  return (
    <div>
      <h1>Tasks</h1>
      <div className="kanban">
        <div>
          <h2>To Do</h2>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
        <div>
          <h2>In Progress</h2>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
        <div>
          <h2>Completed</h2>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Task;