import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

export const createOrder = async (orderData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/orders`, orderData);
    return response.data;
  } catch (error) {
    throw error;
  }
};
