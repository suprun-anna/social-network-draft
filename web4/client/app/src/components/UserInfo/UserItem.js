import React from 'react';

const ListItem = ({ user, club }) => {
  return <li className="user-item">
    {club ?
      <a href={`/clubs/${user.id}`}>
        <div className="small-profile-picture">
          <img src={user.profilePictureURL} alt="Profile" />
        </div>
        <div className="username-clickable">{user.name}</div>
      </a>
      :
      <a href={`/profile/user/${user.username}`}>
      <div className="small-profile-picture">
        <img src={user.profilePictureURL} alt="Profile" />
      </div>
      <div className="username username-clickable">{user.username}</div>
    </a> 
    }
  </li>
};

export default ListItem;