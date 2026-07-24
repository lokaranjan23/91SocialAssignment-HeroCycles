import React, { useContext } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Layout = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    const confirm = window.confirm("Are you sure you want to logout?\n\nYes / No");
    if (confirm) {
      logout();
      navigate('/login');
    }
  };

  return (
    <div style={{ fontFamily: 'sans-serif', margin: 0, padding: 0 }}>
      <header style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center', 
        padding: '1rem 2rem', 
        backgroundColor: '#333', 
        color: '#fff' 
      }}>
        <h2>Cycle Pricing System</h2>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          <span>Welcome, <strong>{user?.name}</strong></span>
          <span>Role: <strong>{user?.role}</strong></span>
          <button 
            onClick={handleLogout}
            style={{ 
              padding: '8px 16px', 
              cursor: 'pointer',
              backgroundColor: '#d9534f',
              color: 'white',
              border: 'none',
              borderRadius: '4px'
            }}
          >
            Logout
          </button>
        </div>
      </header>
      <main style={{ padding: '2rem' }}>
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;
