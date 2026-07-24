import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfigService from '../../services/ConfigService';

const CreateBikeConfiguration = () => {
  const navigate = useNavigate();
  const [bikeConfigurationName, setBikeConfigurationName] = useState('');
  
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  const handleSave = async (e) => {
    e.preventDefault();
    if (!bikeConfigurationName.trim()) {
      setError('Bike Configuration Name is required.');
      return;
    }

    setSaving(true);
    setError(null);
    setSuccessMsg('');
    
    try {
      const payload = {
        bikeConfigurationName: bikeConfigurationName.trim()
      };
      
      const response = await ConfigService.createBikeConfiguration(payload);
      
      setSuccessMsg(response.data?.message || 'Bike configuration created successfully.');
      setBikeConfigurationName('');
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create bike configuration.');
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    navigate('/config');
  };

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '600px',
    margin: '0 auto',
    padding: '2rem 0'
  };

  const formCardStyle = {
    backgroundColor: '#fff',
    padding: '2rem',
    borderRadius: '8px',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    marginTop: '1rem'
  };

  return (
    <div style={containerStyle}>
      <h2>Create Bike Configuration</h2>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      <div style={formCardStyle}>
        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Bike Configuration Name <span style={{color: 'red'}}>*</span></label>
            <input 
              type="text" 
              value={bikeConfigurationName} 
              onChange={(e) => setBikeConfigurationName(e.target.value)} 
              required
              disabled={saving}
              placeholder="Enter configuration name"
              style={{ width: '100%', padding: '10px', boxSizing: 'border-box', border: '1px solid #ccc', borderRadius: '4px' }}
            />
          </div>
          
          <div style={{ display: 'flex', justifyContent: 'flex-start', gap: '10px', marginTop: '1rem' }}>
            <button 
              type="submit" 
              disabled={saving || !bikeConfigurationName.trim()}
              style={{ padding: '10px 20px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: (saving || !bikeConfigurationName.trim()) ? 'not-allowed' : 'pointer' }}
            >
              {saving ? 'Saving...' : 'Save'}
            </button>
            <button 
              type="button" 
              onClick={handleCancel} 
              disabled={saving}
              style={{ padding: '10px 20px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: saving ? 'not-allowed' : 'pointer' }}
            >
              Cancel
            </button>
            <button 
              type="button" 
              onClick={() => navigate('/config')} 
              disabled={saving}
              style={{ padding: '10px 20px', backgroundColor: '#17a2b8', color: '#fff', border: 'none', borderRadius: '4px', cursor: saving ? 'not-allowed' : 'pointer', marginLeft: 'auto' }}
            >
              Back
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateBikeConfiguration;
