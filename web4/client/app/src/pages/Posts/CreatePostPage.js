import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const SERVER = 'http://localhost:8080/api';


const CreatePostPage = (clubId) => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [picture, setPicture] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();
    const [imageURL, setImageURL] = useState('');
    const [isPictureSelected, setIsPictureSelected] = useState(false);


    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handlePictureChange = (event) => {
        const file = event.target.files[0];
        setPicture(file);
        if (file) {
            setImageURL(URL.createObjectURL(file));
            setIsPictureSelected(true);
        }
    };


    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const formData = new FormData();
            formData.append('title', title);
            formData.append('content', content);
            formData.append('picture', picture);
            let clubId = window.location.pathname.split('/').pop();
            clubId = !isNaN(parseInt(clubId)) ? parseInt(clubId) : undefined;
            clubId && formData.append('club', clubId);
            console.log(clubId);

            if (isPictureSelected) {
                const response = await axios.post(`${SERVER}/posts/create`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                navigate(clubId != undefined ? `/clubs/${clubId}` : '/profile/user/me');
            } else setErrorMessage('Picture is not selected!');
        } catch (error) {
            if (error.response) {
                setErrorMessage(error.response.data.message);
            } else {
                setErrorMessage('Network error. Please try again later.');
            }
        }
    };

    const cancel = () => {
        let clubId = window.location.pathname.split('/').pop();
        clubId = !isNaN(parseInt(clubId)) ? parseInt(clubId) : undefined;
        navigate(clubId != undefined ? `/clubs/${clubId}` : '/profile/user/me');
    };

    return (
        <div className='edit-form'>
            {errorMessage && <div className="error-message">{errorMessage}</div>}
            <form onSubmit={handleSubmit}>
                <h2>Create New Post</h2>
                <div className="form-group-edit">
                    <label>Title:</label>
                    <input type="text" value={title} onChange={handleTitleChange} />
                </div>
                <div className="form-group-edit">
                    <label>Content:</label>
                    <textarea value={content} onChange={handleContentChange} />
                </div>
                <div className="form-group-edit">
                    <label>Picture:</label>
                    <input type="file" onChange={handlePictureChange} accept="image/*" />

                </div>

                {picture && imageURL &&
                    <div className='pic-example'>
                        <img src={imageURL} alt="Selected Profile Picture" />
                    </div>
                }
                <button className='button-edit' type="submit">Save Changes</button>
                <button className='button-edit' onClick={cancel}>Go back</button>
            </form>
        </div>
    );
};

export default CreatePostPage;
