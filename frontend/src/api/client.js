import axios from 'axios';

// Create a centralized axios instance
const apiClient = axios.create({
    baseURL: 'http://localhost:8081/api',
    headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': 'true', // Bypasses ngrok's "Visit Site" warning page
    },
});

export default apiClient;
