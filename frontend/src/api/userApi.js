import apiClient from './client';

// GET /api/users/me/profile  (authenticated)
export const fetchMyProfile = () => apiClient.get('/users/me/profile').then((r) => r.data);

// PUT /api/users/me/profile  (authenticated)
// body: { fullName, email, institution }
export const updateMyProfile = (payload) =>
  apiClient.put('/users/me/profile', payload).then((r) => r.data);

// GET /api/users  (ADMIN)
export const fetchAllUsers = () => apiClient.get('/users').then((r) => r.data);

// PUT /api/users/{id}/role  (ADMIN)
// body: { role: 'RESEARCHER' | 'CURATOR' | 'ADMIN' }
export const updateUserRole = (id, role) =>
  apiClient.put(`/users/${id}/role`, { role }).then((r) => r.data);
