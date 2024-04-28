import React, { useState } from 'react';
import Menu from '../../components/Menu/Menu';
import '../../styles/searchStyles.css';
import MyClubSearch from './MyClubSearch';


const MyClubsPage = () => {
    return (
        <div className='container'>
            <Menu />
            <div className='search-container'>
                {/* <UserSearch /> */}
                <MyClubSearch request={true}/>
                <MyClubSearch request={false}/>
            </div>
        </div>
    );
};

export default MyClubsPage;
