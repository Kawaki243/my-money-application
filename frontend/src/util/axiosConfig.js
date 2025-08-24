import axios from "axios";
import { BASE_URL } from "./apiEndpoints";

// Create an Axios instance with default configuration
const axiosConfig = axios.create({
  baseURL: BASE_URL, // API root URL
  headers: {
    "Content-Type": "application/json", // Send JSON by default
    Accept: "application/json",         // Expect JSON in response
  },
});

// Endpoints that do not require authentication
const noAuthEndpoints = ["/login", "/register", "/status", "/activate", "/about"];

axiosConfig.interceptors.request.use(
  (config) => {
    // Check if current request matches any no-auth endpoint
    const skipToken = noAuthEndpoints.some((endpoint) =>
      config.url?.includes(endpoint)
    );

    // Attach token only if not in no-auth list
    if (!skipToken) {
      const accessToken = localStorage.getItem("token");
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);

axiosConfig.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      // Server responded but with error status
      const { status } = error.response;

      if (status === 401) {
        console.error("Unauthorized access - redirecting to login");
        localStorage.removeItem("token"); // Clear stale token
        window.location.href = "/login"; // Or use router navigation
      } else if (status === 500) {
        console.error("Server error - please try again later");
      }
    } else if (error.code === "ECONNABORTED") {
      // Timeout error
      console.error("Request timed out - please check your connection");
    } else {
      // Other unexpected network errors
      console.error("Network/Unknown error:", error.message);
    }

    return Promise.reject(error);
  }
);

export default axiosConfig;
