import apiClient from './client';

export const fetchNotes = (manuscriptId) =>
    apiClient.get(`/manuscripts/${manuscriptId}/notes`).then((r) => r.data);

export const addNote = (manuscriptId, content) =>
    apiClient.post(`/manuscripts/${manuscriptId}/notes`, {content}).then((r) => r.data);

export const deleteNote = (id) => apiClient.delete(`/notes/${id}`).then((r) => r.data);
