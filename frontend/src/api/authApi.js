import apiClient from './client';

export const register = (payload) => apiClient.post('/auth/register', payload).then((r) => r.data);

export const login = (payload) => apiClient.post('/auth/login', payload).then((r) => r.data);

export const fetchCurrentUser = () => apiClient.get('/auth/me').then((r) => r.data);

export const forgotPassword = (email) =>
    apiClient.post('/auth/forgot-password', {email}).then((r) => r.data);

export const resetPassword = (token, newPassword) =>
    apiClient.post('/auth/reset-password', {
        token,
        newPassword,
    }).then((r) => r.data);