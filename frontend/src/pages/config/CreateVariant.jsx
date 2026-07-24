import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfigService from '../../services/ConfigService';
import LookupService from '../../services/LookupService';

const CreateVariant = () => {
  const navigate = useNavigate();
  
  const [name, setName] = useState('');
  const [bikeConfigId, setBikeConfigId] = useState('');
  const [selectedParts, setSelectedParts] = useState({}); // { categoryName: partId }
  
  const [bikeConfigurations, setBikeConfigurations] = useState([]);
  const [categories, setCategories] = useState([]);
  const [partsByCategory, setPartsByCategory] = useState({}); // { categoryName: [parts array] }
  
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
        setBikeConfigId(configs[0].bikeConfigurationId);
      }

      // 2. Fetch categories
      const categoryRes = await LookupService.getPartCategories();
      const cats = categoryRes.data?.data || [];
      // Backend returns either list of strings or objects {name: "FRAME"}
      const formattedCats = cats.map(c => c.category || c.name || c);
      setCategories(formattedCats);

      // 3. Fetch parts for each category concurrently
      const partsDict = {};
      const initialSelected = {};
      
      const partsPromises = formattedCats.map(async (cat) => {
        try {
          const partsRes = await LookupService.getPartsByCategory(cat);
          const partsList = partsRes.data?.data || [];
          partsDict[cat] = partsList;
          if (partsList.length > 0) {
            initialSelected[cat] = partsList[0].id;
          }
        } catch (catErr) {
          console.error(`Failed to fetch parts for category ${cat}:`, catErr);
          partsDict[cat] = [];
        }
      });
      
      await Promise.all(partsPromises);
      setPartsByCategory(partsDict);
      setSelectedParts(initialSelected);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load dropdown data. Please try again.');
    } finally {
      setLoadingData(false);
    }
  };

  useEffect(() => {
    fetchInitialData();
  }, []);

  const handlePartSelection = (category, partId) => {
    setSelectedParts(prev => ({
      ...prev,
      [category]: partId
    }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Variant Name is required.');
      return;
    }
    if (!bikeConfigId) {
      setError('Bike Configuration is required.');
      return;
    }

    // Validate that all categories have a selected part
    for (let cat of categories) {
      if (!selectedParts[cat]) {
        setError(`A part must be selected for category: ${cat}`);
        return;
      }
    }

    setSaving(true);
    setError(null);
    setSuccessMsg('');
    
    try {
      const partDtoArray = Object.values(selectedParts).map(id => ({
        partId: Number(id)
      }));

      const payload = {
        name: name.trim(),
        bikeConfigId: Number(bikeConfigId),
        partDto: partDtoArray
      };
      
      const response = await ConfigService.createVariant(payload);
      
      setSuccessMsg(response.data?.message || 'Variant created successfully.');
      
      // Clear form only after successful save
      setName('');
      if (bikeConfigurations.length > 0) {
        setBikeConfigId(bikeConfigurations[0].bikeConfigurationId);
      } else {
        setBikeConfigId('');
      }
      
      // Reset selections to defaults
      const resetSelected = {};
      categories.forEach(cat => {
        if (partsByCategory[cat]?.length > 0) {
          resetSelected[cat] = partsByCategory[cat][0].id;
        }
      });
      setSelectedParts(resetSelected);
      
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create variant.');
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
      <h2>Create Variant</h2>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      <div style={formCardStyle}>
        {loadingData ? (
          <p>Loading components...</p>
        ) : (
          <form onSubmit={handleSave} style={{ display: 'flex', flexDirection: 'column', gap: '1.2rem' }}>
            
            {/* Variant Name */}
            <div>
              <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Variant Name <span style={{color: 'red'}}>*</span></label>
              <input 
                type="text" 
                value={name} 
                onChange={(e) => setName(e.target.value)} 
                required
                disabled={saving}
                placeholder="Enter variant name"
                style={{ width: '100%', padding: '10px', boxSizing: 'border-box', border: '1px solid #ccc', borderRadius: '4px' }}
              />
            </div>
            
            {/* Bike Configuration Dropdown */}
            <div>
              <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Bike Configuration <span style={{color: 'red'}}>*</span></label>
              {bikeConfigurations.length === 0 ? (
                <p style={{ margin: 0, color: 'red' }}>No configurations available.</p>
              ) : (
                <select 
                  value={bikeConfigId} 
                  onChange={(e) => setBikeConfigId(e.target.value)}
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
            
            {/* Dynamic Part Category Dropdowns */}
            <div style={{ padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '4px', border: '1px solid #dee2e6' }}>
              <h4 style={{ marginTop: 0, marginBottom: '15px' }}>Parts Configuration</h4>
              {categories.length === 0 && <p>No part categories defined in system.</p>}
              
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
                {categories.map(cat => (
                  <div key={cat}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold', fontSize: '0.9rem' }}>
                      {cat} <span style={{color: 'red'}}>*</span>
                    </label>
                    <select 
                      value={selectedParts[cat] || ''} 
                      onChange={(e) => handlePartSelection(cat, e.target.value)}
                      required
                      disabled={saving || !partsByCategory[cat] || partsByCategory[cat].length === 0}
                      style={{ width: '100%', padding: '8px', boxSizing: 'border-box', border: '1px solid #ccc', borderRadius: '4px' }}
                    >
                      {(!partsByCategory[cat] || partsByCategory[cat].length === 0) ? (
                        <option value="">No parts found</option>
                      ) : (
                        partsByCategory[cat].map(part => (
                          <option key={part.id} value={part.id}>
                            {part.partName}
                          </option>
                        ))
                      )}
                    </select>
                  </div>
                ))}
              </div>
            </div>
            
            <div style={{ display: 'flex', justifyContent: 'flex-start', gap: '10px', marginTop: '1rem' }}>
              <button 
                type="submit" 
                disabled={saving || !name.trim() || !bikeConfigId || categories.length === 0}
                style={{ padding: '10px 20px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: (saving || !name.trim()) ? 'not-allowed' : 'pointer' }}
              >
                {saving ? 'Saving...' : 'Save Variant'}
              </button>
              <button 
                type="button" 
                onClick={() => navigate('/config')} 
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

export default CreateVariant;
