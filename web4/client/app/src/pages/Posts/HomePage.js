import { useState, useEffect, useRef } from 'react';
import '../../styles/authStyles.css';
import '../../styles/styles.css';
import Post from './Post';
import Menu from '../../components/Menu/Menu';
import { sendPostRequest, sendGetRequest } from '../../util/requests';
import { useNavigate } from 'react-router-dom';


const SERVER = 'http://localhost:8080/api';

const HomePage = () => {
    const [posts, setPosts] = useState([]);
    const containerRef = useRef(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const size = 5;
    const navigate = useNavigate();
    const [recommendations, setRecommendations] = useState([]);
    const [user, setUser] = useState(null);

    async function getUserInfo(request, error = 'Error fetching user data: ') {
        try{
            const response = await sendGetRequest(request, error);
            return response.data;
        } catch (e) {
            navigate('/login');
        }
    }


    useEffect(() => {
        const fetchUserData = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                const user = (await getUserInfo(`${SERVER}/user/me`));
                setUser(user);
                if (allLoaded) return;
                try {
                    const newData = await getPosts(`${SERVER}/feed/get?page=${currentPage}&size=${size}`);
                    if (newData.length > 0) {
                        setPosts(prevPosts => [...prevPosts, ...newData]);
                    } else {
                        setAllLoaded(true);
                    }
                } catch (error) {
                    console.error(`Error fetching posts`, error);
                }
            }
        };

        fetchUserData();
    }, [currentPage]);

    async function getPosts(request, error = 'Error fetching posts:') {
        return (await sendGetRequest(request, error)).data;
    }


    useEffect(() => {
        const handleScroll = () => {
            if (containerRef.current) {
                const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
                if (scrollTop + clientHeight >= scrollHeight - 20) {
                    setCurrentPage(prevPage => prevPage + 1);
                }
            }
        };

        if (containerRef.current) {
            containerRef.current.addEventListener('scroll', handleScroll);
        }

        return () => {
            if (containerRef.current) {
                containerRef.current.removeEventListener('scroll', handleScroll);
            }
        };
    }, [posts]);

    return (
        <div className="container">
            <Menu />
            {user && recommendations && 
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
            }
            {posts && posts.length > 0 ? (
                <div className='posts-container' ref={containerRef}>
                    {posts.map((post) => (
                        <Post key={post.id} post={post} />
                    ))}
                </div>
            ) : (
                <div className="no-posts-message">You are not subscribed to anyone yet.</div>
            )}
        </div>
    );

};

export default HomePage;














// import React, { useEffect, useState } from 'react';
// import axios from 'axios';
// import '../../styles/authStyles.css';
// import '../../styles/styles.css';
// import Post from './Post';
// import Menu from '../../components/Menu/Menu';

// const SERVER = 'http://localhost:8080/api';

// const HomePage = () => {
//     const [user, setUser] = useState(null);
//     const [post, setPost] = useState(null);

//     async function sendGetRequest(request, error) {
//         try {
//             const response = await axios.get(request, {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 }
//             });
//             return response.data;
//         } catch (e) {
//             console.error(error, e);
//         }
//     }

//     useEffect(() => {
//         const fetchUserData = async () => {
//             setUser(await getUser(`${SERVER}/user/me`));
//             const postIdentificator = window.location.pathname.split('/').pop();
//             setPost(await getPost(`${SERVER}/posts/get?id=${postIdentificator}`));
//         };

//         fetchUserData();
//     }, []);


//     async function getUser(request, error = 'Error fetching user data: ') {
//         return await sendGetRequest(request, error);
//     }

//     async function getPost(request, error = 'Error fetching connections:') {
//         return await sendGetRequest(request, error);
//     }





//     return (
//         <div className="container">
//             <Menu />
//             {user && post && <Post post={post}></Post>}
//         </div>
//     );

// };

// export default HomePage;