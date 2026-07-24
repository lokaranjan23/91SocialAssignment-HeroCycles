import React, { useState, useEffect } from 'react';
import AdminService from '../../services/AdminService';

const AdminDashboard = () => {
  const [pendingUsers, setPendingUsers] = useState([]);
  const [approvedUsers, setApprovedUsers] = useState([]);
  const [blockedUsers, setBlockedUsers] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  const fetchAllUsers = async () => {
    setLoading(true);
    setError(null);
    try {
      const [pendingRes, approvedRes, blockedRes] = await Promise.all([
        AdminService.getPendingUsers(),
        AdminService.getApprovedUsers(),
        AdminService.getBlockedUsers()
      ]);
      setPendingUsers(pendingRes.data?.data || []);
      setApprovedUsers(approvedRes.data?.data || []);
      setBlockedUsers(blockedRes.data?.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch users.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllUsers();
  }, []);

  const handleAction = async (actionFn, userId, confirmMessage, successMessageText) => {
    const confirm = window.confirm(confirmMessage);
    if (!confirm) return;
    
    setActionLoading(true);
    setSuccessMsg('');
    setError(null);
    try {
      const response = await actionFn(userId);
      setSuccessMsg(response.data?.message || successMessageText);
      await fetchAllUsers();
    } catch (err) {
      setError(err.response?.data?.message || 'Action failed.');
    } finally {
      setActionLoading(false);
    }
  };

  return (
    <div style={{ fontFamily: 'sans-serif' }}>
      
      {error && <p style={{ color: 'red', padding: '10px', backgroundColor: '#ffe6e6', borderRadius: '4px' }}>{error}</p>}
      {successMsg && <p style={{ color: 'green', padding: '10px', backgroundColor: '#e6ffe6', borderRadius: '4px' }}>{successMsg}</p>}
      
      {loading ? (
        <p>Loading users...</p>
      ) : (
        <>
          {/* PENDING USERS TABLE */}
          <h3>Pending Users</h3>
          {pendingUsers.length === 0 ? (
            <p>No pending registrations.</p>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '1rem', marginBottom: '2rem' }}>
              <thead>
                <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>ID</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Name</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Email</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Requested Role</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pendingUsers.map(user => (
                  <tr key={user.id}>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.id}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.name}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.email}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.role}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                      <button 
                        onClick={() => handleAction(AdminService.approveUser, user.id, "Approve this user?\n\nYes / No", "User approved successfully")} 
                        disabled={actionLoading}
                        style={{ padding: '6px 12px', marginRight: '8px', backgroundColor: '#28a745', color: '#fff', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                      >
                        Approve
                      </button>
                      <button 
                        onClick={() => handleAction(AdminService.rejectUser, user.id, "Reject this user?\n\nYes / No", "User rejected successfully")} 
                        disabled={actionLoading}
                        style={{ padding: '6px 12px', backgroundColor: '#dc3545', color: '#fff', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                      >
                        Reject
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}

          {/* APPROVED USERS TABLE */}
          <h3>Approved Users</h3>
          {approvedUsers.length === 0 ? (
            <p>No approved users.</p>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '1rem', marginBottom: '2rem' }}>
              <thead>
                <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>ID</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Name</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Email</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Role</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Status</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {approvedUsers.map(user => (
                  <tr key={user.id}>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.id}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.name}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.email}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.role}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.status}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                      <button 
                        onClick={() => handleAction(AdminService.blockUser, user.id, "Block this user?\n\nYes / No", "User blocked successfully")} 
                        disabled={actionLoading}
                        style={{ padding: '6px 12px', backgroundColor: '#ffc107', color: '#000', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                      >
                        Block
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}

          {/* BLOCKED USERS TABLE */}
          <h3>Blocked Users</h3>
          {blockedUsers.length === 0 ? (
            <p>No blocked users.</p>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '1rem', marginBottom: '2rem' }}>
              <thead>
                <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>ID</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Name</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Email</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Role</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Status</th>
                  <th style={{ padding: '12px', border: '1px solid #ddd' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {blockedUsers.map(user => (
                  <tr key={user.id}>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.id}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.name}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.email}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.role}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>{user.status}</td>
                    <td style={{ padding: '12px', border: '1px solid #ddd' }}>
                      <button 
                        onClick={() => handleAction(AdminService.unblockUser, user.id, "Unblock this user?\n\nYes / No", "User unblocked successfully")} 
                        disabled={actionLoading}
                        style={{ padding: '6px 12px', backgroundColor: '#17a2b8', color: '#fff', border: 'none', borderRadius: '4px', cursor: actionLoading ? 'not-allowed' : 'pointer' }}
                      >
                        Unblock
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
};

export default AdminDashboard;
