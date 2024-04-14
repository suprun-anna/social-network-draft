import { useState, useEffect, useRef } from 'react';
import { sendGetRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';
import UserItem from '../UserInfo/UserItem';
import { forceUnfollow, unfollow } from '../../util/follows';

const SERVER = 'http://localhost:8080/api';

const Modal = ({ text, onClose, request, userIsMe, successCallback }) => {
    const size = 10;

    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);

    useEffect(() => {
        const fetchData = async () => {
            if (allLoaded) return;
            try {
                const response = await sendGetRequest(`${request}&page=${currentPage}&size=${size}`);
                const newData = response.data;
                if (newData.length > 0) {
                    const updatedData = await Promise.all(newData.map(async (row) => {
                        if (!row.profilePicture && !row.username && row.userId) {
                            row = (await sendGetRequest(`${SERVER}/user/id?userId=${row.userId}`)).data;
                        }
                        if (!row.profilePictureURL) {
                            const imageURL = await fetchImage(row.profilePicture);
                            row.profilePictureURL = imageURL;
                        }
                        return row;
                    }));
                    setData(prevData => [...prevData, ...updatedData]);
                } else setAllLoaded(true);
            } catch (error) {
                console.error(`Error fetching ${text}`, error);
            }
        };

        fetchData();
    }, [currentPage]);

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

    const removeFunction = async (userId) => {
        text === 'Followers' ? await forceUnfollow(userId, successCallback) : await unfollow(userId, successCallback);
        setData(prevData => prevData.filter(user => user.id !== userId));
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                <h3>{text}</h3>
                <ul ref={containerRef}>
                    {data.map((user) => (
                        <div key={user.id} className='removable'>
                            <UserItem user={user} />
                            {userIsMe && <button className='remove-button' onClick={() => removeFunction(user.id)}>Remove</button>}
                        </div>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Modal;


































// import { useState, useEffect } from 'react';
// import { sendGetRequest } from '../../util/requests';
// import { fetchImage } from '../../util/fetchImage';
// import FollowerItem from '../UserInfo/FollowerItem'; 

// const Modal = ({ text, onClose, contentType, request }) => {
//     const size = 5;

//     const [data, setData] = useState([]);
//     const [currentPage, setCurrentPage] = useState(0);

//     useEffect(() => {
//         const fetchData = async () => {
//             try {
//                 const response = await sendGetRequest(`${request}&page=${currentPage}&size=${size}`);
//                 const newData = response.data;
//                 if (newData.length > 0) {
//                     const updatedData = await Promise.all(newData.map(async (user) => {
//                         if (!user.profilePictureURL) {
//                             await console.log('qwe');
//                             const imageURL = await fetchImage(user.profilePicture);
//                             user.profilePictureURL = imageURL;
//                         }
//                         return user;
//                     }));
//                     //setData(prevData => [...prevData, ...updatedData]);
//                     setData(updatedData);
//                 }
//             } catch (error) {
//                 console.error('Error fetching followers:', error);
//             }
//         };

//         fetchData();
//     }, [currentPage]); 

// useEffect(() => {
//     const fetchData = async () => {
//         try {
//             const response = await sendGetRequest(`${request}&page=${currentPage}&size=${size}`);
//             const newData = response.data;
//             if (newData.length > 0) {
//                 setData(prevData => [...prevData, ...newData]);
//             }
//         } catch (error) {
//             console.error('Error fetching followers:', error);
//         }
//     };

//     fetchData();

// }, [currentPage]);

//     return (
//         <div className="modal">
//             <div className="modal-content">
//                 <span className="close" onClick={onClose}>&times;</span>
//                 <h3>{text}</h3>
//                 <ul>
//                     {data.map((user) => (
//                         <FollowerItem key={user.id} follower={user} />
//                     ))}
//                 </ul>
//                 <button onClick={() => setCurrentPage(prevPage => prevPage + 1)}>Load More</button>
//             </div>
//         </div>
//     );
// };

// export default Modal;