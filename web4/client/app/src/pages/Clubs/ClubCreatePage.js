import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../styles/authStyles.css';
import '../../styles/styles.css';
import { sendGetRequest, sendPostRequest, sendPutRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';
import { useNavigate } from 'react-router-dom';


const SERVER = 'http://localhost:8080/api';


const ClubCreatePage = () => {
    const [owner, setOwner] = useState(null);
    const [profilePicture, setProfilePicture] = useState(null);
    const [description, setDescription] = useState('');
    const [name, setName] = useState('');
    const [isPictureSelected, setIsPictureSelected] = useState(false);
    const [imageURL, setImageURL] = useState('');
    const [isClosed, setIsOpen] = useState('');
    const navigate = useNavigate();


    useEffect(() => {
        const fetchUserData = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                const me = (await sendGetRequest(`${SERVER}/user/me`)).data;
                setOwner(me);
            }
        }
        fetchUserData();
    }, []);

    async function getClubInfo(request, error = 'Error fetching club data: ') {
        return (await sendGetRequest(request, error)).data;
    }

    const handleProfilePictureChange = (event) => {
        const file = event.target.files[0];
        setProfilePicture(file);
        setImageURL(URL.createObjectURL(file));
        setIsPictureSelected(true);
    };

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    };

    const handleNameChange = (event) => {
        setName(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const club = {
                name: name,
                description: description,
                isOpen: !isClosed
            };
            const response = (await sendPostRequest(`${SERVER}/club`, club)).data;

            if (isPictureSelected) {
                const formData = new FormData();
                formData.append('profilePicture', profilePicture);
                const profilePictureResponse = await axios.post(`${SERVER}/club/update/pfp?id=${response.id}`, formData, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'multipart/form-data'
                    }
                });               
            }

            navigate(`/clubs/${response.id}`);
        } catch (error) {
            console.error('Error updating club:', error);
        }
    };

    const cancel = () => {
        navigate(`/clubs`);
    };

    const handleCheckboxChange = (event) => {
        setIsOpen(event.target.checked);
    };

    return (
        <div className="edit-form">
            <form onSubmit={handleSubmit}>
                <h2>Edit Club</h2>
                <div className="form-group-edit">
                    <label>Profile Picture:</label>
                    <input type="file" onChange={handleProfilePictureChange} accept="image/*" />
                    {imageURL &&
                        <div className='pfp-example'>
                            <img src={imageURL} alt="Selected Profile Picture" />
                        </div>
                    }
                </div>
                <div className="form-group-edit">
                    <label>Name:</label>
                    <input type="text" value={name} onChange={handleNameChange} />
                </div>
                <div className="form-group-edit">
                    <label>Description:</label>
                    <textarea className='textarea' value={description} onChange={handleDescriptionChange} />
                </div>
                <div className="form-group-edit">
                    <label>
                        Is Closed Type:
                        <input type="checkbox" className="checkbox" checked={isClosed} onChange={handleCheckboxChange} />
                    </label>
                </div>
                <button className='button-edit' type="submit">Save Changes</button>
                <button className='button-edit' onClick={cancel}>Go back</button>
            </form>
        </div>
    );
};

export default ClubCreatePage;
