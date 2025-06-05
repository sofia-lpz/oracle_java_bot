import React, { useState, useEffect, useRef } from 'react';
import { Button, Modal, message, Space, Input, Select, Spin, Flex } from 'antd';
import { PlusOutlined, DeleteOutlined, LoadingOutlined, AppstoreAddOutlined } from '@ant-design/icons';
import KanbanColumn from '../components/KanbanColumn';
import NewItem from '../NewItem';
import '../App.css';
import { authenticatedFetch } from '../utils/authUtils';
import { API_LIST, API_STATES } from '../API';
import { DndContext, closestCenter, DragOverlay, useSensors, useSensor, PointerSensor, KeyboardSensor } from '@dnd-kit/core';
import { SortableContext, verticalListSortingStrategy } from '@dnd-kit/sortable';
import TaskCard from '../components/TaskCard';
import { arrayMove } from '@dnd-kit/sortable';

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

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 8,
      },
    }),
    useSensor(KeyboardSensor)
  );

  useEffect(() => {
    fetchTasksAndStates();
  }, []);

  const fetchTasksAndStates = async () => {
    try {
      setLoading(true);
      const [tasksResponse, statesResponse] = await Promise.all([
        authenticatedFetch(API_LIST),
        authenticatedFetch(API_STATES)
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

  const addTask = async (newTask) => {
    try {
      if (!newTask.state || !newTask.state.id) {
        messageApi.error('Debes seleccionar un estado para la tarea');
        return;
      }

      if (newTask.dueDate) {
        const dateObj = new Date(newTask.dueDate);
        newTask.dueDate = dateObj.toISOString();
      }

      console.log('Enviando tarea al servidor:', newTask);

      const response = await authenticatedFetch(API_LIST, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newTask),
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error('Error response:', errorText);
        throw new Error(errorText || 'Error al crear la tarea');
      }

      // Intentar leer la respuesta como texto primero
      const responseText = await response.text();
      console.log('Respuesta del servidor:', responseText);

      // Si la respuesta está vacía pero la tarea se creó correctamente
      if (responseText.trim() === '') {
        // Actualizar el estado local con la nueva tarea
        const tempId = Date.now(); // ID temporal para la tarea
        const newTaskWithId = {
          ...newTask,
          id: tempId,
          state: {
            id: newTask.state.id,
            name: states.find(s => s.id === newTask.state.id)?.name || 'Nuevo'
          }
        };

        setTasks(prevTasks => [...prevTasks, newTaskWithId]);
        setIsModalVisible(false);
        messageApi.success('Tarea creada exitosamente');
        
        // Actualizar con los datos del servidor
        await fetchTasksAndStates();
        return;
      }

      // Si hay respuesta, intentar parsear como JSON
      try {
        const createdTask = JSON.parse(responseText);
        if (!createdTask || !createdTask.id) {
          console.error('Tarea creada inválida:', createdTask);
          throw new Error('La tarea no se creó correctamente');
        }
        await fetchTasksAndStates();
        setIsModalVisible(false);
        messageApi.success('Tarea creada exitosamente');
      } catch (parseError) {
        console.error('Error al parsear la respuesta:', parseError);
        throw new Error('Error al procesar la respuesta del servidor');
      }
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
      
      const response = await authenticatedFetch(API_STATES, {
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

      await fetchTasksAndStates();
      setNewStateName('');
      setIsStateModalVisible(false);
      messageApi.success('Estado creado exitosamente');
    } catch (err) {
      console.error('Error creating state:', err);
      messageApi.error(err.message || 'Error al crear el estado');
    }
  };

  const deleteTask = async (taskId) => {
    try {
      const response = await authenticatedFetch(`${API_LIST}/${taskId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Error al eliminar la tarea');
      }

      await fetchTasksAndStates();
      messageApi.success('Tarea eliminada exitosamente');
    } catch (error) {
      console.error('Error deleting task:', error);
      messageApi.error('Error al eliminar la tarea');
    }
  };

  const deleteState = async (stateId) => {
    try {
      const response = await authenticatedFetch(`${API_STATES}/${stateId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error('Error al eliminar el estado');
      }

      await fetchTasksAndStates();
      messageApi.success('Estado eliminado exitosamente');
      setIsDeleteStateModalVisible(false);
      setSelectedStateToDelete(null);
    } catch (error) {
      console.error('Error deleting state:', error);
      messageApi.error('Error al eliminar el estado');
    }
  };

  const handleDragStart = (event) => {
    const { active } = event;
    setActiveId(active.id);
  };

  const handleDragOver = (event) => {
    const { over } = event;
    if (!over) return;
    setOverId(over.id);
  };

  const handleDragEnd = async (event) => {
    const { active, over } = event;
    
    if (!over) {
      setActiveId(null);
      setOverId(null);
      return;
    }

    const activeTask = tasks.find(task => task.id.toString() === active.id);
    const overColumn = states.find(state => state.id.toString() === over.id);

    if (overColumn && activeTask) {
      try {
        const taskUpdateData = {
          id: activeTask.id,
          title: activeTask.title,
          description: activeTask.description,
          state: { id: overColumn.id },
          user: activeTask.user ? { id: activeTask.user.id } : null,
          project: activeTask.project ? { id: activeTask.project.id } : null,
          sprint: activeTask.sprint,
          dueDate: activeTask.dueDate,
          estimated_hours: activeTask.estimatedHours ?? activeTask.estimated_hours,
          real_hours: activeTask.realHours ?? activeTask.real_hours,
          storyPoints: activeTask.storyPoints,
          priority: activeTask.priority,
          done: activeTask.done,
          deleted: activeTask.deleted
        };

        const response = await authenticatedFetch(`${API_LIST}/${active.id}`, {
          method: 'PUT',
          headers: { 
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          body: JSON.stringify(taskUpdateData)
        });

        if (!response.ok) {
          throw new Error('Error al actualizar el estado de la tarea');
        }

        setTasks(prevTasks => 
          prevTasks.map(task => 
            task.id === activeTask.id 
              ? { ...task, state: { id: overColumn.id } }
              : task
          )
        );

        messageApi.success('Tarea actualizada exitosamente');
      } catch (error) {
        console.error('Error updating task state:', error);
        messageApi.error('Error al actualizar el estado de la tarea');
        await fetchTasksAndStates();
      }
    }

    setActiveId(null);
    setOverId(null);
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

    return colors[stateName] || `#${Math.floor(Math.random()*16777215).toString(16)}`;
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'No date set';
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
      <p style={{ marginTop: '20px', fontSize: '16px' }}>Cargando tareas...</p>
    </div>
  );

  if (error) return <div>Error al cargar las tareas: {error.message}</div>;

  return (
    <div ref={containerRef} style={{ height: '100vh', overflow: 'hidden', backgroundColor: '#1d1d1d' }}>
      <div style={{ 
        padding: '16px 24px', 
        borderBottom: '1px solid #333',
        display: 'flex',
        justifyContent: 'flex-start',
        alignItems: 'center',
        gap: '12px'
      }}>
        <Space>
          <Button 
            type="primary" 
            icon={<PlusOutlined />} 
            onClick={() => setIsModalVisible(true)}
            style={{ backgroundColor: '#c6624b' }}
          >
            Agregar Tarea
          </Button>
          <Button 
            type="default" 
            icon={<AppstoreAddOutlined />} 
            onClick={() => setIsStateModalVisible(true)}
            style={{ backgroundColor: '#c6624b', color: 'white' }}
          >
            Agregar Estado
          </Button>
          <Button 
            type="default" 
            icon={<DeleteOutlined />}
            onClick={() => setIsDeleteStateModalVisible(true)}
            style={{ backgroundColor: '#c6624b', color: 'white' }}
          >
            Eliminar Estado
          </Button>
        </Space>
      </div>

      {contextHolder}

      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragStart={handleDragStart}
        onDragOver={handleDragOver}
        onDragEnd={handleDragEnd}
      >
        <div style={{ 
          display: 'flex', 
          gap: '24px', 
          padding: '24px',
          height: 'calc(100vh - 80px)',
          overflowX: 'auto',
          overflowY: 'hidden',
          justifyContent: 'flex-start',
          alignItems: 'flex-start',
          width: '100%',
          boxSizing: 'border-box',
          position: 'relative',
          '&::-webkit-scrollbar': {
            height: '12px',
          },
          '&::-webkit-scrollbar-track': {
            background: '#1d1d1d',
            borderRadius: '6px',
          },
          '&::-webkit-scrollbar-thumb': {
            background: '#c6624b',
            borderRadius: '6px',
            border: '2px solid #1d1d1d',
          },
          '&::-webkit-scrollbar-thumb:hover': {
            background: '#a84832',
          }
        }}>
          {states.map((state) => (
            <KanbanColumn
              key={state.id}
              state={state}
              tasks={tasksByState[state.id.toString()] || []}
              getStateColor={getStateColor}
              formatDate={formatDate}
              deleteTask={deleteTask}
              style={{
                flexShrink: 0,
                width: '350px',
                minWidth: '350px',
                maxWidth: '350px',
                marginRight: '12px'
              }}
            >
              <SortableContext
                items={tasksByState[state.id.toString()]?.map(task => task.id.toString()) || []}
                strategy={verticalListSortingStrategy}
              >
                {tasksByState[state.id.toString()]?.map(task => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    onDelete={() => deleteTask(task.id)}
                  />
                ))}
              </SortableContext>
            </KanbanColumn>
          ))}
        </div>

        <DragOverlay>
          {activeId && activeTask ? (
            <TaskCard
              task={activeTask}
              onDelete={() => deleteTask(activeTask.id)}
            />
          ) : null}
        </DragOverlay>
      </DndContext>

      <Modal
        title="Agregar Tarea"
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
      >
        <NewItem addItem={addTask} states={states} />
      </Modal>

      <Modal
        title="Agregar Estado"
        open={isStateModalVisible}
        onCancel={() => setIsStateModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setIsStateModalVisible(false)}>
            Cancelar
          </Button>,
          <Button
            key="submit"
            type="primary"
            onClick={addState}
            style={{ backgroundColor: '#c6624b' }}
          >
            Agregar
          </Button>
        ]}
      >
        <Input
          placeholder="Nombre del estado"
          value={newStateName}
          onChange={(e) => setNewStateName(e.target.value)}
          onPressEnter={addState}
        />
      </Modal>

      <Modal
        title="Eliminar Estado"
        open={isDeleteStateModalVisible}
        onCancel={() => {
          setIsDeleteStateModalVisible(false);
          setSelectedStateToDelete(null);
        }}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setIsDeleteStateModalVisible(false);
              setSelectedStateToDelete(null);
            }}
          >
            Cancelar
          </Button>,
          <Button
            key="delete"
            type="primary"
            danger
            onClick={() => deleteState(selectedStateToDelete)}
            disabled={!selectedStateToDelete}
          >
            Eliminar
          </Button>
        ]}
      >
        <Select
          style={{ width: '100%' }}
          placeholder="Selecciona un estado para eliminar"
          onChange={(value) => setSelectedStateToDelete(value)}
          value={selectedStateToDelete}
        >
          {states.map(state => (
            <Select.Option key={state.id} value={state.id}>
              {state.name}
            </Select.Option>
          ))}
        </Select>
        {selectedStateToDelete && (
          <p style={{ color: 'red', marginTop: '16px' }}>
            Advertencia: Al eliminar este estado, se eliminarán todas las tareas asociadas.
          </p>
        )}
      </Modal>
    </div>
  );
};

export default Task;
