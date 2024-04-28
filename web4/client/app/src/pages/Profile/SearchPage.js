import React, { useState } from 'react';
import UserSearch from '../../components/Search/UserSearch';
import Menu from '../../components/Menu/Menu';
import '../../styles/searchStyles.css';
import ClubSearch from '../../components/Search/ClubSearch';


const SearchPage = () => {
    return (
        <div className='container'>
            <Menu />
            <div className='search-container'>
                <UserSearch />
                <ClubSearch />
            </div>
            {/* <GroupSearch/> */}
        </div>
    );
};

export default SearchPage;
