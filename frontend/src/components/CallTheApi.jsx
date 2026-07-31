import React, { useState } from 'react';
import { apiClient } from '../api/apiClient';

export default function CallTheApi({ onEventsImported }) {
  const [source, setSource] = useState('ALL');
  const [customUrl, setCustomUrl] = useState('');
  const [keywords, setKeywords] = useState('AI, Python, React, Cloud');
  const [isScraping, setIsScraping] = useState(false);
  const [scrapingStep, setScrapingStep] = useState('');
  const [scrapedEvents, setScrapedEvents] = useState([]);
  const [importedIds, setImportedIds] = useState(new Set());
  const [isImporting, setIsImporting] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const handleRunScraper = async (e) => {
    e?.preventDefault();
    setIsScraping(true);
    setErrorMsg('');
    setSuccessMsg('');
    setScrapedEvents([]);
    setImportedIds(new Set());

    setScrapingStep('🌐 Initializing Selenium Chrome Web Driver & Parser Pipeline...');

    try {
      setTimeout(() => {
        setScrapingStep('📡 Fetching target HTML source & parsing event listings...');
      }, 700);

      const interestList = keywords
        .split(',')
        .map(k => k.trim())
        .filter(Boolean);

      const payload = {
        source,
        url: source === 'CUSTOM_URL' || customUrl ? customUrl : '',
        query: keywords,
        interests: interestList
      };

      const results = await apiClient.scrapeEvents(payload);
      
      setScrapingStep('✨ Finalizing deduplication, date filtering & scoring...');
      
      setTimeout(() => {
        setIsScraping(false);
        if (Array.isArray(results) && results.length > 0) {
          setScrapedEvents(results);
          setSuccessMsg(`Found ${results.length} high-quality event(s) matching your criteria!`);
        } else {
          setErrorMsg('No events were retrieved. Try broadening your keywords or checking the URL.');
        }
      }, 500);
    } catch (err) {
      console.error('Web scraper failed:', err);
      setIsScraping(false);
      const isGatewayError = err.message && (err.message.includes('502') || err.message.includes('Failed to fetch'));
      if (isGatewayError) {
        setErrorMsg('Backend server on port 8080 is initializing or unreachable. Please wait 5 seconds and click "Scrape & Fetch Required Events" again.');
      } else {
        setErrorMsg(err.message || 'Scraper execution encountered an error.');
      }
    }
  };


  const handleImportSingle = async (eventItem) => {
    setIsImporting(true);
    try {
      const res = await apiClient.importEvents([eventItem]);
      setImportedIds(prev => new Set(prev).add(eventItem.id));
      setSuccessMsg(`Added "${eventItem.title}" to Events page!`);
      if (onEventsImported) {
        onEventsImported(res.events || []);
      }
    } catch (err) {
      setErrorMsg(err.message || 'Failed to import event to page.');
    } finally {
      setIsImporting(false);
    }
  };

  const handleImportAll = async () => {
    if (scrapedEvents.length === 0) return;
    setIsImporting(true);
    try {
      const unimported = scrapedEvents.filter(e => !importedIds.has(e.id));
      const res = await apiClient.importEvents(unimported);
      const allIds = new Set(scrapedEvents.map(e => e.id));
      setImportedIds(allIds);
      setSuccessMsg(`Successfully imported all ${unimported.length} scraped events to your Events page!`);
      if (onEventsImported) {
        onEventsImported(res.events || []);
      }
    } catch (err) {
      setErrorMsg(err.message || 'Failed to import scraped events.');
    } finally {
      setIsImporting(false);
    }
  };

  return (
    <div className="glass-panel glow-card-orange" style={{ padding: '24px', background: 'linear-gradient(145deg, rgba(15, 23, 42, 0.95), rgba(30, 41, 59, 0.85))', border: '1px solid rgba(255, 107, 0, 0.3)', borderRadius: '16px' }}>
      
      {/* HEADER TITLE */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px', marginBottom: '20px' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span style={{ fontSize: '1.2rem' }}>⚡</span>
            <span style={{ fontSize: '0.75rem', fontWeight: '800', letterSpacing: '1px', color: '#FF8533', textTransform: 'uppercase' }}>
              EVENT SCRAPER & API INTEGRATION HUB
            </span>
          </div>
          <h2 style={{ fontSize: '1.5rem', fontWeight: '800', marginTop: '4px' }} className="gradient-text">
            Web Scraping Event Collector
          </h2>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.84rem' }}>
            Scrape hackathons, tech events, and internships from web sources or custom URLs and add them directly to your Events page.
          </p>
        </div>

        {scrapedEvents.length > 0 && (
          <button
            onClick={handleImportAll}
            disabled={isImporting || importedIds.size === scrapedEvents.length}
            className="btn-primary"
            style={{ padding: '10px 18px', fontSize: '0.88rem', background: 'linear-gradient(135deg, #10b981, #059669)', border: 'none' }}
          >
            {isImporting ? '📥 Importing...' : importedIds.size === scrapedEvents.length ? '✓ All Imported' : `📥 Import All Scraped Events (${scrapedEvents.length})`}
          </button>
        )}
      </div>

      {/* SCRAPER CONFIG FORM */}
      <form onSubmit={handleRunScraper} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px' }}>
          
          {/* SOURCE PRESET SELECTOR */}
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '6px' }}>
              Scraper Source Engine
            </label>
            <select
              className="input-field"
              value={source}
              onChange={(e) => setSource(e.target.value)}
              style={{ width: '100%' }}
            >
              <option value="ALL">🌐 All Sources (Devpost + TechEvents + GitHub)</option>
              <option value="Devpost">🏆 Devpost Hackathons API</option>
              <option value="GitHub Internships">💼 GitHub Tech Internships Feed</option>
              <option value="Tech Events">📅 Global Tech Events & Webinars</option>
              <option value="CUSTOM_URL">🔗 Custom Website URL Scraper</option>
            </select>
          </div>

          {/* INTEREST KEYWORDS */}
          <div>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '6px' }}>
              Filter & Interest Keywords
            </label>
            <input
              type="text"
              className="input-field"
              placeholder="e.g. AI, Python, React, Cloud, Web3"
              value={keywords}
              onChange={(e) => setKeywords(e.target.value)}
              style={{ width: '100%' }}
            />
          </div>

        </div>

        {/* CUSTOM URL INPUT (Shown if CUSTOM_URL selected or requested) */}
        {(source === 'CUSTOM_URL' || customUrl) && (
          <div style={{ animation: 'fadeIn 0.2s ease-in-out' }}>
            <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: '700', color: 'var(--text-secondary)', marginBottom: '6px' }}>
              Target Web Page URL to Scrape
            </label>
            <input
              type="url"
              className="input-field"
              placeholder="https://example-hackathon.com/events or target webpage URL"
              value={customUrl}
              onChange={(e) => setCustomUrl(e.target.value)}
              style={{ width: '100%' }}
              required={source === 'CUSTOM_URL'}
            />
          </div>
        )}

        {/* SCRAPE BUTTON */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
          <button
            type="submit"
            disabled={isScraping}
            className="btn-primary"
            style={{ padding: '12px 24px', fontSize: '0.92rem', background: 'linear-gradient(135deg, #FF6B00, #E65C00)' }}
          >
            {isScraping ? '⚡ Scraping Web...' : '🚀 Scrape & Fetch Required Events'}
          </button>
          
          {isScraping && (
            <span style={{ fontSize: '0.82rem', color: '#60a5fa', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
              {scrapingStep}
            </span>
          )}
        </div>
      </form>

      {/* FEEDBACK MESSAGES */}
      {errorMsg && (
        <div style={{ marginTop: '16px', padding: '12px 16px', borderRadius: '10px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.4)', color: '#fca5a5', fontSize: '0.85rem' }}>
          ⚠️ {errorMsg}
        </div>
      )}

      {successMsg && (
        <div style={{ marginTop: '16px', padding: '12px 16px', borderRadius: '10px', background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.4)', color: '#6ee7b7', fontSize: '0.85rem' }}>
          ✅ {successMsg}
        </div>
      )}

      {/* SCRAPED EVENTS PREVIEW GRID */}
      {scrapedEvents.length > 0 && (
        <div style={{ marginTop: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid var(--card-border)', paddingBottom: '10px' }}>
            <span style={{ fontSize: '0.9rem', fontWeight: '800', color: 'var(--text-primary)' }}>
              Scraped Results ({scrapedEvents.length})
            </span>
            <span style={{ fontSize: '0.78rem', color: 'var(--text-secondary)' }}>
              Click "Add to Events Page" to import into active directory
            </span>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
            {scrapedEvents.map((item) => {
              const isAdded = importedIds.has(item.id);
              const eventType = item.eventType || item.type || 'HACKATHON';
              const locationStr = typeof item.location === 'object' 
                ? (item.location?.is_online ? 'Online / Remote' : item.location?.city || 'TBD') 
                : (item.location || 'Online / Remote');

              return (
                <div
                  key={item.id}
                  style={{
                    background: 'var(--inner-bg)',
                    border: isAdded ? '1px solid #10b981' : '1px solid var(--card-border)',
                    borderRadius: '12px',
                    padding: '18px',
                    display: 'flex',
                    flexDirection: 'column',
                    justify: 'space-between',
                    gap: '14px',
                    transition: 'transform 0.2s ease, border-color 0.2s ease'
                  }}
                >
                  <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                      <span className="status-badge status-approved" style={{ fontSize: '0.65rem' }}>
                        {eventType}
                      </span>
                      {item.score && (
                        <span style={{ fontSize: '0.72rem', fontWeight: '700', color: '#38bdf8', background: 'rgba(56, 189, 248, 0.1)', padding: '2px 8px', borderRadius: '12px' }}>
                          🎯 Score: {item.score}
                        </span>
                      )}
                    </div>

                    <h4 style={{ fontSize: '1.05rem', fontWeight: '800', margin: '4px 0', color: 'var(--text-primary)' }}>
                      {item.title}
                    </h4>
                    
                    <div style={{ fontSize: '0.78rem', color: '#FF8533', fontWeight: '600', marginBottom: '6px' }}>
                      🏢 {item.organizer || 'Organized Community'} • 📍 {locationStr}
                    </div>

                    <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                      {item.description}
                    </p>

                    {item.tags && Array.isArray(item.tags) && (
                      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginTop: '10px' }}>
                        {item.tags.map((tag, idx) => (
                          <span key={idx} style={{ fontSize: '0.68rem', padding: '2px 6px', borderRadius: '4px', background: 'rgba(255, 255, 255, 0.06)', color: 'var(--text-secondary)' }}>
                            #{tag}
                          </span>
                        ))}
                      </div>
                    )}
                  </div>

                  <div style={{ paddingTop: '12px', borderTop: '1px solid var(--card-border)', display: 'flex', gap: '10px' }}>
                    <button
                      onClick={() => handleImportSingle(item)}
                      disabled={isAdded || isImporting}
                      className={isAdded ? 'btn-secondary' : 'btn-primary'}
                      style={{ flex: 1, padding: '8px 12px', fontSize: '0.82rem', background: isAdded ? 'transparent' : undefined }}
                    >
                      {isAdded ? '✓ Added to Events Page' : '➕ Add to Events Page'}
                    </button>
                    
                    {item.applicationUrl && (
                      <a
                        href={item.applicationUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="btn-secondary"
                        style={{ padding: '8px 12px', fontSize: '0.82rem', textDecoration: 'none' }}
                      >
                        🔗 View Link
                      </a>
                    )}
                  </div>

                </div>
              );
            })}
          </div>
        </div>
      )}

    </div>
  );
}
