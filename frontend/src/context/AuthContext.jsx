import { createContext, useContext, useState } from 'react';
import * as authApi from '../api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('vitalarc_user');
    return stored ? JSON.parse(stored) : null;
  });

  function persistSession(authResponse) {
    localStorage.setItem('vitalarc_token', authResponse.accessToken);
    localStorage.setItem('vitalarc_user', JSON.stringify(authResponse.user));
    setUser(authResponse.user);
  }

  async function register(email, password, displayName) {
    const response = await authApi.register(email, password, displayName);
    persistSession(response);
  }

  async function login(email, password) {
    const response = await authApi.login(email, password);
    persistSession(response);
  }

  function logout() {
    localStorage.removeItem('vitalarc_token');
    localStorage.removeItem('vitalarc_user');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, register, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}