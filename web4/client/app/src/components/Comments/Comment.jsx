import React, { useState, useEffect, useRef } from 'react';
import { fetchImage } from '../../util/fetchImage';
import { sendGetRequest, sendPostRequest, sendDeleteRequest, sendPutRequest } from '../../util/requests';
import UserItem from '../UserInfo/UserItem';
import LinkifiedUsername from '../../util/LinkifiedUsername';




const Comment = ({ comment, me, onDeleteComment, onEditComment, post }) => {
    const isMyComment = comment.userId === me.id;


    const [menuOpen, setMenuOpen] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [editedText, setEditedText] = useState(comment.text);


    const handleMenuToggle = (commentId) => {
        if (menuOpen === commentId) {
            setMenuOpen(null);
        } else {
            setMenuOpen(commentId);
        }
    };

    const handleDocumentClick = (event) => {
        if (!event.target.classList.contains('action-button')) {
            setMenuOpen(null);
        }
    };

    useEffect(() => {
        document.addEventListener('click', handleDocumentClick);
        return () => {
            document.removeEventListener('click', handleDocumentClick);
        };
    }, []);







    const handleEditButtonClick = () => {
        setEditMode(true);
    };

    const handleCancelEdit = () => {
        setEditMode(false);
        setEditedText(comment.text);
    };

    const handleSaveEdit = async () => {
        try {
            await onEditComment(comment.id, editedText);
            setEditMode(false);
        } catch (error) {
            console.error('Error editing comment:', error);
        }
    };






    function timeSince(timestamp) {
        const currentDate = new Date();
        const commentDate = new Date(timestamp);
        const difference = currentDate.getTime() - commentDate.getTime();
        const seconds = Math.floor(difference / 1000);
        let interval = Math.floor(seconds / 31536000);

        if (interval > 1) {
            return interval + " years ago";
        }
        interval = Math.floor(seconds / 2592000);
        if (interval > 1) {
            return interval + " months ago";
        }
        interval = Math.floor(seconds / 86400);
        if (interval > 1) {
            return interval + " days ago";
        }
        interval = Math.floor(seconds / 3600);
        if (interval > 1) {
            return interval + " hours ago";
        }
        interval = Math.floor(seconds / 60);
        if (interval > 1) {
            return interval + " minutes ago";
        }
        return Math.floor(seconds) + " seconds ago";
    }
    //&nbsp;

    return (
        <div key={comment.id} className='comment'>
            <UserItem user={comment} />
            :&nbsp;&nbsp;&nbsp;&nbsp;
            {editMode ? (
                <div className='new-comment-section'>
                    <textarea className='new-comment-text' value={editedText} onChange={(e) => setEditedText(e.target.value)} />
                    <button className="send-button" title='Send' onClick={handleSaveEdit}>➤</button>
                    <button className="send-button" title='Cancel' onClick={handleCancelEdit}>✖</button>
                </div>
            ) : (
                <div className='comment-text'>
                    <LinkifiedUsername text={comment.text} />
                </div>
            )}
            <div className='time'>{timeSince(comment.leftAt)}</div>
            {(post.userId === me.id || isMyComment) &&
                <div className='comment-actions'>
                    <button className='action-button' onClick={() => handleMenuToggle(comment.id)}>⋮</button>
                    {menuOpen === comment.id && (
                        <div className="comment-menu">
                            {isMyComment && !editMode && <button onClick={handleEditButtonClick}>Edit</button>}
                            {editMode && !editMode && (
                                <>
                                    <button onClick={handleSaveEdit}>Save</button>
                                    <button onClick={handleCancelEdit}>Cancel</button>
                                </>
                            )}
                            {(post.userId === me.id || isMyComment )&& <button onClick={() => onDeleteComment(comment.id)}>Delete</button>}
                        </div>
                    )}
                </div>
            }
        </div>
    );
};


export default Comment;
