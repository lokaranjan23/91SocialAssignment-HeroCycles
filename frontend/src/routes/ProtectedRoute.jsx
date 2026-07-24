import React, { useContext } from 'react';
import { Outlet, Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { ROLES } from '../utils/Roles';

const ProtectedRoute = ({ allowedRoles }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) {
    return <div style={{ padding: '2rem', textAlign: 'center' }}>Loading...</div>;
  }

  if (!user || !user.token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user.role)) {
    // Redirect to their respective dashboards if role does not match
    switch(user.role) {
      case ROLES.ADMIN: return <Navigate to="/admin" replace />;
      case ROLES.PRICING: return <Navigate to="/pricing" replace />;
      case ROLES.CONFIG: return <Navigate to="/config" replace />;
      case ROLES.SALES: return <Navigate to="/sales" replace />;
      default: return <Navigate to="/login" replace />;
    }
  }

  return <Outlet />;
};

export default ProtectedRoute;
