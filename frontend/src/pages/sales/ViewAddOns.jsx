import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import LookupService from '../../services/LookupService';
import SalesService from '../../services/SalesService';

const ViewAddOns = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [addOns, setAddOns] = useState([]);
  const [selectedAddOnIds, setSelectedAddOnIds] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [calculating, setCalculating] = useState(false);
  const [error, setError] = useState(null);
  const [totalPriceData, setTotalPriceData] = useState(null);

  // If no state, bounce back
  const bikeConfigurationId = location.state?.bikeConfigurationId;
  const bikeConfigurationName = location.state?.bikeConfigurationName;
  const variantId = location.state?.variantId;
  const variantName = location.state?.variantName;
  const variantPrice = location.state?.variantPrice;

  useEffect(() => {
    if (!bikeConfigurationId || !variantId) {
      navigate('/sales/view-variants');
      return;
    }

    const fetchAddOns = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await LookupService.getValidAddOns(bikeConfigurationId);
        setAddOns(response.data?.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load add-ons.');
      } finally {
        setLoading(false);
      }
    };

    fetchAddOns();
  }, [bikeConfigurationId, variantId, navigate]);

  const handleCheckboxChange = (id) => {
    setSelectedAddOnIds(prev => 
      prev.includes(id) ? prev.filter(item => item !== id) : [...prev, id]
    );
  };

  const handleCalculate = async () => {
    setCalculating(true);
    setError(null);
    setTotalPriceData(null);
    try {
      const payload = {
        variantId: Number(variantId),
        addonIds: selectedAddOnIds
      };
      
      const response = await SalesService.calculateTotalPrice(payload);
      setTotalPriceData(response.data?.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to calculate total price.');
    } finally {
      setCalculating(false);
    }
  };

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '800px',
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
    display: 'inline-block',
    width: '48%',
    padding: '15px',
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
    backgroundColor: '#6c757d'
  };

  const tableStyle = {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px',
    marginBottom: '20px'
  };

  const thTdStyle = {
    padding: '12px',
    border: '1px solid #ddd',
    textAlign: 'left'
  };

  // Prevent flicker during redirect
  if (!bikeConfigurationId || !variantId) {
    return null;
  }

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        <h2 style={{ marginTop: '0', marginBottom: '10px', textAlign: 'center' }}>
          View Valid Add-ons
        </h2>
        
        {/* We only show this default info if calculation hasn't run yet */}
        {!totalPriceData && (
          <div style={{ marginBottom: '2rem', textAlign: 'center', color: '#555' }}>
            <h4>Selected Bike Configuration: <strong>{bikeConfigurationName}</strong></h4>
            <h4>Selected Variant: <strong>{variantName}</strong></h4>
            <h4>Variant Price: <strong>{variantPrice}</strong></h4>
          </div>
        )}

        {error && (
          <p style={{ color: 'red', backgroundColor: '#ffe6e6', padding: '10px', borderRadius: '4px', textAlign: 'left' }}>
            {error}
          </p>
        )}

        {loading ? (
          <p style={{ textAlign: 'center' }}>Loading add-ons...</p>
        ) : (
          <>
            {addOns.length === 0 ? (
              <p style={{ textAlign: 'center' }}>No valid add-ons found for this configuration.</p>
            ) : (
              <table style={tableStyle}>
                <thead>
                  <tr style={{ backgroundColor: '#f2f2f2' }}>
                    <th style={{ ...thTdStyle, textAlign: 'center' }}>Select</th>
                    <th style={thTdStyle}>Add-on Name</th>
                    <th style={thTdStyle}>Add-on Price</th>
                  </tr>
                </thead>
                <tbody>
                  {addOns.map(addon => (
                    <tr key={addon.addonId}>
                      <td style={{ ...thTdStyle, textAlign: 'center' }}>
                        <input 
                          type="checkbox" 
                          value={addon.addonId}
                          checked={selectedAddOnIds.includes(addon.addonId)}
                          onChange={() => handleCheckboxChange(addon.addonId)}
                          style={{ cursor: 'pointer' }}
                          disabled={calculating}
                        />
                      </td>
                      <td style={thTdStyle}>{addon.addonName}</td>
                      <td style={thTdStyle}>{addon.addonPrice ?? 'N/A'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}

            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '30px' }}>
              <button 
                type="button"
                style={backButtonStyle}
                onClick={() => navigate('/sales/view-variants', {
                  state: { 
                    bikeConfigurationId, 
                    bikeConfigurationName,
                    variantId,
                    variantName,
                    variantPrice
                  }
                })}
                disabled={calculating}
              >
                Back
              </button>

              <button 
                type="button"
                style={{
                  ...buttonStyle,
                  backgroundColor: '#28a745',
                  opacity: calculating ? 0.6 : 1,
                  cursor: calculating ? 'not-allowed' : 'pointer'
                }}
                onClick={handleCalculate}
                disabled={calculating}
              >
                {calculating ? 'Calculating...' : 'Calculate Total Price'}
              </button>
            </div>

            {totalPriceData && (
              <div style={{ marginTop: '40px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px', backgroundColor: '#f9f9f9', textAlign: 'left' }}>
                <h3 style={{ marginTop: '0', marginBottom: '20px', textAlign: 'center' }}>Price Breakdown Result</h3>
                
                {/* Section 1 */}
                <h4 style={{ marginBottom: '5px' }}>Selected Bike Configuration</h4>
                <p style={{ marginTop: '0' }}>{bikeConfigurationName}</p>
                <hr style={{ margin: '15px 0' }} />

                {/* Section 2 */}
                <h4 style={{ marginBottom: '5px' }}>Selected Variant</h4>
                <p style={{ margin: '0' }}><strong>Name:</strong> {totalPriceData.variantName}</p>
                <p style={{ margin: '0 0 10px 0' }}><strong>Price:</strong> {totalPriceData.variantPrice}</p>
                <hr style={{ margin: '15px 0' }} />
                
                {/* Section 3 */}
                <h4 style={{ marginBottom: '5px' }}>Parts Breakdown</h4>
                {totalPriceData.partBreakdown && totalPriceData.partBreakdown.length > 0 ? (
                  <ul style={{ marginTop: '0' }}>
                    {totalPriceData.partBreakdown.map((part, index) => (
                      <li key={index}>
                        <strong>{part.category}:</strong> {part.partName} - {part.price}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p style={{ marginTop: '0' }}>No parts breakdown available.</p>
                )}
                <hr style={{ margin: '15px 0' }} />

                {/* Section 4 */}
                <h4 style={{ marginBottom: '5px' }}>Selected Add-ons</h4>
                {totalPriceData.addOns?.addons && totalPriceData.addOns.addons.length > 0 ? (
                  <ul style={{ marginTop: '0' }}>
                    {totalPriceData.addOns.addons.map((addon) => (
                      <li key={addon.id}>
                        {addon.name}: {addon.price}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p style={{ marginTop: '0', fontStyle: 'italic', color: '#666' }}>No add-ons selected.</p>
                )}
                <hr style={{ margin: '15px 0' }} />

                {/* Section 5 */}
                <h4 style={{ margin: '0 0 10px 0', color: '#333' }}>
                  Add-on Total: {totalPriceData.addOns?.totalPrice ?? 0}
                </h4>
                <hr style={{ margin: '15px 0' }} />
                
                {/* Section 6 */}
                <h2 style={{ margin: '0', color: '#007bff', textAlign: 'center' }}>
                  Grand Total: {totalPriceData.finalPrice}
                </h2>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default ViewAddOns;
