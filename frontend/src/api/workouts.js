import client from './client';

export async function logWorkout(workoutDate, activityType, durationMinutes, rpe) {
  const response = await client.post('/api/workouts', { workoutDate, activityType, durationMinutes, rpe });
  return response.data;
}

export async function getHistory() {
  const response = await client.get('/api/workouts');
  return response.data;
}

export async function getTrainingLoad() {
  const response = await client.get('/api/workouts/training-load');
  return response.data;
}