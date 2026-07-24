import authAxios from '../api/authAxios';

const AdminService = {
  getPendingUsers: async () => {
    const response = await authAxios.get('/api/v1/admin/pending-users');
    return response;
  },
  approveUser: async (userId) => {
    const response = await authAxios.put(`/api/v1/admin/approve/${userId}`);
    return response;
  },
  rejectUser: async (userId) => {
    const response = await authAxios.put(`/api/v1/admin/reject/${userId}`);
    return response;
  },
  getApprovedUsers: async () => {
    const response = await authAxios.get('/api/v1/admin/approved-users');
    return response;
  },
  getBlockedUsers: async () => {
    const response = await authAxios.get('/api/v1/admin/blocked-users');
    return response;
  },
  blockUser: async (userId) => {
    const response = await authAxios.put(`/api/v1/admin/block/${userId}`);
    return response;
  },
  unblockUser: async (userId) => {
    const response = await authAxios.put(`/api/v1/admin/unblock/${userId}`);
    return response;
  }
};

export default AdminService;
