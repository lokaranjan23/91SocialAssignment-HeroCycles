import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

const Rejected = () => {
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div style={{ padding: '2rem', textAlign: 'center', fontFamily: 'sans-serif' }}>
      <h2 style={{ color: '#dc3545' }}>Registration Rejected</h2>
      <p style={{ marginTop: '1rem', marginBottom: '2rem' }}>
        Unfortunately, your account registration was reviewed by an administrator and has been rejected. 
        You do not have access to this system.
      </p>
      <button onClick={handleLogout} style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
        Logout
      </button>
    </div>
  );
};

export default Rejected;
