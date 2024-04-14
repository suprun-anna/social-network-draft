import React, { useState, useEffect } from 'react';
import SockJsClient from 'react-stomp';

const Chat = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [client, setClient] = useState(null); // Состояние для отслеживания клиента

  const handleMessageChange = event => {
    setNewMessage(event.target.value);
  };

  const handleNewMessage = message => {
    setMessages(prevMessages => [...prevMessages, message]);
  };

  const sendMessage = () => {
    if (client && newMessage.trim() !== '') { // Проверка наличия клиента
      const message = {
        messageText: newMessage
      };
      client.sendMessage('/app/chat', JSON.stringify(message));
      setNewMessage('');
    }
  };

  const connectCallback = () => {
    const token = localStorage.getItem('token'); 
    const headers = { Authorization: `Bearer ${token}` };
    return headers;
  };

  useEffect(() => {
    // Устанавливаем клиента в состояние
    if (client === null) {
      setClient(document.querySelector('#sockjs-client'));
    }
  }, [client]);

  const handleConnect = () => {
    console.log("Connected!");
  };

  const handleDisconnect = () => {
    console.log("Disconnected!");
  };

  return (
    <div>
      <h1>Чат</h1>
      <div>
        {messages.map((message, index) => (
          <div key={index}>
            <p>{message.sender}: {message.messageText}</p>
          </div>
        ))}
      </div>
      <div>
        <input type="text" value={newMessage} onChange={handleMessageChange} />
        <SockJsClient
          id="sockjs-client" // Указываем id для поиска клиента
          url={'http://localhost:8080/api/chat'}
          topics={['/topic/messages']}
          onMessage={handleNewMessage}
          headers={connectCallback()}
          onConnect={handleConnect}
          onDisconnect={handleDisconnect}
        />
        <button onClick={sendMessage}>Отправить</button>
      </div>
    </div>
  );
};

export default Chat;


































// import React, { useState, useEffect } from 'react';
// import SockJsClient from 'react-stomp';
// import axios from 'axios';

// const SOCKET_URL = 'http://localhost:8080/ws-message';
// const API_URL = 'http://localhost:8080/api/messages';

// const Chat = ({ currentUser, otherUser }) => {
//   const [messages, setMessages] = useState([]);
//   const [messageText, setMessageText] = useState('');
//   const [clientRef, setClientRef] = useState(null); // Добавленная строка

//   useEffect(() => {
//     // Загрузка предыдущих сообщений при монтировании компонента
//     loadMessages();
//   }, []);

//   const loadMessages = async () => {
//     try {
//       const response = await axios.get(`${API_URL}?senderId=${currentUser.id}&receiverId=${otherUser.id}`);
//       console.log(123);
//       setMessages(response.data);
//     } catch (error) {
//       console.error('Failed to load messages:', error);
//     }
//   };

//   const sendMessage = () => {
//     if (messageText.trim() === '') return;

//     // Отправка сообщения через WebSocket
//     const message = {
//       senderId: currentUser.id,
//       receiverId: otherUser.id,
//       messageText: messageText
//     };
//     clientRef.sendMessage('/app/sendMessage', JSON.stringify(message));

//     // Добавление отправленного сообщения в локальное состояние
//     const sentAt = new Date().toISOString();
//     const newMessage = { sender: currentUser, receiver: otherUser, messageText, sentAt };
//     setMessages([...messages, newMessage]);

//     // Сброс поля ввода сообщения
//     setMessageText('');
//   };

//   // Обработчик входящего сообщения от WebSocket
//   const onMessageReceived = (msg) => {
//     const receivedMessage = JSON.parse(msg.body);
//     setMessages([...messages, receivedMessage]);
//   };

//   return (
//     <div>
//       <h2>Chat with {otherUser.name}</h2>
//       <div>
//         {messages.map((msg, index) => (
//           <div key={index}>
//             <p>{msg.sender.name}: {msg.messageText}</p>
//           </div>
//         ))}
//       </div>
//       <input
//         type="text"
//         value={messageText}
//         onChange={(e) => setMessageText(e.target.value)}
//       />
//       <button onClick={sendMessage}>Send</button>

//       <SockJsClient
//         url={SOCKET_URL}
//         topics={[`/user/${currentUser.id}/queue/messages`]}
//         onMessage={onMessageReceived}
//         ref={(client) => (clientRef = client)}
//       />
//     </div>
//   );
// };

// export default Chat;
