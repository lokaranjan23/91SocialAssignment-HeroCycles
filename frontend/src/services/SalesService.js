import projectAxios from '../api/projectAxios';

const SalesService = {
  calculateTotalPrice: async (payload) => {
    return await projectAxios.post('/api/v1/pricing/calculateTotalPrice', payload);
  }
};

export default SalesService;
