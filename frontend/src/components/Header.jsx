import React, { useState } from 'react';

export default function Header({ activeTab, setActiveTab, onOpenAuth, currentUser, onLogout }) {
  const [theme, setTheme] = useState(() => localStorage.getItem('theme') || 'dark');
  const [showNotifications, setShowNotifications] = useState(false);
  const [showProfileMenu, setShowProfileMenu] = useState(false);

  const toggleTheme = () => {
    const newTheme = theme === 'dark' ? 'light' : 'dark';
    setTheme(newTheme);
    localStorage.setItem('theme', newTheme);
    document.body.setAttribute('data-theme', newTheme);
  };

  const notifications = [
    { id: 1, title: 'Submission Approved', message: 'Your project AI Fraud Detection was approved by judges.', time: '10m ago', read: false },
    { id: 2, title: 'Deadline Extended', message: 'Hackathon submission deadline extended by 24 hours!', time: '1h ago', read: false },
    { id: 3, title: 'Judge Feedback Received', message: 'Awesome tech stack architecture implementation.', time: '3h ago', read: true }
  ];

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <header className="fixed-header-wrapper">
      <div className="fixed-header-inner">
        
        {/* BRAND LOGO */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: '40px',
            height: '40px',
            background: 'linear-gradient(135deg, #FF6B00, #FF3366)',
            borderRadius: '12px',
            fontSize: '1.3rem',
            boxShadow: '0 4px 12px rgba(255, 107, 0, 0.4)'
          }}>
            ⚡
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h1 style={{ fontSize: '1.3rem', fontWeight: '800', margin: 0 }} className="gradient-text">
                HackForge
              </h1>
              <span className="category-pill" style={{ background: 'rgba(255, 107, 0, 0.15)', color: '#FF8533', fontSize: '0.7rem', padding: '2px 8px', borderRadius: '12px' }}>
                Enterprise OS
              </span>
            </div>
            <p style={{ fontSize: '0.7rem', color: 'var(--text-secondary)', margin: 0 }} className="hidden md:block">
              Enterprise Hackathon Gateway & Project Validator
            </p>
          </div>
        </div>

        {/* CONTROLS: THEME, NOTIFICATIONS & PROFILE */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          
          {/* Theme Toggle Button */}
          <button
            onClick={toggleTheme}
            className="btn-secondary"
            title="Toggle Light / Dark Mode"
            style={{ padding: '8px 12px', fontSize: '0.85rem' }}
          >
            {theme === 'dark' ? '☀️ Light' : '🌙 Dark'}
          </button>

          {/* 🔔 Notifications Dropdown */}
          <div style={{ position: 'relative' }}>
            <button
              className="btn-secondary"
              onClick={() => { setShowNotifications(!showNotifications); setShowProfileMenu(false); }}
              style={{ position: 'relative', padding: '8px 12px' }}
            >
              🔔
              {unreadCount > 0 && (
                <span style={{ position: 'absolute', top: '-4px', right: '-4px', background: '#FF6B00', color: '#fff', borderRadius: '50%', padding: '2px 6px', fontSize: '0.65rem', fontWeight: 'bold' }}>
                  {unreadCount}
                </span>
              )}
            </button>

            {showNotifications && (
              <div className="glass-panel animate-fade-in" style={{
                position: 'absolute',
                top: 'calc(100% + 8px)',
                right: 0,
                width: '300px',
                padding: '14px',
                zIndex: 1000,
                background: 'var(--card-bg)',
                boxShadow: '0 12px 32px rgba(0, 0, 0, 0.6)',
                border: '1px solid var(--card-border)'
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px', borderBottom: '1px solid var(--card-border)', paddingBottom: '8px' }}>
                  <span style={{ fontWeight: '700', fontSize: '0.85rem' }}>🔔 Notifications</span>
                  <span style={{ fontSize: '0.7rem', color: '#60a5fa', cursor: 'pointer' }}>Mark all read</span>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', maxHeight: '240px', overflowY: 'auto' }}>
                  {notifications.map(n => (
                    <div key={n.id} style={{ padding: '8px', borderRadius: '8px', background: n.read ? 'transparent' : 'rgba(37, 99, 235, 0.1)', border: '1px solid var(--card-border)' }}>
                      <div style={{ fontWeight: '600', fontSize: '0.8rem', color: 'var(--text-primary)' }}>{n.title}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>{n.message}</div>
                      <span style={{ fontSize: '0.65rem', color: 'var(--text-secondary)' }}>{n.time}</span>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* 👤 Profile Dropdown */}
          {currentUser ? (
            <div style={{ position: 'relative' }}>
              <button
                className="btn-secondary"
                onClick={() => { setShowProfileMenu(!showProfileMenu); setShowNotifications(false); }}
                style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '6px 12px' }}
              >
                {currentUser.picture ? (
                  <img src={currentUser.picture} alt="Profile" style={{ width: '24px', height: '24px', borderRadius: '50%' }} />
                ) : (
                  <span>👤</span>
                )}
                <span>{currentUser.username}</span>
                <span style={{ fontSize: '0.75rem', opacity: 0.7 }}>▾</span>
              </button>

              {showProfileMenu && (
                <div className="glass-panel animate-fade-in" style={{
                  position: 'absolute',
                  top: 'calc(100% + 8px)',
                  right: 0,
                  width: '240px',
                  padding: '16px',
                  zIndex: 1000,
                  background: 'var(--card-bg)',
                  boxShadow: '0 12px 32px rgba(0, 0, 0, 0.6)',
                  border: '1px solid var(--card-border)',
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '10px'
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px', paddingBottom: '10px', borderBottom: '1px solid var(--card-border)' }}>
                    {currentUser.picture ? (
                      <img src={currentUser.picture} alt="Profile" style={{ width: '40px', height: '40px', borderRadius: '50%' }} />
                    ) : (
                      <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: '#2563eb', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold', fontSize: '1.1rem' }}>
                        {currentUser.username.charAt(0).toUpperCase()}
                      </div>
                    )}
                    <div style={{ overflow: 'hidden' }}>
                      <div style={{ fontWeight: '700', fontSize: '0.9rem', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
                        👤 {currentUser.username}
                      </div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
                        📧 {currentUser.email || `${currentUser.username}@hackforge.com`}
                      </div>
                    </div>
                  </div>

                  <div style={{ fontSize: '0.75rem', fontWeight: '700', color: '#2563eb', background: 'rgba(37, 99, 235, 0.12)', padding: '6px 10px', borderRadius: '6px', textAlign: 'center' }}>
                    🎭 {currentUser.role || 'PARTICIPANT'}
                  </div>

                  <button
                    className="nav-tab"
                    style={{
                      color: 'var(--text-primary)',
                      justifyContent: 'flex-start',
                      border: '1px solid var(--card-border)',
                      background: 'rgba(255, 107, 0, 0.12)',
                      width: '100%',
                      textAlign: 'left',
                      padding: '8px 12px',
                      borderRadius: '8px',
                      cursor: 'pointer',
                      fontSize: '0.85rem',
                      fontWeight: '600'
                    }}
                    onClick={() => {
                      setActiveTab('profile');
                      setShowProfileMenu(false);
                    }}
                  >
                    👤 My Profile & Settings
                  </button>

                  <button className="nav-tab" style={{ color: '#ef4444', justifyContent: 'flex-start', border: 'none', background: 'transparent', width: '100%', textAlign: 'left', padding: '8px', cursor: 'pointer' }} onClick={() => { onLogout(); setShowProfileMenu(false); }}>
                    🚪 Logout
                  </button>
                </div>
              )}
            </div>
          ) : (
            <button className="btn-primary" onClick={onOpenAuth} style={{ padding: '8px 14px', fontSize: '0.85rem' }}>
              🔑 Sign In
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
