import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import SubmissionForm from './components/SubmissionForm';
import SubmissionList from './components/SubmissionList';
import Toast from './components/Toast';

export default function App() {
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [toast, setToast] = useState(null);

  const fetchSubmissions = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api');
      if (response.ok) {
        const data = await response.json();
        setSubmissions(data);
      } else {
        showToast('Failed to load submissions from backend server.', 'error');
      }
    } catch (err) {
      showToast('Network error: Is Spring Boot running on port 3000?', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSubmissions();
  }, []);

  const handleCreateSubmission = async (formData, resetForm) => {
    setSubmitting(true);
    try {
      const response = await fetch('/api', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        showToast('Success! Project submission saved.', 'success');
        resetForm();
        fetchSubmissions();
      } else {
        const errorData = await response.json().catch(() => ({}));
        showToast(errorData.message || `Submission failed (Error ${response.status})`, 'error');
      }
    } catch (err) {
      showToast('Network error submitting project.', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteSubmission = async (id, teamName) => {
    if (!window.confirm(`Are you sure you want to delete submission for team "${teamName}"?`)) {
      return;
    }

    try {
      const response = await fetch(`/api/${id}`, { method: 'DELETE' });
      if (response.ok) {
        showToast(`Submission for "${teamName}" deleted.`, 'success');
        fetchSubmissions();
      } else {
        const errorData = await response.json().catch(() => ({}));
        showToast(errorData.message || 'Could not delete submission.', 'error');
      }
    } catch (err) {
      showToast('Network error deleting submission.', 'error');
    }
  };

  const showToast = (message, type = 'info') => {
    setToast({ message, type });
  };

  return (
    <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '32px 16px' }}>
      <Header
        totalCount={submissions ? submissions.length : 0}
        onRefresh={fetchSubmissions}
        loading={loading}
      />

      <main style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
        gap: '28px',
        alignItems: 'start'
      }}>
        <SubmissionForm
          onSubmit={handleCreateSubmission}
          submitting={submitting}
        />

        <SubmissionList
          submissions={submissions}
          loading={loading}
          onDelete={handleDeleteSubmission}
        />
      </main>

      <Toast toast={toast} onClose={() => setToast(null)} />
    </div>
  );
}
