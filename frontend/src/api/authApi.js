import apiClient from './client';

// POST /api/auth/register
// body: { fullName, email, password }
// returns: { id, fullName, email, role }
export const register = (payload) => apiClient.post('/auth/register', payload).then((r) => r.data);

// POST /api/auth/login
// body: { email, password }
// returns: { token, user: { id, fullName, email, role } }
export const login = (payload) => apiClient.post('/auth/login', payload).then((r) => r.data);

// GET /api/auth/me
// returns: { id, fullName, email, role }
export const fetchCurrentUser = () => apiClient.get('/auth/me').then((r) => r.data);
