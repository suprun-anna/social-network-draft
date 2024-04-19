import { useState, useEffect, useRef } from 'react';
import '../../styles/authStyles.css';
import '../../styles/styles.css';
import Post from './Post';
import Menu from '../../components/Menu/Menu';
import { sendPostRequest, sendGetRequest } from '../../util/requests';
import { useNavigate } from 'react-router-dom';
import Recommendations from '../../components/UserInfo/Recommendations';


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
        try {
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

    const handleFollowSuccess = async (id) => {
        const updatedRecommendations = recommendations.map(rec => {
            if (id && rec.id === id) {
                return { ...rec, followed: true };
            }
            return rec;
        });
        setRecommendations(updatedRecommendations);
    };

    const handleUnfollowSuccess = async (id) => {
        const updatedRecommendations = recommendations.map(rec => {
            if (id && rec.id === id) {
                return { ...rec, followed: false };
            }
            return rec;
        });
        setRecommendations(updatedRecommendations);
    };

    return (
        <div className="container">
            <Menu />
            {posts && posts.length > 0 ? (
                <div className='posts-container' ref={containerRef}>
                    {posts.map((post) => (
                        <Post key={post.id} post={post} />
                    ))}
                </div>
            ) : (
                <div className="no-posts-message">
                    <div >{'No posts to see... Follow more users :)'}</div>
                    <Recommendations
                        user={user}
                        handleFollowSuccess={handleFollowSuccess}
                        handleUnfollowSuccess={handleUnfollowSuccess}
                    />
                </div>
            )}
        </div>
    );

};

export default HomePage;
