import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../styles/searchStyles.css';
import { sendGetRequest } from '../../util/requests';
import UserItem from '../../components/UserInfo/UserItem';
import { fetchImage } from '../../util/fetchImage';

const SERVER = 'http://localhost:8080/api';

const MyClubSearch = ({ request }) => {
    const size = 10;
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);
    const [noClubs, setNoClubs] = useState(false);

    const fetchData = async () => {
        // if (allLoaded || !searchQuery) return;
        if (allLoaded) return;
        try {
            const response = await sendGetRequest(`${SERVER}/club/${request ? 'my' : 'myOwn'}?name=${searchQuery}&page=${currentPage}&size=${size}`);
            const newData = response.data;
            if (newData.length > 0) {
                const updatedData = await Promise.all(newData.map(async (row) => {
                    if (!row.profilePictureURL) {
                        const imageURL = await fetchImage(row.profilePicture);
                        row.profilePictureURL = imageURL;
                    }
                    return row;
                }));
                setSearchResults(prevData => [...prevData, ...updatedData]);
            } else {
                setNoClubs(searchResults.length > 0 ? false : true);
                setAllLoaded(true);
            }
        } catch (error) {
            console.error(`Error fetching users`, error);
        }
    };

    useEffect(() => {
        if (searchQuery === '') {
            handleSearch();
        }
    }, [searchQuery]);

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
            {request ?
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <h2>My Clubs
                    </h2>
                </div> :
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <h2>Created Clubs
                    </h2>
                    <a className='redir-button' href={`/clubs/create`}>Create new</a>
                </div>
            }
            <div className='search-bar'>
                <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => handleSearchQueryChange(e.target.value)}
                    placeholder="Enter club name..."
                />
                <button className='button' onClick={handleSearch}>Search</button>
            </div>
            <div className="search-results" ref={containerRef}>
                {noClubs ? <div className='no-posts-message-container'>You have not joined any clubs.
                    <a href='/search'>Search new clubs</a>
                </div> :
                    searchResults.map(club => (
                        <ul key={club.id} className='removable'>
                            <UserItem user={club} club={true} />
                            {request === false && <a className='redir-button' href={`/clubs/edit/${club.id}`}>Edit</a>}
                        </ul>
                    ))}
            </div>
        </div>
    );
};

export default MyClubSearch;