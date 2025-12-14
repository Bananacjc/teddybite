import axios from 'axios';

export const baseURL = 'http://localhost:8081';

// Create a centralized axios instance
const apiClient = axios.create({
    baseURL: `${baseURL}/api`,
    headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': 'true',
    },
});

export default apiClient;
