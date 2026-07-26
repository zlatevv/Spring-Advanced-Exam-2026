import apiClient from './client';

export const fetchManuscripts = (params = {}) =>
    apiClient.get('/manuscripts', {params}).then((r) => r.data);

export const fetchManuscript = (id) => apiClient.get(`/manuscripts/${id}`).then((r) => r.data);

export const createManuscript = (payload) =>
    apiClient.post('/manuscripts', payload).then((r) => r.data);

export const updateManuscript = (id, payload) =>
    apiClient.put(`/manuscripts/${id}`, payload).then((r) => r.data);

export const setManuscriptVisibility = (id, visibility) =>
    apiClient.put(`/manuscripts/${id}/visibility`, {visibility}).then((r) => r.data);

export const requestDigitization = (id, priority) =>
    apiClient.post(`/manuscripts/${id}/digitize`, {priority}).then((r) => r.data);

export const fetchDigitizationStatus = (id) =>
    apiClient.get(`/manuscripts/${id}/digitization-status`).then((r) => r.data);

export const fetchManuscriptSummary = (id) =>
    apiClient.get(`/manuscripts/${id}/summary`).then((r) => r.data);

export const cancelDigitization = (id) =>
    apiClient.delete(`/manuscripts/${id}/digitize`).then((r) => r.data);
