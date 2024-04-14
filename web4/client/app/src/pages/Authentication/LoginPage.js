import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/authStyles.css';
import '../../styles/styles.css';


const SERVER = 'http://localhost:8080/api';

const Login = () => {
    const navigate = useNavigate();

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
            localStorage.setItem('token', token);

            navigate('/profile/user/me');
        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        }
    };

    return (
        <>

            <div className="auth">
                {/* <a className="logo" href="/login">
                    <img id="logo" src={logo} alt="Logo" />
                </a> */}
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
        </>
    );
};

export default Login;
