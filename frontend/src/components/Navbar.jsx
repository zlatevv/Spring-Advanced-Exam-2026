import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const navStyle = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  padding: '1.1rem 2rem',
  borderBottom: '1px solid rgba(201,169,97,0.18)',
  position: 'sticky',
  top: 0,
  zIndex: 10,
  background: 'rgba(16,24,21,0.92)',
  backdropFilter: 'blur(6px)'
};

const linkStyle = ({ isActive }) => ({
  fontFamily: 'var(--font-display)',
  fontSize: '0.68rem',
  letterSpacing: '0.12em',
  textTransform: 'uppercase',
  textDecoration: 'none',
  color: isActive ? 'var(--gold-bright)' : 'rgba(239,232,214,0.65)',
  marginLeft: '1.6rem'
});

export default function Navbar() {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  return (
    <nav style={navStyle}>
      <NavLink to="/" style={{ textDecoration: 'none' }}>
        <span style={{ fontFamily: 'var(--font-display)', color: 'var(--gold-bright)', fontSize: '0.95rem', letterSpacing: '0.08em' }}>
          RAREMANUSCRIPTS
        </span>
      </NavLink>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <NavLink to="/catalog" style={linkStyle}>Catalog</NavLink>
        <NavLink to="/about" style={linkStyle}>About</NavLink>
        {user && <NavLink to="/my-requests" style={linkStyle}>My Requests</NavLink>}
        {user && <NavLink to="/my-reservations" style={linkStyle}>My Reservations</NavLink>}
        {hasRole('CURATOR', 'ADMIN') && <NavLink to="/manage/manuscripts" style={linkStyle}>Manage</NavLink>}
        {hasRole('CURATOR', 'ADMIN') && <NavLink to="/manage/requests" style={linkStyle}>Requests</NavLink>}
        {hasRole('CURATOR', 'ADMIN') && <NavLink to="/manage/digitization" style={linkStyle}>Digitization</NavLink>}
        {hasRole('ADMIN') && <NavLink to="/admin/users" style={linkStyle}>Users</NavLink>}
        {user && <NavLink to="/profile" style={linkStyle}>Profile</NavLink>}
        {user ? (
          <button
            className="btn btn-ghost btn-sm"
            style={{ marginLeft: '1.6rem' }}
            onClick={() => { logout(); navigate('/login'); }}
          >
            Sign Out
          </button>
        ) : (
          <NavLink to="/login" style={linkStyle}>Sign In</NavLink>
        )}
      </div>
    </nav>
  );
}
