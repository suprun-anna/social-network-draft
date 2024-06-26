import React, { useEffect, useState } from 'react';
import { sendPostRequest, sendGetRequest } from '../../util/requests';
import UserProfileInfo from '../../components/UserInfo/UserProfileInfo';
import Menu from '../../components/Menu/Menu';
import { useNavigate } from 'react-router-dom';
import '../../styles/profileStyles.css';
import PostsComponent from '../../components/Posts/PostsComponent';
import ClubInfo from './ClubInfo';

const SERVER = 'http://localhost:8080/api';

const ClubPage = () => {
    const [clubIsMine, setClubIsMine] = useState(false);
    const [club, setClub] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchClubData = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                const clubIdentificator = window.location.pathname.split('/').pop();
                const club = (await sendGetRequest(`${SERVER}/club/byId?id=${clubIdentificator}`)).data;
                if (Object.keys(club).length === 0) navigate('/clubs');
                setClub(club);
                const clubIsMine = ((await sendGetRequest(`${SERVER}/club/isMyOwn?id=${clubIdentificator}`)).data);
                console.log(clubIsMine);
                setClubIsMine(clubIsMine);
            }
        };

        fetchClubData();
    }, []);


    return (
        <div className="container">
            <Menu />
            <div className="profile">
                {club && <PostsComponent request={`posts/getByClub?id=${club.id}`} />}
                <header>
                    <div className="profile-info-container">
                        <div className="profile-info">
                            {club && 
                                <ClubInfo
                                    club={club}
                                    userIsMe={clubIsMine}
                                />
                            }
                        </div>
                    </div>
                </header>
            </div>
        </div>
    );

};

export default ClubPage;










































// import React, { useEffect, useState } from 'react';
// import axios from 'axios';

// import { sendGetRequest, sendPostRequest } from '../../util/requests';

// import ProfileButtons from '../../components/Buttons/ProfileButtons';
// import UserProfileInfo from '../../components/UserInfo/UserProfileInfo';
// import Menu from '../../components/Menu/Menu';

// import '../../styles/profileStyles.css';

// const SERVER = 'http://localhost:8080/api';

// const ProfilePage = () => {
//     const statusMap = {
//         'NONE': 'Follow',
//         'IS_FOLLOWER': 'Follow back',
//         'IS_FOLLOWING': 'Unfollow',
//         'FRIENDS': 'Unfollow',
//     }
//     const [userIsMe, setUserIsMe] = useState(false);
//     const [status, setStatus] = useState('NONE');

//     const [user, setUser] = useState(null);
//     const [followersData, setFollowersData] = useState([]);
//     const [followingsData, setFollowingsData] = useState([]);


//     useEffect(() => {
//         const fetchUserData = async () => {
//             const userIdentificator = window.location.pathname.split('/').pop();
//             const me = await getUserInfo(`${SERVER}/user/me`);
//             const userIsMe = userIdentificator === 'me' || userIdentificator == me.username;
//             setUserIsMe(userIsMe);
//             const user = userIsMe ? me : await getUserInfo(`${SERVER}/user?username=${userIdentificator}`);
//             setUser(user);

//             const status = await getConnections(`${SERVER}/follow/checkConnection?userId=${user.id}`);
//             setStatus(status);

//             fetchFollowsData(user.id);
//         };

//         fetchUserData();
//     }, []);


//     async function getUserInfo(request, error = 'Error fetching user data: ') {
//         return (await sendGetRequest(request, error)).data;
//     }

//     async function getConnections(request, error = 'Error fetching connections:') {
//         return (await sendGetRequest(request, error)).data;
//     }


//     const fetchFollowsData = async (userId) => {
//         setFollowersData(((await sendGetRequest(`${SERVER}/follow/getFollowers?userId=${userId}`)).data));
//         setFollowingsData(((await sendGetRequest(`${SERVER}/follow/getFollowings?userId=${userId}`)).data));
//     };


//     const handleFollowSuccess = async (userId) => {
//         setUser(prevUser => ({
//             ...prevUser,
//             followerCount: prevUser.followerCount + 1
//         }));
//         await fetchFollowsData(userId);
//         await setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${userId}`));
//     };

//     const handleUnfollowSuccess = async (userId) => {
//         setUser(prevUser => ({
//             ...prevUser,
//             followerCount: prevUser.followerCount - 1
//         }));
//         await fetchFollowsData(userId);
//         await setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${userId}`));
//     };



//     return (
//         <div className="container">
//             <Menu />
//             <div className="profile">

//                 {/* {user && <PostsComponent userId={user.id} />} */}

//                 <header>
//                     <div className="profile-info-container">
//                         <div className="profile-info">

//                             {user && followersData && followingsData &&
//                                 <UserProfileInfo
//                                     user={user}
//                                     followersData={followersData}
//                                     followingsData={followingsData}
//                                 />
//                             }

//                             <br />

//                             {user && status &&
//                                 <div className='buttons'>
//                                     <ProfileButtons
//                                         userId={user.id}
//                                         status={statusMap[status]}
//                                         userIsMe={userIsMe}
//                                         followSuccessCallback={handleFollowSuccess}
//                                         unfollowSuccessCallback={handleUnfollowSuccess}
//                                     />
//                                 </div>
//                             }

//                         </div>
//                     </div>
//                 </header>

//             </div>
//         </div>
//     );

// };

// export default ProfilePage;
