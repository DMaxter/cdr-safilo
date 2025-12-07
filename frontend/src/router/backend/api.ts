import axios from "axios";

export const http = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL
    ? import.meta.env.VITE_BACKEND_URL
    : "http://localhost:8080",
  timeout: 10000,
});

http.defaults.headers.post["Content-Type"] = "application/json";
http.defaults.withCredentials = true; // Send cookies
