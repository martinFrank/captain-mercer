import axios from "axios";

export const api = axios.create({ 
  // baseURL: "https://elitegames.v6.rocks/fbwa-backend-api"
  baseURL: "http://localhost:8080/captain-mercer-api"
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
