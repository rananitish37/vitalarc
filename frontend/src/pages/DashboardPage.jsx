import { useAuth } from '../context/AuthContext';

export default function DashboardPage() {
  const { user, logout } = useAuth();

  return (
    <div className="dashboard">
      <header>
        <h1>Welcome, {user?.displayName}</h1>
        <button onClick={logout}>Log out</button>
      </header>
      <p>Workout logging and training load coming next.</p>
    </div>
  );
}