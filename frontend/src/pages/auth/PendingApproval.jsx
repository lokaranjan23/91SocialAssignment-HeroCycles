import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

const PendingApproval = () => {
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div style={{ padding: '2rem', textAlign: 'center', fontFamily: 'sans-serif' }}>
      <h2>Registration Pending</h2>
      <p style={{ marginTop: '1rem', marginBottom: '2rem' }}>
        Your account registration has been submitted and is currently awaiting approval from an administrator. 
        You will not be able to access the dashboard until your account is approved.
      </p>
      <button onClick={handleLogout} style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
        Logout
      </button>
    </div>
  );
};

export default PendingApproval;
