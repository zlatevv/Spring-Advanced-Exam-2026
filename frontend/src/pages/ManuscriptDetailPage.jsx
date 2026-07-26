import {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {fetchManuscript, fetchManuscriptSummary} from '../api/manuscriptApi';
import {submitAccessRequest} from '../api/requestApi';
import {addNote, deleteNote, fetchNotes} from '../api/noteApi';
import {useAuth} from '../context/AuthContext';
import {ErrorBanner, extractErrorMessage, Loading, StatusBadge, SuccessBanner} from '../components/Feedback';

export default function ManuscriptDetailPage() {
  const { id } = useParams();
  const { user, hasRole } = useAuth();
  const [manuscript, setManuscript] = useState(null);
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [purpose, setPurpose] = useState('');
  const [noteText, setNoteText] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [summary, setSummary] = useState(null);
  const [summaryLoading, setSummaryLoading] = useState(false);

  const load = () => {
    setLoading(true);
    fetchManuscript(id)
      .then(setManuscript)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
    if (user) {
      fetchNotes(id).then(setNotes).catch(() => {});
    }
  };

  useEffect(() => { load(); /* eslint-disable-next-line */ }, [id]);

  const onRequestAccess = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSubmitting(true);
    try {
      await submitAccessRequest({ manuscriptId: id, purpose });
      setSuccess('Access request submitted. You can track its status under "My Requests".');
      setPurpose('');
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  const onAddNote = async (e) => {
    e.preventDefault();
    if (!noteText.trim()) return;
    try {
      const created = await addNote(id, noteText);
      setNotes([created, ...notes]);
      setNoteText('');
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  const onDeleteNote = async (noteId) => {
    try {
      await deleteNote(noteId);
      setNotes(notes.filter((n) => n.id !== noteId));
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };
  const onGenerateSummary = async () => {
    setSummaryLoading(true);
    setError('');
    try {
      const result = await fetchManuscriptSummary(id);
      setSummary(result.summary);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setSummaryLoading(false);
    }
  };

  if (loading) return <div className="page"><Loading /></div>;
  if (!manuscript) return <div className="page"><ErrorBanner message={error || 'Manuscript not found.'} /></div>;

  return (
    <div className="page" style={{ maxWidth: 760 }}>
      <ErrorBanner message={error} />
      <SuccessBanner message={success} />

      <div className="eyebrow">{manuscript.era}</div>
      <h1 style={{ fontSize: '2.2rem' }}>{manuscript.title}</h1>
      <p style={{ color: 'var(--parchment)', opacity: 0.8 }}>
        {manuscript.author || 'Unknown author'} · {manuscript.originRegion}
      </p>
      <div style={{ display: 'flex', gap: '0.5rem', margin: '0.8rem 0 1.4rem' }}>
        <StatusBadge status={manuscript.visibility} />
        <StatusBadge status={manuscript.conservationStatus} />
        <StatusBadge status={manuscript.digitizationStatus} />
      </div>
      <p style={{ color: 'var(--parchment)', opacity: 0.85, lineHeight: 1.6 }}>{manuscript.description}</p>
      <section style={{ marginTop: '2rem' }}>
        {!summary && (
            <button className="btn btn-ghost" onClick={onGenerateSummary} disabled={summaryLoading}>
              {summaryLoading ? 'Generating…' : '✦ Generate AI Research Summary'}
            </button>
        )}
        {summary && (
            <div style={{ background: 'var(--parchment-panel)', borderRadius: 4, padding: '1.4rem', marginTop: '1rem' }} className="on-parchment">
              <h3 style={{ fontSize: '1rem' }}>AI Research Summary</h3>
              <p style={{ margin: 0, lineHeight: 1.6 }}>{summary}</p>
            </div>
        )}
      </section>

      {user && hasRole('RESEARCHER') && (
        <section style={{ marginTop: '2.4rem', background: 'var(--parchment-panel)', borderRadius: 4, padding: '1.6rem' }} className="on-parchment">
          <h3>Request Study Access</h3>
          <form onSubmit={onRequestAccess}>
            <div className="field">
              <label htmlFor="purpose">Purpose of research</label>
              <textarea
                id="purpose"
                required
                minLength={10}
                value={purpose}
                onChange={(e) => setPurpose(e.target.value)}
                placeholder="Describe your research purpose for accessing this manuscript…"
              />
            </div>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Submitting…' : 'Submit Request'}
            </button>
          </form>
        </section>
      )}

      {user && (
        <section style={{ marginTop: '2.4rem', background: 'var(--parchment-panel)', borderRadius: 4, padding: '1.6rem' }} className="on-parchment">
          <h3>Study Notes</h3>
          <form onSubmit={onAddNote} style={{ marginBottom: '1.2rem' }}>
            <div className="field">
              <textarea
                placeholder="Add a study note…"
                value={noteText}
                onChange={(e) => setNoteText(e.target.value)}
              />
            </div>
            <button type="submit" className="btn btn-ghost btn-sm">Add Note</button>
          </form>
          {notes.length === 0 ? (
            <p style={{ color: 'var(--muted)' }}>No notes yet.</p>
          ) : (
            notes.map((n) => (
              <div key={n.id} style={{ borderTop: '1px solid var(--parchment-line)', padding: '0.8rem 0', display: 'flex', justifyContent: 'space-between', gap: '1rem' }}>
                <div>
                  <p style={{ margin: 0 }}>{n.content}</p>
                  <small style={{ color: 'var(--muted)' }}>{n.authorName} · {new Date(n.createdAt).toLocaleDateString()}</small>
                </div>
                {(n.authorId === user.id || hasRole('ADMIN')) && (
                  <button className="btn btn-danger btn-sm" onClick={() => onDeleteNote(n.id)}>Delete</button>
                )}
              </div>
            ))
          )}
        </section>
      )}
    </div>
  );
}
