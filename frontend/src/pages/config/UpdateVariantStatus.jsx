import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfigService from '../../services/ConfigService';

const UpdateVariantStatus = () => {
  const navigate = useNavigate();
  const [variants, setVariants] = useState([]);
  
  // Track the selected dropdown status for each variant row
  const [newStatuses, setNewStatuses] = useState({});
  
  const [loadingData, setLoadingData] = useState(false);
  const [savingId, setSavingId] = useState(null); // Track which row is currently saving
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  const fetchVariants = async () => {
    setLoadingData(true);
    setError(null);
    try {
      const response = await ConfigService.searchVariants('');
      const data = response.data?.data || [];
      setVariants(data);
      
      // Initialize the dropdowns to match the current status of each variant
      const initialStatuses = {};
      data.forEach(v => {
        initialStatuses[v.id] = v.status;
      });
      setNewStatuses(initialStatuses);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load variants.');
      setVariants([]);
    } finally {
      setLoadingData(false);
    }
  };

  useEffect(() => {
    fetchVariants();
  }, []);

  const handleStatusChange = (id, newStatus) => {
    setNewStatuses(prev => ({
      ...prev,
      [id]: newStatus
    }));
  };

  const handleUpdate = async (variantId) => {
    const statusToSave = newStatuses[variantId];
    
    // Optional client-side skip if status hasn't changed
    const currentVariant = variants.find(v => v.id === variantId);
    if (currentVariant && currentVariant.status === statusToSave) {
      setError(`Variant is already ${statusToSave}.`);
      setSuccessMsg('');
      return;
    }

    setSavingId(variantId);
    setError(null);
    setSuccessMsg('');
    
    try {
      const payload = { status: statusToSave };
      const response = await ConfigService.updateVariantStatus(variantId, payload);
      
      setSuccessMsg(response.data?.message || 'Variant status updated successfully.');
      
      // Refresh the table
      await fetchVariants();
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update variant status.');
    } finally {
      setSavingId(null);
    }
  };

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '1000px',
    margin: '0 auto',
    padding: '2rem 0'
  };

  const cardStyle = {
    backgroundColor: '#fff',
    padding: '2rem',
    borderRadius: '8px',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    marginTop: '1rem'
  };

  const tableHeaderStyle = {
    backgroundColor: '#f2f2f2',
    padding: '12px',
    border: '1px solid #ddd',
    textAlign: 'left'
  };

  const tableCellStyle = {
    padding: '12px',
    border: '1px solid #ddd'
  };

  return (
    <div style={containerStyle}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>Update Variant Status</h2>
        <button 
          onClick={() => navigate('/config')} 
          style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
        >
          Back
        </button>
      </div>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      <div style={cardStyle}>
        {loadingData ? (
          <p>Loading variants...</p>
        ) : variants.length === 0 ? (
          <p style={{ textAlign: 'center', color: '#666', padding: '20px' }}>No variants found.</p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr>
                  <th style={tableHeaderStyle}>ID</th>
                  <th style={tableHeaderStyle}>Variant Name</th>
                  <th style={tableHeaderStyle}>Bike Configuration</th>
                  <th style={tableHeaderStyle}>Current Price</th>
                  <th style={tableHeaderStyle}>Current Status</th>
                  <th style={tableHeaderStyle}>New Status</th>
                  <th style={tableHeaderStyle}>Action</th>
                </tr>
              </thead>
              <tbody>
                {variants.map((variant) => (
                  <tr key={variant.id}>
                    <td style={tableCellStyle}>{variant.id}</td>
                    <td style={tableCellStyle}>{variant.name}</td>
                    <td style={tableCellStyle}>{variant.bikeConfigurationName}</td>
                    <td style={tableCellStyle}>{variant.currentPrice ?? 'N/A'}</td>
                    <td style={tableCellStyle}>{variant.status}</td>
                    <td style={tableCellStyle}>
                      <select 
                        value={newStatuses[variant.id] || variant.status}
                        onChange={(e) => handleStatusChange(variant.id, e.target.value)}
                        disabled={savingId === variant.id}
                        style={{ padding: '6px', width: '100%', boxSizing: 'border-box' }}
                      >
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="INACTIVE">INACTIVE</option>
                      </select>
                    </td>
                    <td style={tableCellStyle}>
                      <button 
                        onClick={() => handleUpdate(variant.id)}
                        disabled={savingId === variant.id || newStatuses[variant.id] === variant.status}
                        style={{ 
                          padding: '6px 12px', 
                          backgroundColor: '#28a745', 
                          color: '#fff', 
                          border: 'none', 
                          borderRadius: '4px', 
                          cursor: (savingId === variant.id || newStatuses[variant.id] === variant.status) ? 'not-allowed' : 'pointer' 
                        }}
                      >
                        {savingId === variant.id ? 'Updating...' : 'Update'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default UpdateVariantStatus;
