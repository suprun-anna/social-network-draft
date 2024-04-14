import { sendPutRequest } from './requests';

const SERVER = 'http://localhost:8080/api';

export async function follow(userId, onSuccessCallback) {
    const response = await sendPutRequest(`${SERVER}/follow/follow?userId=${userId}`, {});
    if (response.status === 200) {
        if (typeof onSuccessCallback === 'function') {
            onSuccessCallback(userId);
        }
    }
}

export async function unfollow(userId, onSuccessCallback) {
    const response = await sendPutRequest(`${SERVER}/follow/unfollow?userId=${userId}`, {});
    if (response.status === 200) {
        if (typeof onSuccessCallback === 'function') {
            onSuccessCallback(userId);
        }
    }
}

export async function forceUnfollow(userId, onSuccessCallback) {
    const response = await sendPutRequest(`${SERVER}/follow/forceUnfollow?userId=${userId}`, {});
    if (response.status === 200) {
        if (typeof onSuccessCallback === 'function') {
            onSuccessCallback(userId);
        }
    }
}
























    // await axios.put(`${SERVER}/follow/follow?userId=${userId}`, {}, {
    //     headers: {
    //         'Authorization': `Bearer ${localStorage.getItem('token')}`
    //     }
    // });
    // if (response.status !== 200) {
    //     throw new Error('Network response was not ok');
    // } else {
    //     setUser(prevUser => ({
    //         ...prevUser,
    //         followerCount: prevUser.followerCount + 1
    //     }));
    //     await fetchFollowsData(userId);
    //     await setStatus(await getConnections(`${SERVER}/follow/checkConnection?userId=${userId}`))
    //     console.log('follow');
    // }