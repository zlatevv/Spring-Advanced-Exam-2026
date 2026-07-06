import { useEffect, useState } from 'react';
import {
  fetchManuscripts, createManuscript, updateManuscript,
  setManuscriptVisibility, requestDigitization
} from '../api/manuscriptApi';
import { StatusBadge, Loading, ErrorBanner, SuccessBanner, extractErrorMessage } from '../components/Feedback';

const emptyForm = {
  title: '', author: '', era: 'MEDIEVAL', originRegion: '', description: '', conservationStatus: 'STABLE'
};

export default function ManuscriptManagementPage() {
  const [manuscripts, setManuscripts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const load = () => {
    setLoading(true);
    fetchManuscripts({ size: 100 })
      .then((data) => setManuscripts(data.content || []))
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onEdit = (m) => {
    setEditingId(m.id);
    setForm({
      title: m.title, author: m.author || '', era: m.era,
      originRegion: m.originRegion || '', description: m.description || '',
      conservationStatus: m.conservationStatus
    });
  };

  const onCancelEdit = () => { setEditingId(null); setForm(emptyForm); };

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSubmitting(true);
    try {
      if (editingId) {
        await updateManuscript(editingId, form);
        setSuccess('Manuscript updated.');
      } else {
        await createManuscript(form);
        setSuccess('Manuscript added to the catalog.');
      }
      onCancelEdit();
      load();
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  const onToggleVisibility = async (m) => {
    const next = m.visibility === 'PUBLIC' ? 'RESTRICTED' : 'PUBLIC';
    try {
      await setManuscriptVisibility(m.id, next);
      setManuscripts((prev) => prev.map((x) => (x.id === m.id ? { ...x, visibility: next } : x)));
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  const onDigitize = async (m) => {
    try {
      await requestDigitization(m.id, 'MEDIUM');
      setManuscripts((prev) => prev.map((x) => (x.id === m.id ? { ...x, digitizationStatus: 'QUEUED' } : x)));
      setSuccess(`Digitization requested for "${m.title}" (Feign call to microservice).`);
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="page">
      <h1>Manuscript Management</h1>
      <ErrorBanner message={error} />
      <SuccessBanner message={success} />

      <section style={{ background: 'var(--parchment-panel)', borderRadius: 4, padding: '1.6rem', marginBottom: '2.2rem' }} className="on-parchment">
        <h3>{editingId ? 'Edit Manuscript' : 'Add Manuscript'}</h3>
        <form onSubmit={onSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 1rem' }}>
            <div className="field">
              <label>Title</label>
              <input name="title" required value={form.title} onChange={onChange} />
            </div>
            <div className="field">
              <label>Author</label>
              <input name="author" value={form.author} onChange={onChange} />
            </div>
            <div className="field">
              <label>Era</label>
              <select name="era" value={form.era} onChange={onChange}>
                <option value="ANTIQUE">Antique</option>
                <option value="MEDIEVAL">Medieval</option>
                <option value="RENAISSANCE">Renaissance</option>
                <option value="EARLY_MODERN">Early Modern</option>
              </select>
            </div>
            <div className="field">
              <label>Origin Region</label>
              <input name="originRegion" value={form.originRegion} onChange={onChange} />
            </div>
            <div className="field">
              <label>Conservation Status</label>
              <select name="conservationStatus" value={form.conservationStatus} onChange={onChange}>
                <option value="STABLE">Stable</option>
                <option value="FRAGILE">Fragile</option>
                <option value="UNDER_TREATMENT">Under Treatment</option>
              </select>
            </div>
          </div>
          <div className="field">
            <label>Description</label>
            <textarea name="description" required value={form.description} onChange={onChange} />
          </div>
          <div style={{ display: 'flex', gap: '0.6rem' }}>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {editingId ? 'Save Changes' : 'Add Manuscript'}
            </button>
            {editingId && (
              <button type="button" className="btn btn-ghost" onClick={onCancelEdit}>Cancel</button>
            )}
          </div>
        </form>
      </section>

      {loading ? (
        <Loading />
      ) : (
        <table className="data-table">
          <thead>
            <tr><th>Title</th><th>Visibility</th><th>Digitization</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {manuscripts.map((m) => (
              <tr key={m.id}>
                <td>{m.title}</td>
                <td><StatusBadge status={m.visibility} /></td>
                <td><StatusBadge status={m.digitizationStatus} /></td>
                <td style={{ display: 'flex', gap: '0.4rem', flexWrap: 'wrap' }}>
                  <button className="btn btn-ghost btn-sm" onClick={() => onEdit(m)}>Edit</button>
                  <button className="btn btn-ghost btn-sm" onClick={() => onToggleVisibility(m)}>
                    Make {m.visibility === 'PUBLIC' ? 'Restricted' : 'Public'}
                  </button>
                  {m.digitizationStatus === 'NOT_STARTED' && (
                    <button className="btn btn-primary btn-sm" onClick={() => onDigitize(m)}>Request Digitization</button>
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
