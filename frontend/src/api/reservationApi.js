import apiClient from './client';

// POST /api/reservations  (RESEARCHER, requires an APPROVED access request)
// body: { accessRequestId, slotDate, slotTime }
export const createReservation = (payload) =>
  apiClient.post('/reservations', payload).then((r) => r.data);

// GET /api/reservations/mine  (RESEARCHER)
export const fetchMyReservations = () =>
  apiClient.get('/reservations/mine').then((r) => r.data);

// GET /api/reservations  (CURATOR, ADMIN)
export const fetchReservations = () => apiClient.get('/reservations').then((r) => r.data);

// DELETE /api/reservations/{id}  (owner, or CURATOR/ADMIN)
export const cancelReservation = (id) =>
  apiClient.delete(`/reservations/${id}`).then((r) => r.data);
