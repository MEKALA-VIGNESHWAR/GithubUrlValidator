-- Flyway Seed Data V1.1: System Admin & Base Data
-- Admin Default Credentials: admin@hackforge.io / Admin123! (hashed using BCrypt)

INSERT INTO users (username, email, password, role, full_name, bio, email_verified, created_at)
VALUES (
    'admin',
    'admin@hackforge.io',
    '$2a$12$K8M9z71wR0vOqB10uR7y7.t8cK5jB6L6p4eY9Z0w1X2y3z4a5b6c7', -- Admin123!
    'ADMIN',
    'HackForge Platform Admin',
    'Super administrator for HackForge platform.',
    TRUE,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

INSERT INTO organizations (name, description, website_url, created_at)
VALUES (
    'HackForge Global',
    'Official HackForge platform organization.',
    'https://hackforge.io',
    CURRENT_TIMESTAMP
) ON CONFLICT DO NOTHING;
