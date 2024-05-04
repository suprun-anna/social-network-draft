import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../styles/searchStyles.css';
import { sendGetRequest, sendPostRequest } from '../../util/requests';
// import UserItem from '../UserInfo/UserItem';
import { fetchImage } from '../../util/fetchImage';
import ListItem from '../../components/UserInfo/UserItem';

const SERVER = 'http://localhost:8080/api';

const AdminPage = () => {
    const size = 10;
    const [admin, setAdmin] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);
    const [banReasons, setBanReasons] = useState({});

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (email.trim() === '' || password.trim() === '') {
            alert('Please enter username and password.');
            return;
        }
        try {
            const response = await axios.post(`${SERVER}/auth/login`, {
                email,
                password
            });
            const data = response.data;
            const token = data.token;
            localStorage.setItem("token", token);
            const adm = (await sendGetRequest(`${SERVER}/auth/isAdmin`)).data;
            setAdmin(adm);

        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    };

    const fetchData = async () => {
        console.log(123);
        if (allLoaded || !searchQuery) return;
        try {
            const response = await sendGetRequest(`${SERVER}/search/user?username=${searchQuery}&page=${currentPage}&size=${size}`);
            const newData = response.data;
            if (newData.length > 0) {
                const updatedData = await Promise.all(newData.map(async (row) => {
                    if (!row.profilePictureURL) {
                        const imageURL = await fetchImage(row.profilePicture);
                        row.profilePictureURL = imageURL;
                    }
                    if (!row.unbanned) {
                        row.unbanned = await sendGetRequest(`${SERVER}/admin/userStatus?userId=${row.id}`);
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


    const handleBan = async (userId) => {
        try {
            await sendPostRequest(`${SERVER}/admin/ban?userId=${userId}&reason=${banReasons[userId]}`, {});
            setSearchResults(prevResults => prevResults.map(user => {
                if (user.id === userId) {
                    return { ...user, unbanned: false };
                }
                return user;
            }));
        } catch (error) {
            console.error('Error banning user:', error);
        }
    };

    const handleUnban = async (userId) => {
        try {
            await sendPostRequest(`${SERVER}/admin/unban?userId=${userId}`, {});
            setSearchResults(prevResults => prevResults.map(user => {
                if (user.id === userId) {
                    return { ...user, unbanned: true };
                }
                return user;
            }));
        } catch (error) {
            console.error('Error unbanning user:', error);
        }
    };

    return (
        <>
            {admin ?
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <div className="search-page">
                        <h2>Ban Users</h2>
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
                                    <div style={{ width: "100%" }}>
                                        <ListItem user={user} />
                                        {user.unbanned ?
                                            <>
                                            <div />
                                                <input type="text" value={banReasons[user.id] || ''}
                                                    onChange={(e) => setBanReasons(prev => ({ ...prev, [user.id]: e.target.value }))} placeholder="Ban Reason" />
                                                <button className='redir-button' onClick={() => handleBan(user.id)}>Ban</button>
                                            </>
                                            :
                                            <>
                                                <div />
                                                <button className='redir-button' onClick={() => handleUnban(user.id)}>Unban</button>
                                            </>
                                        }
                                    </div>
                                </ul>
                            ))}
                        </div>
                    </div>
                </div>
                :
                <div className="auth">
                    <div className="form">
                        <h2>Login</h2>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label htmlFor="email">Email:</label>
                                <input
                                    type="text"
                                    id="email"
                                    name="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="password">Password:</label>
                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <button type="submit">Login</button>
                            </div>
                        </form>
                    </div>
                </div>
            }
        </>
    );
};

export default AdminPage;