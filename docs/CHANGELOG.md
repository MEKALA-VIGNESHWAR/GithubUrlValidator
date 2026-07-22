# 📜 HackForge Changelog

All notable changes to the **HackForge** platform are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [2.5.0] - 2026-07-22

### Added
- **Spring Security OAuth2 Client Integration**: Integrated Google OAuth 2.0 (`/oauth2/authorization/google`) with custom `OAuth2SuccessHandler` generating JWT access and refresh tokens.
- **AuthProvider Enum**: Created `LOCAL` and `GOOGLE` authentication providers.
- **User Profile Picture**: Added `profilePicture` field support across backend JPA entity, REST DTOs, and React navbar avatar dropdown.
- **Landing Login Page (`LoginPage.jsx`)**: Full-screen landing screen gating unauthenticated visitors, featuring the styled white **"Continue with Google"** button.
- **Modern SaaS Color Palette (80–15–5 Rule)**:
  - `80%`: `#050816` Deep Midnight background & `#0F172A` slate cards.
  - `15%`: `#2563EB` Primary engineering blue for nav tabs, category pills, input focus rings, and analytics charts.
  - `5%`: `#F97316` Accent orange for Submit CTAs, countdown warnings, notification badges, and leaderboard highlights.
- **Documentation Suite**: Added `/docs` folder containing 12 structured architectural markdown documentation files.

### Changed
- **Role-Based Access Control (RBAC)**: Restricted Admin View (`AdminDashboard`) and `🛡️ Admin` header tab strictly to `ADMIN` and `JUDGE` roles.
- **Profile Dropdown Layering**: Increased z-index to `9999` with top offset positioning (`top: calc(100% + 8px)`) to resolve overlap issues.

### Fixed
- Fixed circular dependency error on `passwordEncoder` bean in `OAuth2SuccessHandler.java`.
- Fixed missing `existsByUsernameIgnoreCase` and `existsByEmailIgnoreCase` method declarations in `UserRepository.java`.
- Fixed `401 Unauthorized` handling on `/oauth2/authorization/google` by configuring `SessionCreationPolicy.IF_REQUIRED` and adding `/favicon.ico` to `.permitAll()`.

---

## [2.0.0] - 2026-07-20

### Added
- GitHub REST API live integration (stars, forks, last commit dates).
- Extended team details form (Leader Name, Email, College, Phone, Team Members).
- File upload UI with size validations (PPT ≤ 50MB, PDF ≤ 20MB, Video ≤ 200MB).
- Formula-ranked Leaderboard: `Score = Stars × 2 + Rating × 5 + Completeness × 3`.
- CSV and PDF report export endpoints (`/api/admin/export/csv`, `/api/admin/export/pdf`).

---

## [1.0.0] - 2026-07-15

### Added
- Initial release of HackForge Platform.
- Spring Boot 3 REST API endpoints for user registration and project submission.
- Standard JWT authentication filter (`JwtAuthenticationFilter`).
- React single-page frontend.
