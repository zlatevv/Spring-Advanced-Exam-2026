import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useState } from 'react';

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
  const [menuOpen, setMenuOpen] = useState(false);

  return (
      <nav style={navStyle}>
        <NavLink
            to="/"
            style={{ textDecoration: 'none' }}
            onClick={() => setMenuOpen(false)}
        >
        <span
            style={{
              fontFamily: 'var(--font-display)',
              color: 'var(--gold-bright)',
              fontSize: '0.95rem',
              letterSpacing: '0.08em'
            }}
        >
          RAREMANUSCRIPTS
        </span>
        </NavLink>

        <button
            className="nav-toggle"
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label="Toggle menu"
        >
          ☰
        </button>

        <div className={`nav-links ${menuOpen ? 'nav-links-open' : ''}`}>
          <NavLink
              to="/catalog"
              style={linkStyle}
              onClick={() => setMenuOpen(false)}
          >
            Catalog
          </NavLink>

          <NavLink
              to="/about"
              style={linkStyle}
              onClick={() => setMenuOpen(false)}
          >
            About
          </NavLink>

          {user && (
              <NavLink
                  to="/my-requests"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                My Requests
              </NavLink>
          )}

          {user && (
              <NavLink
                  to="/my-reservations"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                My Reservations
              </NavLink>
          )}

          {hasRole('CURATOR', 'ADMIN') && (
              <NavLink
                  to="/manage/manuscripts"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Manage
              </NavLink>
          )}

          {hasRole('CURATOR', 'ADMIN') && (
              <NavLink
                  to="/manage/requests"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Requests
              </NavLink>
          )}

          {hasRole('CURATOR', 'ADMIN') && (
              <NavLink
                  to="/manage/digitization"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Digitization
              </NavLink>
          )}

          {hasRole('ADMIN') && (
              <NavLink
                  to="/admin/users"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Users
              </NavLink>
          )}

          {user && (
              <NavLink
                  to="/profile"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Profile
              </NavLink>
          )}

          {user ? (
              <button
                  className="btn btn-ghost btn-sm"
                  style={{ marginLeft: '1.6rem' }}
                  onClick={() => {
                    logout();
                    setMenuOpen(false);
                    navigate('/login');
                  }}
              >
                Sign Out
              </button>
          ) : (
              <NavLink
                  to="/login"
                  style={linkStyle}
                  onClick={() => setMenuOpen(false)}
              >
                Sign In
              </NavLink>
          )}
        </div>
      </nav>
  );
}