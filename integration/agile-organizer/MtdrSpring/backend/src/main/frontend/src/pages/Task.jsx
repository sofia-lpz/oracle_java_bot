import React, { useState, useEffect, useRef } from 'react';
import { Button, Modal, message, Space, Input, Select, Spin, Flex } from 'antd';
import { PlusOutlined, DeleteOutlined, LoadingOutlined } from '@ant-design/icons';
import KanbanColumn from '../components/KanbanColumn';
import NewItem from '../NewItem';
import '../App.css';

import { API_LIST, API_STATES } from '../API';
import { DndContext, closestCenter, DragOverlay } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import TaskCard from '../components/TaskCard';

const Task = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const [isStateModalVisible, setIsStateModalVisible] = useState(false);
  const [newStateName, setNewStateName] = useState('');
  const [states, setStates] = useState([]);
  const containerRef = useRef(null);
  const [isDeleteStateModalVisible, setIsDeleteStateModalVisible] = useState(false);
  const [selectedStateToDelete, setSelectedStateToDelete] = useState(null);
  const [activeId, setActiveId] = useState(null);
  const [overId, setOverId] = useState(null);

  useEffect(() => {
    const fetchTasksAndStates = async () => {
      try {
        setLoading(true);
        const [tasksResponse, statesResponse] = await Promise.all([
          fetch(API_LIST),
          fetch(API_STATES)
        ]);

        if (!tasksResponse.ok || !statesResponse.ok) {
          throw new Error(`Error del servidor: ${tasksResponse.status}`);
        }

        const tasksData = await tasksResponse.json();
        const statesData = await statesResponse.json();
        
        if (!Array.isArray(tasksData) || !Array.isArray(statesData)) {
          throw new Error('Datos inválidos recibidos del servidor');
        }

        setTasks(tasksData);
        setStates(statesData);
        setError(null);
      } catch (error) {
        console.error('Error fetching data:', error);
        setError(new Error('No se pudo conectar al servidor. Por favor, asegúrate de que el servidor backend esté corriendo en http://localhost:9898'));
      } finally {
        setLoading(false);
      }
    };

    fetchTasksAndStates();
  }, []);

  const addTask = async (newTask) => {
    try {
      if (newTask.dueDate) {
        const dateObj = new Date(newTask.dueDate);
        newTask.dueDate = dateObj.toISOString();
      }

      const response = await fetch(API_LIST, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newTask),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error al crear la tarea');
      }

      const [tasksResponse, statesResponse] = await Promise.all([
        fetch(API_LIST),
        fetch(API_STATES)
      ]);

      if (tasksResponse.ok) {
        const tasksData = await tasksResponse.json();
        setTasks(tasksData);
      }

      if (statesResponse.ok) {
        const statesData = await statesResponse.json();
        setStates(statesData);
      }

      setIsModalVisible(false);
      messageApi.success('Tarea creada exitosamente');
    } catch (err) {
      console.error('Error creating task:', err);
      messageApi.error(err.message || 'Error al crear la tarea');
    }
  };

  const addState = async () => {
    try {
      if (!newStateName.trim()) {
        messageApi.warning('Por favor ingresa un nombre para el estado');
        return;
      }

      const maxPriority = Math.max(...states.map(state => state.workflow_priority || 0));
      
      const response = await fetch(API_STATES, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
          name: newStateName,
          workflow_priority: maxPriority + 1
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Error al crear el estado');
      }

      const newState = {
        id: Date.now(),
        name: newStateName,
        workflow_priority: maxPriority + 1
      };

      setStates(prevStates => [...prevStates, newState]);
      setNewStateName('');
      setIsStateModalVisible(false);
      messageApi.success('Estado creado exitosamente');

      const statesResponse = await fetch(API_STATES);
      if (statesResponse.ok) {
        const statesData = await statesResponse.json();
        setStates(statesData);
      }
    } catch (err) {
      console.error('Error creating state:', err);
      messageApi.error(err.message || 'Error al crear el estado');
      setStates(prevStates => prevStates.filter(state => state.id !== Date.now()));
    }
  };

  const deleteTask = async (taskId) => {
    try {
      const response = await fetch(`${API_LIST}/${taskId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Error al eliminar la tarea');
      }

      setTasks(prevTasks => prevTasks.filter(task => task.id !== taskId));
      messageApi.success('Tarea eliminada exitosamente');
    } catch (error) {
      console.error('Error deleting task:', error);
      messageApi.error('Error al eliminar la tarea');
    }
  };

  const deleteState = async (stateId) => {
    try {
      const response = await fetch(`${API_STATES}/${stateId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Error al eliminar el estado');
      }

      setStates(prevStates => prevStates.filter(state => state.id !== stateId));
      messageApi.success('Estado eliminado exitosamente');
      setIsDeleteStateModalVisible(false);
      setSelectedStateToDelete(null);
    } catch (error) {
      console.error('Error deleting state:', error);
      messageApi.error('Error al eliminar el estado');
    }
  };

  const handleDragStart = (event) => {
    setActiveId(event.active.id);
  };

  const handleDragOver = (event) => {
    setOverId(event.over?.id || null);
  };

  const handleDragEnd = (event) => {
    const { active, over } = event;
    setActiveId(null);
    setOverId(null);
    if (!over || active.id === over.id) return;

    const activeTask = tasks.find(task => task.id.toString() === active.id);
    const overColumn = states.find(state => state.id.toString() === over.id);

    if (!activeTask || !overColumn || activeTask.state?.id === overColumn.id) return;

    const updatedTask = { ...activeTask, state: overColumn };

    fetch(`${API_LIST}/${active.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedTask)
    })
      .then(() => {
        setTasks(prev =>
          prev.map(task => task.id === updatedTask.id ? updatedTask : task)
        );
      })
      .catch(error => console.error('Error updating task state:', error));
  };

  const tasksByState = states.reduce((acc, state) => {
    acc[state.id.toString()] = tasks.filter(task => task.state?.id.toString() === state.id.toString());
    return acc;
  }, {});

  const activeTask = tasks.find(task => task.id.toString() === activeId);

  const getStateColor = (stateName) => {
    const colors = {
      'TODO': '#3376cd',
      'IN_PROGRESS': '#f7cc4f',
      'COMPLETED': '#2ecc71',
    };

    if (colors[stateName]) {
      return colors[stateName];
    }

    const randomColor = () => {
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    };

    return randomColor();
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  if (loading) return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column',
      alignItems: 'center', 
      justifyContent: 'center', 
      height: '100vh',
      color: 'white'
    }}>
      <Flex align="center" gap="middle">
        <Spin indicator={<LoadingOutlined style={{ fontSize: 48, color: '#c6624b' }} spin />} />
      </Flex>
      <p style={{ marginTop: '20px', fontSize: '16px' }}>Loading tasks...</p>
    </div>
  );
  if (error) return <div>Error loading tasks: {error.message || 'An error occurred'}</div>;

  return (
    <>
      <style>{`
        .delete-state-select .ant-select-item {
          color: white !important;
          background-color: #272727;
        }
        .delete-state-select .ant-select-item-option-selected {
          background-color: #1d1d1d !important;
        }
        .delete-state-select .ant-select-item-option-active {
          background-color: #c6624b !important;
        }
        .delete-state-select .ant-select-selection-placeholder {
          color: #666 !important;
        }
        .add-state-input::placeholder {
          color: #666 !important;
        }
        .kanban {
          flex-direction: column;
          overflow-x: hidden;
        }

        .kanban-column {
          width: 100%;
          margin-bottom: 16px;
        }

        @media (min-width: 768px) {
          .kanban {
            flex-direction: row;
            overflow-x: auto;
          }

          .kanban-column {
            width: 330px;
            margin: 0;
          }
        }
      `}</style>
      <div ref={containerRef} style={{ 
        maxWidth: '1200px', 
        margin: '0 auto', 
        padding: '20px 20px 20px 0',
        height: 'calc(100vh - 40px)',
        overflow: 'hidden'
      }}>
        <h1 style={{ textAlign: 'left', color: 'white' }}>Tasks</h1>
        {contextHolder}
        <div className="menu-bar" style={{ 
          display: 'flex', 
          justifyContent: 'flex-end',
          marginBottom: '20px' 
        }}>
          <Space>
            <Button type="primary" style={{ backgroundColor: '#c6624b', color: 'white' }} icon={<PlusOutlined />} onClick={() => setIsModalVisible(true)}>
              Add task
            </Button>
            <Button type="default" style={{ backgroundColor: '#c6624b', color: 'white' }} onClick={() => setIsStateModalVisible(true)}>
              Add State
            </Button>
            <Button 
              type="default" 
              style={{ backgroundColor: '#c6624b', color: 'white' }} 
              icon={<DeleteOutlined />}
              onClick={() => setIsDeleteStateModalVisible(true)}
            >
              Delete State
            </Button>
          </Space>
        </div>
        <Modal title="Add Task" open={isModalVisible} onCancel={() => setIsModalVisible(false)} footer={null}>
          <NewItem addItem={addTask} states={states} />
        </Modal>
        <Modal title="Add New State" open={isStateModalVisible} onCancel={() => setIsStateModalVisible(false)} footer={null}>
          <Input 
            id="stateName" 
            placeholder="State Name" 
            value={newStateName} 
            onChange={(e) => setNewStateName(e.target.value)}
            onPressEnter={addState}
            className="add-state-input"
            style={{
              backgroundColor: '#272727',
              color: 'white',
              borderColor: '#333'
            }}
          />
          <Button type="primary" onClick={addState} style={{ marginTop: '16px' }}>
            Add State
          </Button>
        </Modal>

        <Modal 
          title="Delete State" 
          open={isDeleteStateModalVisible} 
          onCancel={() => {
            setIsDeleteStateModalVisible(false);
            setSelectedStateToDelete(null);
          }}
          footer={[
            <Button key="cancel" onClick={() => {
              setIsDeleteStateModalVisible(false);
              setSelectedStateToDelete(null);
            }}>
              Cancel
            </Button>,
            <Button 
              key="delete" 
              type="primary" 
              danger 
              onClick={() => deleteState(selectedStateToDelete)}
              disabled={!selectedStateToDelete}
            >
              Delete
            </Button>
          ]}
        >
          <Select
            style={{ 
              width: '100%',
              backgroundColor: '#272727',
              color: 'white'
            }}
            placeholder="Select a state to delete"
            onChange={(value) => setSelectedStateToDelete(value)}
            value={selectedStateToDelete}
            dropdownStyle={{
              backgroundColor: '#272727'
            }}
            className="delete-state-select"
          >
            {states.map(state => (
              <Select.Option 
                key={state.id} 
                value={state.id}
                style={{
                  color: 'white',
                  backgroundColor: '#272727'
                }}
              >
                {state.name}
              </Select.Option>
            ))}
          </Select>
          {selectedStateToDelete && (
            <p style={{ color: 'red', marginTop: '16px' }}>
              Warning: Deleting this state will remove all tasks associated with it.
            </p>
          )}
        </Modal>

        <DndContext
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
          onDragStart={handleDragStart}
          onDragOver={handleDragOver}
          onDragCancel={() => {
            document.body.style.cursor = 'default';
          }}
        >
          <SortableContext
            items={states.flatMap(state => tasksByState[state.id.toString()] || []).map(task => task.id.toString())}
            strategy={verticalListSortingStrategy}
          >
            <div className="ant-layout css-dev-only-do-not-override-1d4w9r2" style={{ 
              overflowX: 'auto', 
              display: 'flex', 
              flexWrap: 'nowrap', 
              height: '100%',
              width: '100%',
              minWidth: '100%',
              padding: '8px',
              gap: '1px',
              backgroundColor: '#1f1f1f',
              borderRadius: '8px',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)'
            }}>
              {states.map((state) => (
                <SortableContext key={state.id} items={tasksByState[state.id.toString()].map(task => task.id.toString())} strategy={verticalListSortingStrategy}>
                  <div style={{ border: state.id.toString() === overId ? '2px solid #c6624b' : 'none', borderRadius: '8px', padding: '8px', height: '76%' }}>
                    <KanbanColumn
                      state={state}
                      tasks={tasksByState[state.id.toString()].map(task => ({
                        ...task,
                        style: task.id === activeId ? { border: '2px dashed #ccc', padding: '8px', visibility: 'hidden' } : {},
                      }))}
                      getStateColor={getStateColor}
                      formatDate={formatDate}
                      deleteTask={deleteTask}
                    />
                  </div>
                </SortableContext>
              ))}
            </div>
          </SortableContext>
          <DragOverlay>
            {activeTask ? <TaskCard {...activeTask} /> : null}
          </DragOverlay>
        </DndContext>
      </div>
    </>
  );
};

export default Task;
