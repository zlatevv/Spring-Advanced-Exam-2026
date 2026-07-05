import apiClient from './client';

// GET /api/manuscripts/{manuscriptId}/notes  (authenticated)
export const fetchNotes = (manuscriptId) =>
  apiClient.get(`/manuscripts/${manuscriptId}/notes`).then((r) => r.data);

// POST /api/manuscripts/{manuscriptId}/notes  (authenticated)
// body: { content }
export const addNote = (manuscriptId, content) =>
  apiClient.post(`/manuscripts/${manuscriptId}/notes`, { content }).then((r) => r.data);

// DELETE /api/notes/{id}  (author, or ADMIN)
export const deleteNote = (id) => apiClient.delete(`/notes/${id}`).then((r) => r.data);
