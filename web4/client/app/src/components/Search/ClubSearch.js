import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../styles/searchStyles.css';
import { sendGetRequest } from '../../util/requests';
import UserItem from '../UserInfo/UserItem';
import { fetchImage } from '../../util/fetchImage';

const SERVER = 'http://localhost:8080/api';

const ClubSearch = () => {
    const size = 10;
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [allLoaded, setAllLoaded] = useState(false);
    const containerRef = useRef(null);

    const fetchData = async () => {
        if (allLoaded || !searchQuery) return;
        try {
            const response = await sendGetRequest(`${SERVER}/search/club?name=${searchQuery}&page=${currentPage}&size=${size}`);
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
            <h2>Search Clubs</h2>
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
                {searchResults.map(club => (
                    <ul key={club.id} className='removable'>
                        <UserItem user={club} club = {true} />
                    </ul>
                ))}
            </div>
        </div>
    );
};

export default ClubSearch;