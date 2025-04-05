import React, { useState, useEffect, useRef } from 'react';
import { Button, Modal, message, Space, Input, Select, Spin, Flex } from 'antd';
import { PlusOutlined, DeleteOutlined, LoadingOutlined } from '@ant-design/icons';
import NewItem from '../NewItem';
import TaskCard from '../components/TaskCard';
import '../App.css';

import API_LIST from '../API';
import API_STATES from '../API_STATES';

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
  const [percent, setPercent] = useState(-50);
  const timerRef = useRef(null);

  useEffect(() => {
    if (loading) {
      timerRef.current = setTimeout(() => {
        setPercent(v => {
          const nextPercent = v + 5;
          return nextPercent > 150 ? -50 : nextPercent;
        });
      }, 100);
    }
    return () => clearTimeout(timerRef.current);
  }, [percent, loading]);

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

      // Obtener el workflow_priority más alto actual
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

      // Crear el nuevo estado localmente
      const newState = {
        id: Date.now(), // ID temporal hasta que se confirme con el servidor
        name: newStateName,
        workflow_priority: maxPriority + 1
      };

      // Actualizar el estado local inmediatamente
      setStates(prevStates => [...prevStates, newState]);
      setNewStateName('');
      setIsStateModalVisible(false);
      messageApi.success('Estado creado exitosamente');

      // Recargar los estados desde el servidor para obtener el ID correcto
      const statesResponse = await fetch(API_STATES);
      if (statesResponse.ok) {
        const statesData = await statesResponse.json();
        setStates(statesData);
      }
    } catch (err) {
      console.error('Error creating state:', err);
      messageApi.error(err.message || 'Error al crear el estado');
      // Revertir el cambio en caso de error
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

  const tasksByState = states.reduce((acc, state) => {
    acc[state.id.toString()] = tasks.filter(task => task.state?.id.toString() === state.id.toString());
    return acc;
  }, {});

  const getStateColor = (stateName) => {
    const colors = {
      'TODO': '#3376cd', // Azul
      'IN_PROGRESS': '#f7cc4f', // Amarillo
      'COMPLETED': '#2ecc71', // Verde
    };

    if (colors[stateName]) {
      return colors[stateName];
    }

    // Generar color aleatorio para nuevos estados
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
      `}</style>
      <div ref={containerRef} style={{ 
        maxWidth: '1200px', 
        margin: '0 auto', 
        padding: '20px 20px 20px 0',
        height: '100vh',
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

        <div className="ant-layout css-dev-only-do-not-override-1d4w9r2" style={{ 
          overflowX: 'auto', 
          display: 'flex', 
          flexWrap: 'nowrap', 
          height: 'calc(100vh - 200px)',
          width: '100%',
          minWidth: '100%',
          padding: '8px',
          gap: '1px',
          backgroundColor: '#1f1f1f',
          borderRadius: '8px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)'
        }}>
          {states.map((state) => (
            <div
              key={state.id.toString()}
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
              <div className="kanban-header" style={{ 
                padding: '8px', 
                borderBottom: '1px solid #333',
                marginBottom: '8px',
                flexShrink: 0,
                width: '100%'
              }}>
                <h2 style={{ 
                  color: 'white', 
                  margin: 0,
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px'
                }}>
                  <span className="status-indicator" style={{ 
                    backgroundColor: getStateColor(state.name),
                    width: '8px',
                    height: '8px',
                    borderRadius: '50%',
                    display: 'inline-block'
                  }}></span> 
                  {state.name}
                </h2>
              </div>
              <div className="kanban-column kanban-scroll" style={{ 
                overflowY: 'auto',
                flex: 1,
                padding: '0 8px',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                width: '100%'
              }}>
                {tasksByState[state.id.toString()]?.map((task) => {
                  if (!task || !task.id) return null;
                  return (
                    <div key={task.id.toString()} style={{ width: '100%', margin: '1px 0' }}>
                      <TaskCard
                        taskId={task.id.toString()}
                        title={task.title || task.name}
                        description={task.description}
                        dueDate={formatDate(task.dueDate)}
                        storyPoints={task.storyPoints}
                        avatarUrl={task.assignee?.avatarUrl}
                        estimatedHours={task.estimatedHours}
                        realHours={task.realHours}
                        onDelete={() => deleteTask(task.id)}
                      />
                    </div>
                  );
                })}
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default Task;
