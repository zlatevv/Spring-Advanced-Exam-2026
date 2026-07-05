import apiClient from './client';

// POST /api/access-requests  (RESEARCHER)
// body: { manuscriptId, purpose }
export const submitAccessRequest = (payload) =>
  apiClient.post('/access-requests', payload).then((r) => r.data);

// GET /api/access-requests/mine  (RESEARCHER)
export const fetchMyAccessRequests = () =>
  apiClient.get('/access-requests/mine').then((r) => r.data);

// GET /api/access-requests?status=PENDING  (CURATOR, ADMIN)
export const fetchAccessRequests = (params = {}) =>
  apiClient.get('/access-requests', { params }).then((r) => r.data);

// PUT /api/access-requests/{id}/decision  (CURATOR, ADMIN)
// body: { decision: 'APPROVED' | 'REJECTED' }
export const decideAccessRequest = (id, decision) =>
  apiClient.put(`/access-requests/${id}/decision`, { decision }).then((r) => r.data);
