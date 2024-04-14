import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/authStyles.css';
import '../../styles/styles.css';

const SERVER = 'http://localhost:8080/api';

const RegistrationPage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: '',
        password: '',
        repeatPassword: '',
        username: '',
        age: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleRegistration = async () => {
        try {
            await axios.post(`${SERVER}/auth/registration`, formData);
        } catch (error) {
            console.error('There was a problem with your registration:', error);
            throw error;
        }
    };

    const handleLogin = async () => {
        try {
            const response = await axios.post(`${SERVER}/auth/login`, {
                email: formData.email,
                password: formData.password
            });
            const data = response.data;
            localStorage.setItem('token', data.token);

            navigate('/profile/user/me');
        } catch (error) {
            console.error('There was a problem with your login:', error);
            throw error;
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            await handleRegistration();
            await handleLogin();
        } catch (error) {
            console.error('There was a problem with your registration and login:', error);
        }
    };

    return (
        <div className="auth">
            <div className="form">
                <h1>Registration</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            minLength="8"
                            maxLength="20"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="repeatPassword">Repeat password:</label>
                        <input
                            type="password"
                            id="repeatPassword"
                            name="repeatPassword"
                            value={formData.repeatPassword}
                            onChange={handleChange}
                            minLength="8"
                            maxLength="20"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="username">Username:</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="age">Age:</label>
                        <input
                            type="number"
                            id="age"
                            name="age"
                            value={formData.age}
                            onChange={handleChange}
                            min="1"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <button type="submit">Register</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegistrationPage;
