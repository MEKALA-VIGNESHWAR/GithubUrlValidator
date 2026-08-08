import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function MatchmakingBoard() {
  const [profiles, setProfiles] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [activeTab, setActiveTab] = useState('marketplace');
  const [showCreateModal, setShowCreateModal] = useState(false);

  // Form State
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [primaryRole, setPrimaryRole] = useState('Frontend Developer');
  const [skills, setSkills] = useState('React, TypeScript, Tailwind');
  const [timezone, setTimezone] = useState('UTC');
  const [bio, setBio] = useState('');

  useEffect(() => {
    fetchProfiles();
    fetchRecommendations();
  }, []);

  const fetchProfiles = async () => {
    try {
      const res = await axios.get('/api/marketplace/profiles');
      setProfiles(res.data);
    } catch (err) {
      console.error("Failed to fetch marketplace profiles", err);
    }
  };

  const fetchRecommendations = async () => {
    try {
      const res = await axios.get('/api/marketplace/recommendations/1');
      setRecommendations(res.data);
    } catch (err) {
      console.error("Failed to fetch recommendations", err);
    }
  };

  const handlePublishSkillCard = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/marketplace/skill-card', {
        userId: 1,
        fullName,
        email,
        primaryRole,
        skillsJson: JSON.stringify(skills.split(',').map(s => s.trim())),
        timezone,
        bio,
        lookingForTeam: true
      });
      setShowCreateModal(false);
      fetchProfiles();
      fetchRecommendations();
    } catch (err) {
      console.error("Failed to publish skill card", err);
    }
  };

  return (
    <div className="bg-slate-900 text-slate-100 min-h-screen p-6 font-sans">
      <div className="max-w-7xl mx-auto space-y-8">
        
        {/* Header */}
        <div className="bg-gradient-to-r from-emerald-900 via-teal-900 to-slate-900 border border-teal-500/30 rounded-2xl p-8 shadow-2xl backdrop-blur-xl">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
            <div>
              <div className="inline-flex items-center space-x-2 px-3 py-1 bg-teal-500/10 border border-teal-500/30 rounded-full text-teal-400 text-xs font-semibold uppercase tracking-wider mb-3">
                <span>🤝 Team Formation Marketplace</span>
              </div>
              <h1 className="text-3xl font-extrabold text-white tracking-tight">Teammate Matchmaking & Skill Cards</h1>
              <p className="text-slate-400 mt-2 max-w-2xl">
                Publish your skill card, search solo participants looking for teams, and get algorithmic recommendations based on complementary skill sets and time zones.
              </p>
            </div>

            <div className="flex items-center gap-3">
              <button
                onClick={() => setShowCreateModal(true)}
                className="px-5 py-2.5 bg-teal-600 hover:bg-teal-500 text-white font-semibold text-sm rounded-xl shadow-lg transition"
              >
                ➕ Publish My Skill Card
              </button>
            </div>
          </div>
        </div>

        {/* Tab Toggle */}
        <div className="flex border-b border-slate-800 space-x-8">
          <button
            onClick={() => setActiveTab('marketplace')}
            className={`pb-4 font-semibold text-sm transition-all border-b-2 ${
              activeTab === 'marketplace' ? 'border-teal-500 text-teal-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            }`}
          >
            📋 "Looking for Team" Board ({profiles.length})
          </button>
          <button
            onClick={() => setActiveTab('recommendations')}
            className={`pb-4 font-semibold text-sm transition-all border-b-2 ${
              activeTab === 'recommendations' ? 'border-teal-500 text-teal-400' : 'border-transparent text-slate-400 hover:text-slate-200'
            }`}
          >
            ✨ Algorithmic Recommendations ({recommendations.length})
          </button>
        </div>

        {/* Marketplace Grid */}
        {activeTab === 'marketplace' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {profiles.map((card) => (
              <div key={card.id} className="bg-slate-800/70 border border-slate-700 hover:border-teal-500/50 rounded-xl p-6 transition-all shadow-lg flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="text-lg font-bold text-white">{card.fullName}</h3>
                      <span className="inline-block mt-1 px-2.5 py-0.5 bg-teal-500/10 border border-teal-500/30 text-teal-400 text-xs font-semibold rounded-md">
                        {card.primaryRole}
                      </span>
                    </div>
                    <span className="text-xs font-mono text-slate-400 bg-slate-900/80 px-2 py-1 rounded border border-slate-700">
                      🌐 {card.timezone || 'UTC'}
                    </span>
                  </div>

                  <p className="text-xs text-slate-300 mt-4 line-clamp-3">{card.bio || "Building high-performance apps."}</p>

                  <div className="mt-4">
                    <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider">Top Skills:</span>
                    <div className="flex flex-wrap gap-1.5 mt-1.5">
                      {card.skillsJson && card.skillsJson.replaceAll("[\\[\\]\"']", "").split(',').map((sk, i) => (
                        <span key={i} className="px-2 py-0.5 bg-slate-700 border border-slate-600 rounded text-[11px] font-mono text-slate-200">
                          {sk.trim()}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>

                <div className="mt-6 pt-4 border-t border-slate-700/60 flex items-center justify-between">
                  <span className="text-xs text-emerald-400 font-medium flex items-center gap-1">
                    🟢 Available for Team
                  </span>
                  <button
                    onClick={() => alert(`Invite sent to ${card.fullName}!`)}
                    className="px-3.5 py-1.5 bg-teal-600 hover:bg-teal-500 text-white font-medium text-xs rounded-lg transition"
                  >
                    📩 Send Join Invite
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Recommendations Grid */}
        {activeTab === 'recommendations' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {recommendations.map((rec, idx) => (
              <div key={idx} className="bg-slate-800/80 border border-teal-500/40 rounded-xl p-6 shadow-xl flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="text-lg font-bold text-white">{rec.fullName}</h3>
                      <p className="text-xs text-teal-400 font-medium">{rec.primaryRole}</p>
                    </div>
                    <span className="px-3 py-1 bg-gradient-to-r from-emerald-500 to-teal-500 text-slate-950 text-xs font-extrabold rounded-full shadow-md">
                      {rec.matchPercentage}% Match
                    </span>
                  </div>

                  <p className="text-xs text-slate-300 mt-4">{rec.bio}</p>

                  <div className="mt-4">
                    <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wider">Complementary Skill Set:</span>
                    <div className="flex flex-wrap gap-1.5 mt-1.5">
                      {rec.skillsJson && rec.skillsJson.replaceAll("[\\[\\]\"']", "").split(',').map((sk, i) => (
                        <span key={i} className="px-2 py-0.5 bg-teal-950/60 border border-teal-700/50 rounded text-[11px] font-mono text-teal-300">
                          {sk.trim()}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>

                <div className="mt-6 pt-4 border-t border-slate-700/60 flex items-center justify-between">
                  <span className="text-xs font-mono text-slate-400">🌐 {rec.timezone}</span>
                  <button
                    onClick={() => alert(`Invite sent to ${rec.fullName}!`)}
                    className="px-4 py-1.5 bg-emerald-600 hover:bg-emerald-500 text-white font-semibold text-xs rounded-lg transition shadow"
                  >
                    ✨ Team Up Now
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Modal: Publish Skill Card */}
        {showCreateModal && (
          <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center p-4 z-50">
            <div className="bg-slate-800 border border-slate-700 rounded-2xl max-w-lg w-full p-6 space-y-6 shadow-2xl">
              <div className="flex justify-between items-center">
                <h3 className="text-lg font-bold text-white">Publish "Looking for Team" Skill Card</h3>
                <button onClick={() => setShowCreateModal(false)} className="text-slate-400 hover:text-white">✕</button>
              </div>

              <form onSubmit={handlePublishSkillCard} className="space-y-4">
                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">Full Name</label>
                  <input
                    type="text"
                    required
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    placeholder="Alex Morgan"
                    className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">Email</label>
                  <input
                    type="email"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="alex@hackforge.io"
                    className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-medium text-slate-300 mb-1">Primary Role</label>
                    <select
                      value={primaryRole}
                      onChange={(e) => setPrimaryRole(e.target.value)}
                      className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                    >
                      <option value="Frontend Developer">Frontend Developer</option>
                      <option value="Backend Engineer">Backend Engineer</option>
                      <option value="ML Engineer">ML / AI Engineer</option>
                      <option value="UI/UX Designer">UI/UX Designer</option>
                      <option value="Full Stack">Full Stack Architect</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-xs font-medium text-slate-300 mb-1">Time Zone</label>
                    <input
                      type="text"
                      value={timezone}
                      onChange={(e) => setTimezone(e.target.value)}
                      placeholder="EST / UTC-5"
                      className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">Top Skills (comma-separated)</label>
                  <input
                    type="text"
                    required
                    value={skills}
                    onChange={(e) => setSkills(e.target.value)}
                    placeholder="React, TypeScript, PyTorch, Docker"
                    className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">Short Bio</label>
                  <textarea
                    rows={3}
                    value={bio}
                    onChange={(e) => setBio(e.target.value)}
                    placeholder="Passionate developer eager to build AI and FinTech hackathon entries..."
                    className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-teal-500"
                  ></textarea>
                </div>

                <div className="flex justify-end gap-3 pt-2">
                  <button
                    type="button"
                    onClick={() => setShowCreateModal(false)}
                    className="px-4 py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 font-medium text-xs rounded-lg transition"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 bg-teal-600 hover:bg-teal-500 text-white font-semibold text-xs rounded-lg transition shadow-md"
                  >
                    Publish Skill Card
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
