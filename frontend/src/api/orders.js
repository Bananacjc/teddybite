import apiClient from './client';

export const createOrder = async (orderData) => {
  try {
    const response = await apiClient.post('/orders', orderData);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getAllOrders = async () => {
  try {
    const response = await apiClient.get('/orders');
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const updateOrder = async (id, orderData) => {
  const response = await apiClient.put(`/orders/${id}`, orderData);
  return response.data;
};

export const deleteOrder = async (id) => {
  await apiClient.delete(`/orders/${id}`);
};

export const deleteOrders = async (ids) => {
  await Promise.all(ids.map(id => apiClient.delete(`/orders/${id}`)));
};
