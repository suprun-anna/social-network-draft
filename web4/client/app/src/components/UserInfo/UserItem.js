import React from 'react';

const UserItem = ({ user }) => (
  <li className="user-item">
    <a href={`/profile/user/${user.username}`}>
      <div className="small-profile-picture">
        <img src={user.profilePictureURL} alt="Profile" />
      </div>
      <div className="username username-clickable">{user.username}</div>
    </a>
  </li>
);

export default UserItem;