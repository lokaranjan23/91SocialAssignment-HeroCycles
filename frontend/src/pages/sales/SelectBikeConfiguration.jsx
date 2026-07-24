import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import LookupService from '../../services/LookupService';

const SelectBikeConfiguration = () => {
  const navigate = useNavigate();

  const [bikeConfigurations, setBikeConfigurations] = useState([]);
  const [selectedBikeConfigurationId, setSelectedBikeConfigurationId] = useState('');
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchConfigurations = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await LookupService.getBikeConfigurations();
        const configs = response.data?.data || [];
        setBikeConfigurations(configs);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load bike configurations.');
      } finally {
        setLoading(false);
      }
    };

    fetchConfigurations();
  }, []);

  const handleNext = (e) => {
    e.preventDefault();
    if (!selectedBikeConfigurationId) return;
    
    const selectedConfig = bikeConfigurations.find(
      config => String(config.bikeConfigurationId) === String(selectedBikeConfigurationId)
    );
    
    if (selectedConfig) {
      navigate('/sales/view-variants', {
        state: {
          bikeConfigurationId: selectedConfig.bikeConfigurationId,
          bikeConfigurationName: selectedConfig.bikeConfigurationName
        }
      });
    }
  };

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
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
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
    fontWeight: 'bold',
    textAlign: 'center'
  };

  const backButtonStyle = {
    ...buttonStyle,
    backgroundColor: '#6c757d',
    marginBottom: '0'
  };

  const inputStyle = {
    width: '100%',
    padding: '10px',
    border: '1px solid #ccc',
    borderRadius: '4px',
    fontSize: '16px',
    marginBottom: '20px'
  };

  const labelStyle = {
    display: 'block',
    marginBottom: '8px',
    fontWeight: 'bold',
    textAlign: 'left'
  };

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        <h2 style={{ marginTop: '0', marginBottom: '2rem', textAlign: 'center' }}>
          Select Bike Configuration
        </h2>

        {error && (
          <p style={{ color: 'red', backgroundColor: '#ffe6e6', padding: '10px', borderRadius: '4px', textAlign: 'left' }}>
            {error}
          </p>
        )}

        {loading ? (
          <p style={{ textAlign: 'center' }}>Loading bike configurations...</p>
        ) : (
          <form onSubmit={handleNext}>
            <div style={{ marginBottom: '20px' }}>
              <label style={labelStyle}>
                Bike Configuration <span style={{ color: 'red' }}>*</span>
              </label>
              
              {bikeConfigurations.length === 0 ? (
                <p style={{ color: 'red', margin: '0', textAlign: 'left' }}>No bike configurations available.</p>
              ) : (
                <select
                  style={inputStyle}
                  value={selectedBikeConfigurationId}
                  onChange={(e) => setSelectedBikeConfigurationId(e.target.value)}
                  required
                >
                  <option value="">-- Select Bike Configuration --</option>
                  {bikeConfigurations.map((config) => (
                    <option key={config.bikeConfigurationId} value={config.bikeConfigurationId}>
                      {config.bikeConfigurationName}
                    </option>
                  ))}
                </select>
              )}
            </div>

            <div style={{ marginTop: '30px' }}>
              <button 
                type="submit"
                style={{
                  ...buttonStyle,
                  opacity: (!selectedBikeConfigurationId || bikeConfigurations.length === 0) ? 0.6 : 1,
                  cursor: (!selectedBikeConfigurationId || bikeConfigurations.length === 0) ? 'not-allowed' : 'pointer'
                }}
                disabled={!selectedBikeConfigurationId || bikeConfigurations.length === 0}
              >
                Next
              </button>

              <button 
                type="button"
                style={backButtonStyle}
                onClick={() => navigate('/sales')}
              >
                Back
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default SelectBikeConfiguration;
