import { useEffect, useState, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import { getHistory, getTrainingLoad } from '../api/workouts';
import { getRecommendation } from '../api/coach';
import TrainingLoadCard from '../components/TrainingLoadCard';
import LogWorkoutForm from '../components/LogWorkoutForm';
import WorkoutHistoryList from '../components/WorkoutHistoryList';
import AiCoachCard from '../components/AiCoachCard';

export default function DashboardPage() {
  const { user, logout } = useAuth();
  const [workouts, setWorkouts] = useState([]);
  const [trainingLoad, setTrainingLoad] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const loadData = useCallback(async () => {
    const [history, load] = await Promise.all([getHistory(), getTrainingLoad()]);
    setWorkouts(history);
    setTrainingLoad(load);
    try {
      setRecommendation(await getRecommendation());
    } catch {
      setRecommendation(null);
    }
  }, []);

  useEffect(() => {
    loadData().finally(() => setIsLoading(false));
  }, [loadData]);

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="bg-white border-b border-slate-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 h-16 flex justify-between items-center">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 bg-emerald-600 rounded-xl flex items-center justify-center text-white font-bold shadow-sm">
              VA
            </div>
            <span className="font-bold text-lg tracking-tight">VitalArc</span>
          </div>
          <div className="flex items-center space-x-4">
            <span className="text-sm font-medium text-slate-700">
              Hello, <span className="font-semibold">{user?.displayName}</span>
            </span>
            <button
              onClick={logout}
              className="px-3.5 py-2 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
            >
              Log out
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="mb-8">
          <h1 className="text-2xl font-extrabold tracking-tight">Welcome, {user?.displayName}</h1>
          <p className="text-sm font-medium text-slate-600 mt-1">
            Monitor your workout load, recovery status, and daily progress.
          </p>
        </div>

        {isLoading ? (
          <p className="text-sm text-slate-500">Loading your data...</p>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2 space-y-6">
              <TrainingLoadCard trainingLoad={trainingLoad} />
              <WorkoutHistoryList workouts={workouts} />
            </div>
            <div className="space-y-6">
              <LogWorkoutForm onLogged={loadData} />
              <AiCoachCard recommendation={recommendation} />
            </div>
          </div>
        )}
      </main>
    </div>
  );
}