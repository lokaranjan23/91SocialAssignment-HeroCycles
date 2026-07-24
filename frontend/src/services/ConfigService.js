import projectAxios from '../api/projectAxios';

const ConfigService = {
  createBikeConfiguration: async (configData) => {
    return await projectAxios.post('/api/v1/config/bike-configurations', configData);
  },

  createVariant: async (variantData) => {
    return await projectAxios.post('/api/v1/config/variants', variantData);
  },

  linkAddOns: async (linkData) => {
    return await projectAxios.post('/api/v1/config/linkAddOns', linkData);
  },

  updateVariantStatus: async (variantId, statusData) => {
    return await projectAxios.put(`/api/v1/config/variants/${variantId}/status`, statusData);
  },

  searchVariants: async (keyword = '') => {
    return await projectAxios.get('/api/v1/config/variants/search', { params: { keyword } });
  }
};

export default ConfigService;
