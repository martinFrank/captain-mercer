import axios from "axios";

export const api = axios.create({ 
  baseURL: "https://elitegames.v6.rocks/fbwa-backend-api"
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
