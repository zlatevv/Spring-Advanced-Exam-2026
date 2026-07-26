import {Link} from 'react-router-dom';
import {StatusBadge} from './Feedback';

export default function ManuscriptCard({manuscript}) {
    return (
        <Link to={`/catalog/${manuscript.id}`} style={{textDecoration: 'none'}} className="manuscript-card">
            <div className="cover">{manuscript.era || 'Undated'}</div>
            <div className="body">
                <h3>{manuscript.title}</h3>
                <div className="meta">{manuscript.author || 'Unknown author'} · {manuscript.originRegion}</div>
                <div style={{marginTop: 'auto', display: 'flex', gap: '0.4rem', flexWrap: 'wrap'}}>
                    <StatusBadge status={manuscript.visibility}/>
                    <StatusBadge status={manuscript.conservationStatus}/>
                </div>
            </div>
        </Link>
    );
}
