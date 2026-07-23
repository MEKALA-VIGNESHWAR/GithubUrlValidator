import React, { useState } from 'react';

export default function SubmissionList({ submissions, loading, onDelete, onUpdateSubmission }) {
  const [editingSub, setEditingSub] = useState(null);

  if (loading && (!submissions || submissions.length === 0)) {
    return (
      <div className="glass-panel" style={{ padding: '40px', textAlign: 'center' }}>
        <p style={{ color: 'var(--text-muted)' }}>⚡ Loading live submissions from database...</p>
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

  const getCategoryTagClass = (cat) => {
    const c = cat ? cat.toLowerCase().replace(/\s+/g, '') : 'general';
    return `tag-${c}`;
  };

  const getStatusBadge = (status) => {
    switch (status?.toUpperCase()) {
      case 'APPROVED':
        return <span className="status-badge status-approved">🟢 Approved</span>;
      case 'REJECTED':
        return <span className="status-badge status-rejected">🔴 Rejected</span>;
      case 'CHANGES_REQUESTED':
        return <span className="status-badge status-changes">🟧 Request Changes</span>;
      default:
        return <span className="status-badge status-pending">🟡 Pending</span>;
    }
  };

  return (
    <div className="glass-panel" style={{ padding: '28px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2 style={{ fontSize: '1.25rem', fontWeight: '800' }} className="gradient-text">
          🔥 Submission Feed ({submissions.length})
        </h2>
        <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Live Backend Feed</span>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px', maxHeight: '780px', overflowY: 'auto', paddingRight: '4px' }}>
        {submissions.map((sub) => (
          <div
            key={sub.id}
            className="animate-fade-in"
            style={{
              background: 'var(--inner-bg)',
              border: '1px solid var(--card-border)',
              borderRadius: '14px',
              padding: '18px',
              display: 'flex',
              flexDirection: 'column',
              gap: '12px'
            }}
          >
            {/* Header: Title, Category & Status */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '8px' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                  <h3 style={{ fontSize: '1.1rem', fontWeight: '800', color: 'var(--text-main)' }}>
                    {sub.projectTitle}
                  </h3>
                  <span className={`category-pill ${getCategoryTagClass(sub.category)}`}>
                    {sub.category || 'General'}
                  </span>
                  {sub.difficulty && (
                    <span style={{ fontSize: '0.7rem', background: 'rgba(255,255,255,0.08)', padding: '2px 6px', borderRadius: '4px', color: 'var(--text-muted)' }}>
                      {sub.difficulty}
                    </span>
                  )}
                </div>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
                  Team: <strong style={{ color: 'var(--primary-accent)' }}>{sub.teamName}</strong>
                  {sub.leaderName && <span> • Leader: {sub.leaderName}</span>}
                  {sub.college && <span> ({sub.college})</span>}
                </p>
              </div>
              <div>{getStatusBadge(sub.status)}</div>
            </div>

            {/* Description & Problem Statement */}
            {sub.description && (
              <p style={{ color: 'var(--text-main)', fontSize: '0.85rem', opacity: 0.9 }}>
                {sub.description}
              </p>
            )}

            {/* Completion Progress Bar */}
            <div style={{ background: 'var(--card-bg)', padding: '8px 12px', borderRadius: '8px', border: '1px solid var(--card-border)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: '4px' }}>
                <span>Project Completion</span>
                <strong style={{ color: 'var(--primary-accent)' }}>{sub.completionRate || 80}%</strong>
              </div>
              <div style={{ background: 'rgba(255,255,255,0.1)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
                <div style={{ width: `${sub.completionRate || 80}%`, background: 'linear-gradient(90deg, #3b82f6, #8b5cf6)', height: '100%' }}></div>
              </div>
            </div>

            {/* Team Members */}
            {sub.members && (
              <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                <strong style={{ color: 'var(--text-main)' }}>Members:</strong> {sub.members}
              </div>
            )}

            {/* Judge Comment if exists */}
            {sub.judgeComment && (
              <div style={{ background: 'rgba(234, 179, 8, 0.1)', border: '1px solid rgba(234, 179, 8, 0.3)', padding: '10px', borderRadius: '8px', fontSize: '0.8rem', color: '#eab308' }}>
                💬 <strong>Judge Feedback:</strong> {sub.judgeComment}
              </div>
            )}

            {/* GitHub Stats Row */}
            <div style={{ display: 'flex', alignItems: 'center', gap: '14px', background: 'var(--card-bg)', padding: '8px 12px', borderRadius: '8px', fontSize: '0.8rem', color: 'var(--text-muted)', flexWrap: 'wrap' }}>
              <span>⭐ <strong>{sub.stars || 12}</strong> Stars</span>
              <span>🍴 <strong>{sub.forks || 5}</strong> Forks</span>
              <span>🕒 Last Commit: <strong>{sub.lastCommitDate || 'Recently'}</strong></span>
              {sub.techStack && <span style={{ color: 'var(--secondary-accent)' }}>🛠️ {sub.techStack}</span>}
            </div>

            {/* Action Footer */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '10px', marginTop: '4px' }}>
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                {sub.githubRepoUrl && (
                  <a href={sub.githubRepoUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary" style={{ color: 'var(--primary-accent)' }}>
                    🔗 GitHub
                  </a>
                )}
                {sub.pptUrl && <a href={sub.pptUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary">📊 PPT</a>}
                {sub.pdfUrl && <a href={sub.pdfUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary">📄 PDF</a>}
                {sub.demoVideoUrl && <a href={sub.demoVideoUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary">🎥 Video</a>}
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <button
                  onClick={() => setEditingSub(sub)}
                  className="btn-secondary"
                  style={{ fontSize: '0.8rem' }}
                >
                  ✏️ Edit
                </button>
                <button
                  onClick={() => onDelete(sub.id, sub.teamName)}
                  style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid rgba(239, 68, 68, 0.25)', color: '#ef4444', padding: '6px 10px', borderRadius: '8px', cursor: 'pointer', fontSize: '0.8rem', fontWeight: '600' }}
                >
                  🗑️ Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* --- EDIT SUBMISSION MODAL --- */}
      {editingSub && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.7)', backdropFilter: 'blur(6px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000, padding: '16px' }}>
          <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '500px', padding: '24px' }}>
            <h3 style={{ fontSize: '1.2rem', fontWeight: '800', marginBottom: '14px' }}>✏️ Edit Project: {editingSub.projectTitle}</h3>
            
            <form onSubmit={(e) => {
              e.preventDefault();
              onUpdateSubmission(editingSub.id, editingSub);
              setEditingSub(null);
            }} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>

              <div>
                <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Project Title</label>
                <input type="text" className="input-field" value={editingSub.projectTitle} onChange={(e) => setEditingSub({ ...editingSub, projectTitle: e.target.value })} required />
              </div>

              <div>
                <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Description</label>
                <textarea className="input-field" rows="3" value={editingSub.description || ''} onChange={(e) => setEditingSub({ ...editingSub, description: e.target.value })} />
              </div>

              <div>
                <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Completion Rate (%)</label>
                <input type="number" min="10" max="100" className="input-field" value={editingSub.completionRate || 80} onChange={(e) => setEditingSub({ ...editingSub, completionRate: Number(e.target.value) })} />
              </div>

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px', marginTop: '10px' }}>
                <button type="button" className="btn-secondary" onClick={() => setEditingSub(null)}>Cancel</button>
                <button type="submit" className="btn-primary">Save Changes</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
