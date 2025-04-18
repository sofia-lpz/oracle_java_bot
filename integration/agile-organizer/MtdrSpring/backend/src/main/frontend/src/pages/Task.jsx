import React, { useState, useEffect } from 'react';
import { Button, Modal } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import NewItem from '../NewItem';
import TaskCard from '../components/TaskCard';
import '../App.css';

import API_LIST from '../API';

const Task = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false); // Modal state

  const formatDate = (dateString) => {
    if (!dateString) return 'No due date';
    
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'Invalid date';
    
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await fetch(API_LIST);
        if (!response.ok) {
          throw new Error('Failed to fetch tasks');
        }
        const data = await response.json();
        setTasks(data);
      } catch (err) {
        setError(err.message);
        console.error('Error fetching tasks:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const addTask = async (newTask) => {
    try {
      if (newTask.dueDate) {
        const dateObj = new Date(newTask.dueDate);
        newTask.dueDate = dateObj.toISOString(); // Converts to ISO format (yyyy-MM-ddTHH:mm:ss.sssZ)
      }
  
      const response = await fetch(API_LIST, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newTask),
      });
  
      if (!response.ok) {
        const errorData = await response.text(); 
        console.error('API Error:', errorData);
        return;
      }
  
      const responseText = await response.text(); 
      const createdTask = responseText ? JSON.parse(responseText) : {};
  
      setTasks([...tasks, createdTask]);
      setIsModalVisible(false);
    } catch (err) {
      console.error('Error creating task:', err);
    }
  };
  
  

  const todoTasks = tasks.filter(task => !task.state?.name || task.state?.name === 'TODO');
  const inProgressTasks = tasks.filter(task => task.state?.name === 'IN_PROGRESS');
  const completedTasks = tasks.filter(task => task.state?.name === 'COMPLETED');

  if (loading) return <div>Loading tasks...</div>;
  if (error) return <div>Error loading tasks: {error}</div>;

  const showModal = () => setIsModalVisible(true);
  const handleCancel = () => setIsModalVisible(false);

  return (
    <div>
      <h1>Tasks</h1>

      <Button type="primary" style={{ backgroundColor: '#c6624b', color: 'white' }} icon={<PlusOutlined />} onClick={showModal}>
        Add task
      </Button>
      <Modal title="Add Task" visible={isModalVisible} onCancel={handleCancel} footer={null}>
        <NewItem addItem={addTask} />
      </Modal>

      <div className="kanban">
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#3376cd' }}></span> To Do</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            {todoTasks.length > 0 ? (
              todoTasks.map(task => (
                <TaskCard
                  key={task.id}
                  title={task.title || task.name}
                  description={task.description}
                  dueDate={formatDate(task.dueDate)}
                  storyPoints={task.storyPoints}
                  avatarUrl={task.assignee?.avatarUrl}
                  estimatedHours={task.estimatedHours}
                  realHours={task.realHours}
                />
              ))
            ) : (
              <p>No tasks to do</p>
            )}
          </div>
        </div>
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#ffbc05' }}></span> In Progress</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            {inProgressTasks.length > 0 ? (
              inProgressTasks.map(task => (
                <TaskCard
                  key={task.id}
                  title={task.title || task.name}
                  description={task.description}
                  dueDate={formatDate(task.dueDate)}
                  storyPoints={task.storyPoints}
                  avatarUrl={task.assignee?.avatarUrl}
                  estimatedHours={task.estimatedHours}
                  realHours={task.realHours}
                />
              ))
            ) : (
              <p>No tasks in progress</p>
            )}
          </div>
        </div>
        <div>
          <div className="kanban-header">
            <h2><span className="status-indicator" style={{ backgroundColor: '#2ECC71' }}></span> Completed</h2>
          </div>
          <div className="kanban-column kanban-scroll">
            {completedTasks.length > 0 ? (
              completedTasks.map(task => (
                <TaskCard
                  key={task.id}
                  title={task.title || task.name}
                  description={task.description}
                  dueDate={formatDate(task.dueDate)}
                  storyPoints={task.storyPoints}
                  avatarUrl={task.assignee?.avatarUrl}
                  estimatedHours={task.estimatedHours}
                  realHours={task.realHours}
                />
              ))
            ) : (
              <p>No completed tasks</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Task;
