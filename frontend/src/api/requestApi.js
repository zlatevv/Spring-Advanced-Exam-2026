import apiClient from './client';

export const submitAccessRequest = (payload) =>
  apiClient.post('/access-requests', payload).then((r) => r.data);

export const fetchMyAccessRequests = () =>
  apiClient.get('/access-requests/mine').then((r) => r.data);

export const fetchAccessRequests = (params = {}) =>
  apiClient.get('/access-requests', { params }).then((r) => r.data);

export const decideAccessRequest = (id, decision) =>
  apiClient.put(`/access-requests/${id}/decision`, { decision }).then((r) => r.data);
