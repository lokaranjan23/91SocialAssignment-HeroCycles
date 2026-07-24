import React, { useState, useEffect } from 'react';
import PricingService from '../../services/PricingService';

const EditAddOnModal = ({ isOpen, onClose, onSuccess, addOnToEdit }) => {
  const [name, setName] = useState('');
  
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    if (isOpen && addOnToEdit) {
      setName(addOnToEdit.name || '');
      setError(null);
      setSuccessMsg('');
    }
  }, [isOpen, addOnToEdit]);

  const handleSave = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Add-on Name is required.');
      return;
    }

    setSaving(true);
    setError(null);
    setSuccessMsg('');
    
    try {
      const payload = {
        name: name.trim()
      };
      
      const response = await PricingService.updateAddOn(addOnToEdit.id, payload);
      
      setSuccessMsg(response.data?.message || 'Add-on updated successfully.');
      
      // Refresh the updated data immediately
      onSuccess();
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update add-on.');
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
        <h3 style={{ marginTop: 0 }}>Edit Add-on</h3>
        
        {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
        {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
        
        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '5px' }}>Add-on Name <span style={{color: 'red'}}>*</span></label>
            <input 
              type="text" 
              value={name} 
              onChange={(e) => setName(e.target.value)} 
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
              disabled={saving || !name.trim()}
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

export default EditAddOnModal;
