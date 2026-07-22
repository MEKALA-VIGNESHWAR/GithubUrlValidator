# 🗺️ HackForge Product Roadmap

## 📅 Release Timeline & Milestones

### Phase 1: Core Foundation (v1.0) - Completed ✅
- [x] Basic Spring Boot API structure and MySQL/PostgreSQL JPA entity definitions.
- [x] React single-page frontend with GitHub repository URL regex matching.
- [x] JWT token generation and standard login/register endpoints.

### Phase 2: Platform Transformation (v2.0) - Completed ✅
- [x] Full Dark Glassmorphism UI theme implementation.
- [x] Extended Team details (Leader Name, Email, College, Phone, Team Members).
- [x] GitHub REST API Integration (fetching live stars, forks, and last commit dates).
- [x] File upload UI with limit validation (PPT ≤ 50MB, PDF ≤ 20MB, Video ≤ 200MB).
- [x] Countdown timer displaying days, hours, and minutes until deadline.
- [x] Formula-driven Leaderboard: `Score = Stars × 2 + Rating × 5 + Completeness × 3`.

### Phase 3: Enterprise OAuth2 & SaaS Design (v2.5) - Current Release 🚀
- [x] Adopted modern SaaS color tokens (`#050816` bg, `#2563EB` primary blue, `#F97316` accent orange).
- [x] Enforced strict 80-15-5 design system.
- [x] Integrated Spring Security OAuth2 Client (`/oauth2/authorization/google`).
- [x] Added `AuthProvider` Enum (`LOCAL`, `GOOGLE`) and `profilePicture` support.
- [x] Full-screen landing Login Page (`LoginPage.jsx`) gating unauthenticated visitors.
- [x] Enforced Role-Based Access Control (RBAC) hiding Admin View from regular users.

### Phase 4: Future Enhancements (v3.0) - Planned 🔮
- [ ] **AI Plagiarism & Code Originality Inspector**: Automated AST code similarity scoring against known public repos.
- [ ] **WebSockets / Real-Time Notification Stream**: STOMP over SockJS notification stream for live judge feedback.
- [ ] **Automated Certificate & Badge Generator**: PDF certificate generation with digital signature for all participants.
- [ ] **Peer Voting & Audience Favorite Award**: Public voting portal with IP rate limiting and anti-bot checks.
