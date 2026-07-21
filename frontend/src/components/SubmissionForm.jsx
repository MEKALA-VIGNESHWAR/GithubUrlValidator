import React, { useState } from 'react';

const GITHUB_REGEX = /^https?:\/\/(www\.)?github\.com\/[A-Za-z0-9_.-]+\/[A-Za-z0-9_.-]+\/?$/;

export default function SubmissionForm({ onSubmit, submitting }) {
  const [teamName, setTeamName] = useState('');
  const [projectTitle, setProjectTitle] = useState('');
  const [githubRepoUrl, setGithubRepoUrl] = useState('');
  const [urlTouched, setUrlTouched] = useState(false);

  const isUrlValid = GITHUB_REGEX.test(githubRepoUrl.trim());

  const handleSubmit = (e) => {
    e.preventDefault();
    setUrlTouched(true);

    if (!teamName.trim() || !projectTitle.trim() || !isUrlValid) {
      return;
    }

    onSubmit({
      teamName: teamName.trim(),
      projectTitle: projectTitle.trim(),
      githubRepoUrl: githubRepoUrl.trim()
    }, () => {
      setTeamName('');
      setProjectTitle('');
      setGithubRepoUrl('');
      setUrlTouched(false);
    });
  };

  return (
    <div className="glass-panel" style={{ padding: '28px', height: 'fit-content' }}>
      <h2 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '20px', color: '#60a5fa' }}>
        ✨ Submit Your Hack
      </h2>

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '18px' }}>
        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', color: '#9ca3af', marginBottom: '6px' }}>
            Team Name
          </label>
          <input
            type="text"
            className="input-field"
            placeholder="e.g. Code Ninja"
            value={teamName}
            onChange={(e) => setTeamName(e.target.value)}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', color: '#9ca3af', marginBottom: '6px' }}>
            Project Title
          </label>
          <input
            type="text"
            className="input-field"
            placeholder="e.g. AI Fraud Detector"
            value={projectTitle}
            onChange={(e) => setProjectTitle(e.target.value)}
            required
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: '600', color: '#9ca3af', marginBottom: '6px' }}>
            GitHub Repository URL
          </label>
          <input
            type="url"
            className={`input-field ${urlTouched && !isUrlValid ? 'invalid' : ''}`}
            placeholder="https://github.com/owner/repository"
            value={githubRepoUrl}
            onChange={(e) => {
              setGithubRepoUrl(e.target.value);
              if (!urlTouched) setUrlTouched(true);
            }}
            required
          />
          {urlTouched && !isUrlValid && (
            <p style={{ color: '#f87171', fontSize: '0.8rem', marginTop: '6px' }}>
              URL must be formatted as https://github.com/owner/repository
            </p>
          )}
        </div>

        <button
          type="submit"
          className="btn-primary"
          disabled={submitting || (urlTouched && !isUrlValid)}
          style={{ marginTop: '10px' }}
        >
          {submitting ? 'Submitting...' : 'Submit Repository'}
        </button>
      </form>
    </div>
  );
}
