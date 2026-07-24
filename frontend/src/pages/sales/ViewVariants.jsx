import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import LookupService from '../../services/LookupService';

const ViewVariants = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [variants, setVariants] = useState([]);
  const [selectedVariantId, setSelectedVariantId] = useState(location.state?.variantId || '');
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // If no state, bounce back
  const bikeConfigurationId = location.state?.bikeConfigurationId;
  const bikeConfigurationName = location.state?.bikeConfigurationName;

  useEffect(() => {
    if (!bikeConfigurationId) {
      navigate('/sales/select-configuration');
      return;
    }

    const fetchVariants = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await LookupService.getVariants(bikeConfigurationId);
        console.log("Fetched Variants from Backend:", response.data?.data); // Temporary debugging per user request
        setVariants(response.data?.data || []);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load variants.');
      } finally {
        setLoading(false);
      }
    };

    fetchVariants();
  }, [bikeConfigurationId, navigate]);

  const handleNext = (e) => {
    e.preventDefault();
    if (!selectedVariantId) return;
    
    const selectedVariant = variants.find(v => String(v.variantId) === String(selectedVariantId));
    
    if (selectedVariant) {
      navigate('/sales/view-addons', {
        state: {
          bikeConfigurationId: bikeConfigurationId,
          bikeConfigurationName: bikeConfigurationName,
          variantId: selectedVariant.variantId,
          variantName: selectedVariant.variantName,
          variantPrice: selectedVariant.variantPrice
        }
      });
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
  if (!bikeConfigurationId) {
    return null;
  }

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        <h2 style={{ marginTop: '0', marginBottom: '10px', textAlign: 'center' }}>
          View Variants
        </h2>
        <h4 style={{ marginTop: '0', marginBottom: '2rem', textAlign: 'center', color: '#555' }}>
          Selected Bike Configuration: <strong>{bikeConfigurationName}</strong>
        </h4>

        {error && (
          <p style={{ color: 'red', backgroundColor: '#ffe6e6', padding: '10px', borderRadius: '4px', textAlign: 'left' }}>
            {error}
          </p>
        )}

        {loading ? (
          <p style={{ textAlign: 'center' }}>Loading variants...</p>
        ) : (
          <form onSubmit={handleNext}>
            {variants.length === 0 ? (
              <p style={{ color: 'red', margin: '0 0 20px 0', textAlign: 'center' }}>
                No active variants available for this Bike Configuration.
              </p>
            ) : (
              <table style={tableStyle}>
                <thead>
                  <tr style={{ backgroundColor: '#f2f2f2' }}>
                    <th style={thTdStyle}>Variant Name</th>
                    <th style={thTdStyle}>Current Price</th>
                    <th style={{ ...thTdStyle, textAlign: 'center' }}>Select</th>
                  </tr>
                </thead>
                <tbody>
                  {variants.map(variant => (
                    <tr key={variant.variantId}>
                      <td style={thTdStyle}>{variant.variantName}</td>
                      <td style={thTdStyle}>{variant.variantPrice ?? 'N/A'}</td>
                      <td style={{ ...thTdStyle, textAlign: 'center' }}>
                        <input 
                          type="radio" 
                          name="selectedVariant" 
                          value={variant.variantId}
                          checked={String(selectedVariantId) === String(variant.variantId)}
                          onChange={() => setSelectedVariantId(variant.variantId)}
                          style={{ cursor: 'pointer' }}
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}

            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '30px' }}>
              <button 
                type="button"
                style={backButtonStyle}
                onClick={() => navigate('/sales/select-configuration')}
              >
                Back
              </button>

              <button 
                type="submit"
                style={{
                  ...buttonStyle,
                  opacity: (!selectedVariantId || variants.length === 0) ? 0.6 : 1,
                  cursor: (!selectedVariantId || variants.length === 0) ? 'not-allowed' : 'pointer'
                }}
                disabled={!selectedVariantId || variants.length === 0}
              >
                Next
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default ViewVariants;
