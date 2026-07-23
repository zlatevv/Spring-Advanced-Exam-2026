import { useEffect, useState } from 'react';
import { fetchReservations, cancelReservation } from '../api/reservationApi';
import { StatusBadge, Loading, EmptyState, ErrorBanner, extractErrorMessage } from '../components/Feedback';

export default function ReservationsOverviewPage() {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = () => {
    setLoading(true);
    fetchReservations()
      .then(setReservations)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const onCancel = async (id) => {
    try {
      await cancelReservation(id);
      setReservations((prev) => prev.map((r) => (r.id === id ? { ...r, status: 'CANCELLED' } : r)));
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="page">
      <h1>Reading Room Reservations</h1>
      <ErrorBanner message={error} />
      {loading ? (
        <Loading />
      ) : reservations.length === 0 ? (
        <EmptyState title="No reservations yet" />
      ) : (
          <div className="table-wrapper">
            <table className="data-table">
              <thead>
                <tr><th>Researcher</th><th>Manuscript</th><th>Date</th><th>Time</th><th>Status</th><th></th></tr>
              </thead>
              <tbody>
                {reservations.map((r) => (
                  <tr key={r.id}>
                    <td>{r.researcherName}</td>
                    <td>{r.manuscriptTitle}</td>
                    <td>{r.slotDate}</td>
                    <td>{r.slotTime}</td>
                    <td><StatusBadge status={r.status} /></td>
                    <td>
                      {r.status === 'CONFIRMED' && (
                        <button className="btn btn-danger btn-sm" onClick={() => onCancel(r.id)}>Cancel</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
      )}
    </div>
  );
}
