import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { sendGetRequest } from '../util/requests';
import '../styles/chatStyles.css'
import Menu from '../components/Menu/Menu';
import { Message, MyMessage } from './Message';

const SERVER = 'http://localhost:8080/api';

function ChatPage() {
  const { username } = useParams(); 
  const [token, setToken] = useState('');
  const [connected, setConnected] = useState(false);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [me, setMe] = useState(null);
  const messagesContainerRef = useRef(null);
  const [nextPageNumber, setNextPageNumber] = useState(1);
  const pageSize = 10; 


  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      setToken(storedToken);
    }
  }, []);

  useEffect(() => {
    if (token) {
      const socket = new SockJS("http://localhost:8080/api/ws");
      const client = Stomp.over(socket);
      client.connect({ Authorization: `Bearer ${token}` }, (frame) => {
        console.log(frame);
        setConnected(true);
      });
      setStompClient(client);
    }
  }, [token]);

  useEffect(() => {

    const fetchUserData = async () => {
      if (username) {
        const user = await getUserInfo(`${SERVER}/user?username=${username}`);
        setCurrentUser(user);
      }
    }

    fetchUserData();
  }, [token, username]);

  useEffect(() => {
    const fetchUserData = async () => {
      const user = await getUserInfo(`${SERVER}/user/me`);
      setMe(user);
    }

    fetchUserData();
  }, [token]);

  async function getUserInfo(request, error = 'Error fetching user data: ') {
    return (await sendGetRequest(request, error)).data;
  }

  const handleSendMessage = (e) => {
    e.preventDefault();
    if (stompClient && connected && message.trim() !== '' && currentUser) {
      stompClient.send("/app/chat", {}, JSON.stringify({
        senderId: me.id,
        receiverId: currentUser.id,
        messageText: message
      }));
      setMessage('');
    }
  };





  useEffect(() => {
    if (stompClient && connected && currentUser && me) {
      const sortedIds = [currentUser.id, me.id].sort((a, b) => a - b);
      const uniqueTopic = `/topic/chat/${sortedIds[0]}-${sortedIds[1]}`;
      let isSubscribed = false;

      stompClient.subscribe(uniqueTopic, (message) => {
        const data = JSON.parse(message.body);
        console.log(data);
        setMessages(prevMessages => [data, ...prevMessages]);
        isSubscribed = true;
      });
    }
  }, [stompClient, connected, currentUser, me]);




  useEffect(() => {
    const fetchMessages = async () => {
      if (me && currentUser) {
        const fetchedMessages = await getAllMessagesBetweenUsers(me.id, currentUser.id, 0, pageSize);
        setMessages(fetchedMessages);
      }
    };


    fetchMessages();
  }, [me, currentUser]);

  async function getAllMessagesBetweenUsers(senderId, receiverId, page, size) {
    const response = await sendGetRequest(`${SERVER}/messages?senderId=${senderId}&receiverId=${receiverId}&page=${page}&size=${size}`);
    const data = response.data;
    return data;
  }





  useEffect(() => {
    const handleScroll = async () => {
      if (messagesContainerRef.current && me && currentUser) {
        const container = messagesContainerRef.current;
        const scrollPosition = container.scrollHeight - container.clientHeight - container.scrollTop;
        const bottomThreshold = 100; 
        if (scrollPosition <= bottomThreshold) {
          const newMessages = await getAllMessagesBetweenUsers(me.id, currentUser.id, nextPageNumber, pageSize);
          setMessages(prevMessages => [...prevMessages, ...newMessages]);
          setNextPageNumber(prevPageNumber => prevPageNumber + 1);
          console.log(newMessages);
        }
      }
    };
  
    if (messagesContainerRef.current) {
      messagesContainerRef.current.addEventListener('scroll', handleScroll);
    }
  
    return () => {
      if (messagesContainerRef.current) {
        messagesContainerRef.current.removeEventListener('scroll', handleScroll);
      }
    };
  }, [me, currentUser, nextPageNumber, pageSize]); 
  


  return (
    <div className='container'>
      <Menu />
      <div className="message-page-container">
        <h1 className="title">Chat with <a href={`/profile/user/${username}`} className='username username-link'>{username}</a></h1>
        <div ref={messagesContainerRef} className="message-container">
          {me && messages.map((msg, index) => (
            msg.senderId === me.id ? (
              <MyMessage key={index} messageText={msg.messageText} />
            ) : (
              <Message key={index} senderName={msg.senderName} messageText={msg.messageText} />
            )
          ))}
        </div>
        <form onSubmit={handleSendMessage} className="input-container">
          <input type="text" placeholder="Type your message..." value={message} onChange={(e) => setMessage(e.target.value)} className="message-input" />
          <button type="submit" className="send-button">Send</button>
        </form>
      </div>
    </div>
  );


}

export default ChatPage;
