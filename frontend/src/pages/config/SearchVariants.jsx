import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfigService from '../../services/ConfigService';

const SearchVariants = () => {
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState('');
  const [variants, setVariants] = useState([]);
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchVariants = async (searchKeyword = '') => {
    setLoading(true);
    setError(null);
    try {
      const response = await ConfigService.searchVariants(searchKeyword);
      setVariants(response.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to search variants.');
      setVariants([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Initial fetch without keyword
    fetchVariants('');
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchVariants(keyword.trim());
  };

  const handleClear = () => {
    setKeyword('');
    fetchVariants('');
  };

  const containerStyle = {
    fontFamily: 'sans-serif',
    maxWidth: '900px',
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
        <h2>Search Variants</h2>
        <button 
          onClick={() => navigate('/config')} 
          style={{ padding: '8px 16px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
        >
          Back
        </button>
      </div>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      
      <div style={cardStyle}>
        <form onSubmit={handleSearch} style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
          <input 
            type="text" 
            placeholder="Search variants by name..." 
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            disabled={loading}
            style={{ flex: 1, padding: '10px', boxSizing: 'border-box', border: '1px solid #ccc', borderRadius: '4px' }}
          />
          <button 
            type="submit" 
            disabled={loading}
            style={{ padding: '10px 20px', backgroundColor: '#007bff', color: '#fff', border: 'none', borderRadius: '4px', cursor: loading ? 'not-allowed' : 'pointer' }}
          >
            {loading ? 'Searching...' : 'Search'}
          </button>
          <button 
            type="button" 
            onClick={handleClear}
            disabled={loading}
            style={{ padding: '10px 20px', backgroundColor: '#6c757d', color: '#fff', border: 'none', borderRadius: '4px', cursor: loading ? 'not-allowed' : 'pointer' }}
          >
            Clear
          </button>
        </form>

        {loading ? (
          <p>Loading...</p>
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
                  <th style={tableHeaderStyle}>Status</th>
                </tr>
              </thead>
              <tbody>
                {variants.map((variant) => (
                  <tr key={variant.id}>
                    <td style={tableCellStyle}>{variant.id}</td>
                    <td style={tableCellStyle}>{variant.name}</td>
                    <td style={tableCellStyle}>{variant.bikeConfigurationName}</td>
                    <td style={tableCellStyle}>
                      {variant.currentPrice ?? 'N/A'}
                    </td>
                    <td style={tableCellStyle}>{variant.status}</td>
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

export default SearchVariants;
