import React from 'react';
import FollowsButtons from './FollowsButtons';
import LogoutButton from '../../pages/Authentication/LogoutButton';

const ProfileButtons = ({ userIsMe, userId, status, followSuccessCallback, unfollowSuccessCallback }) => {
    const handleFollowSuccess = () => {
        followSuccessCallback(userId);
    };

    const handleUnfollowSuccess = () => {
        unfollowSuccessCallback(userId);
    };

    return (
        <div className='buttons'>
            {userIsMe && (
                <>
                    <a id='edit-button' href={'/profile/edit'} className='button'>Edit profile</a>
                    <LogoutButton />
                </>
            )}
            {!userIsMe && userId && (
                <>
                    <FollowsButtons
                        userId={userId}
                        status={status}
                        followSuccessCallback={handleFollowSuccess}
                        unfollowSuccessCallback={handleUnfollowSuccess}
                    />
                    <button className='button'>Message</button>
                </>
            )}
        </div>
    )
};

export default ProfileButtons;









// const ProfileButtons = (userIsMe, userId, status, follow, unfollow) => {
//     return (
//         <div className='buttons'>
//             {(userIsMe === true) && (
//                 <a id='edit-button' href={'/profile/edit'} className='button'>Edit profile</a>
//             )}
//             {(userId && userIsMe === false) && (
//                 <FollowsButtons
//                     userId={userId}
//                     status={status}
//                     follow={follow}
//                     unfollow={unfollow}
//                 />
//             )}
//         </div>
//     )
// };

// export default ProfileButtons;