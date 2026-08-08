import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function SponsorPortal() {
  const [candidates, setCandidates] = useState([]);
  const [trackSubmissions, setTrackSubmissions] = useState([]);
  const [activeTab, setActiveTab] = useState('candidates');
  const [skillFilter, setSkillFilter] = useState('');
  const [selectedTrack, setSelectedTrack] = useState('All');
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [outreachMessage, setOutreachMessage] = useState('');
  const [outreachSent, setOutreachSent] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchCandidates();
    fetchTrackSubmissions();
  }, [skillFilter, selectedTrack]);

  const fetchCandidates = async () => {
    setLoading(true);
    try {
      const url = skillFilter ? `/api/sponsors/candidates?skill=${encodeURIComponent(skillFilter)}` : '/api/sponsors/candidates';
      const res = await axios.get(url);
      setCandidates(res.data);
    } catch (err) {
      console.error("Failed to fetch candidates", err);
    } finally {
      setLoading(false);
    }
  };

  const fetchTrackSubmissions = async () => {
    try {
      const res = await axios.get(`/api/sponsors/track-submissions?track=${encodeURIComponent(selectedTrack)}`);
      setTrackSubmissions(res.data);
    } catch (err) {
      console.error("Failed to fetch track submissions", err);
    }
  };

  const handleSendOutreach = async (e) => {
    e.preventDefault();
    if (!selectedCandidate || !outreachMessage) return;

    try {
      await axios.post('/api/sponsors/outreach', {
        candidateEmail: selectedCandidate.email,
        message: outreachMessage,
        sponsorCompany: 'Enterprise Sponsor'
      });
      setOutreachSent(true);
      setTimeout(() => {
        setOutreachSent(false);
        setSelectedCandidate(null);
        setOutreachMessage('');
      }, 2500);
    } catch (err) {
      console.error("Failed to send outreach", err);
    }
  };

  return (
    <div className="bg-slate-900 text-slate-100 min-h-screen p-6 font-sans">
      <div className="max-w-7xl mx-auto space-y-8">
        
        {/* Header Banner */}
        <div className="bg-gradient-to-r from-indigo-900 via-purple-900 to-slate-900 border border-indigo-500/30 rounded-2xl p-8 shadow-2xl backdrop-blur-xl">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
            <div>
              <div className="inline-flex items-center space-x-2 px-3 py-1 bg-indigo-500/10 border border-indigo-500/30 rounded-full text-indigo-400 text-xs font-semibold uppercase tracking-wider mb-3">
                <span>🏢 Sponsor & Recruiter Portal</span>
              </div>
              <h1 className="text-3xl font-extrabold text-white tracking-tight">Candidate Sourcing & Sponsored Tracks</h1>
              <p className="text-slate-400 mt-2 max-w-2xl">
                Discover top hackathon talent, evaluate track submissions, view GitHub & resume profiles, and send direct outreach messages.
              </p>
            </div>
            
            <div className="flex bg-slate-800/80 p-1.5 rounded-xl border border-slate-700">
              <button
                onClick={() => setActiveTab('candidates')}
                className={`px-5 py-2.5 rounded-lg text-sm font-semibold transition-all ${
                  activeTab === 'candidates' ? 'bg-indigo-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'
                }`}
              >
                🎯 Candidate Sourcing
              </button>
              <button
                onClick={() => setActiveTab('tracks')}
                className={`px-5 py-2.5 rounded-lg text-sm font-semibold transition-all ${
                  activeTab === 'tracks' ? 'bg-indigo-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'
                }`}
              >
                🏆 Sponsored Track Submissions
              </button>
            </div>
          </div>
        </div>

        {/* Tab 1: Candidate Sourcing */}
        {activeTab === 'candidates' && (
          <div className="space-y-6">
            {/* Filter Bar */}
            <div className="flex flex-col md:flex-row items-center justify-between gap-4 bg-slate-800/60 p-4 rounded-xl border border-slate-700/80">
              <div className="flex items-center space-x-3 w-full md:w-auto">
                <input
                  type="text"
                  placeholder="Filter by Tech Stack (e.g. Java, Python, React)..."
                  value={skillFilter}
                  onChange={(e) => setSkillFilter(e.target.value)}
                  className="w-full md:w-96 px-4 py-2.5 bg-slate-900 border border-slate-700 rounded-lg text-sm focus:outline-none focus:border-indigo-500"
                />
                <button
                  onClick={fetchCandidates}
                  className="px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-medium text-sm rounded-lg transition"
                >
                  Search
                </button>
              </div>

              <div className="text-slate-400 text-sm font-medium">
                Found <span className="text-indigo-400 font-bold">{candidates.length}</span> Verified Developers
              </div>
            </div>

            {/* Candidates Grid */}
            <div className="grid grid-[#1] md:grid-cols-2 lg:grid-cols-3 gap-6">
              {candidates.map((cand) => (
                <div key={cand.userId} className="bg-slate-800/70 border border-slate-700/80 hover:border-indigo-500/50 rounded-xl p-6 transition-all shadow-lg hover:shadow-indigo-500/10 flex flex-col justify-between">
                  <div>
                    <div className="flex justify-between items-start">
                      <div>
                        <h3 className="text-lg font-bold text-white">{cand.fullName}</h3>
                        <p className="text-xs text-indigo-400 font-mono mt-0.5">{cand.college}</p>
                      </div>
                      <span className="px-2.5 py-1 bg-amber-500/10 border border-amber-500/30 text-amber-400 text-xs font-bold rounded-lg">
                        ⭐ {cand.judgeRating} / 10
                      </span>
                    </div>

                    <div className="mt-4 space-y-2">
                      <div className="text-xs text-slate-400">
                        <span className="font-semibold text-slate-300">Project:</span> {cand.projectTitle}
                      </div>
                      <div className="text-xs text-slate-400">
                        <span className="font-semibold text-slate-300">Tech Stack:</span>
                        <div className="flex flex-wrap gap-1.5 mt-1.5">
                          {cand.techStack.split(',').map((skill, i) => (
                            <span key={i} className="px-2 py-0.5 bg-slate-700/80 border border-slate-600 rounded text-[11px] font-mono text-slate-200">
                              {skill.trim()}
                            </span>
                          ))}
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="mt-6 pt-4 border-t border-slate-700/60 flex items-center justify-between">
                    <a
                      href={cand.githubUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-xs text-slate-400 hover:text-indigo-400 font-medium inline-flex items-center gap-1"
                    >
                      🔗 GitHub Profile
                    </a>

                    <button
                      onClick={() => setSelectedCandidate(cand)}
                      className="px-3.5 py-1.5 bg-indigo-600/90 hover:bg-indigo-500 text-white font-medium text-xs rounded-lg transition shadow-md"
                    >
                      ✉️ Direct Outreach
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Tab 2: Sponsored Track Submissions */}
        {activeTab === 'tracks' && (
          <div className="space-y-6">
            <div className="flex items-center gap-4 bg-slate-800/60 p-4 rounded-xl border border-slate-700/80">
              <label className="text-sm font-semibold text-slate-300">Select Sponsored Track:</label>
              <select
                value={selectedTrack}
                onChange={(e) => setSelectedTrack(e.target.value)}
                className="px-4 py-2 bg-slate-900 border border-slate-700 rounded-lg text-sm focus:outline-none focus:border-indigo-500 text-slate-200"
              >
                <option value="All">All Tracks</option>
                <option value="AI/ML">AI & Machine Learning Track</option>
                <option value="FinTech">FinTech & Payments Challenge</option>
                <option value="Web3">Web3 & Decentralized Cloud</option>
                <option value="Enterprise">Enterprise SaaS & Security</option>
              </select>
            </div>

            <div className="space-y-4">
              {trackSubmissions.map((sub) => (
                <div key={sub.id} className="bg-slate-800/80 border border-slate-700 rounded-xl p-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                  <div>
                    <div className="flex items-center gap-3">
                      <h3 className="text-xl font-bold text-white">{sub.projectTitle}</h3>
                      <span className="px-2.5 py-0.5 bg-indigo-500/10 border border-indigo-500/30 text-indigo-400 text-xs font-semibold rounded-full">
                        {sub.category || 'General'}
                      </span>
                    </div>
                    <p className="text-sm text-slate-300 mt-2 max-w-3xl">{sub.description}</p>
                    <div className="text-xs text-slate-400 mt-3">
                      <span className="font-semibold text-slate-300">Team:</span> {sub.teamName} | <span className="font-semibold text-slate-300">Leader:</span> {sub.leaderName} ({sub.email})
                    </div>
                  </div>

                  <div className="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto">
                    <a
                      href={sub.githubRepoUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="w-full sm:w-auto text-center px-4 py-2 bg-slate-700 hover:bg-slate-600 text-white font-medium text-xs rounded-lg transition"
                    >
                      💻 View Repo
                    </a>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Outreach Modal */}
        {selectedCandidate && (
          <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center p-4 z-50">
            <div className="bg-slate-800 border border-slate-700 rounded-2xl max-w-lg w-full p-6 space-y-6 shadow-2xl">
              <div className="flex justify-between items-center">
                <h3 className="text-lg font-bold text-white">Send Direct Message to {selectedCandidate.fullName}</h3>
                <button onClick={() => setSelectedCandidate(null)} className="text-slate-400 hover:text-white">✕</button>
              </div>

              {outreachSent ? (
                <div className="p-4 bg-emerald-500/10 border border-emerald-500/30 rounded-xl text-emerald-400 text-sm font-semibold text-center">
                  ✅ Direct outreach message sent successfully!
                </div>
              ) : (
                <form onSubmit={handleSendOutreach} className="space-y-4">
                  <div>
                    <label className="block text-xs font-medium text-slate-300 mb-1.5">Candidate Email</label>
                    <input
                      type="text"
                      disabled
                      value={selectedCandidate.email}
                      className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-slate-400 text-sm font-mono"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-medium text-slate-300 mb-1.5">Outreach Message / Offer / Interview Invitation</label>
                    <textarea
                      rows={4}
                      value={outreachMessage}
                      onChange={(e) => setOutreachMessage(e.target.value)}
                      placeholder="Hi! We were impressed by your HackForge project submission and would love to chat about engineering roles at our company..."
                      className="w-full px-3.5 py-2 bg-slate-900 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-indigo-500"
                      required
                    ></textarea>
                  </div>

                  <div className="flex justify-end gap-3">
                    <button
                      type="button"
                      onClick={() => setSelectedCandidate(null)}
                      className="px-4 py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 font-medium text-xs rounded-lg transition"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      className="px-5 py-2 bg-indigo-600 hover:bg-indigo-500 text-white font-medium text-xs rounded-lg transition shadow-md"
                    >
                      Send Outreach Email
                    </button>
                  </div>
                </form>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
