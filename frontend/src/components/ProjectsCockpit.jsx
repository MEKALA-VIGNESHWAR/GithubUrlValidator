import React, { useState, useEffect } from 'react';

export default function ProjectsCockpit({ submissions }) {
  const [dbProjects, setDbProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeFilter, setActiveFilter] = useState('All');

  useEffect(() => {
    fetchMyProjects();
    fetchTasks();
  }, []);

  const fetchMyProjects = async () => {
    try {
      const res = await fetch('/api/me/projects');
      if (res.ok) {
        const data = await res.json();
        setDbProjects(Array.isArray(data) ? data : []);
      } else {
        setDbProjects(submissions || []);
      }
    } catch {
      setDbProjects(submissions || []);
    } finally {
      setLoading(false);
    }
  };

  const fetchTasks = async () => {
    try {
      const res = await fetch('/api/tasks');
      if (res.ok) {
        const data = await res.json();
        setTasks(Array.isArray(data) ? data : []);
      }
    } catch (err) {
      console.error('Failed to fetch tasks', err);
    }
  };

  const displayProjects = dbProjects.length > 0 ? dbProjects : submissions || [];

  const filteredProjects = displayProjects.filter(proj => {
    const title = proj.projectTitle || proj.title || '';
    const team = proj.teamName || '';
    const matchesSearch = title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          team.toLowerCase().includes(searchQuery.toLowerCase());
    if (activeFilter === 'All') return matchesSearch;
    if (activeFilter === 'Approved') return matchesSearch && proj.status === 'APPROVED';
    if (activeFilter === 'Pending') return matchesSearch && proj.status === 'PENDING';
    return matchesSearch;
  });

  return (
    <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* --- PAGE HEADER --- */}
      <div className="glass-panel glow-card-blue" style={{ padding: '28px', background: 'linear-gradient(135deg, rgba(15, 23, 42, 0.95), rgba(37, 99, 235, 0.14))' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
              <span style={{ fontSize: '1.2rem' }}>🚀</span>
              <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1.2px', color: '#60a5fa', textTransform: 'uppercase' }}>
                BUILDER WORKSPACE & COMMAND CENTER
              </span>
            </div>
            <h1 style={{ fontSize: '2.2rem', fontWeight: '800', margin: '4px 0' }} className="gradient-text">
              My Projects
            </h1>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
              Live database records of your registered hackathon projects and team submissions.
            </p>
          </div>
        </div>

        {/* SEARCH & FILTERS */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px', marginTop: '24px', paddingTop: '20px', borderTop: '1px solid var(--card-border)' }}>
          <input
            type="text"
            className="input-field"
            placeholder="🔍 Search projects in database by title or team name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{ maxWidth: '400px' }}
          />

          <div style={{ display: 'flex', gap: '8px' }}>
            {['All', 'Approved', 'Pending'].map(tab => (
              <button
                key={tab}
                className={`role-pill ${activeFilter === tab ? 'active' : ''}`}
                onClick={() => setActiveFilter(tab)}
              >
                {tab}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* --- QUICK METRICS --- */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
        <div className="glass-panel" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)' }}>MY PROJECTS IN DB</span>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', marginTop: '4px', color: 'var(--text-primary)' }}>{displayProjects.length}</div>
        </div>
        <div className="glass-panel glow-card-blue" style={{ padding: '18px' }}>
          <span style={{ fontSize: '0.75rem', fontWeight: '700', color: '#10b981' }}>APPROVED PROJECTS</span>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', marginTop: '4px', color: '#10b981' }}>
            {displayProjects.filter(p => p.status === 'APPROVED').length}
          </div>
        </div>
      </div>

      {/* --- PROJECTS BENTO GRID OR EMPTY STATE --- */}
      {filteredProjects.length === 0 ? (
        <div className="glass-panel" style={{ padding: '48px', textAlign: 'center', background: 'var(--inner-bg)', border: '1px solid var(--card-border)' }}>
          <span style={{ fontSize: '2.5rem' }}>📂</span>
          <h3 style={{ fontSize: '1.3rem', fontWeight: '700', marginTop: '12px', color: 'var(--text-primary)' }}>No Active Projects Yet</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.88rem', marginTop: '6px', maxWidth: '480px', margin: '6px auto 20px auto' }}>
            You haven't submitted any projects yet. Use the "Submit Project" tab to submit your team project to the database.
          </p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '24px' }}>
          {filteredProjects.map((proj) => (
            <div key={proj.id} className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', gap: '20px' }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                  <span className={`status-badge status-${proj.status?.toLowerCase() || 'pending'}`}>
                    {proj.status || 'PENDING'}
                  </span>
                  <span style={{ fontSize: '0.72rem', color: 'var(--text-secondary)' }}>ID #{proj.id}</span>
                </div>

                <h3 style={{ fontSize: '1.3rem', fontWeight: '800', marginBottom: '4px' }}>{proj.projectTitle || proj.title}</h3>
                <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '12px' }}>
                  Team: <strong style={{ color: 'var(--primary)' }}>{proj.teamName}</strong> • Leader: {proj.leaderName || 'Leader'}
                </div>

                <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', lineHeight: '1.4', marginBottom: '16px' }}>
                  {proj.description || 'Enterprise hackathon project submitted for evaluation.'}
                </p>

                {proj.githubRepoUrl && (
                  <div style={{ fontSize: '0.78rem', background: 'var(--inner-bg)', padding: '10px', borderRadius: '8px', border: '1px solid var(--card-border)', wordBreak: 'break-all' }}>
                    🐙 Repo: <a href={proj.githubRepoUrl} target="_blank" rel="noopener noreferrer" style={{ color: '#60a5fa', textDecoration: 'none' }}>{proj.githubRepoUrl}</a>
                  </div>
                )}
              </div>

              <div style={{ display: 'flex', gap: '10px', paddingTop: '16px', borderTop: '1px solid var(--card-border)' }}>
                {proj.githubRepoUrl && (
                  <a href={proj.githubRepoUrl} target="_blank" rel="noopener noreferrer" className="btn-primary" style={{ flex: 1, textDecoration: 'none', textAlign: 'center', padding: '8px' }}>
                    🔗 Open Repository
                  </a>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

    </div>
  );
}
