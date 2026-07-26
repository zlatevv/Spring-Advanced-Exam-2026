import apiClient from './client';

export const createReservation = (payload) =>
    apiClient.post('/reservations', payload).then((r) => r.data);

export const fetchMyReservations = () =>
    apiClient.get('/reservations/mine').then((r) => r.data);

export const fetchReservations = () => apiClient.get('/reservations').then((r) => r.data);

export const cancelReservation = (id) =>
    apiClient.delete(`/reservations/${id}`).then((r) => r.data);
