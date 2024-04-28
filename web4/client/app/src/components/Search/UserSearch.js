import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../styles/searchStyles.css';
import { sendGetRequest } from '../../util/requests';
import UserItem from '../UserInfo/UserItem';
import { fetchImage } from '../../util/fetchImage';

const SERVER = 'http://localhost:8080/api';

const UserSearch = () => {
    const size = 10;
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);

    const fetchData = async () => {
        if (allLoaded || !searchQuery) return;
        try {
            const response = await sendGetRequest(`${SERVER}/search/user?username=${searchQuery}&page=${currentPage}&size=${size}`);
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
                setSearchResults(prevData => [...prevData, ...updatedData]);
            } else setAllLoaded(true);
        } catch (error) {
            console.error(`Error fetching users`, error);
        }
    };

    useEffect(() => {
        if (searchQuery && currentPage > 0) {
            fetchData();
        }
    }, [currentPage]);

    const handleSearchQueryChange = async (query) => {
        setSearchQuery(query);
        setCurrentPage(0); 
        setAllLoaded(false);
    };

    const handleSearch = () => {
        setCurrentPage(0); 
        setSearchResults([]); 
        fetchData();
        setAllLoaded(false);
    };

    const handleScroll = () => {
        if (containerRef.current) {
            const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
            if (scrollTop + clientHeight >= scrollHeight - 5) {
                setCurrentPage(prevPage => prevPage + 1);
            }
        }
    };

    useEffect(() => {
        if (containerRef.current) {
            containerRef.current.addEventListener('scroll', handleScroll);
        }

        return () => {
            if (containerRef.current) {
                containerRef.current.removeEventListener('scroll', handleScroll);
            }
        };
    }, []);

    return (
        <div className="search-page">
            <h2>Search Users</h2>
            <div className='search-bar'>
                <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => handleSearchQueryChange(e.target.value)}
                    placeholder="Enter username..."
                />
                <button className='button' onClick={handleSearch}>Search</button>
            </div>
            <div className="search-results" ref={containerRef}>
                {searchResults.map(user => (
                    <ul key={user.id} className='removable'>
                        <UserItem user={user} />
                    </ul>
                ))}
            </div>
        </div>
    );
};

export default UserSearch;
















// import { useState, useEffect, useRef } from 'react';
// import axios from 'axios';
// import '../../styles/searchStyles.css';
// import { sendGetRequest } from '../../util/requests';
// import UserItem from '../UserInfo/UserItem';
// import { fetchImage } from '../../util/fetchImage';

// const SERVER = 'http://localhost:8080/api';


// const UserSearch = () => {
//     const [searchQuery, setSearchQuery] = useState('');
//     const [searchResults, setSearchResults] = useState([]);
//     const size = 10;

//     const [currentPage, setCurrentPage] = useState(0);
//     const [allLoaded, setAllLoaded] = useState(false);
//     const containerRef = useRef(null);

//     const handleSearch = async () => {
//         try {
//             console.log(99999);
//             if (searchQuery) {
//                 console.log(currentPage);
//                 setSearchResults([]);
//                 setCurrentPage(0);
//                 if (currentPage === 0) fetchData();
//                 //setCurrentPage(1);
//             }
//         } catch (error) {
//             console.error('Error searching for users:', error);
//         }
//     };

//     const handleSearchQueryChange = async (query) => {
//         setSearchQuery(query);
//         console.log(query);
//     }

//     useEffect(() => {
//         const handleScroll = () => {
//             if (containerRef.current) {
//                 const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
//                 if (scrollTop + clientHeight >= scrollHeight - 5) {
//                     setCurrentPage(prevPage => prevPage + 1);
//                     fetchData();
//                     console.log(currentPage);
//                 }
//             }
//         };

//         if (containerRef.current) {
//             containerRef.current.addEventListener('scroll', handleScroll);
//         }

//         return () => {
//             if (containerRef.current) {
//                 containerRef.current.removeEventListener('scroll', handleScroll);
//             }
//         };
//     }, [currentPage]);

