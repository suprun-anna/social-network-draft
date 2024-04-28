import React, { useEffect, useState, useRef } from 'react';
import '../../styles/profileStyles.css';
import LinkifiedUsername from '../../util/LinkifiedUsername';
import { fetchImage } from '../../util/fetchImage';
import { sendGetRequest, sendPostRequest } from '../../util/requests';
import ProfileButtons from '../../components/Buttons/ProfileButtons';
import Modal from '../../components/Modal/ModalWindow';
// import Modal from '../Modal/ModalWindow';
// import ProfileButtons from '../../components/Buttons/ProfileButtons';
// import { sendGetRequest } from '../../util/requests';
// import UserItem from './UserItem';
// import { follow, unfollow } from '../../util/follows';
// import Recommendations from './Recommendations';


const SERVER = 'http://localhost:8080/api';

const ClubInfo = ({
    club,
    userIsMe
}
) => {

    const [profilePictureURL, setProfilePictureURL] = useState(null);
    const [status, setStatus] = useState(false);
    const [memberCount, setMemberCount] = useState(club.followerCount);
    const [followersOpen, setFollowersOpen] = useState(false);

    useEffect(() => {
        const fetchProfilePicture = async () => {
            try {
                const imageURL = await fetchImage(club.profilePicture);
                setProfilePictureURL(imageURL);
                setMemberCount(club.memberCount);
            } catch (error) {
                console.error('Error loading profile picture:', error);
            }
        };
        fetchProfilePicture();
    }, [club]);

    useEffect(() => {
        const fetchMember = async () => {
            try {
                const member = (await sendGetRequest(`${SERVER}/club/isMember?id=${club.id}`)).data;
                setStatus(member);
            } catch (error) {
                console.error('Error:', error);
            }
        };
        fetchMember();
    }, [club]);

    const openFollowers = () => {
        setFollowersOpen(true);
    };

    const closeFollowers = () => {
        setFollowersOpen(false);
    };

    const handleFollowSuccess = async () => {
        const response = await sendPostRequest(`${SERVER}/club/join?clubId=${club.id}`);
        if (response.status === 200) {
            const fCount = memberCount + 1;
            setMemberCount(fCount);
            setStatus(true);
        }
    };

    const handleUnfollowSuccess = async () => {
        const response = await sendPostRequest(`${SERVER}/club/leave?clubId=${club.id}`);
        if (response.status === 200) {
            const fCount = memberCount - 1;
            setMemberCount(fCount);
            setStatus(false);
        }
    };


    return (
        <>
            <div className="profile-info-top">
                <div className="profile-picture"><img alt="Profile Picture" src={profilePictureURL} /></div>
                <div className="user-info">
                    <div>
                        <h2>{club.name}</h2>
                        <div className="follow-info">
                            <a className="follow-info-button" id="follower-info-button" onClick={openFollowers}>
                                <p className="followers">Members:&nbsp;&nbsp;</p>
                                <p className="followers">{memberCount}</p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div className="user-info-bio">
                <h4 className='display-name'>Description:</h4>
                <div className="bio" id="bio" >
                    {club.description && <LinkifiedUsername text={club.description} />}
                </div>
            </div>

            <br />
            <div className='buttons'>
                {!status && (
                    <button className='button' onClick={handleFollowSuccess}>Join club</button>
                )}
                {status && (
                    <button className='button' onClick={handleUnfollowSuccess}>Leave club</button>
                )}
                <a className="button" href={`/posts/post/create/${club.id}`}>Create post</a>
            </div>

        
        
            {followersOpen &&
                <Modal
                    text="Members"
                    request={`${SERVER}/club/members?clubId=${club.id}`}
                    onClose={closeFollowers}
                    successCallback={handleUnfollowSuccess}
                />
            }


        </>
    );
};

export default ClubInfo;
