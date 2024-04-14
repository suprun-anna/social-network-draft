import axios from 'axios';

export async function fetchImage(imageName) {
    try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:8080/api/images/${encodeURIComponent(imageName)}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            },
            responseType: 'blob'
        });
        if (response.status !== 200) {
            throw new Error('Failed to fetch image');
        }
        const imageURL = URL.createObjectURL(response.data);
        return imageURL;
    } catch (error) {
        console.error('Error fetching image:', error);
        throw error;
    }
};