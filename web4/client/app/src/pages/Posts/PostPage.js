import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../../styles/authStyles.css';
import '../../styles/styles.css';
import Post from './Post';
import Menu from '../../components/Menu/Menu';
import { sendGetRequest, sendPostRequest } from '../../util/requests';
import { useNavigate } from 'react-router-dom';

const SERVER = 'http://localhost:8080/api';

const PostPage = () => {
    const [post, setPost] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPostData = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                const postIdentificator = window.location.pathname.split('/').pop();
                const postResponse = await getPost(`${SERVER}/posts/get?id=${postIdentificator}`);
                setPost(postResponse);
            }
        };

        fetchPostData();
    }, []);

    async function getPost(request, error = 'Error fetching connections:') {
        return (await sendGetRequest(request, error)).data;
    }


    return (
        <div className="container">
            <Menu />
            <div className="posts-container">
                {post && <Post post={post}></Post>}
            </div>
        </div>
    );

};

export default PostPage;