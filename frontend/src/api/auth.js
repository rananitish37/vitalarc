import client from './client';

export async function register(email, password, displayName) {
  const response = await client.post('/api/auth/register', { email, password, displayName });
  return response.data;
}

export async function login(email, password) {
  const response = await client.post('/api/auth/login', { email, password });
  return response.data;
}