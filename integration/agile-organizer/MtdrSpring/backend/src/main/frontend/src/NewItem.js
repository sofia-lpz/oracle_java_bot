import React, { useState } from "react";
import Button from '@mui/material/Button';
import './App.css';

function NewItem(props) {
  const [task, setTask] = useState({
    title: '',
    description: '',
    dueDate: '',
    
  });

  function handleSubmit(e) {
    e.preventDefault();
    if (!task.title.trim() || !task.description.trim()) {
      return;
    }
    props.addItem(task);
    setTask({ title: '', description: '', dueDate: '' }); // Clear form
  }

  function handleChange(e) {
    const { name, value } = e.target;
    setTask(prevTask => ({
      ...prevTask,
      [name]: value,
    }));
  }

  return (
    <form>
      <input
        name="title"
        placeholder="Title"
        type="text"
        value={task.title}
        onChange={handleChange}
      />
      <input
        name="description"
        placeholder="Description"
        type="text"
        value={task.description}
        onChange={handleChange}
      />
      <input
        name="dueDate"
        placeholder="Due date"
        type="date"
        value={task.dueDate}
        onChange={handleChange}
      />
        
      <Button type="primary" style={{ backgroundColor: '#c6624b', color: 'white' }}  onClick={handleSubmit}>
        Add task
      </Button>
    </form>
  );
}

export default NewItem;