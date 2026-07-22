import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import SubmissionForm from './components/SubmissionForm';
import SubmissionList from './components/SubmissionList';
import SidebarWidgets from './components/SidebarWidgets';
import AdminDashboard from './components/AdminDashboard';
import AuthModal from './components/AuthModal';
import LoginPage from './components/LoginPage';
import Toast from './components/Toast';

export default function App() {
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [activeTab, setActiveTab] = useState('participant');
  const [authModalOpen, setAuthModalOpen] = useState(false);
  const [toast, setToast] = useState(null);

  // Restore authenticated session from localStorage only if accessToken exists
  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const saved = localStorage.getItem('currentUser');
      const token = localStorage.getItem('accessToken');
      return (saved && token) ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });

  // Handle OAuth2 Redirect callback parameters (?accessToken=...&refreshToken=...)
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const refreshToken = params.get('refreshToken');
    const username = params.get('username');
    const email = params.get('email');
    const role = params.get('role');
    const picture = params.get('picture');

    if (accessToken && username) {
      localStorage.setItem('accessToken', accessToken);
      if (refreshToken) localStorage.setItem('refreshToken', refreshToken);

      const userObj = { username, email, role: role || 'PARTICIPANT', picture };
      localStorage.setItem('currentUser', JSON.stringify(userObj));
      setCurrentUser(userObj);

      // Clean query parameters from URL
      window.history.replaceState({}, document.title, window.location.pathname);
      showToast(`Welcome to HackForge, ${username}!`, 'success');
    }
  }, []);

  const fetchSubmissions = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api/submissions');
      if (response.ok) {
        const data = await response.json();
        setSubmissions(Array.isArray(data) ? data : data.content || []);
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
    if (currentUser) {
      fetchSubmissions();
    }
  }, [currentUser]);

  const handleLoginSuccess = (userObj) => {
    if (userObj.token || userObj.accessToken) {
      localStorage.setItem('accessToken', userObj.token || userObj.accessToken);
    }
    if (userObj.refreshToken) {
      localStorage.setItem('refreshToken', userObj.refreshToken);
    }
    localStorage.setItem('currentUser', JSON.stringify(userObj));
    setCurrentUser(userObj);
    showToast(`Welcome back, ${userObj.username}!`, 'success');
  };

  const handleLogout = () => {
    localStorage.clear();
    setCurrentUser(null);
    showToast('Signed out of HackForge session.', 'info');
  };

  const handleCreateSubmission = async (formData, resetForm) => {
    setSubmitting(true);
    try {
      const response = await fetch('/api/submissions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('accessToken') || ''}`
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        showToast('🎉 Success! HackForge project submission verified & saved.', 'success');
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

  const handleUpdateSubmission = async (id, updatedData) => {
    try {
      const response = await fetch(`/api/submissions/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('accessToken') || ''}`
        },
        body: JSON.stringify(updatedData)
      });

      if (response.ok) {
        showToast('Submission updated successfully.', 'success');
        fetchSubmissions();
      } else {
        setSubmissions(prev => prev.map(s => s.id === id ? { ...s, ...updatedData } : s));
        showToast('Submission updated locally.', 'success');
      }
    } catch (err) {
      setSubmissions(prev => prev.map(s => s.id === id ? { ...s, ...updatedData } : s));
      showToast('Submission updated locally.', 'success');
    }
  };

  const handleUpdateStatus = async (id, newStatus) => {
    try {
      const response = await fetch(`/api/submissions/${id}/status?status=${newStatus}`, {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken') || ''}`
        }
      });
      if (response.ok) {
        showToast(`Status updated to ${newStatus}`, 'success');
        fetchSubmissions();
      } else {
        setSubmissions(prev => prev.map(s => s.id === id ? { ...s, status: newStatus } : s));
        showToast(`Status updated to ${newStatus}`, 'success');
      }
    } catch (err) {
      setSubmissions(prev => prev.map(s => s.id === id ? { ...s, status: newStatus } : s));
      showToast(`Status updated to ${newStatus}`, 'success');
    }
  };

  const handleDeleteSubmission = async (id, teamName) => {
    if (!window.confirm(`Are you sure you want to delete submission for team "${teamName}"?`)) {
      return;
    }

    try {
      const response = await fetch(`/api/submissions/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken') || ''}`
        }
      });
      if (response.ok) {
        showToast(`Submission for "${teamName}" deleted.`, 'success');
        fetchSubmissions();
      } else {
        setSubmissions(prev => prev.filter(s => s.id !== id));
        showToast(`Submission for "${teamName}" deleted.`, 'success');
      }
    } catch (err) {
      setSubmissions(prev => prev.filter(s => s.id !== id));
      showToast(`Submission for "${teamName}" deleted.`, 'success');
    }
  };

  const showToast = (message, type = 'info') => {
    setToast({ message, type });
  };

  // If user is unauthenticated -> Render full-screen LoginPage
  if (!currentUser) {
    return (
      <>
        <LoginPage onLoginSuccess={handleLoginSuccess} />
        <Toast toast={toast} onClose={() => setToast(null)} />
      </>
    );
  }

  // Once authenticated -> Render full Dashboard
  return (
    <div style={{ maxWidth: '1440px', margin: '0 auto', padding: '24px 16px' }}>
      <Header
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onOpenAuth={() => setAuthModalOpen(true)}
        currentUser={currentUser}
        onLogout={handleLogout}
      />

      {activeTab === 'admin' && (currentUser.role === 'ADMIN' || currentUser.role === 'JUDGE') ? (
        <AdminDashboard
          submissions={submissions}
          onUpdateStatus={handleUpdateStatus}
          onDelete={handleDeleteSubmission}
          onUpdateSubmission={handleUpdateSubmission}
        />
      ) : (
        <main style={{
          display: 'grid',
          gridTemplateColumns: 'minmax(320px, 1.4fr) minmax(300px, 1fr) 340px',
          gap: '24px',
          alignItems: 'start'
        }}>
          {/* Column 1: Submission Form */}
          <SubmissionForm
            onSubmit={handleCreateSubmission}
            submitting={submitting}
          />

          {/* Column 2: Submission List */}
          <SubmissionList
            submissions={submissions}
            loading={loading}
            onDelete={handleDeleteSubmission}
            onUpdateSubmission={handleUpdateSubmission}
          />

          {/* Column 3: Sidebar Widgets */}
          <SidebarWidgets submissions={submissions} />
        </main>
      )}

      <AuthModal
        isOpen={authModalOpen}
        onClose={() => setAuthModalOpen(false)}
        onLoginSuccess={(userObj) => {
          handleLoginSuccess(userObj);
          showToast(`Switched account to ${userObj.username}!`, 'success');
        }}
      />

      <Toast toast={toast} onClose={() => setToast(null)} />
    </div>
  );
}
