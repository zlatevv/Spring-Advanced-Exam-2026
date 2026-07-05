import { createContext, useContext, useState, useCallback } from 'react';
import * as authApi from '../api/authApi';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const raw = localStorage.getItem('rm_user');
        return raw ? JSON.parse(raw) : null;
    });

    const [initializing] = useState(false);

    const login = useCallback(async (email, password) => {
        const data = await authApi.login({ email, password });

        localStorage.setItem('rm_token', data.token);
        localStorage.setItem('rm_user', JSON.stringify(data.user));

        setUser(data.user);

        return data.user;
    }, []);

    const register = useCallback(async (fullName, email, password) => {
        await authApi.register({ fullName, email, password });
    }, [])

    const logout = useCallback(() => {
        localStorage.removeItem('rm_token');
        localStorage.removeItem('rm_user');
        setUser(null);
    }, []);

    const hasRole = useCallback(
        (...roles) => !!user && roles.includes(user.role),
        [user]
    );

    return (
        <AuthContext.Provider
            value={{ user, initializing, login, register, logout, hasRole, isAuthenticated: !!user }}
        >
            {children}
        </AuthContext.Provider>
    )
}


export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}