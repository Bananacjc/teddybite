import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

export const login = async (credentials) => {
  // This is a placeholder. 
  // If backend has an auth endpoint, use:
  // const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
  // return response.data;

  // For now, mock the response
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (credentials.email && credentials.password) {
        resolve({
          id: "EMP001",
          name: "John Doe",
          email: credentials.email,
          token: "mock-jwt-token"
        });
      } else {
        reject(new Error("Invalid credentials"));
      }
    }, 1000);
  });
};
