import React from 'react';
import axios from 'axios';

const SERVER = 'http://localhost:8080/api';

const LogoutButton = () => {
  const handleLogout = async () => {
    try {
      await axios.post(`${SERVER}/auth/logout`, {}, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}` 
        }
      });
      localStorage.removeItem('token');
      window.location.href = '/login';
    } catch (error) {
      console.error('Error logging out:', error);
    }
  };

  return (
    <button className='button' onClick={handleLogout}>Logout</button>
  );
};

export default LogoutButton;
