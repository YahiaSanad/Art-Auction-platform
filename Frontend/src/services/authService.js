import * as authApi from '../api/AuthenticationApis';

// Login
export async function login({ email, password }) {
  // Calls the Login API and returns the user + tokens (processed via authDto)
  const userData = await authApi.login({ email, password });
  return {
    user: userData
  };
}

// Register buyer 
export async function registerBuyer(buyerData) {
  // Mapping frontend 'name' to backend 'fullName'
  const payload = {
    fullName: buyerData.fullName,
    email: buyerData.email,
    password: buyerData.password,
    country: buyerData.country,
    city: buyerData.city,
    address: buyerData.address,
    phoneNumber: buyerData.phoneNumber
  };

  const userData = await authApi.registerBuyer(payload);
  return userData;
}

// Artist registration 
export async function registerArtist(artistData) {
  // Mapping frontend 'name' to backend 'fullName'
  const payload = {
    fullName: artistData.fullName,
    email: artistData.email,
    password: artistData.password,
    country: artistData.country,
    city: artistData.city,
    phoneNumber: artistData.phoneNumber
  };

  const userData = await authApi.registerArtist(payload);
  return userData;
}

export async function logout() {
  // Clears cookies and local state
  await authApi.logout();
}

export async function refreshSession() {
  // Useful for app initialization to check if a user can stay logged in
  try {
    const userData = await authApi.refreshToken();
    return userData;
  } catch (error) {
    return null;
  }
}
