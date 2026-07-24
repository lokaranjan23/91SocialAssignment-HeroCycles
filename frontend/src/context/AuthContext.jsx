import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Restore authentication state from localStorage
    const storedToken = localStorage.getItem('token');
    const storedRole = localStorage.getItem('role');
    const storedName = localStorage.getItem('name');
    const storedEmail = localStorage.getItem('email');
    const storedStatus = localStorage.getItem('status');

    if (storedToken && storedRole) {
      setUser({
        token: storedToken,
        role: storedRole,
        name: storedName,
        email: storedEmail,
        status: storedStatus
      });
    }
    setLoading(false);
  }, []);

  const login = (userData) => {
    // Save to localStorage
    localStorage.setItem('token', userData.token);
    localStorage.setItem('role', userData.role);
    localStorage.setItem('name', userData.name);
    localStorage.setItem('email', userData.email);
    localStorage.setItem('status', userData.status);

    // Set state
    setUser(userData);
  };

  const logout = () => {
    // Clear localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('name');
    localStorage.removeItem('email');
    localStorage.removeItem('status');

    // Clear state
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
