const ZONE_STYLES = {
  SWEET_SPOT: { label: 'Sweet spot', bg: 'bg-emerald-50', border: 'border-emerald-200', text: 'text-emerald-700', dot: 'bg-emerald-500' },
  CAUTION: { label: 'Caution', bg: 'bg-amber-50', border: 'border-amber-200', text: 'text-amber-700', dot: 'bg-amber-500' },
  HIGH_RISK: { label: 'High risk', bg: 'bg-rose-50', border: 'border-rose-200', text: 'text-rose-700', dot: 'bg-rose-500' },
  UNDERTRAINING: { label: 'Undertraining', bg: 'bg-sky-50', border: 'border-sky-200', text: 'text-sky-700', dot: 'bg-sky-500' },
  NO_DATA: { label: 'No data yet', bg: 'bg-slate-50', border: 'border-slate-200', text: 'text-slate-600', dot: 'bg-slate-400' },
};

export default function TrainingLoadCard({ trainingLoad }) {
  if (!trainingLoad) return null;

  const zone = ZONE_STYLES[trainingLoad.riskZone] || ZONE_STYLES.NO_DATA;

  return (
    <div className={`rounded-2xl border ${zone.border} ${zone.bg} p-6`}>
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-sm font-semibold text-slate-700">Training Load</h2>
        <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold ${zone.text} bg-white/60`}>
          <span className={`w-1.5 h-1.5 rounded-full ${zone.dot}`} />
          {zone.label}
        </span>
      </div>

      <div className="grid grid-cols-3 gap-4 text-center">
        <div>
          <p className="text-2xl font-extrabold text-slate-900">{trainingLoad.acwr.toFixed(2)}</p>
          <p className="text-xs font-medium text-slate-500 mt-0.5">ACWR</p>
        </div>
        <div>
          <p className="text-2xl font-extrabold text-slate-900">{trainingLoad.acuteLoad.toFixed(0)}</p>
          <p className="text-xs font-medium text-slate-500 mt-0.5">7-day load</p>
        </div>
        <div>
          <p className="text-2xl font-extrabold text-slate-900">{trainingLoad.chronicLoad.toFixed(0)}</p>
          <p className="text-xs font-medium text-slate-500 mt-0.5">28-day load</p>
        </div>
      </div>
    </div>
  );
}