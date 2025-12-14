import apiClient from './client';

export const login = async (credentials) => {
  try {
    const response = await apiClient.post('/auth/login', credentials);
    const user = response.data;

    return {
      ...user,
      id: user.employeeID, // Ensure standard ID field mechanism matches frontend expectation
      token: "mock-jwt-token" // Continue using mock token until backend issues real JWT
    };
  } catch (error) {
    if (error.response && error.response.status === 401) {
      throw new Error("Invalid email or password");
    }
    throw new Error(error.response?.data?.message || "Login failed");
  }
};
