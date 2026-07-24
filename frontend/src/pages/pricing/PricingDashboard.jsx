import React, { useState, useEffect } from 'react';
import PricingService from '../../services/PricingService';

import AddPartModal from './AddPartModal';
import AddAddOnModal from './AddAddOnModal';
import UpdatePartPriceModal from './UpdatePartPriceModal';
import UpdateAddOnPriceModal from './UpdateAddOnPriceModal';
import EditPartModal from './EditPartModal';
import EditAddOnModal from './EditAddOnModal';

const PricingDashboard = () => {
  const [parts, setParts] = useState([]);
  const [addOns, setAddOns] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');
  const [actionLoading, setActionLoading] = useState(false);

  const [partSearchKeyword, setPartSearchKeyword] = useState('');
  const [addOnSearchKeyword, setAddOnSearchKeyword] = useState('');

  // Modal Visibility States
  const [isAddPartModalOpen, setIsAddPartModalOpen] = useState(false);
  const [isAddAddOnModalOpen, setIsAddAddOnModalOpen] = useState(false);
  const [isUpdatePartPriceModalOpen, setIsUpdatePartPriceModalOpen] = useState(false);
  const [isUpdateAddOnPriceModalOpen, setIsUpdateAddOnPriceModalOpen] = useState(false);
  
  const [isEditPartModalOpen, setIsEditPartModalOpen] = useState(false);
  const [partToEdit, setPartToEdit] = useState(null);
  
  const [isEditAddOnModalOpen, setIsEditAddOnModalOpen] = useState(false);
  const [addOnToEdit, setAddOnToEdit] = useState(null);

  const fetchInitialData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [partsRes, addOnsRes] = await Promise.all([
        PricingService.getPricingParts(),
        PricingService.getPricingAddOns()
      ]);
      setParts(partsRes.data?.data || []);
      setAddOns(addOnsRes.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch pricing data.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInitialData();
  }, []);

  const handleAction = async (actionFn, id, confirmMessage, successMessageText, refreshFn) => {
    const confirmed = window.confirm(confirmMessage);
    if (!confirmed) return;
    
    setActionLoading(true);
    setSuccessMsg('');
    setError(null);
    try {
      const response = await actionFn(id);
      setSuccessMsg(response.data?.message || successMessageText);
      if (refreshFn) {
        await refreshFn();
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Action failed.');
    } finally {
      setActionLoading(false);
    }
  };

  const refreshParts = async () => {
    setActionLoading(true);
    try {
      const partsRes = await PricingService.getPricingParts();
      setParts(partsRes.data?.data || []);
      setPartSearchKeyword('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to refresh parts.');
    } finally {
      setActionLoading(false);
    }
  };

  const refreshAddOns = async () => {
    setActionLoading(true);
    try {
      const addOnsRes = await PricingService.getPricingAddOns();
      setAddOns(addOnsRes.data?.data || []);
      setAddOnSearchKeyword('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to refresh add-ons.');
    } finally {
      setActionLoading(false);
    }
  };

  const handleSearchParts = async () => {
    if (!partSearchKeyword.trim()) {
      return refreshParts();
    }
    setActionLoading(true);
    setError(null);
    try {
      const res = await PricingService.searchParts(partSearchKeyword);
      setParts(res.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to search parts.');
    } finally {
      setActionLoading(false);
    }
  };

  const handleSearchAddOns = async () => {
    if (!addOnSearchKeyword.trim()) {
      return refreshAddOns();
    }
    setActionLoading(true);
    setError(null);
    try {
      const res = await PricingService.searchAddOns(addOnSearchKeyword);
      setAddOns(res.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to search add-ons.');
    } finally {
      setActionLoading(false);
    }
  };

  // Modal Handlers
  const handleAddPart = () => setIsAddPartModalOpen(true);
  const handleUpdatePartPrices = () => setIsUpdatePartPriceModalOpen(true);
  const handleAddAddOn = () => setIsAddAddOnModalOpen(true);
  const handleUpdateAddOnPrices = () => setIsUpdateAddOnPriceModalOpen(true);

  // Edit Handlers
  const handleEditPart = (part) => {
    setPartToEdit(part);
    setIsEditPartModalOpen(true);
  };
  const handleEditAddOn = (addOn) => {
    setAddOnToEdit(addOn);
    setIsEditAddOnModalOpen(true);
  };

  return (
    <div style={{ fontFamily: 'sans-serif' }}>
      <h2>Pricing Management Dashboard</h2>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      {loading ? (
        <p>Loading pricing data...</p>
      ) : (
        <>
          {/* PARTS MANAGEMENT */}
          <div style={{ marginTop: '2rem' }}>
            <h3>Parts Management</h3>
            <div style={{ display: 'flex', gap: '10px', marginBottom: '1rem', alignItems: 'center' }}>
              <input 
                type="text" 
                placeholder="Search Parts..." 
                value={partSearchKeyword}
                onChange={(e) => setPartSearchKeyword(e.target.value)}
                style={{ padding: '8px', minWidth: '200px' }}
              />
              <button onClick={handleSearchParts} disabled={actionLoading} style={{ padding: '8px 12px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Search</button>
              <button onClick={refreshParts} disabled={actionLoading} style={{ padding: '8px 12px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Refresh</button>
              
              <div style={{ marginLeft: 'auto', display: 'flex', gap: '10px' }}>
                <button onClick={handleAddPart} disabled={actionLoading} style={{ padding: '8px 12px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Add Part</button>
                <button onClick={handleUpdatePartPrices} disabled={actionLoading} style={{ padding: '8px 12px', backgroundColor: '#6f42c1', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Bulk Update Prices</button>
              </div>
            </div>

            {parts.length === 0 ? (
              <p>No parts found.</p>
            ) : (
              <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                  <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Part Name</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Category</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Status</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Current Price</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>New Price</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Effective From</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {parts.map(part => (
                    <tr key={part.id}>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.partName}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.category}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.status}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.currentPrice}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.newPrice || 'N/A'}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{part.effectiveFrom || 'N/A'}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                        <button 
                          onClick={() => handleEditPart(part)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', marginRight: '5px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Edit
                        </button>
                        <button 
                          onClick={() => handleAction(PricingService.activatePart, part.id, "Activate this part?\n\nYes / No", "Part activated successfully", refreshParts)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', marginRight: '5px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Activate
                        </button>
                        <button 
                          onClick={() => handleAction(PricingService.deactivatePart, part.id, "Deactivate this part?\n\nYes / No", "Part deactivated successfully", refreshParts)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Deactivate
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>

          <hr style={{ margin: '3rem 0', border: '0', borderTop: '1px solid #ccc' }} />

          {/* ADD-ONS MANAGEMENT */}
          <div>
            <h3>Add-on Management</h3>
            <div style={{ display: 'flex', gap: '10px', marginBottom: '1rem', alignItems: 'center' }}>
              <input 
                type="text" 
                placeholder="Search Add-ons..." 
                value={addOnSearchKeyword}
                onChange={(e) => setAddOnSearchKeyword(e.target.value)}
                style={{ padding: '8px', minWidth: '200px' }}
              />
              <button onClick={handleSearchAddOns} disabled={actionLoading} style={{ padding: '8px 12px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Search</button>
              <button onClick={refreshAddOns} disabled={actionLoading} style={{ padding: '8px 12px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Refresh</button>
              
              <div style={{ marginLeft: 'auto', display: 'flex', gap: '10px' }}>
                <button onClick={handleAddAddOn} disabled={actionLoading} style={{ padding: '8px 12px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Add Add-on</button>
                <button onClick={handleUpdateAddOnPrices} disabled={actionLoading} style={{ padding: '8px 12px', backgroundColor: '#6f42c1', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}>Bulk Update Prices</button>
              </div>
            </div>

            {addOns.length === 0 ? (
              <p>No add-ons found.</p>
            ) : (
              <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                  <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Add-on Name</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Status</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Current Price</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>New Price</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Effective From</th>
                    <th style={{ padding: '12px', border: '1px solid #ddd' }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {addOns.map(addon => (
                    <tr key={addon.id}>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.name}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.status}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.currentPrice}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.newPrice || 'N/A'}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>{addon.effectiveFrom || 'N/A'}</td>
                      <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                        <button 
                          onClick={() => handleEditAddOn(addon)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', marginRight: '5px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Edit
                        </button>
                        <button 
                          onClick={() => handleAction(PricingService.activateAddOn, addon.id, "Activate this add-on?\n\nYes / No", "Add-on activated successfully", refreshAddOns)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', marginRight: '5px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Activate
                        </button>
                        <button 
                          onClick={() => handleAction(PricingService.deactivateAddOn, addon.id, "Deactivate this add-on?\n\nYes / No", "Add-on deactivated successfully", refreshAddOns)}
                          disabled={actionLoading}
                          style={{ padding: '4px 8px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                        >
                          Deactivate
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </>
      )}

      {/* MODALS */}
      <AddPartModal 
        isOpen={isAddPartModalOpen} 
        onClose={() => setIsAddPartModalOpen(false)} 
        onSuccess={() => {
          setSuccessMsg('Part added successfully.');
          refreshParts();
        }} 
      />
      
      <AddAddOnModal 
        isOpen={isAddAddOnModalOpen} 
        onClose={() => setIsAddAddOnModalOpen(false)} 
        onSuccess={() => {
          setSuccessMsg('Add-on added successfully.');
          refreshAddOns();
        }} 
      />
      
      <UpdatePartPriceModal 
        isOpen={isUpdatePartPriceModalOpen} 
        onClose={() => setIsUpdatePartPriceModalOpen(false)} 
        onSuccess={() => {
          setSuccessMsg('Part prices updated successfully.');
          refreshParts();
        }} 
      />
      
      <UpdateAddOnPriceModal 
        isOpen={isUpdateAddOnPriceModalOpen} 
        onClose={() => setIsUpdateAddOnPriceModalOpen(false)} 
        onSuccess={() => {
          setSuccessMsg('Add-on prices updated successfully.');
          refreshAddOns();
        }} 
      />
      
      <EditPartModal 
        isOpen={isEditPartModalOpen} 
        onClose={() => {
          setIsEditPartModalOpen(false);
          setPartToEdit(null);
        }} 
        onSuccess={() => {
          setSuccessMsg('Part updated successfully.');
          refreshParts();
        }} 
        partToEdit={partToEdit}
      />
      
      <EditAddOnModal 
        isOpen={isEditAddOnModalOpen} 
        onClose={() => {
          setIsEditAddOnModalOpen(false);
          setAddOnToEdit(null);
        }} 
        onSuccess={() => {
          setSuccessMsg('Add-on updated successfully.');
          refreshAddOns();
        }} 
        addOnToEdit={addOnToEdit}
      />
      
    </div>
  );
};

export default PricingDashboard;
