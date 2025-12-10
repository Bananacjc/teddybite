import apiClient from './client';

export const getAllPayments = async () => {
    const response = await apiClient.get('/payments');
    return response.data;
};

export const getPaymentById = async (id) => {
    const response = await apiClient.get(`/payments/${id}`);
    return response.data;
};

export const createPayment = async (paymentData) => {
    const response = await apiClient.post('/payments', paymentData);
    return response.data;
};

export const updatePayment = async (id, paymentData) => {
    const response = await apiClient.put(`/payments/${id}`, paymentData);
    return response.data;
};

export const deletePayment = async (id) => {
    await apiClient.delete(`/payments/${id}`);
};

export const deletePayments = async (ids) => {
    await Promise.all(ids.map(id => apiClient.delete(`/payments/${id}`)));
};
