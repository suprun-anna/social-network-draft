import React from 'react';
import { follow, unfollow } from '../../util/follows';

const FollowsButtons = ({ userId, status, followSuccessCallback, unfollowSuccessCallback }) => {
    const handleButtonClick = async () => {
        if (status === 'Unfollow') {
            await unfollow(userId, unfollowSuccessCallback);
        } else {
            await follow(userId, followSuccessCallback);
        }
    };

    return (
        <button className='button' onClick={handleButtonClick}>
            {status === 'Unfollow' ? 'Unfollow' : 'Follow'} 
        </button>
    );
};

export default FollowsButtons;