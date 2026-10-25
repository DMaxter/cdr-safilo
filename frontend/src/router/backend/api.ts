import axios from "axios";

const DEV_BACKEND_URL = "http://localhost:8080";
const backendURL = import.meta.env.VITE_BACKEND_URL;

if (import.meta.env.PROD && !backendURL) {
  // Build mistake: without VITE_BACKEND_URL the bundle would call the user's own machine.
  console.error(
    "VITE_BACKEND_URL is not set. API requests will target localhost instead of the backend.",
  );
}

export const http = axios.create({
  baseURL: backendURL || DEV_BACKEND_URL,
  timeout: 10000,
});

http.defaults.headers.post["Content-Type"] = "application/json";
http.defaults.withCredentials = true; // Send cookies

type UnauthorizedHandler = () => void;

let unauthorizedHandler: UnauthorizedHandler | null = null;

export function onUnauthorized(handler: UnauthorizedHandler) {
  unauthorizedHandler = handler;
}

http.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      unauthorizedHandler?.();
    }

    return Promise.reject(error);
  },
);
