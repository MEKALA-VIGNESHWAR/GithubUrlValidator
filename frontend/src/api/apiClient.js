const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api/v1';

async function request(endpoint, options = {}) {
  const token = localStorage.getItem('accessToken');
  
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    ...options.headers,
  };

  let response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers,
  });

  // If 401 Unauthorized, attempt refresh token rotation once
  if (response.status === 401 && localStorage.getItem('refreshToken')) {
    const refreshRes = await fetch(`${API_BASE}/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken: localStorage.getItem('refreshToken') })
    });

    if (refreshRes.ok) {
      const data = await refreshRes.json();
      localStorage.setItem('accessToken', data.accessToken);
      if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken);

      // Retry original request with new access token
      headers['Authorization'] = `Bearer ${data.accessToken}`;
      response = await fetch(`${API_BASE}${endpoint}`, {
        ...options,
        headers,
      });
    } else {
      localStorage.clear();
      window.location.href = '/';
      throw new Error('Session expired. Please log in again.');
    }
  }

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || `API Request Failed with status ${response.status}`);
  }

  if (response.status === 204) return null;
  return response.json();
}

export const apiClient = {
  get: (url, headers) => request(url, { method: 'GET', headers }),
  post: (url, body, headers) => request(url, { method: 'POST', body: JSON.stringify(body), headers }),
  put: (url, body, headers) => request(url, { method: 'PUT', body: JSON.stringify(body), headers }),
  patch: (url, body, headers) => request(url, { method: 'PATCH', body: JSON.stringify(body), headers }),
  delete: (url, headers) => request(url, { method: 'DELETE', headers }),
  scrapeEvents: (params) => request('/hackathons/scrape', { method: 'POST', body: JSON.stringify(params) }),
  importEvents: (events) => request('/hackathons/import', { method: 'POST', body: JSON.stringify(events) }),
};

