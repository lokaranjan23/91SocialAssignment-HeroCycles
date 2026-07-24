import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfigService from '../../services/ConfigService';
import LookupService from '../../services/LookupService';

const LinkAddOns = () => {
  const navigate = useNavigate();
  
  const [bikeConfigurationId, setBikeConfigurationId] = useState('');
  const [selectedAddOnIds, setSelectedAddOnIds] = useState([]);
  
  const [bikeConfigurations, setBikeConfigurations] = useState([]);
  const [availableAddOns, setAvailableAddOns] = useState([]);
  
  const [loadingData, setLoadingData] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  const fetchInitialData = async () => {
    setLoadingData(true);
    setError(null);
    try {
      // 1. Fetch configurations
      const configRes = await LookupService.getBikeConfigurations();
      const configs = configRes.data?.data || [];
      setBikeConfigurations(configs);
      if (configs.length > 0) {
        setBikeConfigurationId(configs[0].bikeConfigurationId); 
      }

      // 2. Fetch all add-ons for the checkbox list
      const addOnRes = await LookupService.getAllAddOns();
      setAvailableAddOns(addOnRes.data?.data || []);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load dropdown data. Please try again.');
    } finally {
      setLoadingData(false);
    }
  };

  useEffect(() => {
    fetchInitialData();
  }, []);

  const handleCheckboxChange = (addOnId) => {
    setSelectedAddOnIds(prev => {
      if (prev.includes(addOnId)) {
        return prev.filter(id => id !== addOnId);
      } else {
        return [...prev, addOnId];
      }
    });
  };

  const handleCancel = () => {
    setSelectedAddOnIds([]);
    if (bikeConfigurations.length > 0) {
      setBikeConfigurationId(bikeConfigurations[0].bikeConfigurationId);
    } else {
      setBikeConfigurationId('');
    }
    setError(null);
    setSuccessMsg('');
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!bikeConfigurationId) {
      setError('Bike Configuration is required.');
      return;
    }
    
    setSaving(true);
    setError(null);
    setSuccessMsg('');
    
    try {
      const payload = {
        bikeConfigurationId: Number(bikeConfigurationId),
        addOnIds: selectedAddOnIds
      };
      
      const response = await ConfigService.linkAddOns(payload);
      
      setSuccessMsg(response.data?.message || 'Add-ons linked successfully.');
      
      // Clear checkboxes after success
      setSelectedAddOnIds([]);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to link add-ons.');
    } finally {
      setSaving(false);
    }
  };

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '700px',
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
      <h2>Link Add-ons</h2>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      <div style={formCardStyle}>
        {loadingData ? (
          <p>Loading components...</p>
        ) : (
          <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1.2rem' }}>
            
            {/* Bike Configuration Dropdown */}
            <div>
              <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Bike Configuration <span style={{color: 'red'}}>*</span></label>
              {bikeConfigurations.length === 0 ? (
                <p style={{ margin: 0, color: 'red' }}>No configurations available.</p>
              ) : (
                <select 
                  value={bikeConfigurationId} 
                  onChange={(e) => setBikeConfigurationId(e.target.value)}
                  required
                  disabled={saving}
                  style={{ width: '100%', padding: '10px', boxSizing: 'border-box', border: '1px solid #ccc', borderRadius: '4px' }}
                >
                  <option value="" disabled>Select a configuration</option>
                  {bikeConfigurations.map(config => (
                    <option key={config.bikeConfigurationId} value={config.bikeConfigurationId}>
                      {config.bikeConfigurationName}
                    </option>
                  ))}
                </select>
              )}
            </div>
            
            {/* Available Add-ons List */}
            <div style={{ padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '4px', border: '1px solid #dee2e6' }}>
              <h4 style={{ marginTop: 0, marginBottom: '15px' }}>Available Add-ons</h4>
              {availableAddOns.length === 0 ? (
                <p style={{ margin: 0 }}>No add-ons available.</p>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
                  {availableAddOns.map(addon => (
                    <label key={addon.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                      <input 
                        type="checkbox"
                        checked={selectedAddOnIds.includes(addon.id)}
                        onChange={() => handleCheckboxChange(addon.id)}
                        disabled={saving}
                      />
                      {addon.name}
                    </label>
                  ))}
                </div>
              )}
            </div>
            
            <div style={{ display: 'flex', justifyContent: 'flex-start', gap: '10px', marginTop: '1rem' }}>
              <button 
                type="submit" 
                disabled={saving || !bikeConfigurationId || selectedAddOnIds.length === 0}
                style={{ padding: '10px 20px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: (saving || !bikeConfigurationId || selectedAddOnIds.length === 0) ? 'not-allowed' : 'pointer' }}
              >
                {saving ? 'Saving...' : 'Link Add-ons'}
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
        )}
      </div>
    </div>
  );
};

export default LinkAddOns;
