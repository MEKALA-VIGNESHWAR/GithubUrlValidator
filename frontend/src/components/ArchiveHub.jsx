import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/apiClient';

export default function ArchiveHub() {
  const [archivedSubmissions, setArchivedSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    fetchArchived();
  }, []);

  const fetchArchived = async () => {
    try {
      const data = await apiClient.get('/submissions');
      const list = Array.isArray(data) ? data : data.content || [];
      setArchivedSubmissions(list);
    } catch (err) {
      console.error('Failed to fetch archived submissions', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredItems = archivedSubmissions.filter(item => {
    const title = item.projectTitle || item.title || '';
    const team = item.teamName || '';
    return title.toLowerCase().includes(searchQuery.toLowerCase()) ||
           team.toLowerCase().includes(searchQuery.toLowerCase());
  });

  return (
    <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* --- HERO HEADER --- */}
      <div className="glass-panel glow-card-blue" style={{ padding: '28px', background: 'linear-gradient(135deg, rgba(15, 23, 42, 0.95), rgba(249, 115, 22, 0.12))' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
              <span style={{ fontSize: '1.2rem' }}>🏆</span>
              <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1.2px', color: 'var(--accent)', textTransform: 'uppercase' }}>
                HISTORICAL HACKATHON SHOWCASE & LEGACY HALL OF FAME
              </span>
            </div>
            <h1 style={{ fontSize: '2.2rem', fontWeight: '800', margin: '4px 0' }} className="gradient-text">
              Archived Events
            </h1>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
              Historical showcase of submitted project repositories and hackathon entries.
            </p>
          </div>
        </div>

        <div style={{ marginTop: '20px', paddingTop: '16px', borderTop: '1px solid var(--card-border)' }}>
          <input
            type="text"
            className="input-field"
            placeholder="🔍 Search approved archived projects by title or team name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{ maxWidth: '400px' }}
          />
        </div>
      </div>

      {/* --- ARCHIVED GRID OR EMPTY STATE --- */}
      {loading ? (
        <div className="glass-panel" style={{ padding: '48px', textAlign: 'center' }}>
          <span style={{ fontSize: '1.1rem', color: 'var(--text-secondary)' }}>Loading archive from server...</span>
        </div>
      ) : filteredItems.length === 0 ? (
        <div className="glass-panel" style={{ padding: '48px', textAlign: 'center', background: 'var(--inner-bg)', border: '1px solid var(--card-border)' }}>
          <span style={{ fontSize: '2.5rem' }}>🏆</span>
          <h3 style={{ fontSize: '1.3rem', fontWeight: '700', marginTop: '12px', color: 'var(--text-primary)' }}>No Archived Events Found</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.88rem', marginTop: '6px' }}>
            No project submissions match your query filter.
          </p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '24px' }}>
          {filteredItems.map((item) => (
            <div key={item.id} className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', gap: '20px' }}>
              <div>
                <div style={{ background: 'rgba(234, 179, 8, 0.15)', color: '#eab308', border: '1px solid rgba(234, 179, 8, 0.3)', padding: '4px 10px', borderRadius: '12px', fontSize: '0.75rem', fontWeight: '700', display: 'inline-block', marginBottom: '12px' }}>
                  🏆 Project Submission
                </div>

                <h3 style={{ fontSize: '1.25rem', fontWeight: '800', marginBottom: '4px' }}>{item.projectTitle}</h3>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-secondary)', marginBottom: '12px' }}>
                  Team: <strong style={{ color: 'var(--primary)' }}>{item.teamName}</strong>
                </div>

                <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', lineHeight: '1.4', marginBottom: '16px' }}>
                  {item.description || 'Verified hackathon project entry.'}
                </p>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '16px', borderTop: '1px solid var(--card-border)' }}>
                {item.githubUrl && (
                  <a href={item.githubUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary" style={{ flex: 1, textDecoration: 'none', textAlign: 'center', fontSize: '0.82rem' }}>
                    🔗 Code Repository
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
