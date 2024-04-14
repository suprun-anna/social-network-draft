import { sendGetRequest, sendPostRequest, sendDeleteRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';
import React, { useEffect, useState } from 'react';
import LinkifiedUsername from '../../util/LinkifiedUsername';
import '../../styles/postStyles.css';
import Modal from '../../components/Modal/ModalWindow';
import Comments from '../../components/Comments/Comments';
import { useNavigate } from 'react-router-dom';
import ConfirmationModal from '../../components/Modal/ConfirmationModal';

const SERVER = 'http://localhost:8080/api';

const Post = ({ post }) => {
    const [profilePictureURL, setProfilePictureURL] = useState(null);
    const [postPictureURL, setPostPictureURL] = useState(null);
    const [firstLikeToShow, setFirstLikeToShow] = useState(null);
    const [isLiked, setLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(post.likeCount);
    const [showLikesModal, setShowLikesModal] = useState(false);
    const [postAuthor, setPostAuthor] = useState(false);
    const [user, setUser] = useState(false);
    const navigate = useNavigate();
    const [showConfirmation, setShowConfirmation] = useState(false);


    const openLikes = () => {
        setShowLikesModal(true);
    };

    const closeLikes = () => {
        setShowLikesModal(false);
    };

    useEffect(() => {
        const fetchUserData = async () => {
            setUser(await getUser(`${SERVER}/user/me`));
            setPostAuthor(await getUser(`${SERVER}/user/id?userId=${post.userId}`));
        };

        fetchUserData();
    }, []);

    async function getUser(request, error = 'Error fetching user data: ') {
        return (await sendGetRequest(request, error)).data;
    }

    useEffect(() => {
        const fetchLikeStatus = async () => {
            try {
                const firstLike = await getFirstLike();
                setFirstLikeToShow(firstLike);
            } catch (error) {
                console.error('Error checking like status:', error);
            }
        };

        fetchLikeStatus();
    }, [user.id, post.id, isLiked]);


    useEffect(() => {
        const fetchLikeStatus = async () => {
            try {
                const liked = await checkIfLiked();
                setLiked(liked);
            } catch (error) {
                console.error('Error checking like status:', error);
            }
        };

        fetchLikeStatus();
    }, [user.id, post.id]);

    useEffect(() => {
        const fetchProfilePicture = async () => {
            try {
                const imageURL = await fetchImage(postAuthor.profilePicture);
                setProfilePictureURL(imageURL);
            } catch (error) {
                console.error('Error loading profile picture:', error);
            }
        };

        fetchProfilePicture();
    }, [postAuthor.profilePicture]);

    useEffect(() => {
        const fetchPostPicture = async () => {
            try {
                const imageURL = await fetchImage(post.pictureUrl);
                setPostPictureURL(imageURL);
            } catch (error) {
                console.error('Error loading profile picture:', error);
            }
        };

        fetchPostPicture();
    }, [user.profilePicture]);

    useEffect(() => {
        setLikeCount(post.likeCount);
    }, [post.likeCount]);

    const checkIfLiked = async () => {
        try {
            const response = await sendGetRequest(`${SERVER}/interact/likes/isLiked?postId=${post.id}&userId=${user.id}`, '');
            return response.data;
        } catch (error) {
            console.error('Error checking if liked:', error);
            return false;
        }
    };

    const getFirstLike = async () => {
        try {
            const requestData = (await sendGetRequest(`${SERVER}/interact/likes/getAll?postId=${post.id}&page=${0}&size=${1}`, '')).data;
            if (requestData.length > 0) {
                const firstLikeUserId = requestData[0].userId;
                const firstLikeUserUsername = (await sendGetRequest(`${SERVER}/user/id?userId=${firstLikeUserId}`, '')).data.username;
                return firstLikeUserUsername;
            } else return null;
        } catch (error) {
            console.error('Error checking if liked:', error);
        }
    };

    const handleLikeClick = async () => {
        try {
            if (isLiked) {
                await sendDeleteRequest(`${SERVER}/interact/likes/remove?postId=${post.id}`);
                post.likeCount -= 1;
                setLiked(false);
            } else {
                await sendPostRequest(`${SERVER}/interact/likes/place?postId=${post.id}`, {});
                post.likeCount += 1;
                setLiked(true);
            }
        } catch (error) {
            console.error('Error toggling like:', error);
        }
    };


    function formatCreatedAt(dateTimeString) {
        return dateTimeString.replace('T', ' ').slice(0, dateTimeString.lastIndexOf(":"));;
    }

    const handleEdit = () => {
        navigate(`/posts/post/edit/${post.id}`);
    };


    const handleDelete = () => {
        setShowConfirmation(true);
    };

    const confirmDelete = async () => {
        setShowConfirmation(false);
        try {
            await sendDeleteRequest(`${SERVER}/posts/delete?id=${post.id}`);
            navigate('/profile/user/me');
        } catch (error) {
            console.error('Error deleting post:', error);
        }
    };

    const cancelDelete = () => {
        setShowConfirmation(false);
    };

    return (
        <div className="post-card">
            <div className="post-header">
                <div className='user-button'>
                    <div className='post-header-container'><img alt="Profile Picture" className="profile-picture" src={profilePictureURL} /></div>
                    <a className="username username-link" href={`/profile/user/${postAuthor.username}`} >{postAuthor.username}</a>
                </div>
                {user && post && user.id === post.userId && (
                    <div className="post-change-actions">
                        <button className="edit-button" onClick={handleEdit}>Edit</button>
                        <span>•</span>
                        <button className="delete-button" onClick={handleDelete}>Delete</button>
                    </div>
                )}

                {showConfirmation && (
                    <ConfirmationModal
                        message="Are you sure you want to delete this post?"
                        onConfirm={confirmDelete}
                        onCancel={cancelDelete}
                    />
                )}

            </div>
            <div className="post-content">
                <div className="post-image-container">
                    <img alt="Post Image" id="post-image" src={postPictureURL} />
                </div>
                <div className='post-description'>
                    {post && <h2 className="post-title" id="post-title">
                        {post.title && post.title.trim() !== "" ? (
                            <LinkifiedUsername text={post.title} />
                        ) : (
                            "Untitled"
                        )}
                    </h2>}
                    {post && <div className="post-text" id="post-text">
                        {post && <LinkifiedUsername text={post.content} />}
                    </div>}
                    {post && <p className="post-date" id="post-date">Created at: {formatCreatedAt(post.createdAt)}</p>}
                    {post.isUpdated && <p className="post-edited" id="post-edited">(redacted)</p>}

                    <div className="post-actions">
                        <button
                            className={`like-button ${isLiked ? 'like-button-liked' : ''}`}
                            onClick={handleLikeClick}
                            title={isLiked ? 'Unlike' : 'Like'}>
                            🤍
                        </button>
                        <div className="like-info">
                            {likeCount > 1 &&
                                <div className='row'>
                                    <a className='username username-link' href={`/profile/user/${firstLikeToShow}`}>{`${firstLikeToShow}`}</a>
                                    <a className='clickable' onClick={openLikes}>&nbsp;
                                        {`and ${post.likeCount - 1} more liked this post.`}
                                    </a>
                                </div>
                            }
                            {likeCount == 0 &&
                                <div>Be first to like this post!</div>
                            }
                            {likeCount == 1 &&
                                <div className='row'>
                                    <a className='username username-link' href={`/profile/user/${firstLikeToShow}`}>{`${firstLikeToShow}`}</a>
                                    <div>&nbsp; liked this post.</div>
                                </div>
                            }
                        </div>
                        {showLikesModal &&
                            <Modal
                                text="Likes"
                                request={`${SERVER}/interact/likes/getAll?postId=${post.id}`}
                                onClose={closeLikes}
                            />
                        }
                    </div>
                    <br />
                    <hr />
                    <Comments post={post} />
                </div>
            </div>
        </div>
    );
};

export default Post;































