import axios from 'axios'
import { useAuthStore } from '../store/authStore'
import { refreshToken } from './AuthenticationApis';
import Cookies from 'js-cookie';

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8000',
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 60000,
})

// Request Interceptor (Already exists in your code)
apiClient.interceptors.request.use((config) => {
    // 1. Grab the latest token from your Zustand store
    const token = Cookies.get("jwt_token");

    // 2. If the token exists, "stamp" it onto the request headers
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

// NEW: Response Interceptor for Silent Refresh
apiClient.interceptors.response.use(
    (response) => response, // Standard 2xx response
    async (error) => {
        const originalRequest = error.config;
        const requestUrl = String(originalRequest?.url || '')
        const isAuthRoute = requestUrl.startsWith('/Authentication/')
        const isRefreshCall = requestUrl.includes('/Authentication/RefreshToken')
        const hasRefreshToken = Boolean(Cookies.get('refresh_token'))

        // Only try silent refresh for non-auth routes when a refresh token exists.
        if (
            error.response?.status === 401 &&
            !originalRequest?._retry &&
            !isAuthRoute &&
            !isRefreshCall &&
            hasRefreshToken
        ) {
            originalRequest._retry = true; // Mark to prevent infinite loops

            try {
                const userData = await refreshToken();
                console.log(userData);
                
                // Update your Zustand store with new data/token
                useAuthStore.getState().setSession({ user: userData });

                // Update the header and retry the original request
                originalRequest.headers = originalRequest.headers || {}
                originalRequest.headers.Authorization = `Bearer ${userData.token}`;
                return apiClient(originalRequest);
            } catch (refreshError) {
                // If refreshing fails, the user must log in again
                useAuthStore.getState().logout();
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export { apiClient }
