async function apiRequest(endpoint, method = 'GET', data = null, auth = true) {
    const headers = { 'Content-Type': 'application/json' };
  
    if (auth) {
      const token = localStorage.getItem('authToken');
      if (token) headers['Authorization'] = `Bearer ${token}`;
    }
  
    try {
      const response = await axios({
        method,
        url: `${CONFIG.API_BASE_URL}${endpoint}`,
        data,
        headers
      });
      return response.data;
    } catch (error) {
      console.error('API Error:', error.response?.data || error.message);
      throw error;
    }
  }
  