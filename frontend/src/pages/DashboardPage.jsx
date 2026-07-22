import { useAuth } from '../context/AuthContext';

export default function DashboardPage() {
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      {/* Top Navbar */}
      <header className="bg-white border-b border-slate-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 h-16 flex justify-between items-center">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 bg-emerald-600 rounded-xl flex items-center justify-center text-white font-bold shadow-sm">
              VA
            </div>
            <span className="font-bold text-lg tracking-tight text-slate-900">VitalArc</span>
          </div>

          <div className="flex items-center space-x-4">
            <span className="text-sm font-medium text-slate-700">
              Hello, <span className="text-slate-900 font-semibold">{user?.displayName}</span>
            </span>
            <button
              onClick={logout}
              className="px-3.5 py-2 text-xs font-semibold text-slate-700 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors cursor-pointer"
            >
              Log out
            </button>
          </div>
        </div>
      </header>

      {/* Main Dashboard Body */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="mb-8">
          <h1 className="text-2xl font-extrabold tracking-tight" style={{ color: '#000000' }}>
            Welcome, {user?.displayName}
          </h1>
          <p className="text-sm font-medium text-slate-600 mt-1">
            Monitor your workout load, recovery status, and daily progress.
          </p>
        </div>

        {/* Content Card */}
        <div className="bg-white rounded-2xl border border-slate-200 p-8 shadow-sm text-center">
          <h2 className="text-lg font-bold text-slate-900">Workout Logging & Training Load</h2>
          <p className="text-sm font-medium text-slate-600 mt-1 max-w-md mx-auto">
            Workout logging and training load coming next.
          </p>
        </div>
      </main>
    </div>
  );
}