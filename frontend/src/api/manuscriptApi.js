import apiClient from './client';

// GET /api/manuscripts?search=&era=&page=&size=  (public)
// returns: { content: Manuscript[], totalPages, totalElements, page }
export const fetchManuscripts = (params = {}) =>
  apiClient.get('/manuscripts', { params }).then((r) => r.data);

// GET /api/manuscripts/{id}  (public if visibility=PUBLIC, else requires auth)
export const fetchManuscript = (id) => apiClient.get(`/manuscripts/${id}`).then((r) => r.data);

// POST /api/manuscripts  (CURATOR, ADMIN)
// body: { title, author, era, originRegion, description, conservationStatus }
export const createManuscript = (payload) =>
  apiClient.post('/manuscripts', payload).then((r) => r.data);

// PUT /api/manuscripts/{id}  (CURATOR, ADMIN)
export const updateManuscript = (id, payload) =>
  apiClient.put(`/manuscripts/${id}`, payload).then((r) => r.data);

// PUT /api/manuscripts/{id}/visibility  (CURATOR, ADMIN)
// body: { visibility: 'PUBLIC' | 'RESTRICTED' }
export const setManuscriptVisibility = (id, visibility) =>
  apiClient.put(`/manuscripts/${id}/visibility`, { visibility }).then((r) => r.data);

// POST /api/manuscripts/{id}/digitize  (CURATOR, ADMIN)
// Triggers a Feign call from the Main app to the Digitization REST microservice.
// body: { priority: 'LOW' | 'MEDIUM' | 'HIGH' }
export const requestDigitization = (id, priority) =>
  apiClient.post(`/manuscripts/${id}/digitize`, { priority }).then((r) => r.data);

// GET /api/manuscripts/{id}/digitization-status  (CURATOR, ADMIN)
// Main app proxies this via Feign Client GET call to the microservice.
export const fetchDigitizationStatus = (id) =>
  apiClient.get(`/manuscripts/${id}/digitization-status`).then((r) => r.data);
