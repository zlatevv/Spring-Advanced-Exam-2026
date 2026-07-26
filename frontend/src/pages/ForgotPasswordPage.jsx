import {useState} from 'react';
import ParchmentPanel from '../components/ParchmentPanel';
import {ErrorBanner} from '../components/Feedback';
import {forgotPassword} from '../api/authApi';

export default function ForgotPasswordPage() {
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    const onSubmit = async (e) => {
        e.preventDefault();

        setError('');
        setSubmitting(true);

        try {
            await forgotPassword(email);

            setSuccess(true);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Failed to send reset email.'
            );
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div
            style={{
                minHeight: '100vh',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                padding: '2rem',
            }}
        >
            <ParchmentPanel style={{ width: '100%', maxWidth: 420 }}>
                <h2>Forgot Password</h2>

                <ErrorBanner message={error} />

                {success ? (
                    <p>
                        If an account exists, a password reset link has been sent.
                    </p>
                ) : (
                    <form onSubmit={onSubmit}>
                        <div className="field">
                            <label>Email</label>

                            <input
                                type="email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <button
                            className="btn btn-primary"
                            style={{ width: '100%' }}
                            disabled={submitting}
                        >
                            {submitting
                                ? 'Sending...'
                                : 'Send reset link'}
                        </button>
                    </form>
                )}
            </ParchmentPanel>
        </div>
    );
}