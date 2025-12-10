import apiClient from './client';

export const getAllItems = async () => {
    const response = await apiClient.get('/items');
    return response.data; // Assuming backend returns List<Item> directly
};

export const getItemById = async (id) => {
    const response = await apiClient.get(`/items/${id}`);
    return response.data;
};

export const createItem = async (itemData) => {
    const formData = new FormData();
    const { imageFile, ...rest } = itemData;
    formData.append('item', new Blob([JSON.stringify(rest)], { type: 'application/json' }));
    if (imageFile) {
        formData.append('image', imageFile);
    }

    const response = await apiClient.post('/items', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
};

export const updateItem = async (id, itemData) => {
    const formData = new FormData();
    const { imageFile, ...rest } = itemData;
    formData.append('item', new Blob([JSON.stringify(rest)], { type: 'application/json' }));
    if (imageFile) {
        formData.append('image', imageFile);
    }

    const response = await apiClient.put(`/items/${id}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
};

export const deleteItem = async (id) => {
    await apiClient.delete(`/items/${id}`);
};

// ...
export const deleteItems = async (ids) => {
    // Implementing batch delete via multiple requests since backend doesn't have batch endpoint
    await Promise.all(ids.map(id => apiClient.delete(`/items/${id}`)));
};

export const getItemCategories = async () => {
    const response = await apiClient.get('/items/categories');
    return response.data;
};

export const getItemRemarks = async () => {
    const response = await apiClient.get('/items/remarks');
    return response.data;
};
