import React from 'react';

export default function SidebarWidgets({ submissions }) {
  const total = submissions ? submissions.length : 0;

  // Category counts
  const countOf = (cat) => (submissions || []).filter(s => (s.category || '').toUpperCase() === cat.toUpperCase()).length;

  // Dynamic Leaderboard Formula: Score = Stars * 2 + (JudgeRating || 4.5) * 5 + (Completeness || 80) * 3
  const rankedSubmissions = [...(submissions || [])].map((s, idx) => {
    const stars = s.stars || 12;
    const rating = s.judgeRating || 4.5;
    const completeness = s.completionRate || 80;
    const score = Math.round(stars * 2 + rating * 5 + (completeness / 100) * 30);

    let badge = null;
    if (idx === 0) badge = '🥇 Innovation Award';
    else if (s.category?.toUpperCase() === 'AI') badge = '🏆 Best AI Project';
    else if (completeness >= 90) badge = '🚀 Fastest Build';

    return { ...s, score, badge };
  }).sort((a, b) => b.score - a.score).slice(0, 4);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* 📊 HACKFORGE ANALYTICS & TREND CHARTS */}
      <div className="glass-panel" style={{ padding: '20px' }}>
        <h3 style={{ fontSize: '1rem', fontWeight: '800', marginBottom: '14px', display: 'flex', alignItems: 'center', gap: '6px' }} className="gradient-text">
          📊 HackForge Analytics
        </h3>
        
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '10px', marginBottom: '16px' }}>
          <div style={{ background: 'var(--inner-bg)', padding: '12px', borderRadius: '10px', textAlign: 'center' }}>
            <div style={{ fontSize: '1.4rem', fontWeight: '800', color: 'var(--text-main)' }}>{total > 0 ? total : 48}</div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Total Submissions</div>
          </div>
          <div style={{ background: 'rgba(168, 85, 247, 0.15)', padding: '12px', borderRadius: '10px', textAlign: 'center', border: '1px solid rgba(168, 85, 247, 0.3)' }}>
            <div style={{ fontSize: '1.4rem', fontWeight: '800', color: '#a855f7' }}>{countOf('AI') || 18}</div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>AI Projects</div>
          </div>
        </div>

        {/* Visual Weekly Submission Trend Bar Chart */}
        <div style={{ background: 'var(--inner-bg)', padding: '12px', borderRadius: '10px', border: '1px solid var(--card-border)' }}>
          <div style={{ fontSize: '0.8rem', fontWeight: '700', marginBottom: '10px', color: 'var(--text-main)' }}>
            📈 Weekly Submission Trend
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '0.75rem' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '2px', color: 'var(--text-muted)' }}>
                <span>Week 1</span>
                <span>12 Projects</span>
              </div>
              <div style={{ background: 'rgba(255,255,255,0.1)', height: '8px', borderRadius: '4px', overflow: 'hidden' }}>
                <div style={{ width: '40%', background: '#3b82f6', height: '100%' }}></div>
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '2px', color: 'var(--text-muted)' }}>
                <span>Week 2</span>
                <span>24 Projects</span>
              </div>
              <div style={{ background: 'rgba(255,255,255,0.1)', height: '8px', borderRadius: '4px', overflow: 'hidden' }}>
                <div style={{ width: '70%', background: '#8b5cf6', height: '100%' }}></div>
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '2px', color: 'var(--text-muted)' }}>
                <span>Week 3 (Current)</span>
                <span>36 Projects</span>
              </div>
              <div style={{ background: 'rgba(255,255,255,0.1)', height: '8px', borderRadius: '4px', overflow: 'hidden' }}>
                <div style={{ width: '95%', background: '#ec4899', height: '100%' }}></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 🏆 FORMULA-DRIVEN LEADERBOARD */}
      <div className="glass-panel" style={{ padding: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
          <h3 style={{ fontSize: '1rem', fontWeight: '800', color: '#f97316', display: 'flex', alignItems: 'center', gap: '6px' }}>
            🏆 Dynamic Leaderboard
          </h3>
          <span style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>Formula Ranked</span>
        </div>

        <p style={{ fontSize: '0.725rem', color: 'var(--text-muted)', marginBottom: '12px', fontStyle: 'italic' }}>
          Score = Stars × 2 + Rating × 5 + Completeness × 3
        </p>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
          {rankedSubmissions.map((team, idx) => (
            <div
              key={idx}
              style={{
                background: idx === 0 ? 'rgba(249, 115, 22, 0.12)' : 'var(--inner-bg)',
                border: `1px solid ${idx === 0 ? 'rgba(249, 115, 22, 0.4)' : 'var(--card-border)'}`,
                padding: '12px',
                borderRadius: '10px',
                display: 'flex',
                flexDirection: 'column',
                gap: '4px'
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <span style={{ fontSize: '1rem', fontWeight: '800', color: idx === 0 ? '#f97316' : 'var(--text-main)' }}>
                    #{idx + 1}
                  </span>
                  <div>
                    <div style={{ fontWeight: '700', fontSize: '0.875rem', color: 'var(--text-main)' }}>{team.teamName}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{team.projectTitle}</div>
                  </div>
                </div>

                <div style={{ textAlign: 'right' }}>
                  <div style={{ fontSize: '0.9rem', fontWeight: '800', color: '#eab308' }}>{team.score} pts</div>
                  <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>⭐ {team.stars || 12} stars</div>
                </div>
              </div>

              {/* Award Badge */}
              {team.badge && (
                <div style={{ marginTop: '4px', fontSize: '0.725rem', fontWeight: '700', color: '#10b981', background: 'rgba(16, 185, 129, 0.15)', padding: '2px 8px', borderRadius: '12px', alignSelf: 'flex-start' }}>
                  {team.badge}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* 📜 HACKFORGE RULES */}
      <div className="glass-panel" style={{ padding: '20px' }}>
        <h3 style={{ fontSize: '1rem', fontWeight: '800', color: 'var(--secondary-accent)', marginBottom: '10px' }}>
          📜 HackForge Guidelines
        </h3>
        <ul style={{ paddingLeft: '16px', fontSize: '0.8rem', color: 'var(--text-muted)', display: 'flex', flexDirection: 'column', gap: '6px' }}>
          <li>Maximum 4 members per team.</li>
          <li>Must provide public GitHub repo with README.</li>
          <li>File limits: PPT ≤50MB, PDF ≤20MB, Video ≤200MB.</li>
          <li>Plagiarism checks strictly enforced.</li>
        </ul>
      </div>

    </div>
  );
}
