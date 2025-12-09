import apiClient from './client';

export const getAllEmployees = async () => {
  const response = await apiClient.get('/employees');
  return response.data;
};

export const getEmployeeById = async (id) => {
  const response = await apiClient.get(`/employees/${id}`);
  return response.data;
};

export const createEmployee = async (employeeData) => {
  const response = await apiClient.post('/employees', employeeData);
  return response.data;
};

export const updateEmployee = async (id, employeeData) => {
  const response = await apiClient.put(`/employees/${id}`, employeeData);
  return response.data;
};

export const deleteEmployee = async (id) => {
  await apiClient.delete(`/employees/${id}`);
};

export const deleteEmployees = async (ids) => {
  // Pass data in the config object for DELETE requests
  await apiClient.delete('/employees/batch', { data: ids });
};

export const getEmployeePositions = async () => {
  const response = await apiClient.get('/employees/positions');
  return response.data;
};
