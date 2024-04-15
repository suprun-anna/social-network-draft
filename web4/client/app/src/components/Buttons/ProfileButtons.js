import React from 'react';
import FollowsButtons from './FollowsButtons';
import LogoutButton from '../../pages/Authentication/LogoutButton';

const ProfileButtons = ({ userIsMe, user, status, followSuccessCallback, unfollowSuccessCallback }) => {
    const handleFollowSuccess = () => {
        followSuccessCallback(user.id);
    };

    const handleUnfollowSuccess = () => {
        unfollowSuccessCallback(user.id);
    };

    return (
        <div className='buttons'>
            {userIsMe && (
                <>
                    <a id='edit-button' href={'/profile/edit'} className='button'>Edit profile</a>
                    <LogoutButton />
                </>
            )}
            {!userIsMe && user && (
                <>
                    <FollowsButtons
                        userId={user.id}
                        status={status}
                        followSuccessCallback={handleFollowSuccess}
                        unfollowSuccessCallback={handleUnfollowSuccess}
                    />
                    <a href={`/chat/${user.username}`} className='button'>Message</a>
                </>
            )}
        </div>
    )
};

export default ProfileButtons;
