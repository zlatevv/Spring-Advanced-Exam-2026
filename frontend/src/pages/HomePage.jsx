import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchManuscripts } from '../api/manuscriptApi';
import ManuscriptCard from '../components/ManuscriptCard';
import { Loading } from '../components/Feedback';

export default function HomePage() {
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchManuscripts({ page: 0, size: 3 })
      .then((data) => setFeatured(data.content || []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
        <div className="eyebrow">Est. for scholars and conservators</div>
        <h1 style={{ fontSize: '2.6rem' }}>Rare Manuscripts, Carefully Kept</h1>
        <p style={{ color: 'var(--parchment)', opacity: 0.75, maxWidth: 560, margin: '0 auto' }}>
          Browse the catalog, request access to restricted works, reserve a reading-room
          session, and follow each manuscript's journey through conservation and digitization.
        </p>
        <div style={{ marginTop: '1.6rem' }}>
          <Link to="/catalog" className="btn btn-primary">Browse the Catalog</Link>
        </div>
      </div>

      <h2 style={{ color: 'var(--gold-bright)', fontSize: '1.1rem', textAlign: 'center', marginBottom: '1.5rem' }}>
        Recently Cataloged
      </h2>
      {loading ? (
        <Loading />
      ) : (
        <div className="grid">
          {featured.map((m) => (
            <ManuscriptCard key={m.id} manuscript={m} />
          ))}
        </div>
      )}
    </div>
  );
}
