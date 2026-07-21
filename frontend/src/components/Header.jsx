import React from 'react';

export default function Header({ totalCount, onRefresh, loading }) {
  return (
    <header className="glass-panel" style={{ padding: '24px 32px', marginBottom: '32px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <span style={{ fontSize: '1.8rem' }}>🚀</span>
            <h1 className="gradient-text" style={{ fontSize: '2rem', fontWeight: '800' }}>
              Hackathon Gateway
            </h1>
          </div>
          <p style={{ color: 'var(--text-muted)', marginTop: '4px', fontSize: '0.95rem' }}>
            Verify and submit GitHub repositories before the deadline
          </p>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div style={{
            background: 'rgba(59, 130, 246, 0.1)',
            border: '1px solid rgba(59, 130, 246, 0.3)',
            borderRadius: '20px',
            padding: '6px 16px',
            fontSize: '0.875rem',
            color: '#60a5fa',
            fontWeight: '600'
          }}>
            {totalCount} {totalCount === 1 ? 'Submission' : 'Submissions'}
          </div>

          <button
            onClick={onRefresh}
            disabled={loading}
            style={{
              background: 'rgba(255, 255, 255, 0.05)',
              border: '1px solid rgba(255, 255, 255, 0.1)',
              color: '#d1d5db',
              padding: '8px 14px',
              borderRadius: '8px',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
              fontSize: '0.875rem',
              transition: 'all 0.2s'
            }}
          >
            <span style={{ display: 'inline-block', transform: loading ? 'rotate(180deg)' : 'none', transition: 'transform 0.4s ease' }}>
              🔄
            </span>
            Refresh
          </button>
        </div>
      </div>
    </header>
  );
}
