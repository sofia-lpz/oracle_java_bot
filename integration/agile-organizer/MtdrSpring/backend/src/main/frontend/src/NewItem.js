import React, { useState } from "react";
import { Button, Form, Input, DatePicker, Select } from 'antd';
import './App.css';

// Añadir estilos globales
const calendarStyles = `
  .ant-picker-header-view button {
    color: white !important;
  }
  .ant-picker-header button {
    color: white !important;
  }
  .ant-picker-content th {
    color: white !important;
  }
  .ant-picker-cell {
    color: white !important;
  }
  .ant-picker-header-super-prev-btn,
  .ant-picker-header-prev-btn,
  .ant-picker-header-next-btn,
  .ant-picker-header-super-next-btn {
    color: white !important;
  }
  .ant-picker-month-btn,
  .ant-picker-year-btn {
    color: white !important;
  }
  .custom-datepicker input::placeholder,
  .ant-select-selection-placeholder,
  .task-title-input::placeholder,
  .task-description-input::placeholder,
  textarea.ant-input::placeholder,
  input.ant-input::placeholder {
    color: #666 !important;
  }
  .ant-select-dropdown {
    background-color: #272727 !important;
  }
  .ant-select-item {
    color: white !important;
    background-color: #272727 !important;
  }
  .ant-select-item-option-active {
    background-color: #c6624b !important;
  }
  .ant-select-item-option-selected {
    background-color: #1d1d1d !important;
  }
  .ant-picker-suffix {
    color: #666 !important;
  }
  .ant-picker-clear {
    color: #c6624b !important;
    background: #272727 !important;
  }
  .ant-picker-clear:hover {
    color: #d17a66 !important;
  }
  .ant-picker:hover .ant-picker-clear {
    opacity: 1;
  }
  .ant-picker-cell-in-view.ant-picker-cell-selected .ant-picker-cell-inner,
  .ant-picker-cell-in-view.ant-picker-cell-range-start .ant-picker-cell-inner,
  .ant-picker-cell-in-view.ant-picker-cell-range-end .ant-picker-cell-inner {
    background-color: #c6624b !important;
    color: white !important;
  }
  .ant-picker-cell-in-view.ant-picker-cell-today .ant-picker-cell-inner::before {
    border-color: #c6624b !important;
  }
  .ant-picker-cell:hover .ant-picker-cell-inner {
    background-color: #c6624b !important;
  }
  .ant-picker-now-btn {
    color: #c6624b !important;
  }
  .ant-picker-now-btn:hover {
    color: #d17a66 !important;
  }
`;

function NewItem({ addItem, states }) {
  const [form] = Form.useForm();

  function handleSubmit(values) {
    if (!values.title.trim() || !values.description.trim()) {
      return;
    }
    
    const newTask = {
      title: values.title,
      description: values.description,
      dueDate: values.dueDate,
      state: {
        id: values.state_id
      }
    };
    
    addItem(newTask);
    form.resetFields();
  }

  return (
    <>
      <style>{calendarStyles}</style>
      <Form form={form} className="new-task-form" onFinish={handleSubmit}>
        <Form.Item
          label="Título"
          name="title"
          rules={[{ required: true, message: 'Por favor ingresa el título' }]}
        >
          <Input
            placeholder="Ingresa el título de la tarea"
            className="task-title-input"
            style={{
              backgroundColor: '#272727',
              color: 'white',
              borderColor: '#333'
            }}
          />
        </Form.Item>

        <Form.Item
          label="Descripción"
          name="description"
          rules={[{ required: true, message: 'Por favor ingresa la descripción' }]}
        >
          <Input.TextArea
            placeholder="Ingresa la descripción de la tarea"
            className="task-description-input"
            style={{
              backgroundColor: '#272727',
              color: 'white',
              borderColor: '#333'
            }}
          />
        </Form.Item>

        <Form.Item
          label="Estado"
          name="state_id"
          rules={[{ required: true, message: 'Por favor selecciona un estado' }]}
        >
          <Select
            placeholder="Selecciona un estado"
            options={states.map(state => ({
              value: state.id,
              label: state.name
            }))}
            style={{
              backgroundColor: '#272727',
              color: 'white',
              borderColor: '#333'
            }}
            dropdownStyle={{
              backgroundColor: '#272727'
            }}
          />
        </Form.Item>

        <Form.Item
          label="Fecha de vencimiento"
          name="dueDate"
        >
          <DatePicker
            placeholder="Selecciona una fecha"
            className="task-date-input custom-datepicker"
            style={{
              backgroundColor: '#272727',
              color: 'white',
              borderColor: '#333',
              width: '100%'
            }}
            popupStyle={{
              backgroundColor: '#1d1d1d',
              color: 'white'
            }}
            placeholderStyle={{
              color: '#666'
            }}
            panelRender={(panelNode) => (
              <div style={{ 
                backgroundColor: '#1d1d1d',
                color: 'white'
              }}>
                {panelNode}
              </div>
            )}
            cellRender={(date, info) => {
              if (info.type === 'date') {
                return (
                  <div 
                    style={{ 
                      color: 'white',
                      backgroundColor: '#1d1d1d',
                      cursor: 'pointer',
                      transition: 'background-color 0.3s',
                      ':hover': {
                        backgroundColor: '#c6624b'
                      }
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.backgroundColor = '#c6624b';
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.backgroundColor = '#1d1d1d';
                    }}
                  >
                    {date.date()}
                  </div>
                );
              }
              return info.originNode;
            }}
            headerRender={({ value, type, onChange, onTypeChange }) => (
              <div style={{ 
                display: 'flex', 
                justifyContent: 'space-between', 
                alignItems: 'center',
                color: 'white',
                padding: '8px'
              }}>
                <button
                  onClick={() => onChange(value.clone().subtract(1, 'month'))}
                  style={{
                    background: 'none',
                    border: 'none',
                    color: 'white',
                    cursor: 'pointer'
                  }}
                >
                  {'<'}
                </button>
                <div style={{ color: 'white' }}>
                  {value.format('MMMM YYYY')}
                </div>
                <button
                  onClick={() => onChange(value.clone().add(1, 'month'))}
                  style={{
                    background: 'none',
                    border: 'none',
                    color: 'white',
                    cursor: 'pointer'
                  }}
                >
                  {'>'}
                </button>
              </div>
            )}
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" className="add-task-button">
            Agregar Tarea
          </Button>
        </Form.Item>
      </Form>
    </>
  );
}

export default NewItem;