import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { fetchImage } from '../../util/fetchImage';
import { sendGetRequest } from '../../util/requests';

const SERVER = 'http://localhost:8080/api';



const PostsComponent = ({ userId }) => {
    const size = 15;

    const [posts, setPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const containerRef = useRef(null);


    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await sendGetRequest(`${SERVER}/posts/getAll?id=${userId}&page=${currentPage}&size=${size}`);
                const newData = response.data;
                if (newData.length > 0) {
                    const updatedData = await Promise.all(newData.map(async (post) => {
                        if (!post.pictureURL) {
                            const imageURL = await fetchImage(post.pictureUrl);
                            post.pictureURL = imageURL;
                        }
                        return post;
                    }));
                    setPosts(prevData => [...prevData, ...updatedData]);
                }
            } catch (error) {
                console.error('Error fetching posts:', error);
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


    return (
        <main className="main" ref={containerRef}>
            {posts.length > 0 ? (
                <section className="posts" >{
                    posts.map(post => (
                        <a key={post.id} className="post" href={`/posts/post/${post.id}`}>
                            <div className="post-photo">
                                {post.pictureURL && (
                                    <img src={post.pictureURL} alt="Post Image" />
                                )}
                            </div>
                        </a>
                    ))}
                </section>
            ) : (
                <div className="no-posts-message-container">
                    <div className="no-posts-message">The user has not published any posts yet.</div>
                </div>
            )}
        </main>
    );
};

export default PostsComponent;


