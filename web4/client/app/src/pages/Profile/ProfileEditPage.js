import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../styles/authStyles.css';
import '../../styles/styles.css';
import { sendGetRequest, sendPostRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';
import { useNavigate } from 'react-router-dom';


const SERVER = 'http://localhost:8080/api';


const ProfileEditPage = () => {
    const [profilePicture, setProfilePicture] = useState(null);
    const [bio, setBio] = useState('');
    const [displayName, setDisplayName] = useState('');
    const [age, setAge] = useState('');
    const [isPictureSelected, setIsPictureSelected] = useState(false);
    const [imageURL, setImageURL] = useState('');
    const navigate = useNavigate();


    useEffect(() => {
        const fetchUserData = async () => {
            if (localStorage.getItem('token') === null) {
                navigate('/login');
            } else {
                const userData = await getUserInfo(`${SERVER}/user/me`);
                setBio(userData.bio ? userData.bio.replace(/\n/g, '\r\n') : '');
                setDisplayName(userData.displayName || '');
                setAge(userData.age || '');
                setProfilePicture(await fetchImage(userData.profilePicture));
            }
        }
        fetchUserData();
    }, []);

    async function getUserInfo(request, error = 'Error fetching user data: ') {
        return (await sendGetRequest(request, error)).data;
    }

    const handleProfilePictureChange = (event) => {
        const file = event.target.files[0];
        setProfilePicture(file);
        setImageURL(URL.createObjectURL(file));
        setIsPictureSelected(true);
    };

    const handleBioChange = (event) => {
        setBio(event.target.value);
    };

    const handleDisplayNameChange = (event) => {
        setDisplayName(event.target.value);
    };

    const handleAgeChange = (event) => {
        setAge(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (isPictureSelected) {
                const formData = new FormData();
                formData.append('profilePicture', profilePicture);
                const profilePictureResponse = await axios.post(`${SERVER}/user/update/pfp`, formData, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'multipart/form-data'
                    }
                });
            }
            const formattedBio = encodeURIComponent(bio);
            const bioResponse = await sendPostRequest(`${SERVER}/user/update/bio?bio=${formattedBio}`, {});
            const displayNameResponse = await sendPostRequest(`${SERVER}/user/update/displayName?displayName=${displayName}`, {});
            const ageToUpdate = age ? age : 0;
            const ageResponse = await sendPostRequest(`${SERVER}/user/update/age?age=${ageToUpdate}`, {});
            navigate('/profile/user/me');
        } catch (error) {
            console.error('Error updating profile:', error);
        }
    };


    return (
        <div className="edit-form">
            <form onSubmit={handleSubmit}>
                <h2>Edit Profile</h2>
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
                    <label>Bio:</label>
                    <textarea value={bio} onChange={handleBioChange} />
                </div>
                <div className="form-group-edit">
                    <label>Display Name:</label>
                    <input type="text" value={displayName} onChange={handleDisplayNameChange} />
                </div>
                <div className="form-group-edit">
                    <label>Age:</label>
                    <input type="number" value={age} onChange={handleAgeChange} />
                </div>
                <button className='button-edit' type="submit">Save Changes</button>
            </form>
        </div>
    );
};

export default ProfileEditPage;
