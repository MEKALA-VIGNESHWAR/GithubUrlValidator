import React from 'react';

export default function SubmissionList({ submissions, loading, onDelete }) {
  if (loading && (!submissions || submissions.length === 0)) {
    return (
      <div className="glass-panel" style={{ padding: '40px', textAlign: 'center' }}>
        <p style={{ color: 'var(--text-muted)' }}>Loading live submissions...</p>
      </div>
    );
  }

  if (!submissions || submissions.length === 0) {
    return (
      <div className="glass-panel" style={{ padding: '40px', textAlign: 'center' }}>
        <p style={{ color: 'var(--text-muted)', fontStyle: 'italic' }}>
          No project submissions yet. Be the first team to submit!
        </p>
      </div>
    );
  }

  return (
    <div className="glass-panel" style={{ padding: '28px' }}>
      <h2 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '20px', color: '#c084fc' }}>
        🔥 Live Submissions
      </h2>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', maxHeight: '520px', overflowY: 'auto', paddingRight: '4px' }}>
        {submissions.map((sub) => (
          <div
            key={sub.id}
            className="animate-fade-in"
            style={{
              background: 'rgba(30, 41, 59, 0.6)',
              border: '1px solid rgba(255, 255, 255, 0.08)',
              borderRadius: '12px',
              padding: '16px 20px',
              display: 'flex',
              justifySpace: 'space-between',
              alignItems: 'center',
              flexWrap: 'wrap',
              gap: '12px',
              transition: 'border-color 0.2s'
            }}
          >
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: '700', color: '#ffffff' }}>
                {sub.projectTitle}
              </h3>
              <p style={{ color: '#9ca3af', fontSize: '0.875rem', marginTop: '2px' }}>
                Team: <span style={{ color: '#e5e7eb', fontWeight: '600' }}>{sub.teamName}</span>
              </p>
              <a
                href={sub.githubRepoUrl}
                target="_blank"
                rel="noopener noreferrer"
                style={{
                  color: '#60a5fa',
                  fontSize: '0.85rem',
                  textDecoration: 'none',
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: '4px',
                  marginTop: '6px'
                }}
              >
                🔗 View Repository
              </a>
            </div>

            <button
              onClick={() => onDelete(sub.id, sub.teamName)}
              style={{
                background: 'rgba(239, 68, 68, 0.1)',
                border: '1px solid rgba(239, 68, 68, 0.2)',
                color: '#f87171',
                padding: '8px 12px',
                borderRadius: '8px',
                cursor: 'pointer',
                fontSize: '0.85rem',
                fontWeight: '600',
                transition: 'all 0.2s'
              }}
            >
              🗑️ Delete
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
