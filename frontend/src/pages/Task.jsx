import React from 'react';
import TaskCard from '../components/TaskCard';
import '../App.css'

const Task = () => {
  return (
    <div>
      <h1>Tasks</h1>
      <div className="kanban">
        <div>
          <h1>To Do</h1>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
        <div>
          <h1>In Progress</h1>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
        <div>
          <h1>Completed</h1>
          <div className="kanban-column">
            <TaskCard/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Task;