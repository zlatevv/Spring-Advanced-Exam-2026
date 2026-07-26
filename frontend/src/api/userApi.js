import apiClient from './client';

export const fetchMyProfile = () => apiClient.get('/users/me/profile').then((r) => r.data);

export const updateMyProfile = (payload) =>
    apiClient.put('/users/me/profile', payload).then((r) => r.data);

export const fetchAllUsers = () => apiClient.get('/users').then((r) => r.data);

export const updateUserRole = (id, role) =>
    apiClient.put(`/users/${id}/role`, {role}).then((r) => r.data);
