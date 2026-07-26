import {useEffect, useState} from 'react';
import {fetchManuscripts} from '../api/manuscriptApi';
import ManuscriptCard from '../components/ManuscriptCard';
import {EmptyState, ErrorBanner, extractErrorMessage, Loading} from '../components/Feedback';

export default function CatalogPage() {
    const [manuscripts, setManuscripts] = useState([]);
    const [search, setSearch] = useState('');
    const [era, setEra] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const load = (params) => {
        setLoading(true);
        setError('');
        fetchManuscripts(params)
            .then((data) => setManuscripts(data.content || []))
            .catch((err) => setError(extractErrorMessage(err)))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        load({});
    }, []);

    const onSearch = (e) => {
        e.preventDefault();
        load({search: search || undefined, era: era || undefined});
    };

    return (
        <div className="page">
            <h1>Manuscript Catalog</h1>
            <form onSubmit={onSearch} style={{display: 'flex', gap: '0.8rem', marginBottom: '2rem', flexWrap: 'wrap'}}>
                <input
                    placeholder="Search title or author…"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    style={{
                        flex: '1 1 260px',
                        padding: '0.7rem 0.9rem',
                        borderRadius: 3,
                        border: '1px solid var(--parchment-line)',
                        background: '#f4efe0'
                    }}
                />
                <select
                    value={era}
                    onChange={(e) => setEra(e.target.value)}
                    style={{
                        padding: '0.7rem 0.9rem',
                        borderRadius: 3,
                        border: '1px solid var(--parchment-line)',
                        background: '#f4efe0'
                    }}
                >
                    <option value="">All eras</option>
                    <option value="MEDIEVAL">Medieval</option>
                    <option value="RENAISSANCE">Renaissance</option>
                    <option value="EARLY_MODERN">Early Modern</option>
                    <option value="ANTIQUE">Antique</option>
                </select>
                <button type="submit" className="btn btn-primary">Search</button>
            </form>

            <ErrorBanner message={error}/>

            {loading ? (
                <Loading/>
            ) : manuscripts.length === 0 ? (
                <EmptyState title="No manuscripts found" hint="Try a different search term or era."/>
            ) : (
                <div className="grid">
                    {manuscripts.map((m) => (
                        <ManuscriptCard key={m.id} manuscript={m}/>
                    ))}
                </div>
            )}
        </div>
    );
}
