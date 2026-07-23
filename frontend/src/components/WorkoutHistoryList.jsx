export default function WorkoutHistoryList({ workouts }) {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6">
      <h2 className="text-sm font-semibold text-slate-700 mb-4">Recent Workouts</h2>
      {workouts.length === 0 ? (
        <p className="text-sm text-slate-500">No workouts logged yet.</p>
      ) : (
        <ul className="divide-y divide-slate-100">
          {workouts.map((w) => (
            <li key={w.id} className="py-3 flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-900">{w.activityType}</p>
                <p className="text-xs text-slate-500">{w.workoutDate}</p>
              </div>
              <div className="text-right">
                <p className="text-sm font-semibold text-slate-900">{w.durationMinutes} min</p>
                <p className="text-xs text-slate-500">RPE {w.rpe} · load {w.load}</p>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}