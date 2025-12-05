import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api';

export const getAllEmployees = async () => {
  const response = await axios.get(`${API_BASE_URL}/employees`);
  return response.data;
};

export const getEmployeeById = async (id) => {
  const response = await axios.get(`${API_BASE_URL}/employees/${id}`);
  return response.data;
};

export const createEmployee = async (employeeData) => {
  const response = await axios.post(`${API_BASE_URL}/employees`, employeeData);
  return response.data;
};

export const updateEmployee = async (id, employeeData) => {
  const response = await axios.put(`${API_BASE_URL}/employees/${id}`, employeeData);
  return response.data;
};

export const deleteEmployee = async (id) => {
  await axios.delete(`${API_BASE_URL}/employees/${id}`);
};
