import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { fetchCurrentUser } from '../api/authApi';
import { Loading, ErrorBanner } from '../components/Feedback';

export default function OAuthCallbackPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { setUserFromToken } = useAuth();
    const [error, setError] = useState('');

    useEffect(() => {
        const token = searchParams.get('token');

        if (!token) {
            setError('No authentication token received.');
            return;
        }

        localStorage.setItem('rm_token', token);

        fetchCurrentUser()
            .then((user) => {
                localStorage.setItem('rm_user', JSON.stringify(user));
                setUserFromToken(user);
                navigate('/catalog', { replace: true });
            })
            .catch(() => {
                setError('Could not complete sign-in. Please try again.');
            });
    }, [searchParams, navigate, setUserFromToken]);

    if (error) {
        return (
            <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <ErrorBanner message={error} />
            </div>
        );
    }

    return (
        <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Loading label="Completing sign-in…" />
        </div>
    );
}