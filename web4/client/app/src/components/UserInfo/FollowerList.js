import React from 'react';
import FollowerItem from './FollowerItem';
import '../profileStyles.css';

const FollowersList = ({ followsData }) => {
    return (
        <>
            <ul>
                {followsData.map((follower) => (
                    <FollowerItem key={follower.id} follower={follower} />
                ))}
            </ul>
        </>
    );
};

export default FollowersList;