import React from 'react';
import { useNavigate } from 'react-router-dom';

const SalesDashboard = () => {
  const navigate = useNavigate();

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '600px',
    margin: '0 auto',
    padding: '4rem 0'
  };

  const cardStyle = {
    backgroundColor: '#fff',
    padding: '3rem 2rem',
    borderRadius: '8px',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    textAlign: 'center'
  };

  const buttonStyle = {
    display: 'block',
    width: '100%',
    padding: '15px',
    marginBottom: '0',
    fontSize: '16px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontWeight: 'bold'
  };

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        <h2 style={{ marginTop: '0', marginBottom: '2rem' }}>SALES DASHBOARD</h2>
        
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/sales/select-configuration')}
        >
          Select Bike Configuration
        </button>
      </div>
    </div>
  );
};

export default SalesDashboard;
