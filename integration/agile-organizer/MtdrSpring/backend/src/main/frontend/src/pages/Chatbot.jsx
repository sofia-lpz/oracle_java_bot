import React, { useState, useRef, useEffect } from 'react';
import { Input, Button, List, Typography } from 'antd';
import { EditFilled, LeftCircleFilled } from '@ant-design/icons';

const ChatBot = () => {
  // Estado para almacenar los mensajes del chat
  const [messages, setMessages] = useState([]);
  const messagesEndRef = useRef(null);
  const listRef = useRef(null);

  // Estado para manejar el mensaje actual que escribe el usuario
  const [inputValue, setInputValue] = useState('');

  // Maneja el envío del mensaje
  const handleSend = () => {
    if (!inputValue.trim()) return;

    // Agrega el mensaje del usuario al estado
    const newMessages = [...messages, { from: 'user', text: inputValue }];

    // Simula una respuesta del bot (llamar backend aqui)
    newMessages.push({ from: 'bot', text: 'respuesta automática.' });

    // Actualiza los mensajes y limpia el campo de entrada
    setMessages(newMessages);
    setInputValue('');
  };

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTo(0, listRef.current.scrollHeight);
    }
  }, [messages]);

  return (
    <div style={{ padding: 24 }}>
      <style>
        {`
          input::placeholder {
            color: #d3d3d3 !important;
          }
        `}
      </style>
      <h1 level={2}>ChatBot</h1>

      <List
        ref={listRef}
        bordered
        itemLayout="horizontal"
        dataSource={messages}
        locale={{
          emptyText: (
            <div style={{
              color: '#d3d3d3',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              height: 'calc(100vh - 290px)'
            }}>
              <EditFilled style={{ fontSize: 40, marginBottom: 8, color: '#d3d3d3' }} />
              <div>No messages yet.</div>
            </div>
          )
        }}
        style={{
          marginBottom: 16,
          height: 'calc(100vh - 250px)',
          overflowY: 'auto',
          backgroundColor: '#272727',
          color: 'white',
          border: '1px solid #272727'
        }}
        renderItem={(item) => (
          <List.Item style={{ backgroundColor: '#272727', color: 'white' }}>
            <strong style={{ color: 'white' }}>
              {item.from === 'user' ? 'Me' : 'Bot'}:
            </strong> {item.text}
          </List.Item>
        )}
      />
      <div ref={messagesEndRef} />

      <Input.Group compact style={{ width: '102.8%' }}>
        <Input
          style={{
            width: 'calc(100% - 100px)',
            backgroundColor: '#272727',
            color: 'white',
            border: '1px solid #272727'
          }}
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onPressEnter={handleSend}
          placeholder="Message..."
        />
        <Button
          style={{
            backgroundColor: '#c6624b',
            borderColor: '#c6624b'
          }}
          type="primary"
          onClick={handleSend}
        >
          <LeftCircleFilled />
        </Button>
      </Input.Group>
    </div>
  );
};

export default ChatBot;