export function StatusBadge({ status }) {
  const className = `badge badge-${String(status).toLowerCase().replace(/_/g, '')}`;
  return <span className={className}>{status.replace(/_/g, ' ')}</span>;
}

export function Loading({ label = 'Retrieving the archive…' }) {
  return <p className="loading-line">{label}</p>;
}

export function EmptyState({ title, hint }) {
  return (
    <div className="empty-state">
      <h3 style={{ color: 'var(--parchment)' }}>{title}</h3>
      {hint && <p>{hint}</p>}
    </div>
  );
}

export function ErrorBanner({ message }) {
  if (!message) return null;
  return <div className="form-error-banner">{message}</div>;
}

export function SuccessBanner({ message }) {
  if (!message) return null;
  return <div className="form-success-banner">{message}</div>;
}

export function extractErrorMessage(err) {
  return (
    err?.response?.data?.message ||
    err?.response?.data?.error ||
    'Something went wrong. Please try again.'
  );
}
