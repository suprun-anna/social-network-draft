
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
import ClubPage from './pages/Clubs/ClubPage';
import MyClubsPage from './pages/Clubs/MyClubsPage';
import ClubEditPage from './pages/Clubs/ClubEditPage';
import AdminPage from './pages/Admin/AdminPage';
import ClubCreatePage from './pages/Clubs/ClubCreatePage';


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
        <Route path="/posts/post/create/:id" element={<CreatePostPage />} />
        <Route path="/posts/post/edit/:id" element={<EditPostPage />} />
        <Route path="/chat" element={<ChatListPage />} />
        <Route path="/chat/:username" element={<ChatPage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/clubs" element={<MyClubsPage />} />
        <Route path="/clubs/create" element={<ClubCreatePage />} />
        <Route path="/clubs/:id" element={<ClubPage />} />
        <Route path="/clubs/edit/:id" element={<ClubEditPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </Router>
  );
};

export default App;
