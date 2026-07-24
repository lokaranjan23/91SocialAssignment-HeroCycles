import React, { useState, useEffect } from 'react';
import PricingService from '../../services/PricingService';

const UpdateAddOnPriceModal = ({ isOpen, onClose, onSuccess }) => {
  const [addOns, setAddOns] = useState([]);
  const [editedPrices, setEditedPrices] = useState({});
  const [effectiveFrom, setEffectiveFrom] = useState('');
  
  const [loadingData, setLoadingData] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    if (isOpen) {
      setError(null);
      setSuccessMsg('');
      setEditedPrices({});
      setSaving(false);
      
      // Default to tomorrow
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);
      setEffectiveFrom(tomorrow.toISOString().split('T')[0]);
      
      fetchAddOns();
    }
  }, [isOpen]);

  const fetchAddOns = async () => {
    setLoadingData(true);
    setError(null);
    try {
      const response = await PricingService.getPricingAddOns();
      setAddOns(response.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch add-ons.');
    } finally {
      setLoadingData(false);
    }
  };

  const handlePriceChange = (id, newPriceVal) => {
    setEditedPrices(prev => ({
      ...prev,
      [id]: newPriceVal
    }));
  };

  const handleSave = async () => {
    setError(null);
    setSuccessMsg('');
    
    if (!effectiveFrom) {
      setError('Effective From date is required.');
      return;
    }
    
    // Validate date is today or future
    const selectedDate = new Date(effectiveFrom);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (selectedDate < today) {
      setError('Effective From date must be today or a future date.');
      return;
    }

    const priceUpdates = Object.keys(editedPrices).reduce((acc, id) => {
      const parsedPrice = parseFloat(editedPrices[id]);
      if (!isNaN(parsedPrice)) {
        acc[id] = parsedPrice;
      }
      return acc;
    }, {});

    if (Object.keys(priceUpdates).length === 0) {
      setError('No valid price changes were detected. Modify at least one new price before saving.');
      return;
    }

    setSaving(true);
    
    try {
      const payload = {
        addOnPrices: priceUpdates,
        effectiveFrom: effectiveFrom
      };
      
      const response = await PricingService.updateAddOnPrices(payload);
      
      setSuccessMsg(response.data?.message || 'Prices updated successfully.');
      
      setTimeout(() => {
        onSuccess();
        onClose();
      }, 1500);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update add-on prices.');
      setSaving(false);
    }
  };

  if (!isOpen) return null;

  const modalOverlayStyle = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1000,
    fontFamily: 'sans-serif'
  };

  const modalContentStyle = {
    backgroundColor: '#fff',
    padding: '2rem',
    borderRadius: '8px',
    width: '900px',
    maxWidth: '95%',
    maxHeight: '90vh',
    display: 'flex',
    flexDirection: 'column',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
  };

  return (
    <div style={modalOverlayStyle}>
      <div style={modalContentStyle}>
        <h3 style={{ marginTop: 0 }}>Bulk Update Add-on Prices</h3>
        
        {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
        {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
        
        <div style={{ marginBottom: '1rem' }}>
          <label style={{ display: 'block', marginBottom: '5px' }}>Effective From <span style={{color: 'red'}}>*</span></label>
          <input 
            type="date" 
            value={effectiveFrom}
            onChange={(e) => setEffectiveFrom(e.target.value)}
            disabled={saving}
            style={{ padding: '8px', boxSizing: 'border-box' }}
          />
        </div>

        <div style={{ flex: 1, overflowY: 'auto', marginTop: '1rem' }}>
          {loadingData ? (
            <p>Loading add-ons...</p>
          ) : addOns.length === 0 ? (
            <p>No add-ons available to update.</p>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left', position: 'sticky', top: 0 }}>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Add-on Name</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Current Price</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>New Price</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Effective From</th>
                </tr>
              </thead>
              <tbody>
                {addOns.map(addon => (
                  <tr key={addon.id}>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.name}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.currentPrice}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                      <input 
                        type="number" 
                        step="0.01"
                        placeholder="Enter new price"
                        value={editedPrices[addon.id] || ''}
                        onChange={(e) => handlePriceChange(addon.id, e.target.value)}
                        disabled={saving}
                        style={{ padding: '6px', width: '100%', boxSizing: 'border-box' }}
                      />
                    </td>
                    <td style={{ padding: '12px', border: '1px solid #ddd', color: '#666' }}>
                      {addon.effectiveFrom || 'Pending Change'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '1.5rem', paddingTop: '1rem', borderTop: '1px solid #eee' }}>
          <button 
            type="button" 
            onClick={onClose} 
            disabled={saving}
            style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: saving ? 'not-allowed' : 'pointer' }}
          >
            Cancel
          </button>
          <button 
            type="button"
            onClick={handleSave}
            disabled={saving || loadingData || addOns.length === 0}
            style={{ padding: '8px 16px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: (saving || loadingData) ? 'not-allowed' : 'pointer' }}
          >
            {saving ? 'Saving...' : 'Save Prices'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default UpdateAddOnPriceModal;
