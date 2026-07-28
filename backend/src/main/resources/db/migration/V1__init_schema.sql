-- Flyway Migration V1: Initial Schema for HackForge

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'PARTICIPANT',
    profile_picture VARCHAR(500),
    provider_id VARCHAR(255),
    provider VARCHAR(50) DEFAULT 'LOCAL',
    organization_id BIGINT,
    full_name VARCHAR(150),
    bio TEXT,
    skills TEXT,
    github_url VARCHAR(500),
    linkedin_url VARCHAR(500),
    resume_url VARCHAR(500),
    portfolio_url VARCHAR(500),
    country VARCHAR(100),
    university VARCHAR(255),
    refresh_token TEXT,
    reset_password_token VARCHAR(255),
    reset_password_token_expiry TIMESTAMP,
    email_verified BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    logo_url VARCHAR(500),
    description TEXT,
    website_url VARCHAR(500),
    owner_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hackathons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'UPCOMING',
    banner_url VARCHAR(500),
    max_team_size INT DEFAULT 4,
    organization_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS submissions (
    id BIGSERIAL PRIMARY KEY,
    team_name VARCHAR(150) NOT NULL UNIQUE,
    project_title VARCHAR(255) NOT NULL,
    github_repo_url VARCHAR(500),
    leader_name VARCHAR(255),
    email VARCHAR(255),
    college VARCHAR(255),
    description TEXT,
    tech_stack TEXT,
    demo_video_url VARCHAR(500),
    ppt_url VARCHAR(500),
    pdf_url VARCHAR(500),
    phone_number VARCHAR(100),
    members TEXT,
    problem_statement TEXT,
    category VARCHAR(100),
    project_image_url VARCHAR(500),
    difficulty VARCHAR(50),
    completion_rate INT,
    judge_comment TEXT,
    languages_json TEXT,
    submitted_at TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    repo_owner VARCHAR(255),
    repo_name VARCHAR(255),
    stars INT,
    forks INT,
    open_issues INT,
    last_commit_date VARCHAR(100),
    judge_rating DOUBLE PRECISION,
    score DOUBLE PRECISION DEFAULT 0.0,
    file_path VARCHAR(500),
    submitted_by VARCHAR(100),
    hackathon_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    hackathon_id BIGINT NOT NULL,
    leader_id BIGINT NOT NULL,
    invite_code VARCHAR(100) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS team_members (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(50) DEFAULT 'MEMBER',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS judge_evaluations (
    id BIGSERIAL PRIMARY KEY,
    submission_id BIGINT NOT NULL,
    judge_id BIGINT NOT NULL,
    innovation_score DOUBLE PRECISION DEFAULT 0.0,
    technical_score DOUBLE PRECISION DEFAULT 0.0,
    design_score DOUBLE PRECISION DEFAULT 0.0,
    presentation_score DOUBLE PRECISION DEFAULT 0.0,
    total_score DOUBLE PRECISION DEFAULT 0.0,
    feedback TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS certificates (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hackathon_id BIGINT NOT NULL,
    certificate_code VARCHAR(100) NOT NULL UNIQUE,
    certificate_url VARCHAR(500),
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) DEFAULT 'INFO',
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookmarks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hackathon_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS file_entities (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL,
    s3_key VARCHAR(500) NOT NULL,
    uploaded_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
