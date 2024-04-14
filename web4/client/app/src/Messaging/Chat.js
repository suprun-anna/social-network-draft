import React, { useState, useEffect } from 'react';

const Chat = () => {
    const [ws, setWs] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');

    useEffect(() => {
        const newWs = new WebSocket('ws://localhost:8080/websocket');

        console.log(123);

        newWs.onopen = () => {
            console.log('WebSocket connection established.');
        };

        newWs.onmessage = (event) => {
            setMessages(prevMessages => [...prevMessages, event.data]);
        };

        setWs(newWs);

        return () => {
            newWs.close();
        };
    }, []);

    const sendMessage = () => {
        if (ws && inputValue.trim() !== '') {
            ws.send(inputValue.trim());
            setInputValue('');
        }
    };

    return (
        <div>
            <div>
                <input type="text" value={inputValue} onChange={(e) => setInputValue(e.target.value)} />
                <button onClick={sendMessage}>Send</button>
            </div>
            <div>
                {messages.map((message, index) => (
                    <div key={index}>{message}</div>
                ))}
            </div>
        </div>
    );
};

export default Chat;
