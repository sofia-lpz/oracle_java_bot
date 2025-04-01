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
    <div className="chatbot-container">
      <h1 level={2}>ChatBot</h1>

      <List
        ref={listRef}
        bordered
        itemLayout="horizontal"
        dataSource={messages}
        locale={{
          emptyText: (
            <div className="chatbot-empty">
              <EditFilled className="chatbot-empty-icon" />
              <div>No messages yet.</div>
            </div>
          )
        }}
        className="chatbot-list"
        renderItem={(item) => (
          <List.Item className="chatbot-item">
            <strong style={{ color: 'white' }}>
              {item.from === 'user' ? 'Me' : 'Bot'}:
            </strong> {item.text}
          </List.Item>
        )}
      />
      <div ref={messagesEndRef} />

      <Input.Group compact className="chatbot-input-group">
        <Input
          className="chatbot-input"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onPressEnter={handleSend}
          placeholder="Message..."
        />
        <Button
          className="chatbot-send-button"
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