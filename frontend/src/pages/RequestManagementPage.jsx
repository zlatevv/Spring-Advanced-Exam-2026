import { useEffect, useState } from 'react';
import { fetchAccessRequests, decideAccessRequest } from '../api/requestApi';
import { StatusBadge, Loading, EmptyState, ErrorBanner, extractErrorMessage } from '../components/Feedback';

export default function RequestManagementPage() {
  const [requests, setRequests] = useState([]);
  const [filter, setFilter] = useState('PENDING');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = (status) => {
    setLoading(true);
    fetchAccessRequests(status ? { status } : {})
      .then(setRequests)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(filter); /* eslint-disable-next-line */ }, [filter]);

  const onDecide = async (id, decision) => {
    setError('');
    try {
      await decideAccessRequest(id, decision);
      setRequests((prev) => prev.map((r) => (r.id === id ? { ...r, status: decision } : r)));
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="page">
      <h1>Access Request Management</h1>
      <div style={{ marginBottom: '1.4rem' }}>
        {['PENDING', 'APPROVED', 'REJECTED', ''].map((s) => (
          <button
            key={s || 'ALL'}
            className={`btn btn-sm ${filter === s ? 'btn-primary' : 'btn-ghost'}`}
            style={{ marginRight: '0.5rem' }}
            onClick={() => setFilter(s)}
          >
            {s || 'ALL'}
          </button>
        ))}
      </div>
      <ErrorBanner message={error} />
      {loading ? (
        <Loading />
      ) : requests.length === 0 ? (
        <EmptyState title="No requests in this view" />
      ) : (
          <div className="table-wrapper">
            <table className="data-table">
              <thead>
                <tr><th>Researcher</th><th>Manuscript</th><th>Purpose</th><th>Status</th><th>Decision</th></tr>
              </thead>
              <tbody>
                {requests.map((r) => (
                  <tr key={r.id}>
                    <td>{r.researcherName}</td>
                    <td>{r.manuscriptTitle}</td>
                    <td style={{ maxWidth: 260 }}>{r.purpose}</td>
                    <td><StatusBadge status={r.status} /></td>
                    <td>
                      {r.status === 'PENDING' && (
                        <div style={{ display: 'flex', gap: '0.4rem' }}>
                          <button className="btn btn-primary btn-sm" onClick={() => onDecide(r.id, 'APPROVED')}>Approve</button>
                          <button className="btn btn-danger btn-sm" onClick={() => onDecide(r.id, 'REJECTED')}>Reject</button>
                        </div>
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
