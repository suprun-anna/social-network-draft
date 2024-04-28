import React from 'react';
import LinkifiedUsername from '../util/LinkifiedUsername';

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

const Message = ({ senderName, messageText, timestamp }) => {
    return (
        <div className="not-my-message-container">
            <div className="message">
                <div className="message-text"><LinkifiedUsername text={messageText}></LinkifiedUsername></div>
                <div className="message-text timestamp">{timeSince(timestamp)}</div>
            </div>
        </div>
    );
};

const MyMessage = ({ messageText, timestamp }) => {
    return (
        <div className="my-message-container">
            <div className="my-message">
                <div className="my-message-text"><LinkifiedUsername text={messageText} color={'black'}></LinkifiedUsername></div>
                <div className="my-message-text timestamp">{timeSince(timestamp)}</div>
            </div>
        </div>
    );
};

export { Message, MyMessage };
