import React, { useState, useRef, useEffect } from 'react';
import { Input, Button, List } from 'antd';
import { EditFilled, LeftCircleFilled } from '@ant-design/icons';

const ChatBot = () => {
  // Estado para almacenar los mensajes del chat
  const [messages, setMessages] = useState([]);
  const messagesEndRef = useRef(null);
  const listRef = useRef(null);

  // Estado para manejar el mensaje actual que escribe el usuario
  const [inputValue, setInputValue] = useState('');

  // Maneja el envío del mensaje
  const handleSend = async () => {
    if (!inputValue.trim()) return;

    // const newMessages = [...messages, { from: 'user', text: inputValue }]; // Mantener todos los mensajes
    // Solo mantener el último mensaje y su respuesta
    const newMessages = [{ from: 'user', text: inputValue }];

    try {
      const response = await fetch('/api/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(inputValue),
      });

      const data = await response.text();
      newMessages.push({ from: 'bot', text: data });
    } catch (error) {
      newMessages.push({ from: 'bot', text: 'Error al comunicarse con el servicio.' });
    }

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

<style>{`
  .chatbot-container {
    padding: 24px;
    max-width: 100%;
    box-sizing: border-box;
  }

  .chatbot-list {
    height: calc(100vh - 250px);
    max-height: 400px;
    overflow-y: auto;
  }

  .chatbot-input-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .chatbot-input {
    width: 100% !important;
  }

  .chatbot-send-button {
    width: 100%;
  }

  @media (min-width: 768px) {
    .chatbot-input-group {
      flex-direction: row;
    }

    .chatbot-input {
      width: 80% !important;
    }

    .chatbot-send-button {
      width: 20%;
    }
  }
`}</style>