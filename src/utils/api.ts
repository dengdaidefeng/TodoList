import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端 API 地址
  timeout: 5000, // 超时时间
});

// 请求拦截器（可选：添加 Token）
api.interceptors.request.use(config => {
  return config;
}, error => {
  return Promise.reject(error);
});

// 响应拦截器（处理错误）
api.interceptors.response.use(response => {
  return response;
}, error => {
  console.error('API Error:', error.response?.data);
  return Promise.reject(error);
});

export default api;