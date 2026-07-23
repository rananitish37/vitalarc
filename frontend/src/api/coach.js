import client from './client';

export async function getRecommendation() {
  const response = await client.get('/api/coach/recommendation');
  return response.data;
}

export async function sendChatMessage(message) {
  const response = await client.post('/api/coach/chat', { message });
  return response.data;
}