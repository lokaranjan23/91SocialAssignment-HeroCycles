import projectAxios from '../api/projectAxios';

const PricingService = {
  // Parts APIs
  getPricingParts: async () => {
    return await projectAxios.get('/api/v1/lookup/pricing/parts');
  },
  
  addPart: async (partData) => {
    return await projectAxios.post('/api/v1/pricing/parts', partData);
  },
  
  updatePart: async (partId, partData) => {
    return await projectAxios.put(`/api/v1/pricing/parts/${partId}`, partData);
  },
  
  activatePart: async (partId) => {
    return await projectAxios.put(`/api/v1/pricing/parts/${partId}/activate`);
  },
  
  deactivatePart: async (partId) => {
    return await projectAxios.put(`/api/v1/pricing/parts/${partId}/deactivate`);
  },
  
  searchParts: async (keyword) => {
    return await projectAxios.get('/api/v1/pricing/parts/search', {
      params: { keyword }
    });
  },
  
  updatePartPrices: async (priceUpdateData) => {
    return await projectAxios.put('/api/v1/pricing/parts/prices', priceUpdateData);
  },

  // Add-ons APIs
  getPricingAddOns: async () => {
    return await projectAxios.get('/api/v1/lookup/pricing/addons');
  },
  
  addAddOn: async (addOnData) => {
    return await projectAxios.post('/api/v1/pricing/addons', addOnData);
  },
  
  updateAddOn: async (addOnId, addOnData) => {
    return await projectAxios.put(`/api/v1/pricing/addons/${addOnId}`, addOnData);
  },
  
  activateAddOn: async (addOnId) => {
    return await projectAxios.put(`/api/v1/pricing/addons/${addOnId}/activate`);
  },
  
  deactivateAddOn: async (addOnId) => {
    return await projectAxios.put(`/api/v1/pricing/addons/${addOnId}/deactivate`);
  },
  
  searchAddOns: async (keyword) => {
    return await projectAxios.get('/api/v1/pricing/addons/search', {
      params: { keyword }
    });
  },
  
  updateAddOnPrices: async (priceUpdateData) => {
    return await projectAxios.put('/api/v1/pricing/addons/prices', priceUpdateData);
  },

  // Lookup APIs
  getPartCategories: async () => {
    return await projectAxios.get('/api/v1/lookup/partCategories');
  }
};

export default PricingService;
