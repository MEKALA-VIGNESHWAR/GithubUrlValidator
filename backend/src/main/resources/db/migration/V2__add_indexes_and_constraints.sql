-- Flyway Migration V2: Indexes and Referential Constraints

-- User Indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Hackathon Indexes
CREATE INDEX IF NOT EXISTS idx_hackathons_status ON hackathons(status);
CREATE INDEX IF NOT EXISTS idx_hackathons_start_date ON hackathons(start_date);
CREATE INDEX IF NOT EXISTS idx_hackathons_deleted ON hackathons(deleted);

-- Submission Indexes
CREATE INDEX IF NOT EXISTS idx_submissions_hackathon_id ON submissions(hackathon_id);
CREATE INDEX IF NOT EXISTS idx_submissions_status ON submissions(status);
CREATE INDEX IF NOT EXISTS idx_submissions_score ON submissions(score DESC);
CREATE INDEX IF NOT EXISTS idx_submissions_created_at ON submissions(created_at);

-- Team & TeamMember Indexes
CREATE INDEX IF NOT EXISTS idx_teams_hackathon_id ON teams(hackathon_id);
CREATE INDEX IF NOT EXISTS idx_team_members_team_id ON team_members(team_id);
CREATE INDEX IF NOT EXISTS idx_team_members_user_id ON team_members(user_id);

-- Judge Evaluations
CREATE INDEX IF NOT EXISTS idx_judge_eval_submission ON judge_evaluations(submission_id);
CREATE INDEX IF NOT EXISTS idx_judge_eval_judge ON judge_evaluations(judge_id);

-- Notifications
CREATE INDEX IF NOT EXISTS idx_notifications_user_read ON notifications(user_id, read);

-- Bookmarks
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_hackathon_bookmark ON bookmarks(user_id, hackathon_id);
