import React from 'react';

const UserItem = ({ user, club }) => {
  console.log(club);

  return <li className="user-item">
    {club ?
      <a href={`/clubs/${user.name}`}>
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

export default UserItem;