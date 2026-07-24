import projectAxios from '../api/projectAxios';

const LookupService = {
  getBikeConfigurations: async () => {
    return await projectAxios.get('/api/v1/lookup/bike-configurations');
  },

  getPartCategories: async () => {
    return await projectAxios.get('/api/v1/lookup/partCategories');
  },

  getPartsByCategory: async (category) => {
    return await projectAxios.get(`/api/v1/lookup/parts/${category}`);
  },

  getAllParts: async () => {
    return await projectAxios.get('/api/v1/lookup/dropdowns/parts');
  },

  getAllAddOns: async () => {
    return await projectAxios.get('/api/v1/lookup/dropdowns/addons');
  },

  getVariants: async (bikeConfigId) => {
    return await projectAxios.get(`/api/v1/lookup/bike-configurations/${bikeConfigId}/variants`);
  },

  getValidAddOns: async (bikeConfigId) => {
    return await projectAxios.get(`/api/v1/lookup/bike-configurations/${bikeConfigId}/addons`);
  }
};

export default LookupService;
