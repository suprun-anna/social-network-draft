import React, { useEffect, useState, useRef } from 'react';
import ListItem from './UserItem';
import { follow, unfollow } from '../../util/follows';
import { sendGetRequest } from '../../util/requests';
import { fetchImage } from '../../util/fetchImage';

const SERVER = 'http://localhost:8080/api';

const Recommendations = ({
    user,
    handleFollowSuccess,
    handleUnfollowSuccess
}
) => {
    const [recommendations, setRecommendations] = useState([]);

    const removeRecommendation = (id) => {
        const updatedRecommendations = recommendations.filter(user => user.id !== id);
        setRecommendations(updatedRecommendations);
    };

    const updateIfFollow = (id) => {
        handleFollowSuccess();
        const updatedRecommendations = recommendations.map(rec => {
            if (id && rec.id === id) {
                return { ...rec, followed: true };
            }
            return rec;
        });
        setRecommendations(updatedRecommendations);
    }

    const updateIfUnfollow = (id) => {
        handleUnfollowSuccess();
        const updatedRecommendations = recommendations.map(rec => {
            if (id && rec.id === id) {
                return { ...rec, followed: false };
            }
            return rec;
        });
        setRecommendations(updatedRecommendations);
    }

    useEffect(() => {
        const fetchRecommendations = async () => {
            const recommendations = (await sendGetRequest(`${SERVER}/follow/recommend`)).data;
            if (recommendations.length > 0) {
                const updatedData = await Promise.all(recommendations.map(async (row) => {
                    row.followed = false;
                    if (!row.profilePictureURL) {
                        const imageURL = await fetchImage(row.profilePicture);
                        row.profilePictureURL = imageURL;
                    }
                    return row;
                }));
                setRecommendations(updatedData);
            }
        };
        fetchRecommendations();
    }, [user]);

    return (
        user && recommendations &&
        <ul className='recomms'>
            <h4>Recommendations</h4>
            {recommendations.map((rec) => (
                <div key={rec.id} className='recom'>
                    <ListItem user={rec} />
                    <div className='act'>
                        {!rec.followed && <button className='smol-button'
                            onClick={() => follow(rec.id, updateIfFollow(rec.id))}>Follow</button>}
                        {rec.followed && <button className='smol-button'
                            onClick={() => unfollow(rec.id, updateIfUnfollow(rec.id))}>Unfollow</button>}
                        <button className='smol-button' onClick={() => removeRecommendation(rec.id)}>x</button>
                    </div>
                </div>
            ))}
        </ul>
    );
};

export default Recommendations;


























// import React, { useEffect, useState, useRef } from 'react';
// import UserItem from './UserItem';
// import { follow, unfollow } from '../../util/follows';
// import { sendGetRequest } from '../../util/requests';
// import { fetchImage } from '../../util/fetchImage';

// const SERVER = 'http://localhost:8080/api';

// const Recommendations = ({
//     user,
//     handleFollowSuccess,
//     handleUnfollowSuccess
// }
// ) => {
//     const [recommendations, setRecommendations] = useState([]);

//     const removeRecommendation = (id) => {
//         const updatedRecommendations = recommendations.filter(user => user.id !== id);
//         setRecommendations(updatedRecommendations);
//     };

//     const updateIfFollow = (id) => {
//         handleFollowSuccess();
//         console.log(recommendations);
//         const updatedRecommendations = recommendations.map(rec => {
//             if (id && rec.id === id) {
//                 return { ...rec, followed: true };
//             }
//             return rec;
//         });
//         console.log(updatedRecommendations);
//         setRecommendations(updatedRecommendations);
//     }

//     const updateIfUnfollow = (id) => {
//         handleUnfollowSuccess();
//         const updatedRecommendations = recommendations.map(rec => {
//             if (id && rec.id === id) {
//                 console.log(2);
//                 return { ...rec, followed: false };
//             }
//             return rec;
//         });
//         setRecommendations(updatedRecommendations);
//     }

//     useEffect(() => {
//         const fetchRecommendations = async () => {
//             const recommendations = (await sendGetRequest(`${SERVER}/follow/recommend`)).data;
//             if (recommendations.length > 0) {
//                 const updatedData = await Promise.all(recommendations.map(async (row) => {
//                     row.followed = false;
//                     if (!row.profilePictureURL) {
//                         const imageURL = await fetchImage(row.profilePicture);
//                         row.profilePictureURL = imageURL;
//                     }
//                     return row;
//                 }));
//                 setRecommendations(prevData => [...prevData, ...updatedData]);
//             }
//         };
//         fetchRecommendations();
//     }, [user]);

//     return (
//         user && recommendations &&
//         <ul className='recomms'>
//             <h4>Recommendations</h4>
//             {recommendations.map((rec) => (
//                 <div key={rec.id} className='recom'>
//                     <UserItem user={rec} />
//                     <div className='act'>
//                         {!rec.followed && <button className='smol-button'
//                             onClick={() => follow(rec.id, updateIfFollow(rec.id))}>Follow</button>}
//                         {rec.followed && <button className='smol-button'
//                             onClick={() => unfollow(rec.id, updateIfUnfollow(rec.id))}>Unfollow</button>}
//                         <button className='smol-button' onClick={() => removeRecommendation(rec.id)}>x</button>
//                     </div>
//                 </div>
//             ))}
//         </ul>
//     );
// };

// export default Recommendations;