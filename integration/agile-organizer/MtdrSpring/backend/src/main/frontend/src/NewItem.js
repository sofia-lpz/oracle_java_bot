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
    <form className="new-task-form">
      <div className="form-group">
        <label htmlFor="title">Título</label>
        <input
          id="title"
          name="title"
          placeholder="Ingresa el título de la tarea"
          type="text"
          value={task.title}
          onChange={handleChange}
          className="task-title-input"
        />
      </div>

      <div className="form-group">
        <label htmlFor="description">Descripción</label>
        <textarea
          id="description"
          name="description"
          placeholder="Ingresa la descripción de la tarea"
          value={task.description}
          onChange={handleChange}
          className="task-description-input"
          rows={3}
        />
      </div>

      <div className="form-group">
        <label htmlFor="dueDate">Fecha de entrega</label>
        <input
          id="dueDate"
          name="dueDate"
          placeholder="Selecciona la fecha"
          type="date"
          value={task.dueDate}
          onChange={handleChange}
          className="task-due-date-input"
        />
      </div>
        
      <div className="form-actions">
        <Button 
          type="primary" 
          style={{ 
            backgroundColor: '#c6624b', 
            color: 'white',
            padding: '8px 24px',
            borderRadius: '8px',
            marginTop: '16px'
          }}  
          onClick={handleSubmit}
        >
          Agregar tarea
        </Button>
      </div>
    </form>
  );
}

export default NewItem;