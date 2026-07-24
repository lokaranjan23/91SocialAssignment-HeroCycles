import React, { useState, useEffect } from 'react';
import LookupService from '../../services/LookupService';
import PricingService from '../../services/PricingService';

const AddPartModal = ({ isOpen, onClose, onSuccess }) => {
  const [categories, setCategories] = useState([]);
  const [partName, setPartName] = useState('');
  const [category, setCategory] = useState('');
  const [currentPrice, setCurrentPrice] = useState('');
  
  const [loadingCategories, setLoadingCategories] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    if (isOpen) {
      // Reset state when modal opens
      setPartName('');
      setCategory('');
      setCurrentPrice('');
      setError(null);
      setSuccessMsg('');
      fetchCategories();
    }
  }, [isOpen]);

  const fetchCategories = async () => {
    setLoadingCategories(true);
    setError(null);
    try {
      const response = await LookupService.getPartCategories();
      setCategories(response.data?.data || []);
      // If categories exist, default select the first one
      if (response.data?.data?.length > 0) {
        setCategory(response.data.data[0].category);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch categories.');
    } finally {
      setLoadingCategories(false);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!partName.trim() || !category) {
      setError('Part Name and Category are required.');
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
        partName: partName.trim(),
        category: category,
        currentPrice: parsedPrice
      };
      const response = await PricingService.addPart(payload);
      
      setSuccessMsg(response.data?.message || 'Part added successfully.');
      
      // Reset the form for the next entry
      setPartName('');
      setCurrentPrice('');
      if (categories.length > 0) {
        setCategory(categories[0].category);
      }
      
      // Refresh the parent dashboard table
      onSuccess();
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save part.');
    } finally {
      // Ensure the button is always re-enabled regardless of success or failure
      setSaving(false);
    }
  };

  if (!isOpen) return null;

  // Simple modal overlay styling matching the plain CSS requirement
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
        <h3 style={{ marginTop: 0 }}>Add New Part</h3>
        
        {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
        {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
        
        <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <label style={{ display: 'block', marginBottom: '5px' }}>Part Name <span style={{color: 'red'}}>*</span></label>
            <input 
              type="text" 
              value={partName} 
              onChange={(e) => setPartName(e.target.value)} 
              required
              disabled={saving}
              style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
            />
          </div>
          
          <div>
            <label style={{ display: 'block', marginBottom: '5px' }}>Category <span style={{color: 'red'}}>*</span></label>
            {loadingCategories ? (
              <p style={{ margin: '5px 0', fontSize: '0.9rem' }}>Loading categories...</p>
            ) : (
              <select 
                value={category} 
                onChange={(e) => setCategory(e.target.value)}
                required
                disabled={saving || categories.length === 0}
                style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}
              >
                {categories.length === 0 && <option value="">No categories available</option>}
                {categories.map((item) => (
                  <option key={item.id} value={item.category}>{item.category}</option>
                ))}
              </select>
            )}
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
              disabled={saving || loadingCategories || categories.length === 0 || currentPrice <= 0}
              style={{ padding: '8px 16px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: (saving || loadingCategories) ? 'not-allowed' : 'pointer' }}
            >
              {saving ? 'Saving...' : 'Save'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddPartModal;
