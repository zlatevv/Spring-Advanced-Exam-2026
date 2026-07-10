import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import ParchmentPanel from '../components/ParchmentPanel';
import { ErrorBanner, extractErrorMessage } from '../components/Feedback';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await login(form.email, form.password);
      const redirectTo = location.state?.from?.pathname || '/catalog';
      navigate(redirectTo, { replace: true });
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '2rem' }}>
      <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: '1.6rem', letterSpacing: '0.1em', color: 'var(--parchment)' }}>
          RAREMANUSCRIPTS
        </div>
        <div className="eyebrow" style={{ marginTop: '0.2rem' }}>Digital Preservation Portal</div>
      </div>

      <ParchmentPanel style={{ width: '100%', maxWidth: 420 }}>
        <h2 style={{ textAlign: 'center', marginBottom: 0 }}>Welcome Back</h2>
        <p style={{ textAlign: 'center', color: 'var(--muted)', marginTop: '0.2rem', marginBottom: '1.6rem' }}>
          Secure Access
        </p>

        <ErrorBanner message={error} />

        <form onSubmit={onSubmit}>
          <div className="field">
            <label htmlFor="email">Email</label>
            <input id="email" name="email" type="email" required value={form.email} onChange={onChange} />
          </div>
          <div className="field">
            <label htmlFor="password">Password</label>
            <input id="password" name="password" type="password" required value={form.password} onChange={onChange} />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '0.4rem' }} disabled={submitting}>
            {submitting ? 'Signing In…' : 'Sign In'}
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: '1.4rem', color: 'var(--muted)', fontSize: '0.9rem' }}>
          For <Link to="/register" style={{ color: 'var(--teal-deep)', fontWeight: 600 }}>Request an account</Link>
        </p>
      </ParchmentPanel>
    </div>
  );
}
