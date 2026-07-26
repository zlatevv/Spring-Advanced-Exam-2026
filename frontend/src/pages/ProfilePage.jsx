import {useEffect, useState} from 'react';
import {fetchMyProfile, updateMyProfile} from '../api/userApi';
import {ErrorBanner, extractErrorMessage, Loading, SuccessBanner} from '../components/Feedback';

export default function ProfilePage() {
    const [profile, setProfile] = useState(null);
    const [form, setForm] = useState({fullName: '', email: '', institution: ''});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        fetchMyProfile()
            .then((data) => {
                setProfile(data);
                setForm({fullName: data.fullName, email: data.email, institution: data.institution || ''});
            })
            .catch((err) => setError(extractErrorMessage(err)))
            .finally(() => setLoading(false));
    }, []);

    const onChange = (e) => setForm({...form, [e.target.name]: e.target.value});

    const onSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setSubmitting(true);
        try {
            const updated = await updateMyProfile(form);
            setProfile(updated);
            setSuccess('Profile updated.');
        } catch (err) {
            setError(extractErrorMessage(err));
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div className="page"><Loading/></div>;

    return (
        <div className="page" style={{maxWidth: 520}}>
            <h1>My Profile</h1>
            <ErrorBanner message={error}/>
            <SuccessBanner message={success}/>
            <div style={{background: 'var(--parchment-panel)', borderRadius: 4, padding: '1.6rem'}}
                 className="on-parchment">
                <p style={{color: 'var(--muted)', marginTop: 0}}>Role: <strong>{profile?.role}</strong></p>
                <form onSubmit={onSubmit}>
                    <div className="field">
                        <label>Full Name</label>
                        <input name="fullName" required value={form.fullName} onChange={onChange}/>
                    </div>
                    <div className="field">
                        <label>Email</label>
                        <input name="email" type="email" required value={form.email} onChange={onChange}/>
                    </div>
                    <div className="field">
                        <label>Institution</label>
                        <input name="institution" value={form.institution} onChange={onChange}/>
                    </div>
                    <button type="submit" className="btn btn-primary" disabled={submitting}>
                        {submitting ? 'Saving…' : 'Save Changes'}
                    </button>
                </form>
            </div>
        </div>
    );
}
