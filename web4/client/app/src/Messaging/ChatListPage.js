import React, { useState, useEffect, useRef } from 'react';
import { sendGetRequest } from '../util/requests';
import '../styles/chatStyles.css'
import { fetchImage } from '../util/fetchImage';
import Menu from '../components/Menu/Menu';

const SERVER = 'http://localhost:8080/api';

function ChatListPage() {
    const [dialogIds, setDialogIds] = useState([]);
    const [me, setMe] = useState(null);
    const [dialogs, setDialogs] = useState([]);

    useEffect(() => {
        const fetchUserData = async () => {
            const user = await getUserInfo(`${SERVER}/user/me`);
            setMe(user);
        };

        fetchUserData();
    }, []);

    async function getUserInfo(request, error = 'Error fetching user data: ') {
        return (await sendGetRequest(request, error)).data;
    }

    useEffect(() => {
        const fetchDialogData = async () => {
            if (me) {
                const dialogs = await sendGetRequest(`${SERVER}/messages/allDialogs?userId=${me.id}`);
                console.log(dialogs.data);
                setDialogIds(dialogs.data);
            }
        };

        fetchDialogData();
    }, [me]);

    useEffect(() => {
        const fetchDialogData = async () => {
            if (me && dialogIds) {
                const dialogs = [];
                if (dialogIds.length > 0) {
                    for (const dialogId of dialogIds) {
                        const lastMessage = await sendGetRequest(`${SERVER}/messages/last-message?dialogId=${dialogId}`);
                        console.log(123, dialogId, lastMessage.data);
                        if (lastMessage.status === 200) {
                            const lastMessageData = lastMessage.data;
                            if (lastMessageData !== '') {
                                const dialog = {
                                    id: dialogId,
                                    username: lastMessageData.senderId === me.id ? lastMessageData.receiverName : lastMessageData.senderName,
                                    avatar: await fetchImage(lastMessageData.senderId === me.id ? lastMessageData.receiverAvatar : lastMessageData.senderAvatar),
                                    messageText: lastMessageData.messageText,
                                    sender: lastMessageData.senderId === me.id ? 'me:' : `${lastMessageData.senderName}:`,
                                    sentAt: lastMessageData.sentAt
                                };
                                dialogs.push(dialog);
                            }
                        }
                    }
                }
                setDialogs(prevDialogs => [...prevDialogs, ...dialogs]);
            }
        };

        fetchDialogData();
    }, [me, dialogIds]);

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
    return (
        <div className='container'>
            <Menu />
            <div className="chat-container">
                <h1>Dialogs</h1>
                <div className="dialogs-list">
                    {dialogs.length === 0 ? (
                        <div className="no-messages">No messages yet</div>
                    ) : (
                        dialogs.map(dialog => (
                            <a href={`/chat/${dialog.username}`} key={dialog.id} className="dialog-item">
                                <img src={dialog.avatar} alt="Avatar" className="avatar" />
                                <div className="dialog-info">
                                    <h2 className="dialog-name">{dialog.username}</h2>
                                    <div className="last-message">{dialog.sender + ' ' + dialog.messageText}
                                        <div className='timestamp right'>{timeSince(dialog.sentAt)}</div>
                                    </div>
                                </div>
                            </a>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}

export default ChatListPage;