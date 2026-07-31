import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/apiClient';
import CallTheApi from './CallTheApi';

export default function EventsDiscovery() {
  const [hackathons, setHackathons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedMode, setSelectedMode] = useState('ALL');
  const [registeredEventIds, setRegisteredEventIds] = useState([]);
  const [showScraper, setShowScraper] = useState(false);

  useEffect(() => {
    fetchHackathons();
  }, []);

  const fetchHackathons = async () => {
    try {
      const data = await apiClient.get('/hackathons');
      setHackathons(Array.isArray(data) ? data : data.content || []);
    } catch (err) {
      console.error('Failed to fetch hackathons', err);
    } finally {
      setLoading(false);
    }
  };

  const handleEventsImported = (newEvents) => {
    fetchHackathons();
  };

  const toggleRegister = (id) => {
    if (registeredEventIds.includes(id)) {
      setRegisteredEventIds(prev => prev.filter(eId => eId !== id));
    } else {
      setRegisteredEventIds(prev => [...prev, id]);
    }
  };

  const filteredEvents = hackathons.filter(event => {
    const title = event.title || event.name || '';
    const matchesSearch = title.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesMode = selectedMode === 'ALL' || (event.eventType || event.mode) === selectedMode;
    return matchesSearch && matchesMode;
  });

  return (
    <div className="animate-fade-in" style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* --- HERO HEADER --- */}
      <div className="glass-panel glow-card-blue" style={{ padding: '28px', background: 'linear-gradient(135deg, rgba(15, 23, 42, 0.95), rgba(37, 99, 235, 0.14))' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
              <span style={{ fontSize: '1.2rem' }}>🌐</span>
              <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1.2px', color: '#60a5fa', textTransform: 'uppercase' }}>
                PRODUCTION HACKATHON DIRECTORY
              </span>
            </div>
            <h1 style={{ fontSize: '2.2rem', fontWeight: '800', margin: '4px 0' }} className="gradient-text">
              Explore Events
            </h1>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
              Discover active hackathons running on the HackForge SaaS platform or scrape & add new web events.
            </p>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', flexWrap: 'wrap' }}>
            <button
              onClick={() => setShowScraper(!showScraper)}
              className="btn-primary"
              style={{
                padding: '10px 18px',
                fontSize: '0.88rem',
                background: showScraper ? 'rgba(255, 255, 255, 0.1)' : 'linear-gradient(135deg, #FF6B00, #E65C00)',
                border: '1px solid rgba(255, 107, 0, 0.4)'
              }}
            >
              {showScraper ? '❌ Close Web Scraper Form' : '⚡ Open Web Scraper & Event Collector'}
            </button>

            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', background: 'var(--inner-bg)', padding: '10px 18px', borderRadius: '14px', border: '1px solid var(--card-border)' }}>
              <div>
                <div style={{ fontSize: '0.7rem', color: 'var(--text-secondary)' }}>ACTIVE EVENTS</div>
                <div style={{ fontSize: '1.2rem', fontWeight: '800', color: '#10b981' }}>{hackathons.length} Events</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* --- WEB SCRAPING EVENT FORM PANEL --- */}
      {showScraper && (
        <div style={{ animation: 'fadeIn 0.3s ease-in-out' }}>
          <CallTheApi onEventsImported={handleEventsImported} />
        </div>
      )}

      {/* --- SEARCH & FILTERS --- */}
      <div className="glass-panel" style={{ padding: '18px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <input
          type="text"
          className="input-field"
          placeholder="🔍 Search events by name..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          style={{ maxWidth: '380px' }}
        />

        <select className="input-field" value={selectedMode} onChange={(e) => setSelectedMode(e.target.value)} style={{ width: '140px' }}>
          <option value="ALL">All Modes</option>
          <option value="ONLINE">Online</option>
          <option value="HYBRID">Hybrid</option>
          <option value="OFFLINE">Offline</option>
        </select>
      </div>

      {/* --- EVENTS GRID --- */}
      {loading ? (
        <div className="glass-panel" style={{ padding: '48px', textAlign: 'center' }}>
          <span style={{ fontSize: '1.1rem', color: 'var(--text-secondary)' }}>Loading hackathons from server...</span>
        </div>
      ) : filteredEvents.length === 0 ? (
        <div className="glass-panel" style={{ padding: '48px', textAlign: 'center', background: 'var(--inner-bg)', border: '1px solid var(--card-border)' }}>
          <span style={{ fontSize: '2.5rem' }}>📅</span>
          <h3 style={{ fontSize: '1.3rem', fontWeight: '700', marginTop: '12px', color: 'var(--text-primary)' }}>No Events Found</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.88rem', marginTop: '6px', marginBottom: '16px' }}>
            No active hackathons currently match your query filter.
          </p>
          {!showScraper && (
            <button
              onClick={() => setShowScraper(true)}
              className="btn-primary"
              style={{ padding: '10px 20px', fontSize: '0.88rem' }}
            >
              ⚡ Scrape Events from Web Now
            </button>
          )}
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '24px' }}>
          {filteredEvents.map((event) => {
            const isRegistered = registeredEventIds.includes(event.id);
            return (
              <div key={event.id} className="glass-panel" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', gap: '20px' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                    <span className="status-badge status-approved" style={{ fontSize: '0.65rem' }}>
                      {event.eventType || event.status || 'ONLINE'}
                    </span>
                    {event.organizer && (
                      <span style={{ fontSize: '0.72rem', color: 'var(--text-secondary)' }}>
                        🏢 {event.organizer}
                      </span>
                    )}
                  </div>

                  <h3 style={{ fontSize: '1.25rem', fontWeight: '800', marginBottom: '4px' }}>{event.title || event.name}</h3>
                  <p style={{ fontSize: '0.82rem', color: 'var(--text-secondary)', marginBottom: '12px' }}>
                    {event.description || 'Global Hackathon Competition'}
                  </p>

                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', fontSize: '0.75rem', color: '#60a5fa' }}>
                    {event.prizePool && <span>🏆 Prize: {event.prizePool}</span>}
                    {event.location && <span>• 📍 {event.location}</span>}
                  </div>
                </div>

                <div style={{ paddingTop: '16px', borderTop: '1px solid var(--card-border)' }}>
                  <button
                    onClick={() => toggleRegister(event.id)}
                    className={isRegistered ? 'btn-secondary' : 'btn-primary'}
                    style={{ width: '100%', padding: '10px', fontSize: '0.88rem' }}
                  >
                    {isRegistered ? '🚀 Participated' : '⚡ Register Now'}
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}

    </div>
  );
}
