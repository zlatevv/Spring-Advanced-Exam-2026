import { useEffect, useState } from 'react';
import { fetchAllUsers, updateUserRole } from '../api/userApi';
import { Loading, ErrorBanner, SuccessBanner, extractErrorMessage } from '../components/Feedback';

const ROLES = ['RESEARCHER', 'CURATOR', 'ADMIN'];

export default function AdminUsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const load = () => {
    setLoading(true);
    fetchAllUsers()
      .then(setUsers)
      .catch((err) => setError(extractErrorMessage(err)))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const onRoleChange = async (id, role) => {
    setError(''); setSuccess('');
    try {
      await updateUserRole(id, role);
      setUsers((prev) => prev.map((u) => (u.id === id ? { ...u, role } : u)));
      setSuccess('Role updated.');
    } catch (err) {
      setError(extractErrorMessage(err));
    }
  };

  return (
    <div className="page">
      <h1>User & Role Management</h1>
      <ErrorBanner message={error} />
      <SuccessBanner message={success} />
      {loading ? (
        <Loading />
      ) : (
          <div className="table-wrapper">
            <table className="data-table">
              <thead>
                <tr><th>Name</th><th>Email</th><th>Role</th></tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.fullName}</td>
                    <td>{u.email}</td>
                    <td>
                      <select value={u.role} onChange={(e) => onRoleChange(u.id, e.target.value)}>
                        {ROLES.map((r) => <option key={r} value={r}>{r}</option>)}
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
      )}
    </div>
  );
}
