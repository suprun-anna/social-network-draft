import axios from 'axios';

export async function sendGetRequest(url) {
    try {
        const response = await axios.get(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        return response;
    } catch (error) {
        console.error('Error sending GET request:', error);
        throw new Error('Failed to send GET request');
    }
}


export async function sendPostRequest(url, data) {
    try {
        const response = await axios.post(url, data, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });
        return response;
    } catch (error) {
        console.error('Error sending POST request:', error);
        throw new Error('Failed to send POST request');
    }
}


export async function sendPutRequest(url, data) {
    try {
        const response = await axios.put(url, data, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });
        return response;
    } catch (error) {
        console.error('Error sending PUT request:', error);
        throw new Error('Failed to send PUT request');
    }
}

export async function sendDeleteRequest(url) {
    try {
        const response = await axios.delete(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        return response;
    } catch (error) {
        console.error('Error sending DELETE request:', error);
        throw new Error('Failed to send DELETE request');
    }
}
