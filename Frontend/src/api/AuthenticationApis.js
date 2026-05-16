// Imports
import { apiClient } from './ApiClient';
import Cookies from 'js-cookie';

const isHttps = typeof window !== 'undefined' && window.location.protocol === 'https:'
const baseCookieOptions = {
    sameSite: 'lax',
    secure: isHttps,
}

function extractErrorMessage(error, fallbackMessage) {
    const responseData = error?.response?.data
    if (typeof responseData === 'string' && responseData.trim().length > 0) return responseData
    if (responseData?.message) return responseData.message
    if (typeof error?.message === 'string' && error.message.trim().length > 0) return error.message
    return fallbackMessage
}

// Save data in cookie
const processAuthDto = (data) => {
    if (data?.token) {
        const jwtCookieOptions = { ...baseCookieOptions }
        const expiresOn = data?.expiresOn ? new Date(data.expiresOn) : null
        if (expiresOn instanceof Date && !Number.isNaN(expiresOn.getTime())) {
            jwtCookieOptions.expires = expiresOn
        }
        Cookies.set('jwt_token', data.token, jwtCookieOptions)
    }

    if (data?.refreshToken) {
        Cookies.set('refresh_token', data.refreshToken, {
            ...baseCookieOptions,
            expires: 30, // days
        })
    }

    // Return the user data to be stored in your Zustand/Redux store
    return {
        id: data?.id ?? data?.Id ?? null,
        name: data?.name ?? data?.fullName ?? '',
        email: data?.email ?? '',
        role: data?.role ?? '',
        adminId: data?.adminId ?? null,
        token: data?.token ?? '',
        refreshToken: data?.refreshToken ?? '',
        expiresOn: data?.expiresOn ?? null,
    };
};

// Buyer registration API
export const registerBuyer = async (buyerData) => {
    try {
        const response = await apiClient.post('/Authentication/BuyerRegistration', buyerData);
        return processAuthDto(response.data); 
    } catch (error) {
        throw new Error(extractErrorMessage(error, 'Registration failed'));
    }
};

// Artist registration API
export const registerArtist = async (artistData) => {
    try {
        const response = await apiClient.post('/Authentication/ArtistRegistration', artistData);
        return processAuthDto(response.data);
    } catch (error) {
        throw new Error(extractErrorMessage(error, 'Registration failed'));
    }
};

// Login API
export const login = async (loginData) => {
    try {
        const response = await apiClient.post('/Authentication/Login', loginData);
        return processAuthDto(response.data);
    } catch (error) {
        throw new Error(extractErrorMessage(error, 'Login failed'));
    }
};

// Refresh token API
export const refreshToken = async () => {
    try {
        const currentRefreshToken = Cookies.get('refresh_token');
        if (!currentRefreshToken) throw new Error('No refresh token found');

        // Sending raw string as JSON body for your C# [FromBody] string
        const response = await apiClient.post('/Authentication/RefreshToken', JSON.stringify(currentRefreshToken));
        return processAuthDto(response.data);
    } catch (error) {
        throw new Error(extractErrorMessage(error, 'Session expired'));
    }
};


// Logout 
export const logout = async () => {
    const currentRefreshToken = Cookies.get('refresh_token');
    try {
        if (currentRefreshToken) {
            await apiClient.post('/Authentication/Logout', { refreshToken: currentRefreshToken });
        }
    } finally {
        Cookies.remove('jwt_token');
        Cookies.remove('refresh_token');
    }
};
