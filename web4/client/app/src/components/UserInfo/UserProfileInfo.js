import React, { useEffect, useState, useRef } from 'react';
import '../../styles/profileStyles.css';
import LinkifiedUsername from '../../util/LinkifiedUsername';
import Modal from '../Modal/ModalWindow';
import ProfileButtons from '../../components/Buttons/ProfileButtons';
import { sendGetRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';
import UserItem from './UserItem';
import { follow, unfollow } from '../../util/follows';
import Recommendations from './Recommendations';


const SERVER = 'http://localhost:8080/api';

const UserProfileInfo = ({
    user,
    userIsMe
}
) => {
    const statusMap = {
        'NONE': 'Follow',
        'IS_FOLLOWER': 'Follow back',
        'IS_FOLLOWING': 'Unfollow',
        'FRIENDS': 'Unfollow',
    }

    const [profilePictureURL, setProfilePictureURL] = useState(null);
    const [status, setStatus] = useState('NONE');
    const [followerCount, setFollowerCount] = useState(user.followerCount);
    const [followingCount, setFollowingCount] = useState(user.followingCount);

    const [followersOpen, setFollowersOpen] = useState(false);
    const [followingsOpen, setFollowingsOpen] = useState(false);
    const [recommendations, setRecommendations] = useState([]);

    useEffect(() => {
        const fetchProfilePicture = async () => {
            try {
                const imageURL = await fetchImage(user.profilePicture);
                setProfilePictureURL(imageURL);
            } catch (error) {
                console.error('Error loading profile picture:', error);
            }
        };
        fetchProfilePicture();
    }, [user.profilePicture]);

    useEffect(() => {
        const fetchStatus = async () => {
            const status = await getConnections(`${SERVER}/follow/checkConnection?userId=${user.id}`);
            setStatus(status);
        };
        fetchStatus();
    }, [user]);


    async function getConnections(request, error = 'Error fetching connections:') {
        return (await sendGetRequest(request, error)).data;
    }

    const openFollowers = () => {
        setFollowersOpen(true);
    };

    const closeFollowers = () => {
        setFollowersOpen(false);
    };

    const openFollowings = () => {
        setFollowingsOpen(true);
    };

    const closeFollowings = () => {
        setFollowingsOpen(false);
    };

    const handleFollowSuccess = async () => {
        if (!userIsMe) {
            const fCount = followerCount + 1;
            setFollowerCount(fCount);
            setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${user.id}`));
        }
        else {
            const fCount = followingCount + 1;
            setFollowingCount(fCount);
        }
    };

    const handleUnfollowSuccess = async () => {
        if (!userIsMe) {
            const fCount = followerCount - 1;
            setFollowerCount(fCount);
            setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${user.id}`));
        }
        else {
            const fCount = followingCount - 1;
            setFollowingCount(fCount);
        }
    };

    const handleForceUnfollowSuccess = async () => {
        const fCount = followerCount - 1;
        setFollowerCount(fCount);
        setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${user.id}`));
    };

    return (
        <>
            <div className="profile-info-top">
                <div className="profile-picture"><img alt="Profile Picture" src={profilePictureURL} /></div>
                <div className="user-info">
                    <div>
                        <h2 className="username">{user.username}</h2>
                        <div className="follow-info">
                            <a className="follow-info-button" id="follower-info-button" onClick={openFollowers}>
                                <p className="followers">Followers:&nbsp;&nbsp;</p>
                                <p className="followers">{followerCount}</p>
                            </a>
                            <a className="follow-info-button" id="following-info-button" onClick={openFollowings}>
                                <p className="followers">Following:&nbsp;&nbsp;</p>
                                <p className="followers">{followingCount}</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div className="user-info-bio">
                <h4 className="display-name" id="display-name">{user.displayName}</h4>
                <div className="bio" id="bio" >
                    {user.bio ? <LinkifiedUsername text={user.bio} />
                        : <><br/><br/></>}
                </div>
            </div>

            {user && status &&
                <div className='buttons'>
                    <ProfileButtons
                        user={user}
                        status={statusMap[status]}
                        userIsMe={userIsMe}
                        followSuccessCallback={handleFollowSuccess}
                        unfollowSuccessCallback={handleUnfollowSuccess}
                    />
                </div>
            }

            {/* {user && recommendations && userIsMe &&
                <ul className='recomms'>
                    <h4>Recommendations</h4>
                    {recommendations.map((rec) => (
                        <div key={rec.id} className='recom'>
                            <UserItem user={rec} />
                            <div className='act'>
                                {!rec.followed && <button className='smol-button'
                                    onClick={() => follow(rec.id, handleFollowSuccess(rec.id))}>Follow</button>}
                                {rec.followed && <button className='smol-button'
                                    onClick={() => unfollow(rec.id, handleUnfollowSuccess(rec.id))}>Unfollow</button>}
                                <button className='smol-button' onClick={() => removeRecommendation(rec.id)}>x</button>
                            </div>
                        </div>
                    ))}
                </ul>
            } */}

            {userIsMe && <Recommendations
                user={user}
                handleFollowSuccess={handleFollowSuccess}
                handleUnfollowSuccess={handleUnfollowSuccess} />
            }

            {followersOpen &&
                <Modal
                    text="Followers"
                    request={`${SERVER}/follow/getFollowers?userId=${user.id}`}
                    onClose={closeFollowers}
                    userIsMe={userIsMe}
                    successCallback={handleForceUnfollowSuccess}
                />
            }

            {followingsOpen &&
                <Modal
                    text="Followings"
                    request={`${SERVER}/follow/getFollowings?userId=${user.id}`}
                    onClose={closeFollowings}
                    userIsMe={userIsMe}
                    successCallback={handleUnfollowSuccess}
                />
            }
        </>
    );
};

export default UserProfileInfo;
