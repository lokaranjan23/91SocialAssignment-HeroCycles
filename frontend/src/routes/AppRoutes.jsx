import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import Layout from '../components/Layout';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';
import PendingApproval from '../pages/auth/PendingApproval';
import Rejected from '../pages/auth/Rejected';
import NotFound from '../pages/NotFound';
import AdminDashboard from '../pages/admin/AdminDashboard';
import PricingDashboard from '../pages/pricing/PricingDashboard';
import ConfigDashboard from '../pages/config/ConfigDashboard';
import CreateBikeConfiguration from '../pages/config/CreateBikeConfiguration';
import CreateVariant from '../pages/config/CreateVariant';
import LinkAddOns from '../pages/config/LinkAddOns';
import SearchVariants from '../pages/config/SearchVariants';
import UpdateVariantStatus from '../pages/config/UpdateVariantStatus';
import SalesDashboard from '../pages/sales/SalesDashboard';
import SelectBikeConfiguration from '../pages/sales/SelectBikeConfiguration';
import ViewVariants from '../pages/sales/ViewVariants';
import ViewAddOns from '../pages/sales/ViewAddOns';
import { ROLES } from '../utils/Roles';

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/pending-approval" element={<PendingApproval />} />
      <Route path="/rejected" element={<Rejected />} />
      
      <Route element={<Layout />}>
        <Route element={<ProtectedRoute allowedRoles={[ROLES.ADMIN]} />}>
          <Route path="/admin" element={<AdminDashboard />} />
        </Route>
        
        <Route element={<ProtectedRoute allowedRoles={[ROLES.PRICING]} />}>
          <Route path="/pricing" element={<PricingDashboard />} />
        </Route>

        <Route element={<ProtectedRoute allowedRoles={[ROLES.CONFIG]} />}>
          <Route path="/config" element={<ConfigDashboard />} />
          <Route path="/config/create-configuration" element={<CreateBikeConfiguration />} />
          <Route path="/config/create-variant" element={<CreateVariant />} />
          <Route path="/config/link-addons" element={<LinkAddOns />} />
          <Route path="/config/search-variants" element={<SearchVariants />} />
          <Route path="/config/update-variant-status" element={<UpdateVariantStatus />} />
        </Route>

        <Route element={<ProtectedRoute allowedRoles={[ROLES.SALES]} />}>
          <Route path="/sales" element={<SalesDashboard />} />
          <Route path="/sales/select-configuration" element={<SelectBikeConfiguration />} />
          <Route path="/sales/view-variants" element={<ViewVariants />} />
          <Route path="/sales/view-addons" element={<ViewAddOns />} />
        </Route>
      </Route>
      
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
