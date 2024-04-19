
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/Authentication/LoginPage';
import RegistrationPage from './pages/Authentication/RegisterPage';
import ProfilePage from './pages/Profile/ProfilePage';
import PostPage from './pages/Posts/PostPage';
import HomePage from './pages/Posts/HomePage';
import ProfileEditPage from './pages/Profile/ProfileEditPage';
import CreatePostPage from './pages/Posts/CreatePostPage';
import EditPostPage from './pages/Posts/EditPostPage';
import Chat from './Messaging/Chat';
import ChatPage from './Messaging/ChatPage';
import ChatListPage from './Messaging/ChatListPage';
import UserSearch from './components/Search/UserSearch';
import SearchPage from './pages/Profile/SearchPage';


const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/profile/user/me" element={<ProfilePage />} />
        <Route path="/profile/user/:username" element={<ProfilePage />} />
        <Route path="/profile/user/:username" element={<ProfilePage />} />
        <Route path="/posts/post/:id" element={<PostPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/profile/edit" element={<ProfileEditPage />} />
        <Route path="/posts/post/create" element={<CreatePostPage />} />
        <Route path="/posts/post/edit/:id" element={<EditPostPage />} />
        <Route path="/chat" element={<ChatListPage />} />
        <Route path="/chat/:username" element={<ChatPage />} />
        <Route path="/search" element={<SearchPage />} />
      </Routes>
    </Router>
  );
};

export default App;


// import React, { useState } from 'react';
// import SockJsClient from 'react-stomp';

// const SOCKET_URL = 'http://localhost:8080/api/ws-message';

// const App = () => {
//   const [message, setMessage] = useState('You server message here.');

//   let onConnected = () => {
//     console.log("Connected!!")
//   }

//   let onMessageReceived = (msg) => {
//     setMessage(msg.message);
//   }

//   return (
//     <div>
//       <SockJsClient
//         url={SOCKET_URL}
//         topics={['/topic/message']}
//         onConnect={onConnected}
//         onDisconnect={console.log("Disconnected!")}
//         onMessage={msg => onMessageReceived(msg)}
//         debug={false}
//       />
//       <div>{message}</div>
//     </div>
//   );
// }

// export default App;























// import React, { useState } from 'react';
// import SockJsClient from 'react-stomp';

// const SOCKET_URL = 'ws://localhost:8080/ws-message';

// const App = () => {
//   const [message, setMessage] = useState('You server message here.');

//   let onConnected = () => {
//     console.log("Connected!!")
//   }

//   let onMessageReceived = (msg) => {
//     setMessage(msg.message);
//   }

//   return (
//     <div>
//       <SockJsClient
//         url={SOCKET_URL}
//         topics={['/topic/message']}
//         onConnect={onConnected}
//         onDisconnect={console.log("Disconnected!")}
//         onMessage={msg => onMessageReceived(msg)}
//         debug={false}
//       />
//       <div>{message}</div>
//     </div>
//   );
// }

// export default App;
