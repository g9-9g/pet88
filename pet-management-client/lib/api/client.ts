import axios, {
  AxiosError,
  InternalAxiosRequestConfig,
  AxiosResponse,
} from "axios";
import Cookies from "js-cookie";

const API_URL = process.env.NEXT_PUBLIC_API_ENDPOINT;

// Create public axios instance (no auth)
export const publicApi = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Create private axios instance (with auth)
export const privateApi = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // Enable sending cookies
});

// Add request interceptor to add auth token
privateApi.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = Cookies.get("auth_token");
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle common errors
privateApi.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Handle unauthorized access (e.g., redirect to login)
      Cookies.remove("token");
      window.location.href = "/sign-in";
    }
    return Promise.reject(error);
  }
);
