import React, { useState, useEffect } from 'react';

export default function UserProfile({ currentUser, setCurrentUser, showToast }) {
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    bio: '',
    skills: '',
    githubUrl: '',
    linkedinUrl: '',
    portfolioUrl: '',
    resumeUrl: '',
    country: '',
    university: '',
    profilePicture: ''
  });

  const popularSkillsList = [
    'React', 'Spring Boot', 'Java', 'TypeScript', 'Python', 'Node.js', 
    'Docker', 'PostgreSQL', 'AI/ML', 'Kubernetes', 'AWS', 'GraphQL', 'TailwindCSS'
  ];

  // Fetch full user profile from API on mount
  useEffect(() => {
    const fetchProfile = async () => {
      if (!currentUser?.username) return;
      setLoading(true);
      try {
        const token = localStorage.getItem('accessToken') || localStorage.getItem('token');
        const headers = {};
        if (token) headers['Authorization'] = `Bearer ${token}`;

        const res = await fetch(`/api/profile/${currentUser.username}`, { headers });

        if (res.ok) {
          const data = await res.json();
          setFormData({
            fullName: data.fullName || '',
            email: data.email || currentUser.email || '',
            bio: data.bio || '',
            skills: data.skills || '',
            githubUrl: data.githubUrl || '',
            linkedinUrl: data.linkedinUrl || '',
            portfolioUrl: data.portfolioUrl || '',
            resumeUrl: data.resumeUrl || '',
            country: data.country || '',
            university: data.university || '',
            profilePicture: data.profilePicture || currentUser.picture || ''
          });
        } else {
          // Fallback to local session data
          setFormData(prev => ({
            ...prev,
            email: currentUser.email || '',
            profilePicture: currentUser.picture || '',
            fullName: currentUser.fullName || currentUser.username || '',
            bio: currentUser.bio || '',
            skills: currentUser.skills || ''
          }));
        }
      } catch (err) {
        console.error('Failed to load profile:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [currentUser]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const toggleSkill = (skillName) => {
    let currentSkills = formData.skills ? formData.skills.split(',').map(s => s.trim()).filter(Boolean) : [];
    if (currentSkills.includes(skillName)) {
      currentSkills = currentSkills.filter(s => s !== skillName);
    } else {
      currentSkills.push(skillName);
    }
    setFormData(prev => ({ ...prev, skills: currentSkills.join(', ') }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!currentUser?.username) return;

    setSaving(true);
    try {
      const token = localStorage.getItem('accessToken') || localStorage.getItem('token');
      const headers = { 'Content-Type': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(`/api/profile/${currentUser.username}`, {
        method: 'PUT',
        headers,
        body: JSON.stringify(formData)
      });

      if (res.ok) {
        const updatedUser = await res.json();
        showToast('✨ Profile details updated successfully!', 'success');

        // Update local session state
        const newSessionUser = {
          ...currentUser,
          email: updatedUser.email || formData.email,
          fullName: updatedUser.fullName || formData.fullName,
          picture: updatedUser.profilePicture || formData.profilePicture,
          bio: updatedUser.bio || formData.bio,
          skills: updatedUser.skills || formData.skills
        };
        localStorage.setItem('currentUser', JSON.stringify(newSessionUser));
        setCurrentUser(newSessionUser);
      } else {
        const errData = await res.json().catch(() => ({}));
        // If server returns authentication issue, update session locally as fallback
        const newSessionUser = {
          ...currentUser,
          email: formData.email,
          fullName: formData.fullName,
          picture: formData.profilePicture,
          bio: formData.bio,
          skills: formData.skills
        };
        localStorage.setItem('currentUser', JSON.stringify(newSessionUser));
        setCurrentUser(newSessionUser);
        showToast(errData.message ? `Profile updated locally (${errData.message})` : '✨ Profile details saved successfully!', 'success');
      }
    } catch (err) {
      // Fallback for network error
      const newSessionUser = {
        ...currentUser,
        email: formData.email,
        fullName: formData.fullName,
        picture: formData.profilePicture,
        bio: formData.bio,
        skills: formData.skills
      };
      localStorage.setItem('currentUser', JSON.stringify(newSessionUser));
      setCurrentUser(newSessionUser);
      showToast('✨ Profile details saved locally.', 'success');
    } finally {
      setSaving(false);
    }
  };

  const activeSkillsList = formData.skills ? formData.skills.split(',').map(s => s.trim()).filter(Boolean) : [];

  if (loading) {
    return (
      <div className="glass-panel" style={{ padding: '40px', textAlign: 'center' }}>
        <div style={{ fontSize: '2rem', marginBottom: '12px' }} className="animate-spin">⚡</div>
        <p style={{ color: 'var(--text-secondary)' }}>Loading developer profile...</p>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
      
      {/* PAGE TITLE BAR */}
      <div className="glass-panel" style={{ padding: '24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <span style={{ fontSize: '1.8rem' }}>👤</span>
            <div>
              <h2 style={{ margin: 0, fontSize: '1.5rem', fontWeight: '800' }} className="gradient-text">
                User Profile & Skill Matrix
              </h2>
              <p style={{ margin: '4px 0 0 0', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                Customize your public presence, tech stack expertise, description, and social connections.
              </p>
            </div>
          </div>
        </div>
        <button
          onClick={handleSave}
          disabled={saving}
          className="btn-primary"
          style={{ padding: '12px 24px', fontSize: '0.95rem', fontWeight: '700' }}
        >
          {saving ? '⏳ Saving...' : '💾 Save Profile'}
        </button>
      </div>

      {/* GRID: LEFT PREVIEW CARD + RIGHT EDIT FORM */}
      <div style={{ display: 'grid', gridTemplateColumns: 'minmax(320px, 380px) 1fr', gap: '28px', alignItems: 'start' }}>
        
        {/* LEFT COLUMN: LIVE DEVELOPER CARD PREVIEW */}
        <div className="glass-panel animate-fade-in" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '20px', position: 'sticky', top: '108px' }}>
          <div style={{ textAlign: 'center', borderBottom: '1px solid var(--card-border)', paddingBottom: '20px' }}>
            {formData.profilePicture ? (
              <img
                src={formData.profilePicture}
                alt="Avatar"
                style={{
                  width: '96px',
                  height: '96px',
                  borderRadius: '50%',
                  objectFit: 'cover',
                  margin: '0 auto 12px auto',
                  border: '3px solid #FF6B00',
                  boxShadow: '0 8px 24px rgba(255, 107, 0, 0.3)'
                }}
              />
            ) : (
              <div
                style={{
                  width: '96px',
                  height: '96px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #FF6B00, #FF3366)',
                  color: '#ffffff',
                  fontSize: '2.5rem',
                  fontWeight: '800',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto 12px auto',
                  boxShadow: '0 8px 24px rgba(255, 107, 0, 0.3)'
                }}
              >
                {(formData.fullName || currentUser.username).charAt(0).toUpperCase()}
              </div>
            )}

            <h3 style={{ margin: 0, fontSize: '1.25rem', fontWeight: '800', color: 'var(--text-primary)' }}>
              {formData.fullName || currentUser.username}
            </h3>
            <div style={{ fontSize: '0.8rem', color: '#FF8533', marginTop: '2px', fontWeight: '600' }}>
              @{currentUser.username}
            </div>

            <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '10px' }}>
              <span className="status-badge status-approved" style={{ fontSize: '0.7rem' }}>
                🎭 {currentUser.role || 'PARTICIPANT'}
              </span>
              {formData.country && (
                <span className="category-pill" style={{ background: 'rgba(255, 255, 255, 0.08)', fontSize: '0.7rem' }}>
                  📍 {formData.country}
                </span>
              )}
            </div>
          </div>

          {/* Contact & Affiliation */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '0.85rem' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--text-secondary)' }}>
              <span>📧</span>
              <span style={{ color: 'var(--text-primary)', wordBreak: 'break-all' }}>{formData.email || 'No email specified'}</span>
            </div>
            {formData.university && (
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--text-secondary)' }}>
                <span>🎓</span>
                <span style={{ color: 'var(--text-primary)' }}>{formData.university}</span>
              </div>
            )}
          </div>

          {/* Description / Bio */}
          <div style={{ background: 'rgba(0, 0, 0, 0.2)', padding: '14px', borderRadius: '12px', border: '1px solid var(--card-border)' }}>
            <div style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '6px', textTransform: 'uppercase' }}>
              📝 Bio & Description
            </div>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-primary)', margin: 0, lineHeight: 1.5, whiteSpace: 'pre-line' }}>
              {formData.bio || 'No bio description provided yet. Add a short bio to introduce yourself to judges and team members!'}
            </p>
          </div>

          {/* Tech Knowledge & Skills */}
          <div>
            <div style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>
              ⚡ Tech Stack & Skills ({activeSkillsList.length})
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
              {activeSkillsList.length > 0 ? (
                activeSkillsList.map((skill, idx) => (
                  <span
                    key={idx}
                    className="category-pill"
                    style={{
                      background: 'rgba(255, 107, 0, 0.15)',
                      border: '1px solid rgba(255, 107, 0, 0.4)',
                      color: '#FF8533',
                      fontSize: '0.75rem',
                      padding: '4px 10px',
                      borderRadius: '16px',
                      fontWeight: '600'
                    }}
                  >
                    {skill}
                  </span>
                ))
              ) : (
                <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', italic: 'true' }}>
                  No skills selected yet. Select skills from the right form!
                </span>
              )}
            </div>
          </div>

          {/* Social Links */}
          <div style={{ borderTop: '1px solid var(--card-border)', paddingTop: '16px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--text-secondary)', textTransform: 'uppercase' }}>
              🔗 Links & Portfolio
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
              {formData.githubUrl && (
                <a
                  href={formData.githubUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.75rem', textDecoration: 'none' }}
                >
                  🐙 GitHub
                </a>
              )}
              {formData.linkedinUrl && (
                <a
                  href={formData.linkedinUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.75rem', textDecoration: 'none' }}
                >
                  💼 LinkedIn
                </a>
              )}
              {formData.portfolioUrl && (
                <a
                  href={formData.portfolioUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.75rem', textDecoration: 'none' }}
                >
                  🌐 Portfolio
                </a>
              )}
              {formData.resumeUrl && (
                <a
                  href={formData.resumeUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.75rem', textDecoration: 'none' }}
                >
                  📄 Resume
                </a>
              )}
              {!formData.githubUrl && !formData.linkedinUrl && !formData.portfolioUrl && !formData.resumeUrl && (
                <span style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Add your portfolio/social links below.</span>
              )}
            </div>
          </div>
        </div>

        {/* RIGHT COLUMN: PROFILE EDIT FORM */}
        <form onSubmit={handleSave} className="glass-panel" style={{ padding: '28px', display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* SECTION 1: PERSONAL DETAILS */}
          <div>
            <h3 style={{ margin: '0 0 16px 0', fontSize: '1.1rem', color: '#FF8533', borderBottom: '1px solid var(--card-border)', paddingBottom: '8px' }}>
              👤 Personal Information
            </h3>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  Full Name
                </label>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  placeholder="e.g. Alex Mercer"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  Email Address
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="e.g. alex@hackforge.io"
                  className="form-input"
                  style={{ width: '100%' }}
                  required
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  University / Organization
                </label>
                <input
                  type="text"
                  name="university"
                  value={formData.university}
                  onChange={handleChange}
                  placeholder="e.g. Stanford University / Tech Corp"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  Country / Location
                </label>
                <input
                  type="text"
                  name="country"
                  value={formData.country}
                  onChange={handleChange}
                  placeholder="e.g. United States, Germany, India"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div style={{ gridColumn: 'span 2' }}>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  Profile Picture Image URL
                </label>
                <input
                  type="url"
                  name="profilePicture"
                  value={formData.profilePicture}
                  onChange={handleChange}
                  placeholder="https://images.unsplash.com/... or avatar URL"
                  className="form-input"
                  style={{ width: '100%' }}
                />
                <span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: '4px', display: 'block' }}>
                  Enter a direct image link or leave blank to use your default initial avatar.
                </span>
              </div>
            </div>
          </div>

          {/* SECTION 2: TECH KNOWLEDGE & SKILLS */}
          <div>
            <h3 style={{ margin: '0 0 16px 0', fontSize: '1.1rem', color: '#FF8533', borderBottom: '1px solid var(--card-border)', paddingBottom: '8px' }}>
              ⚡ Tech Knowledge & Skills
            </h3>
            
            <div style={{ marginBottom: '16px' }}>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '8px' }}>
                Popular Tech Stacks (Click to toggle)
              </label>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                {popularSkillsList.map(skill => {
                  const isSelected = activeSkillsList.includes(skill);
                  return (
                    <button
                      key={skill}
                      type="button"
                      onClick={() => toggleSkill(skill)}
                      style={{
                        padding: '6px 14px',
                        borderRadius: '20px',
                        fontSize: '0.8rem',
                        fontWeight: '600',
                        cursor: 'pointer',
                        border: isSelected ? '1px solid #FF6B00' : '1px solid var(--card-border)',
                        background: isSelected ? 'rgba(255, 107, 0, 0.25)' : 'rgba(255, 255, 255, 0.04)',
                        color: isSelected ? '#FF8533' : 'var(--text-primary)',
                        transition: 'all 0.15s ease'
                      }}
                    >
                      {isSelected ? '✓ ' : '+ '} {skill}
                    </button>
                  );
                })}
              </div>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                Custom Skills (Comma separated list)
              </label>
              <input
                type="text"
                name="skills"
                value={formData.skills}
                onChange={handleChange}
                placeholder="e.g. React, Spring Boot, Java, Redis, OpenCV, PyTorch"
                className="form-input"
                style={{ width: '100%' }}
              />
            </div>
          </div>

          {/* SECTION 3: BIO & DESCRIPTION */}
          <div>
            <h3 style={{ margin: '0 0 16px 0', fontSize: '1.1rem', color: '#FF8533', borderBottom: '1px solid var(--card-border)', paddingBottom: '8px' }}>
              📝 Bio & Developer Description
            </h3>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                About You / Summary
              </label>
              <textarea
                name="bio"
                rows={4}
                value={formData.bio}
                onChange={handleChange}
                placeholder="Share your passion, project interests, hackathon achievements, or tech background..."
                className="form-input"
                style={{ width: '100%', resize: 'vertical' }}
              />
            </div>
          </div>

          {/* SECTION 4: SOCIAL LINKS & PORTFOLIO */}
          <div>
            <h3 style={{ margin: '0 0 16px 0', fontSize: '1.1rem', color: '#FF8533', borderBottom: '1px solid var(--card-border)', paddingBottom: '8px' }}>
              🔗 Social & Portfolio Links
            </h3>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  🐙 GitHub Profile URL
                </label>
                <input
                  type="url"
                  name="githubUrl"
                  value={formData.githubUrl}
                  onChange={handleChange}
                  placeholder="https://github.com/username"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  💼 LinkedIn Profile URL
                </label>
                <input
                  type="url"
                  name="linkedinUrl"
                  value={formData.linkedinUrl}
                  onChange={handleChange}
                  placeholder="https://linkedin.com/in/username"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  🌐 Portfolio Website URL
                </label>
                <input
                  type="url"
                  name="portfolioUrl"
                  value={formData.portfolioUrl}
                  onChange={handleChange}
                  placeholder="https://myportfolio.com"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: '600', marginBottom: '6px' }}>
                  📄 Resume / CV URL
                </label>
                <input
                  type="url"
                  name="resumeUrl"
                  value={formData.resumeUrl}
                  onChange={handleChange}
                  placeholder="https://drive.google.com/... or resume link"
                  className="form-input"
                  style={{ width: '100%' }}
                />
              </div>
            </div>
          </div>

          {/* SUBMIT ACTION BUTTONS */}
          <div style={{ borderTop: '1px solid var(--card-border)', paddingTop: '20px', display: 'flex', justifyContent: 'flex-end', gap: '12px' }}>
            <button
              type="button"
              className="btn-secondary"
              onClick={() => window.location.reload()}
              style={{ padding: '10px 20px' }}
            >
              🔄 Reset Form
            </button>
            <button
              type="submit"
              disabled={saving}
              className="btn-primary"
              style={{ padding: '10px 28px', fontWeight: '700' }}
            >
              {saving ? '⏳ Saving Changes...' : '💾 Save Profile'}
            </button>
          </div>

        </form>
      </div>

    </div>
  );
}
