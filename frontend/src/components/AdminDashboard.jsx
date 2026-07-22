import React, { useState } from 'react';

export default function AdminDashboard({ submissions, onUpdateStatus, onDelete, onUpdateSubmission }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [categoryFilter, setCategoryFilter] = useState('ALL');
  const [selectedSubmissionForComment, setSelectedSubmissionForComment] = useState(null);
  const [judgeCommentText, setJudgeCommentText] = useState('');

  const total = submissions ? submissions.length : 0;
  const pending = submissions ? submissions.filter(s => !s.status || s.status === 'PENDING').length : 0;
  const approved = submissions ? submissions.filter(s => s.status === 'APPROVED').length : 0;
  const rejected = submissions ? submissions.filter(s => s.status === 'REJECTED').length : 0;
  const changesRequested = submissions ? submissions.filter(s => s.status === 'CHANGES_REQUESTED').length : 0;

  const filteredSubmissions = (submissions || []).filter(sub => {
    const matchesSearch = (sub.teamName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           sub.projectTitle?.toLowerCase().includes(searchTerm.toLowerCase()));
    
    const matchesStatus = statusFilter === 'ALL' || (sub.status || 'PENDING') === statusFilter;
    const matchesCategory = categoryFilter === 'ALL' || (sub.category || 'General').toUpperCase() === categoryFilter.toUpperCase();

    return matchesSearch && matchesStatus && matchesCategory;
  });

  // Bulk Operations
  const handleBulkApproveAI = () => {
    if (window.confirm('Approve all AI category project submissions?')) {
      (submissions || []).forEach(sub => {
        if (sub.category?.toUpperCase() === 'AI') {
          onUpdateStatus(sub.id, 'APPROVED');
        }
      });
    }
  };

  // CSV Export
  const handleExportCSV = () => {
    if (!submissions || submissions.length === 0) return;
    const headers = ['ID', 'Team Name', 'Project Title', 'Category', 'Status', 'Leader', 'Email', 'GitHub URL', 'Stars', 'Submitted At'];
    const rows = submissions.map(s => [
      s.id,
      `"${s.teamName}"`,
      `"${s.projectTitle}"`,
      s.category || 'General',
      s.status || 'PENDING',
      `"${s.leaderName || ''}"`,
      s.email || '',
      s.githubRepoUrl || '',
      s.stars || 0,
      s.submittedAt || ''
    ]);

    const csvContent = 'data:text/csv;charset=utf-8,' + [headers.join(','), ...rows.map(r => r.join(','))].join('\n');
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `hackforge_submissions_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // JSON Export
  const handleExportJSON = () => {
    if (!submissions || submissions.length === 0) return;
    const dataStr = 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(submissions, null, 2));
    const link = document.createElement('a');
    link.setAttribute('href', dataStr);
    link.setAttribute('download', `hackforge_submissions_${Date.now()}.json`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // Save Judge Comment
  const handleSaveComment = (e) => {
    e.preventDefault();
    if (!selectedSubmissionForComment) return;

    if (onUpdateSubmission) {
      onUpdateSubmission(selectedSubmissionForComment.id, {
        ...selectedSubmissionForComment,
        judgeComment: judgeCommentText
      });
    }

    setSelectedSubmissionForComment(null);
    setJudgeCommentText('');
  };

  return (
    <div className="glass-panel animate-fade-in" style={{ padding: '28px' }}>
      
      {/* Header & Controls */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: '800' }} className="gradient-text">
            🛡️ Admin & Judge Control Panel (/admin)
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
            Evaluate projects, leave judge comments, request changes, and export reporting data
          </p>
        </div>

        {/* Action Controls */}
        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
          <button className="btn-secondary" onClick={handleBulkApproveAI} style={{ fontSize: '0.8rem' }}>
            ⚡ Approve All AI Projects
          </button>
          <button className="btn-secondary" onClick={handleExportCSV} style={{ fontSize: '0.8rem', color: '#3b82f6' }}>
            📥 Export CSV
          </button>
          <button className="btn-secondary" onClick={handleExportJSON} style={{ fontSize: '0.8rem', color: '#8b5cf6' }}>
            📥 Export JSON
          </button>
        </div>
      </div>

      {/* --- SUMMARY COUNTER CARDS --- */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: '14px', marginBottom: '24px' }}>
        <div style={{ background: 'var(--inner-bg)', padding: '16px', borderRadius: '12px', border: '1px solid var(--card-border)' }}>
          <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Total Submissions</div>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', marginTop: '4px' }}>{total}</div>
        </div>

        <div style={{ background: 'rgba(234, 179, 8, 0.1)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(234, 179, 8, 0.3)' }}>
          <div style={{ fontSize: '0.8rem', color: '#eab308' }}>Pending</div>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', color: '#eab308', marginTop: '4px' }}>{pending}</div>
        </div>

        <div style={{ background: 'rgba(34, 197, 94, 0.1)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(34, 197, 94, 0.3)' }}>
          <div style={{ fontSize: '0.8rem', color: '#22c55e' }}>Approved</div>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', color: '#22c55e', marginTop: '4px' }}>{approved}</div>
        </div>

        <div style={{ background: 'rgba(245, 158, 11, 0.1)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(245, 158, 11, 0.3)' }}>
          <div style={{ fontSize: '0.8rem', color: '#f59e0b' }}>Changes Requested</div>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', color: '#f59e0b', marginTop: '4px' }}>{changesRequested}</div>
        </div>

        <div style={{ background: 'rgba(239, 68, 68, 0.1)', padding: '16px', borderRadius: '12px', border: '1px solid rgba(239, 68, 68, 0.3)' }}>
          <div style={{ fontSize: '0.8rem', color: '#ef4444' }}>Rejected</div>
          <div style={{ fontSize: '1.8rem', fontWeight: '800', color: '#ef4444', marginTop: '4px' }}>{rejected}</div>
        </div>
      </div>

      {/* --- SEARCH & FILTERS --- */}
      <div style={{ display: 'flex', gap: '14px', marginBottom: '20px', flexWrap: 'wrap' }}>
        <input
          type="text"
          className="input-field"
          placeholder="🔍 Search by Team Name or Project Title..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ flex: '1', minWidth: '240px' }}
        />

        <select
          className="input-field"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          style={{ width: '170px', cursor: 'pointer' }}
        >
          <option value="ALL">All Statuses</option>
          <option value="PENDING">🟡 Pending</option>
          <option value="APPROVED">🟢 Approved</option>
          <option value="CHANGES_REQUESTED">🟧 Request Changes</option>
          <option value="REJECTED">🔴 Rejected</option>
        </select>

        <select
          className="input-field"
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          style={{ width: '170px', cursor: 'pointer' }}
        >
          <option value="ALL">All Categories</option>
          <option value="AI">AI</option>
          <option value="WEB">Web</option>
          <option value="HEALTHCARE">Healthcare</option>
          <option value="BLOCKCHAIN">Blockchain</option>
          <option value="IOT">IoT</option>
          <option value="CYBERSECURITY">Cybersecurity</option>
          <option value="FINTECH">Fintech</option>
          <option value="EDTECH">EdTech</option>
        </select>
      </div>

      {/* --- SUBMISSION MANAGEMENT LIST --- */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
        {filteredSubmissions.length === 0 ? (
          <p style={{ color: 'var(--text-muted)', textAlign: 'center', padding: '30px', fontStyle: 'italic' }}>
            No submissions match your filters.
          </p>
        ) : (
          filteredSubmissions.map((sub) => (
            <div
              key={sub.id}
              style={{
                background: 'var(--inner-bg)',
                border: '1px solid var(--card-border)',
                borderRadius: '12px',
                padding: '16px 20px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                flexWrap: 'wrap',
                gap: '14px'
              }}
            >
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <h4 style={{ fontSize: '1.05rem', fontWeight: '800' }}>
                    {sub.projectTitle}
                  </h4>
                  <span className={`category-pill tag-${(sub.category || 'general').toLowerCase()}`}>
                    {sub.category || 'General'}
                  </span>
                </div>

                <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '4px' }}>
                  Team: <strong style={{ color: 'var(--primary-accent)' }}>{sub.teamName}</strong> • Leader: {sub.leaderName || 'N/A'} • {sub.email || 'No email'}
                </p>

                {sub.judgeComment && (
                  <p style={{ fontSize: '0.75rem', color: '#eab308', marginTop: '4px' }}>
                    💬 Feedback: <em>"{sub.judgeComment}"</em>
                  </p>
                )}
              </div>

              {/* Status Action Controls */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap' }}>
                <button
                  onClick={() => onUpdateStatus(sub.id, 'APPROVED')}
                  style={{
                    background: sub.status === 'APPROVED' ? '#22c55e' : 'rgba(34, 197, 94, 0.15)',
                    border: '1px solid #22c55e',
                    color: sub.status === 'APPROVED' ? '#ffffff' : '#22c55e',
                    padding: '6px 10px',
                    borderRadius: '8px',
                    fontWeight: '700',
                    fontSize: '0.75rem',
                    cursor: 'pointer'
                  }}
                >
                  🟢 Approve
                </button>

                <button
                  onClick={() => onUpdateStatus(sub.id, 'CHANGES_REQUESTED')}
                  style={{
                    background: sub.status === 'CHANGES_REQUESTED' ? '#f59e0b' : 'rgba(245, 158, 11, 0.15)',
                    border: '1px solid #f59e0b',
                    color: sub.status === 'CHANGES_REQUESTED' ? '#ffffff' : '#f59e0b',
                    padding: '6px 10px',
                    borderRadius: '8px',
                    fontWeight: '700',
                    fontSize: '0.75rem',
                    cursor: 'pointer'
                  }}
                >
                  🟧 Changes
                </button>

                <button
                  onClick={() => onUpdateStatus(sub.id, 'REJECTED')}
                  style={{
                    background: sub.status === 'REJECTED' ? '#ef4444' : 'rgba(239, 68, 68, 0.15)',
                    border: '1px solid #ef4444',
                    color: sub.status === 'REJECTED' ? '#ffffff' : '#ef4444',
                    padding: '6px 10px',
                    borderRadius: '8px',
                    fontWeight: '700',
                    fontSize: '0.75rem',
                    cursor: 'pointer'
                  }}
                >
                  🔴 Reject
                </button>

                <button
                  onClick={() => { setSelectedSubmissionForComment(sub); setJudgeCommentText(sub.judgeComment || ''); }}
                  className="btn-secondary"
                  style={{ fontSize: '0.75rem', padding: '6px 10px' }}
                >
                  💬 Comment
                </button>

                <button
                  onClick={() => onDelete(sub.id, sub.teamName)}
                  style={{
                    background: 'rgba(255, 255, 255, 0.05)',
                    border: '1px solid rgba(255, 255, 255, 0.15)',
                    color: 'var(--text-muted)',
                    padding: '6px 8px',
                    borderRadius: '8px',
                    fontSize: '0.75rem',
                    cursor: 'pointer'
                  }}
                >
                  🗑️
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* --- JUDGE COMMENT MODAL --- */}
      {selectedSubmissionForComment && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.7)', backdropFilter: 'blur(6px)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000, padding: '16px' }}>
          <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '460px', padding: '24px' }}>
            <h3 style={{ fontSize: '1.2rem', fontWeight: '800', marginBottom: '10px' }}>
              💬 Add Judge Feedback
            </h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginBottom: '14px' }}>
              Feedback for team <strong>{selectedSubmissionForComment.teamName}</strong> ({selectedSubmissionForComment.projectTitle})
            </p>

            <form onSubmit={handleSaveComment} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <textarea
                className="input-field"
                rows="4"
                placeholder="e.g. Please improve documentation and add a demo video link..."
                value={judgeCommentText}
                onChange={(e) => setJudgeCommentText(e.target.value)}
                required
              />

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                <button type="button" className="btn-secondary" onClick={() => setSelectedSubmissionForComment(null)}>Cancel</button>
                <button type="submit" className="btn-primary">Save Comment</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