//     const fetchData = async () => {
//         if (allLoaded) return;
//         try {
//             if (searchQuery) {
//                 console.log(123);
//                 const response = await sendGetRequest(`${SERVER}/search/user?username=${searchQuery}&page=${currentPage}&size=${size}`);
//                 const newData = response.data;
//                 if (newData.length > 0) {
//                     console.log(1234);
//                     const updatedData = await Promise.all(newData.map(async (row) => {
//                         if (!row.profilePicture && !row.username && row.userId) {
//                             row = (await sendGetRequest(`${SERVER}/user/id?userId=${row.userId}`)).data;
//                         }
//                         if (!row.profilePictureURL) {
//                             const imageURL = await fetchImage(row.profilePicture);
//                             row.profilePictureURL = imageURL;
//                         }
//                         return row;
//                     }));
//                     setSearchResults(prevData => [...prevData, ...updatedData]);
//                 } else setAllLoaded(true);
//             }
//         } catch (error) {
//             console.error(`Error fetching users`, error);
//         }
//     };

//     return (
//         <div className="search-page">
//             <h2>Search Users</h2>
//             <div className='search-bar'>
//                 <input
//                     type="text"
//                     value={searchQuery}
//                     onChange={(e) => handleSearchQueryChange(e.target.value)}
//                     placeholder="Enter username..."
//                 />
//                 <button className='button' onClick={() => handleSearch()}>Search</button>
//             </div>
//             <div className="search-results" ref={containerRef}>
//                 {searchResults.map(user => (
//                     <ul key={user.id} className='removable'>
//                         <UserItem user={user} />
//                     </ul>
//                 ))}
//             </div>
//         </div>
//     );
// };


// export default UserSearch;














// import { useState, useEffect, useRef } from 'react';
// import axios from 'axios';
// import '../../styles/searchStyles.css';
// import Menu from '../Menu/Menu';
// import { sendGetRequest } from '../../util/requests';
// import UserItem from '../UserInfo/UserItem';


// const SERVER = 'http://localhost:8080/api';

// const UserSearch = () => {
//     const [searchQuery, setSearchQuery] = useState('');
//     const [searchResults, setSearchResults] = useState([]);
//     const size = 10;

//     const [currentPage, setCurrentPage] = useState(0);
//     const [allLoaded, setAllLoaded] = useState(false);
//     const containerRef = useRef(null);

//     useEffect(() => {
//         const fetchData = async () => {
//             if (allLoaded) return;
//             try {
//                 const response = await sendGetRequest(`${SERVER}/search/user?username=${searchQuery}&page=${currentPage}&size=${size}`);
//                 const newData = response.data;
//                 if (newData.length > 0) {
//                     const updatedData = await Promise.all(newData.map(async (row) => {
//                         if (!row.profilePicture && !row.username && row.userId) {
//                             row = (await sendGetRequest(`${SERVER}/user/id?userId=${row.userId}`)).data;
//                         }
//                         if (!row.profilePictureURL) {
//                             const imageURL = await fetchImage(row.profilePicture);
//                             row.profilePictureURL = imageURL;
//                         }
//                         return row;
//                     }));
//                     setSearchResults(prevData => [...prevData, ...updatedData]);
//                 } else setAllLoaded(true);
//             } catch (error) {
//                 console.error(`Error fetching ${text}`, error);
//             }
//         };

//         fetchData();
//     }, [currentPage]);

//     useEffect(() => {
//         const handleScroll = () => {
//             if (containerRef.current) {
//                 const { scrollTop, scrollHeight, clientHeight } = containerRef.current;
//                 if (scrollTop + clientHeight >= scrollHeight - 20) {
//                     setCurrentPage(prevPage => prevPage + 1);
//                 }
//             }
//         };

//         if (containerRef.current) {
//             containerRef.current.addEventListener('scroll', handleScroll);
//         }

//         return () => {
//             if (containerRef.current) {
//                 containerRef.current.removeEventListener('scroll', handleScroll);
//             }
//         };
//     }, []);


//     // const handleSearch = async () => {
//     //     try {
//     //         const response = await axios.get(`${SERVER}/search/user?username=${searchQuery}`);
//     //         setSearchResults(response.data);
//     //     } catch (error) {
//     //         console.error('Error searching for users:', error);
//     //     }
//     // };





//     return (

//         <div className="search-page">
//             <h2>Search Users</h2>
//             <div className='search-bar'>
//                 <input
//                     type="text"
//                     value={searchQuery}
//                     onChange={(e) => setSearchQuery(e.target.value)}
//                     placeholder="Enter username..."
//                 />
//                 <button className='button' onClick={handleSearch}>Search</button>
//             </div>
//             <div className="search-results">
//                 {searchResults.map(user => (
//                     <ul key={user.id} className='removable'>
//                         <UserItem user={user} />
//                     </ul>
//                 ))}
//             </div>
//         </div>

//     );
// };


// export default UserSearch;