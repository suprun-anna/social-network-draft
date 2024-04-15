import React, { useState, useEffect, useRef } from 'react';
import { fetchImage } from '../../util/fetchImage';
import { sendGetRequest, sendPostRequest, sendDeleteRequest, sendPutRequest } from '../../util/requests';
import Comment from './Comment';

const SERVER = 'http://localhost:8080/api';

const Comments = ({ post }) => {
    const size = 10;
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [me, setMe] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);
    const [commentCount, setCommentCount] = useState(post.commentCount);
    const postId = post.id;
    const [collapsed, setCollapsed] = useState(true);


    useEffect(() => {
        const fetchMyInfo = async () => {
            try {
                const response = await sendGetRequest(`${SERVER}/user/me`, '');
                const user = response.data;
                if (!user.profilePictureURL) {
                    const imageURL = await fetchImage(user.profilePicture);
                    user.profilePictureURL = imageURL;
                }
                setMe(user);
            } catch (error) {
                console.error('Error fetching user\'s info:', error);
            }
        };
        fetchMyInfo();
    }, []);

    useEffect(() => {
        const fetchComments = async () => {
            if (allLoaded) return;
            try {
                const response = await sendGetRequest(`${SERVER}/interact/comments/getAll?postId=${postId}&page=${currentPage}&size=${size}`, '');
                if (response.data.length > 0) {
                    const commentsWithUsername = await Promise.all(response.data.map(async (comment) => {
                        const userResponse = await sendGetRequest(`${SERVER}/user/id?userId=${comment.userId}`, '');
                        const user = userResponse.data;
                        if (user) {
                            comment.username = user.username;
                            if (!comment.profilePictureURL) {
                                const imageURL = await fetchImage(user.profilePicture);
                                comment.profilePictureURL = imageURL;
                            }
                        }
                        return comment;
                    }));
                    setComments(prevComments => [...prevComments, ...commentsWithUsername]);
                } else setAllLoaded(true);
            } catch (error) {
                console.error('Error fetching comments:', error);
            }
        };

        fetchComments();
    }, [postId, currentPage]);

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
    }, []);



    const handleChange = (event) => {
        setNewComment(event.target.value);
    };

    const handleAddComment = async () => {
        try {
            const response = await sendPostRequest(`${SERVER}/interact/comments/place`, {
                postId: postId,
                text: newComment
            });
            const newCommentDto = response.data;
            newCommentDto.username = me.username;
            newCommentDto.profilePictureURL = me.profilePictureURL;
            setComments([...comments, newCommentDto]);
            setNewComment('');
            setCommentCount(prevCount => prevCount + 1);
        } catch (error) {
            console.error('Error placing comment:', error);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            await sendDeleteRequest(`${SERVER}/interact/comments/remove?commentId=${commentId}`);
            setComments(prevComments => prevComments.filter(comment => comment.id !== commentId));
            setCommentCount(prevCount => prevCount - 1);
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    const handleEditComment = async (commentId, newText) => {
        try {
            await sendPutRequest(`${SERVER}/interact/comments/edit?commentId=${commentId}&text=${newText}`, {});
            const updatedComments = comments.map(comment => {
                if (comment.id === commentId) {
                    comment.text = newText;
                }
                return comment;
            });
            setComments(updatedComments);
        } catch (error) {
            console.error('Error updating comment:', error);
        }
    };

    const toggleCollapse = () => {
        setCollapsed(!collapsed);
    };


    return (
        <div className="comments-container">
            <div className='comments-top'>
                <h3>Comments</h3>
                <div className='comment-count'>{commentCount}</div>
                <button className='collapse-button' title={collapsed ? 'Show comments' : 'Hide comments'} onClick={toggleCollapse}>
                    {collapsed ? '▶' : '▼'}
                </button>
            </div>
            {!collapsed && (
                <ul className='comments' ref={containerRef}>
                    {comments.map(comment => (
                        <React.Fragment key={comment.id}>
                            <Comment
                                key={comment.id}
                                comment={comment}
                                me={me}
                                onDeleteComment={handleDeleteComment}
                                onEditComment={handleEditComment}
                                post={post} />
                            <hr />
                        </React.Fragment>
                    ))}
                </ul>
            )}
            <div className='new-comment-section'>
                <textarea className='new-comment-text' value={newComment} onChange={handleChange} />
                <button className="send-button" title='Send comment' onClick={handleAddComment}>➤</button>
            </div>
        </div>
    );
};

export default Comments;
