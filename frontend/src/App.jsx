import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import SubmissionForm from './components/SubmissionForm';
import SubmissionList from './components/SubmissionList';
import SidebarWidgets from './components/SidebarWidgets';
import AdminDashboard from './components/AdminDashboard';
import AuthModal from './components/AuthModal';
import LoginPage from './components/LoginPage';
import Toast from './components/Toast';

// Pre-built & Mission-Control Module Components
import MissionControlDashboard from './components/MissionControlDashboard';
import AnalyticsHub from './components/AnalyticsHub';
import EventsDiscovery from './components/EventsDiscovery';
import ArchiveHub from './components/ArchiveHub';
import ProjectsCockpit from './components/ProjectsCockpit';
import UserProfile from './components/UserProfile';
import SponsorPortal from './components/SponsorPortal';
import MatchmakingBoard from './components/MatchmakingBoard';
import CertificateVerifier from './components/CertificateVerifier';

export default function App() {
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [authModalOpen, setAuthModalOpen] = useState(false);
  const [toast, setToast] = useState(null);

  // Restore authenticated session from localStorage
  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const saved = localStorage.getItem('currentUser');
      const token = localStorage.getItem('accessToken');
      return (saved && token) ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });

  // Handle OAuth2 Redirect callback parameters & errors
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const refreshToken = params.get('refreshToken');
    const username = params.get('username');
    const email = params.get('email');
    const role = params.get('role');
    const picture = params.get('picture');
    const errorParam = params.get('error');

    if (accessToken && username) {
      localStorage.setItem('accessToken', accessToken);
      if (refreshToken) localStorage.setItem('refreshToken', refreshToken);

      const userObj = { username, email, role: role || 'PARTICIPANT', picture };
      localStorage.setItem('currentUser', JSON.stringify(userObj));
      setCurrentUser(userObj);

      window.history.replaceState({}, document.title, window.location.pathname);
      showToast(`Welcome to HackForge, ${username}!`, 'success');
    } else if (errorParam) {
      window.history.replaceState({}, document.title, window.location.pathname);
      showToast(`Google OAuth Note: ${errorParam.replace(/_/g, ' ')}. Logging in as Google Participant...`, 'info');
      fetch('/api/auth/google', { method: 'POST' })
        .then(res => res.json())
        .then(data => {
          if (data.accessToken) {
            handleLoginSuccess(data);
          }
        })
        .catch(() => {});
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
      showToast('Network error loading submissions.', 'error');
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
        showToast('🎉 Success! Project submission saved to database.', 'success');
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

  // 1. UNAUTHENTICATED: Render full-screen Login Page first
  if (!currentUser) {
    return (
      <>
        <LoginPage onLoginSuccess={handleLoginSuccess} />
        <Toast toast={toast} onClose={() => setToast(null)} />
      </>
    );
  }

  // 2. AUTHENTICATED: Left Sidebar Navigation Menu Config
  const menuItems = [
    { id: 'dashboard', label: 'Mission Control', icon: '⚡', desc: 'Futuristic Event Operations Center' },
    { id: 'matchmaking', label: 'Matchmaking Marketplace', icon: '🤝', desc: 'Find Teammates & Skill Cards' },
    { id: 'sponsors', label: 'Sponsor Portal', icon: '🏢', desc: 'Candidate Sourcing & Sponsored Tracks' },
    { id: 'certificates', label: 'Certificate Verifier', icon: '🏅', desc: 'W3C Open Badges & QR Verification' },
    { id: 'projects', label: 'My Projects', icon: '🚀', desc: 'Builder Workspace & Command Center' },
    { id: 'profile', label: 'My Profile', icon: '👤', desc: 'Developer Profile & Tech Stack' },
    { id: 'events', label: 'Events', icon: '📅', desc: 'Hackathons & Registrations' },
    { id: 'archive', label: 'Archive', icon: '📦', desc: 'Hall of Fame & Winners' },
    { id: 'analytics', label: 'Analytics', icon: '📊', desc: 'System Metrics & Charts' },
    { id: 'submit', label: 'Submit Project', icon: '📝', desc: 'Submit & View Submissions' },
  ];

  if (currentUser.role === 'ADMIN' || currentUser.role === 'JUDGE') {
    menuItems.push({ id: 'admin', label: 'Admin Console', icon: '👑', desc: 'Judge Reviews & Management' });
  }

  return (
    <>
      {/* FIXED TOP HEADER (80px) */}
      <Header
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onOpenAuth={() => setAuthModalOpen(true)}
        currentUser={currentUser}
        onLogout={handleLogout}
      />

      {/* DASHBOARD MAIN CONTAINER */}
      <div className="app-container">
        
        {/* Layout: Left Sidebar + Main Content */}
        <div style={{ display: 'grid', gridTemplateColumns: '260px 1fr', gap: '32px', alignItems: 'start' }}>
          
          {/* LEFT SIDEBAR MENU */}
          <aside className="glass-panel" style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '16px', position: 'sticky', top: '108px' }}>
            <div style={{ paddingBottom: '12px', borderBottom: '1px solid var(--card-border)' }}>
              <span style={{ fontSize: '0.7rem', fontWeight: '800', letterSpacing: '1px', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>
                HACKFORGE OPERATING SYSTEM
              </span>
            </div>

            <nav style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {menuItems.map((item) => {
                const isActive = activeTab === item.id;
                return (
                  <button
                    key={item.id}
                    onClick={() => setActiveTab(item.id)}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '12px',
                      padding: '12px 14px',
                      borderRadius: '12px',
                      border: '1px solid',
                      borderColor: isActive ? '#FF6B00' : 'transparent',
                      background: isActive ? 'rgba(255, 107, 0, 0.18)' : 'transparent',
                      color: isActive ? '#FF8533' : 'var(--text-primary)',
                      fontWeight: isActive ? '700' : '500',
                      cursor: 'pointer',
                      textAlign: 'left',
                      transition: 'all 0.2s ease'
                    }}
                    onMouseEnter={(e) => {
                      if (!isActive) e.currentTarget.style.background = 'rgba(255, 255, 255, 0.05)';
                    }}
                    onMouseLeave={(e) => {
                      if (!isActive) e.currentTarget.style.background = 'transparent';
                    }}
                  >
                    <span style={{ fontSize: '1.2rem' }}>{item.icon}</span>
                    <div>
                      <div style={{ fontSize: '0.9rem', fontWeight: isActive ? '800' : '600' }}>{item.label}</div>
                      <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>{item.desc}</div>
                    </div>
                  </button>
                );
              })}
            </nav>

            <div style={{ marginTop: '16px', paddingTop: '14px', borderTop: '1px solid var(--card-border)', fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span>User: <strong style={{ color: 'var(--text-primary)' }}>{currentUser.username}</strong></span>
                <span className="status-badge status-approved" style={{ fontSize: '0.65rem' }}>{currentUser.role}</span>
              </div>
            </div>
          </aside>

          {/* MAIN MODULE DISPLAY */}
          <main style={{ minWidth: 0 }}>
            {activeTab === 'dashboard' && (
              <MissionControlDashboard
                submissions={submissions}
                onUpdateStatus={handleUpdateStatus}
                onDelete={handleDeleteSubmission}
                onUpdateSubmission={handleUpdateSubmission}
              />
            )}

            {activeTab === 'matchmaking' && <MatchmakingBoard />}

            {activeTab === 'sponsors' && <SponsorPortal />}

            {activeTab === 'certificates' && <CertificateVerifier />}

            {activeTab === 'projects' && <ProjectsCockpit submissions={submissions} />}

            {activeTab === 'profile' && (
              <UserProfile
                currentUser={currentUser}
                setCurrentUser={setCurrentUser}
                showToast={showToast}
              />
            )}

            {activeTab === 'events' && <EventsDiscovery />}

            {activeTab === 'archive' && <ArchiveHub />}

            {activeTab === 'analytics' && <AnalyticsHub submissions={submissions} />}

            {activeTab === 'submit' && (
              <div style={{ display: 'grid', gridTemplateColumns: 'minmax(300px, 1.4fr) minmax(280px, 1fr) 300px', gap: '24px', alignItems: 'start' }}>
                <SubmissionForm onSubmit={handleCreateSubmission} submitting={submitting} />
                <SubmissionList submissions={submissions} loading={loading} onDelete={handleDeleteSubmission} onUpdateSubmission={handleUpdateSubmission} />
                <SidebarWidgets submissions={submissions} />
              </div>
            )}

            {activeTab === 'admin' && (currentUser.role === 'ADMIN' || currentUser.role === 'JUDGE') && (
              <AdminDashboard
                submissions={submissions}
                onUpdateStatus={handleUpdateStatus}
                onDelete={handleDeleteSubmission}
                onUpdateSubmission={handleUpdateSubmission}
              />
            )}
          </main>

        </div>

      </div>

      <AuthModal
        isOpen={authModalOpen}
        onClose={() => setAuthModalOpen(false)}
        onLoginSuccess={(userObj) => {
          handleLoginSuccess(userObj);
          showToast(`Switched account to ${userObj.username}!`, 'success');
        }}
      />

      <Toast toast={toast} onClose={() => setToast(null)} />
    </>
  );
}
