import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchMyAccessRequests } from '../api/requestApi';
import { createReservation } from '../api/reservationApi';
import { StatusBadge, Loading, EmptyState, ErrorBanner, SuccessBanner, extractErrorMessage } from '../components/Feedback';

export default function MyRequestsPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [slots, setSlots] = useState({});

  const load = () => {
    setLoading(true);
    fetchMyAccessRequests()
      .then(setRequests)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const onReserve = async (requestId) => {
    const slot = slots[requestId];
    if (!slot?.date || !slot?.time) {
      setError('Choose a date and time for the reading room before reserving.');
      return;
    }
    setError(''); setSuccess('');
    try {
      await createReservation({ accessRequestId: requestId, slotDate: slot.date, slotTime: slot.time });
      setSuccess('Reading room slot reserved. See "My Reservations" for details.');
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="page">
      <h1>My Access Requests</h1>
      <ErrorBanner message={error} />
      <SuccessBanner message={success} />
      {loading ? (
        <Loading />
      ) : requests.length === 0 ? (
        <EmptyState title="No requests yet" hint={<>Browse the <Link to="/catalog">catalog</Link> and request access to a manuscript.</>} />
      ) : (
        <table className="data-table">
          <thead>
            <tr><th>Manuscript</th><th>Purpose</th><th>Status</th><th>Requested</th><th>Reserve Slot</th></tr>
          </thead>
          <tbody>
            {requests.map((r) => (
              <tr key={r.id}>
                <td><Link to={`/catalog/${r.manuscriptId}`}>{r.manuscriptTitle}</Link></td>
                <td style={{ maxWidth: 240 }}>{r.purpose}</td>
                <td><StatusBadge status={r.status} /></td>
                <td>{new Date(r.requestedDate).toLocaleDateString()}</td>
                <td>
                  {r.status === 'APPROVED' ? (
                    <div style={{ display: 'flex', gap: '0.4rem' }}>
                      <input
                        type="date"
                        onChange={(e) => setSlots({ ...slots, [r.id]: { ...slots[r.id], date: e.target.value } })}
                        style={{ padding: '0.35rem', borderRadius: 3, border: '1px solid var(--parchment-line)' }}
                      />
                      <input
                        type="time"
                        onChange={(e) => setSlots({ ...slots, [r.id]: { ...slots[r.id], time: e.target.value } })}
                        style={{ padding: '0.35rem', borderRadius: 3, border: '1px solid var(--parchment-line)' }}
                      />
                      <button className="btn btn-primary btn-sm" onClick={() => onReserve(r.id)}>Reserve</button>
                    </div>
                  ) : (
                    <span style={{ color: 'var(--muted)' }}>—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
