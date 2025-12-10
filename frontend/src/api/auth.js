import apiClient from './client';

export const login = async (credentials) => {
  try {
    // 1. Fetch all employees (In a real app, this should be a POST to /auth/login)
    const response = await apiClient.get('/employees');
    const employees = response.data;

    // 2. Find employee by email
    const user = employees.find(e => e.email.toLowerCase() === credentials.email.toLowerCase());

    if (user) {
      // Note: In a real app, we would verify the password on the server.
      // For this prototype, we'll accept the login if the email exists.
      return {
        ...user,
        id: user.employeeID, // Ensure standard ID field
        token: "mock-jwt-token"
      };
    } else {
      throw new Error("User not found");
    }
  } catch (error) {
    throw new Error(error.response?.data?.message || "Login failed");
  }
};
