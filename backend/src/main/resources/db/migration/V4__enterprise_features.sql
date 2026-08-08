-- Flyway Migration V4: Enterprise Feature Additions & Outbox Pattern

-- Plagiarism Reports Table
CREATE TABLE IF NOT EXISTS plagiarism_reports (
    id BIGSERIAL PRIMARY KEY,
    submission_id BIGINT NOT NULL,
    similarity_score DOUBLE PRECISION NOT NULL,
    matched_submission_id BIGINT,
    matched_source_url VARCHAR(1000),
    flagged_snippets_json TEXT,
    status VARCHAR(50) DEFAULT 'COMPLETED',
    scanned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_plagiarism_submission_id ON plagiarism_reports(submission_id);

-- Transactional Outbox Events Table
CREATE TABLE IF NOT EXISTS outbox_events (
    id VARCHAR(100) PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_outbox_status_created ON outbox_events(status, created_at);

-- Skill Cards (Teammate Matchmaking Marketplace)
CREATE TABLE IF NOT EXISTS skill_cards (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,
    primary_role VARCHAR(100) NOT NULL,
    skills_json TEXT NOT NULL,
    timezone VARCHAR(50) DEFAULT 'UTC',
    bio TEXT,
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    looking_for_team BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_skill_cards_role ON skill_cards(primary_role);
CREATE INDEX IF NOT EXISTS idx_skill_cards_looking ON skill_cards(looking_for_team);

-- Judge Expertise Mapping
CREATE TABLE IF NOT EXISTS judge_expertise (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    expertise_tags_json TEXT NOT NULL,
    max_workload INT DEFAULT 10,
    current_workload INT DEFAULT 0
);

-- Certificates Table Enhancements
CREATE TABLE IF NOT EXISTS certificates (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hackathon_id BIGINT,
    participant_name VARCHAR(255) NOT NULL,
    achievement_title VARCHAR(255) NOT NULL,
    verification_token VARCHAR(255) NOT NULL UNIQUE,
    badge_json TEXT,
    pdf_url VARCHAR(1000),
    qr_code_url VARCHAR(1000),
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_certificates_token ON certificates(verification_token);
CREATE INDEX IF NOT EXISTS idx_certificates_user ON certificates(user_id);
