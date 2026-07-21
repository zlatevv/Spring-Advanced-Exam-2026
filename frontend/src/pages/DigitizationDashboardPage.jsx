import { useEffect, useState } from 'react';
import { fetchManuscripts, fetchDigitizationStatus } from '../api/manuscriptApi';
import { StatusBadge, Loading, EmptyState, ErrorBanner, extractErrorMessage } from '../components/Feedback';

export default function DigitizationDashboardPage() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    fetchManuscripts({ size: 100 })
      .then(async (data) => {
        const queued = (data.content || []).filter((m) => m.digitizationStatus !== 'NOT_STARTED');
        const withStatus = await Promise.all(
          queued.map(async (m) => {
            try {
              const status = await fetchDigitizationStatus(m.id);
              return { ...m, jobStatus: status };
            } catch {
              return { ...m, jobStatus: null };
            }
          })
        );
        setRows(withStatus);
      })
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <h1>Digitization Dashboard</h1>
      <ErrorBanner message={error} />
      {loading ? (
        <Loading label="Contacting the digitization microservice…" />
      ) : rows.length === 0 ? (
        <EmptyState title="No digitization jobs queued" hint="Request digitization from the Manuscript Management page." />
      ) : (
        <table className="data-table">
          <thead>
            <tr><th>Manuscript</th><th>Priority</th><th>Job Status</th><th>Technician</th></tr>
          </thead>
          <tbody>
            {rows.map((m) => (
              <tr key={m.id}>
                <td>{m.title}</td>
                <td>{m.jobStatus?.priority || '—'}</td>
                <td><StatusBadge status={m.jobStatus?.status || m.digitizationStatus} /></td>
                <td>{m.jobStatus?.technician || 'Unassigned'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
