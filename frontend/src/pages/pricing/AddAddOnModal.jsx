import React, { useState, useEffect } from 'react';
import PricingService from '../../services/PricingService';

const AddAddOnModal = ({ isOpen, onClose, onSuccess }) => {
  const [addOnName, setAddOnName] = useState('');
  const [currentPrice, setCurrentPrice] = useState('');
  
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    if (isOpen) {
      // Reset state when modal opens
      setAddOnName('');
      setCurrentPrice('');
      setError(null);
      setSuccessMsg('');
    }
  }, [isOpen]);

  const handleSave = async (e) => {
    e.preventDefault();
    if (!addOnName.trim()) {
      setError('Add-on Name is required.');
      return;
    }
    
    const parsedPrice = parseFloat(currentPrice);
    if (isNaN(parsedPrice) || parsedPrice <= 0) {
      setError('Current Price must be greater than 0.');
      return;
    }

    setSaving(true);
    setError(null);
    setSuccessMsg('');
    
    try {
      const payload = {
        addOnName: addOnName.trim(),
        currentPrice: parsedPrice
      };
      const response = await PricingService.addAddOn(payload);
      
      setSuccessMsg(response.data?.message || 'Add-on added successfully.');
      
      // Reset the form for the next entry
      setAddOnName('');
      setCurrentPrice('');
      
      // Refresh the parent dashboard table immediately
      onSuccess();
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save add-on.');
    } finally {
      // Ensure the button is always re-enabled regardless of success or failure
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
    width: '400px',
    maxWidth: '90%',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
  };

  return (
    <div style={modalOverlayStyle}>
      <div style={modalContentStyle}>
        <h3 style={{ marginTop: 0 }}>Add New Add-on</h3>
        
        {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
        {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
        
        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '5px' }}>Add-on Name <span style={{color: 'red'}}>*</span></label>
            <input 
              type="text" 
              value={addOnName} 
              onChange={(e) => setAddOnName(e.target.value)} 
              required
              disabled={saving}
              style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
            />
          </div>
          
          <div>
            <label style={{ display: 'block', marginBottom: '5px' }}>Current Price <span style={{color: 'red'}}>*</span></label>
            <input 
              type="number" 
              step="0.01"
              value={currentPrice} 
              onChange={(e) => setCurrentPrice(e.target.value)} 
              required
              disabled={saving}
              style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
            />
          </div>
          
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '1rem' }}>
            <button 
              type="button" 
              onClick={onClose} 
              disabled={saving}
              style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: saving ? 'not-allowed' : 'pointer' }}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              disabled={saving || !addOnName.trim() || currentPrice <= 0}
              style={{ padding: '8px 16px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: saving ? 'not-allowed' : 'pointer' }}
            >
              {saving ? 'Saving...' : 'Save'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddAddOnModal;
