import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthenticationService from '../../services/AuthenticationService';
import { AuthContext } from '../../context/AuthContext';
import { ROLES } from '../../utils/Roles';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const apiResponse = await AuthenticationService.login({ email, password });
      const user = apiResponse.data?.data || apiResponse.data; // Depending on backend wrapper structure
      
      if (user && user.token) {
        login(user);
        redirectBasedOnRoleAndStatus(user);
      } else {
        setError('Login failed. No token received.');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
    }
  };

  const redirectBasedOnRoleAndStatus = (user) => {
    if (user.status === 'PENDING') {
      navigate('/pending-approval');
      return;
    }
    if (user.status === 'REJECTED') {
      navigate('/rejected');
      return;
    }
    
    switch(user.role) {
      case ROLES.ADMIN:
        navigate('/admin');
        break;
      case ROLES.PRICING:
        navigate('/pricing');
        break;
      case ROLES.CONFIG:
        navigate('/config');
        break;
      case ROLES.SALES:
        navigate('/sales');
        break;
      default:
        navigate('/');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '4rem auto', padding: '2rem', border: '1px solid #ccc', borderRadius: '8px', fontFamily: 'sans-serif' }}>
      <h2 style={{ textAlign: 'center' }}>Login</h2>
      {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <div>
          <label>Email:</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }}/>
        </div>
        <div>
          <label>Password:</label>
          <div style={{ display: 'flex', alignItems: 'center', marginTop: '4px' }}>
            <input 
              type={showPassword ? "text" : "password"} 
              value={password} 
              onChange={e => setPassword(e.target.value)} 
              required 
              style={{ flex: 1, padding: '8px', boxSizing: 'border-box' }}
            />
            <button 
              type="button" 
              onClick={() => setShowPassword(!showPassword)}
              style={{ marginLeft: '8px', padding: '8px 12px', cursor: 'pointer', backgroundColor: '#f0f0f0', border: '1px solid #ccc', borderRadius: '4px' }}
            >
              {showPassword ? "Hide" : "Show"}
            </button>
          </div>
        </div>
        <button type="submit" style={{ padding: '10px', backgroundColor: '#007BFF', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>Login</button>
      </form>
      <div style={{ marginTop: '1rem', textAlign: 'center' }}>
        <Link to="/register">Don't have an account? Register here.</Link>
      </div>
    </div>
  );
};

export default Login;
