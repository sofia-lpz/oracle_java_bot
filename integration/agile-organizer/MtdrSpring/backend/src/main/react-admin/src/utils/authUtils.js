/**
 * Authentication Utilities
 * Contains functions for handling JWT token storage, retrieval, and authenticated requests
 */

// Get the stored token
export const getToken = () => {
    return localStorage.getItem('token');
  };
  
  // Check if user is authenticated
  export const isAuthenticated = () => {
    return !!getToken();
  };
  
  // Remove token and logout
  export const logout = () => {
    localStorage.removeItem('token');
    // Optionally redirect to login page
    window.location.href = '/login';
  };
  
  // Handle API requests with authentication
  export const authenticatedFetch = async (url, options = {}) => {
    const token = getToken();
    
    // If there's no token and this is a protected route
    if (!token && !url.includes('/auth/')) {
      // You might want to redirect to login or handle this case
      console.warn('Attempting to access protected resource without authentication');
    }
  
    // Create default headers if none provided
    const headers = options.headers || {};
    
    // Add authorization header if token exists
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
  
    // Create the final options with headers
    const finalOptions = {
      ...options,
      headers: {
        ...headers,
        'Content-Type': 'application/json',
      },
    };
  
    try {
      const response = await fetch(url, finalOptions);
      
      // Handle token expiration (401 Unauthorized)
      if (response.status === 401) {
        // Token might be expired or invalid
        console.warn('Authentication failed - token may be expired');
        logout();
        throw new Error('Authentication token expired');
      }
      
      return response;
    } catch (error) {
      console.error('Request failed:', error);
      throw error;
    }
  };