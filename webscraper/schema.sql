-- Database Schema for Events Scraper Pipeline (PostgreSQL)

CREATE TYPE event_category AS ENUM (
    'internship',
    'hackathon',
    'tech_event',
    'webinar',
    'coding_competition',
    'campus_event',
    'career_fair'
);

CREATE TABLE IF NOT EXISTS events (
    id VARCHAR(64) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type event_category NOT NULL,
    organizer VARCHAR(255) NOT NULL,
    is_online BOOLEAN NOT NULL DEFAULT TRUE,
    city VARCHAR(100),
    country VARCHAR(100),
    venue VARCHAR(255),
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ,
    deadline TIMESTAMPTZ,
    description TEXT NOT NULL,
    application_url TEXT NOT NULL UNIQUE,
    source_website VARCHAR(100) NOT NULL,
    tags TEXT[] DEFAULT '{}',
    image_url TEXT,
    score NUMERIC(5, 2) DEFAULT 0.0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_events_type ON events(type);
CREATE INDEX IF NOT EXISTS idx_events_start_date ON events(start_date);
CREATE INDEX IF NOT EXISTS idx_events_deadline ON events(deadline);
CREATE INDEX IF NOT EXISTS idx_events_score ON events(score DESC);
CREATE INDEX IF NOT EXISTS idx_events_is_online ON events(is_online);
CREATE INDEX IF NOT EXISTS idx_events_tags ON events USING GIN(tags);
