import { useState } from 'react';
import { sendChatMessage } from '../api/coach';

export default function AiCoachCard({ recommendation }) {
  const [message, setMessage] = useState('');
  const [reply, setReply] = useState('');
  const [isSending, setIsSending] = useState(false);

  async function handleSend(e) {
    e.preventDefault();
    if (!message.trim()) return;
    setIsSending(true);
    try {
      const response = await sendChatMessage(message);
      setReply(response.reply);
      setMessage('');
    } catch {
      setReply('The coach is unavailable right now. Try again shortly.');
    } finally {
      setIsSending(false);
    }
  }

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6">
      <h2 className="text-sm font-semibold text-slate-700 mb-4">AI Coach</h2>

      {recommendation && (
        <p className="text-sm text-slate-700 leading-relaxed mb-4 bg-slate-50 rounded-lg p-3">
          {recommendation.recommendationText}
        </p>
      )}

      {reply && (
        <p className="text-sm text-slate-700 leading-relaxed mb-4 bg-emerald-50 rounded-lg p-3">
          {reply}
        </p>
      )}

      <form onSubmit={handleSend} className="flex gap-2">
        <input
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Ask the coach a question..."
          className="flex-1 px-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none"
        />
        <button
          type="submit"
          disabled={isSending}
          className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 disabled:opacity-60 text-white font-medium rounded-lg text-sm transition-colors"
        >
          {isSending ? '...' : 'Send'}
        </button>
      </form>
    </div>
  );
}