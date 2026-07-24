import React from 'react';
import { useNavigate } from 'react-router-dom';

const ConfigDashboard = () => {
  const navigate = useNavigate();

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '800px',
    margin: '0 auto',
    padding: '2rem 0'
  };

  const buttonStyle = {
    display: 'block',
    width: '100%',
    padding: '15px',
    marginBottom: '15px',
    fontSize: '16px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    textAlign: 'left'
  };

  const backButtonStyle = {
    ...buttonStyle,
    backgroundColor: '#6c757d',
    marginTop: '30px',
    display: 'none' // Removed as per instructions
  };

  return (
    <div style={containerStyle}>
      <h2>Configuration Management Dashboard</h2>
      <p style={{ marginBottom: '2rem', color: '#555' }}>
        Select a configuration action below.
      </p>
      
      <div>
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/config/create-configuration')}
        >
          Create Bike Configuration
        </button>
        
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/config/create-variant')}
        >
          Create Variant
        </button>
        
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/config/link-addons')}
        >
          Link Add-ons
        </button>
        
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/config/search-variants')}
        >
          Search Variants
        </button>
        
        <button 
          style={buttonStyle} 
          onClick={() => navigate('/config/update-variant-status')}
        >
          Activate / Deactivate Variant
        </button>
      </div>
    </div>
  );
};

export default ConfigDashboard;
