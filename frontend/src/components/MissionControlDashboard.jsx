import React, { useState, useEffect } from 'react';

export default function MissionControlDashboard({ submissions, onUpdateStatus }) {
  const [currentRole, setCurrentRole] = useState('ORGANIZER');
  const [meDashboardData, setMeDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchMeDashboard();
  }, []);

  const fetchMeDashboard = async () => {
    try {
      const res = await fetch('/api/me/dashboard', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken') || ''}`
        }
      });
      if (res.ok) {
        const data = await res.json();
        setMeDashboardData(data);
      }
    } catch (err) {
      console.error('Failed to fetch dashboard data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleQuickApprove = (id) => {
    if (onUpdateStatus) {
      onUpdateStatus(id, 'APPROVED');
    }
  };

  const totalSubmissions = meDashboardData?.totalSubmissions || submissions?.length || 0;
  const approvedSubmissions = meDashboardData?.approvedProjectsCount || submissions?.filter(s => s.status === 'APPROVED')?.length || 0;
  const pendingSubmissions = submissions?.filter(s => s.status === 'PENDING')?.length || 0;
  const activeHackathonsCount = meDashboardData?.totalHackathons || 1;

  return (
    <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* --- TOP CONTROL BAR: EVENT OVERVIEW & ROLE SWITCHER --- */}
      <div className="glass-panel glow-card-cyan" style={{ padding: '28px', background: 'linear-gradient(135deg, rgba(2, 73, 80, 0.95), rgba(15, 164, 175, 0.18))' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
              <span className="live-pulse-dot"></span>
              <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1.2px', color: '#0FA4AF', textTransform: 'uppercase' }}>
                MISSION CONTROL • LIVE OPERATIONS
              </span>
              <span style={{ fontSize: '0.75rem', background: 'rgba(15, 164, 175, 0.25)', color: '#AFDDE5', padding: '2px 10px', borderRadius: '12px', fontWeight: '700' }}>
                LIVE PERSISTENCE
              </span>
            </div>

            <h1 style={{ fontSize: '2rem', fontWeight: '800', margin: '4px 0' }} className="gradient-text">
              HackForge Event Control Center
            </h1>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
              Organization: <strong style={{ color: 'var(--text-primary)' }}>HackForge Enterprise Foundation</strong> • Role Perspective: <strong style={{ color: 'var(--accent)' }}>{currentRole}</strong>
            </p>
          </div>

          {/* ROLE SELECTOR */}
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '12px' }}>
            <div style={{ display: 'flex', gap: '6px' }}>
              {['ORGANIZER', 'PARTICIPANT', 'JUDGE', 'MENTOR', 'SPONSOR'].map(role => (
                <button
                  key={role}
                  className={`role-pill ${currentRole === role ? 'active' : ''}`}
                  onClick={() => setCurrentRole(role)}
                >
                  {role}
                </button>
              ))}
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', background: 'rgba(0,0,0,0.3)', padding: '8px 16px', borderRadius: '12px', border: '1px solid var(--card-border)' }}>
              <span style={{ fontSize: '0.8rem', color: 'var(--accent)', fontWeight: '800' }}>⏳ PHASE DEADLINE:</span>
              <span style={{ fontSize: '1.1rem', fontWeight: '800', fontFamily: 'var(--font-mono)', color: '#ffffff' }}>04h : 18m : 32s</span>
            </div>
          </div>
        </div>
      </div>

      {/* --- SECTION 2: DATABASE-DRIVEN KPI CARDS BENTO --- */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '20px' }}>
        
        <div className="glass-panel" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)' }}>TOTAL HACKATHONS</span>
            <span style={{ fontSize: '1.2rem' }}>🌐</span>
          </div>
          <div style={{ fontSize: '2.2rem', fontWeight: '800', color: 'var(--text-primary)' }}>{activeHackathonsCount}</div>
          <div style={{ fontSize: '0.75rem', color: '#10b981', marginTop: '6px', fontWeight: '700' }}>Live in DB</div>
        </div>

        <div className="glass-panel glow-card-blue" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--primary)' }}>SUBMISSIONSPersisted</span>
            <span style={{ fontSize: '1.2rem' }}>📦</span>
          </div>
          <div style={{ fontSize: '2.2rem', fontWeight: '800', color: '#ffffff' }}>{totalSubmissions}</div>
          <div style={{ fontSize: '0.75rem', color: '#60a5fa', marginTop: '6px', fontWeight: '700' }}>Real DB Submissions</div>
        </div>

        <div className="glass-panel" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)' }}>APPROVED PROJECTS</span>
            <span style={{ fontSize: '1.2rem' }}>✅</span>
          </div>
          <div style={{ fontSize: '2.2rem', fontWeight: '800', color: '#10b981' }}>{approvedSubmissions}</div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: '6px' }}>Evaluated by Judges</div>
        </div>

        <div className="glass-panel glow-card-orange" style={{ padding: '22px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--accent)' }}>PENDING REVIEWS</span>
            <span style={{ fontSize: '1.2rem' }}>⏳</span>
          </div>
          <div style={{ fontSize: '2.2rem', fontWeight: '800', color: 'var(--accent)' }}>{pendingSubmissions}</div>
          <div style={{ fontSize: '0.75rem', color: '#fdba74', marginTop: '6px', fontWeight: '700' }}>Requires Review</div>
        </div>

      </div>

      {/* --- SECTION 3: DATABASE-DRIVEN ACTION CENTER --- */}
      <div className="glass-panel" style={{ padding: '26px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1px', color: 'var(--accent)', textTransform: 'uppercase' }}>
              ⚡ COMMAND CENTER
            </span>
            <h3 style={{ fontSize: '1.3rem', fontWeight: '800', marginTop: '4px' }}>Real Database Submissions</h3>
          </div>
          <span className="status-badge status-pending">{submissions?.length || 0} Total Items</span>
        </div>

        {/* Submissions List or Empty State */}
        {(!submissions || submissions.length === 0) ? (
          <div style={{ padding: '32px', textAlign: 'center', background: 'var(--inner-bg)', borderRadius: '12px', border: '1px solid var(--card-border)' }}>
            <h4 style={{ fontSize: '1.1rem', fontWeight: '700', color: 'var(--text-primary)', marginBottom: '6px' }}>
              No submissions found in database yet
            </h4>
            <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)' }}>
              Submissions submitted by participants will appear here in real-time.
            </p>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            {submissions.map((sub) => (
              <div
                key={sub.id}
                style={{
                  background: 'var(--inner-bg)',
                  border: '1px solid var(--card-border)',
                  borderRadius: '12px',
                  padding: '16px',
                  display: 'flex',
                  justify: 'space-between',
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  gap: '12px'
                }}
              >
                <div>
                  <div style={{ fontWeight: '700', fontSize: '0.95rem', color: 'var(--text-primary)' }}>
                    {sub.projectTitle}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                    Team: <strong style={{ color: 'var(--primary)' }}>{sub.teamName}</strong> • Submitted by: {sub.leaderName || 'Leader'}
                  </div>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <span className={`status-badge status-${sub.status?.toLowerCase() || 'pending'}`}>
                    {sub.status || 'PENDING'}
                  </span>
                  {sub.status !== 'APPROVED' && (
                    <button
                      onClick={() => handleQuickApprove(sub.id)}
                      className="btn-primary"
                      style={{ padding: '6px 12px', fontSize: '0.8rem' }}
                    >
                      ⚡ Quick Approve
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}
