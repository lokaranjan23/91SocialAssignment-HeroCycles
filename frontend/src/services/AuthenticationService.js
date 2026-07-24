import authAxios from '../api/authAxios';

const AuthenticationService = {
  login: async (credentials) => {
    const response = await authAxios.post('/api/v1/auth/login', credentials);
    return response;
  },
  
  register: async (userData) => {
    const response = await authAxios.post('/api/v1/auth/register', userData);
    return response;
  }
};

export default AuthenticationService;
