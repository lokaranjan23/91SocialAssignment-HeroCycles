import axios from 'axios';

const projectAxios = axios.create({
  baseURL: import.meta.env.VITE_FINALPROJECT_URL
});

projectAxios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default projectAxios;
