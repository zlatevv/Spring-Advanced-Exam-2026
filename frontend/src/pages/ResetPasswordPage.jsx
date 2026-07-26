import {useLocation, useNavigate} from 'react-router-dom';
import {useState} from 'react';
import ParchmentPanel from '../components/ParchmentPanel';
import {ErrorBanner} from '../components/Feedback';
import {resetPassword} from '../api/authApi';

export default function ResetPasswordPage() {
    const location = useLocation();
    const navigate = useNavigate();

    const token = new URLSearchParams(location.search).get('token');

    const [newPassword, setNewPassword] = useState('');
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const onSubmit = async (e) => {
        e.preventDefault();

        setError('');
        setSubmitting(true);

        try {
            await resetPassword(token, newPassword);

            navigate('/login');
        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Password reset failed.'
            );
        } finally {
            setSubmitting(false);
        }
    };

    if (!token) {
        return (
            <ParchmentPanel>
                <h2>Invalid reset link</h2>
            </ParchmentPanel>
        );
    }

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
            <ParchmentPanel style={{width: '100%', maxWidth: 420}}>
                <h2>Reset Password</h2>

                <ErrorBanner message={error}/>

                <form onSubmit={onSubmit}>
                    <div className="field">
                        <label>New Password</label>

                        <input
                            type="password"
                            required
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                        />
                    </div>

                    <button
                        className="btn btn-primary"
                        style={{width: '100%'}}
                        disabled={submitting}
                    >
                        {submitting
                            ? 'Updating...'
                            : 'Update Password'}
                    </button>
                </form>
            </ParchmentPanel>
        </div>
    );
}