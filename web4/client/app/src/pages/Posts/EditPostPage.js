import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { sendGetRequest, sendPutRequest, sendPostRequest } from '../../util/requests';


const SERVER = 'http://localhost:8080/api';


const EditPostPage = () => {
    const [post, setPost] = useState(null);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [picture, setPicture] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();
    const [imageURL, setImageURL] = useState('');
    const [isPictureSelected, setIsPictureSelected] = useState(false);

    useEffect(() => {
        const fetchPost = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                try {
                    const postIdentificator = window.location.pathname.split('/').pop();
                    const response = await sendGetRequest(`${SERVER}/posts/get?id=${postIdentificator}`);
                    const postData = response.data;
                    setPost(postData);
                    setTitle(postData.title);
                    setContent(postData.content);
                } catch (error) {
                    console.error('Error fetching post:', error);
                }
            }
        };

        fetchPost();
    }, []);

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handlePictureChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            setPicture(file);
            setImageURL(URL.createObjectURL(file));
            setIsPictureSelected(true);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (post.title === title && post.content === content) {
                navigate(`/posts/post/${post.id}`);
            }
            else {
                const formData = new FormData();
                formData.append('id', post.id);
                formData.append('title', title);
                formData.append('content', content);
                const response = await sendPutRequest(`${SERVER}/posts/edit`, formData);
                navigate(`/posts/post/${post.id}`);
            }
        } catch (error) {
            console.error('Error fetching post:', error);

            if (error.response) {
                setErrorMessage(error.response.data.message);
            } else {
                setErrorMessage('Network error. Please try again later.');
            }
        }
    };

    const cancel = () => {
        navigate(`/posts/post/${post.id}`);
    };

    return (
        <div className='edit-form'>
            {errorMessage && <div className="error-message">{errorMessage}</div>}
            <form onSubmit={handleSubmit}>
                <h2>Edit Post</h2>
                <div className="form-group-edit">
                    <label>Title:</label>
                    <input type="text" value={title} onChange={handleTitleChange} />
                </div>
                <div className="form-group-edit">
                    <label>Content:</label>
                    <textarea value={content} onChange={handleContentChange} />
                </div>
                {/* 
                <div className="form-group-edit">
                    <label>Picture:</label>
                    <input type="file" onChange={handlePictureChange} accept="image/*" />

                </div>
                {imageURL &&
                    <div className='pic-example'>
                        <img src={imageURL} alt="Selected Profile Picture" />
                    </div>
                } */}
                <button className='button-edit' type="submit">Save Changes</button>
                <button className='button-edit' onClick={cancel}>Go back</button>
            </form>
        </div>
    );
};

export default EditPostPage;
