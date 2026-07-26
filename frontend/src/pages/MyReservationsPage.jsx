import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {cancelReservation, fetchMyReservations} from '../api/reservationApi';
import {EmptyState, ErrorBanner, extractErrorMessage, Loading, StatusBadge} from '../components/Feedback';

export default function MyReservationsPage() {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = () => {
    setLoading(true);
    fetchMyReservations()
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
      <h1>My Reading Room Reservations</h1>
      <ErrorBanner message={error} />
      {loading ? (
        <Loading />
      ) : reservations.length === 0 ? (
        <EmptyState title="No reservations yet" hint="Once an access request is approved, you can reserve a reading-room slot from My Requests." />
      ) : (
          <div className="table-wrapper">
            <table className="data-table">
              <thead>
                <tr><th>Manuscript</th><th>Date</th><th>Time</th><th>Status</th><th></th></tr>
              </thead>
              <tbody>
                {reservations.map((r) => (
                  <tr key={r.id}>
                    <td><Link to={`/catalog/${r.manuscriptId}`}>{r.manuscriptTitle}</Link></td>
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
