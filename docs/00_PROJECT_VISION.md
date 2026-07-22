# ⚡ HackForge - Multi-Tenant AI-Powered Hackathon Operating System

## 1. Executive Summary & Product Vision
**HackForge** is an enterprise-grade, multi-tenant, AI-powered Hackathon Operating System comparable to Devfolio, HackerEarth, and Unstop. Built for colleges, universities, startups, accelerators, and global enterprises, HackForge manages the complete innovation event lifecycle across online, offline, and hybrid formats.

### Core Lifecycles Managed
`Applications` → `Team Formation & AI Matching` → `Mentoring & SLA Support` → `Project Submission` → `AI Review & GitHub Intelligence` → `Advanced Judging & Anomaly Detection` → `Demo Day Control Center` → `Live Leaderboard` → `QR Verification & Certificates` → `Sponsor Hiring Suite` → `Developer Passport & Alumni Network`

---

## 2. Multi-Tenant SaaS Architecture
- **Tenant Isolation**: Shared Database with Discriminator `tenant_id` (`organization_id`) & Tenant-Aware Row-Level Security (RLS).
- **Multi-Tenant Entities**: `organizations`, `hackathons`, `tracks`, `challenges`, `teams`, `submissions`.
- **White-Label Support**: Custom domains, custom CSS variables, tenant branding logos, and custom registration forms.

---

## 3. Role-Based Access Control (RBAC) Matrix

| Role | Tenant Scope | Event Scope | Key Capabilities |
| :--- | :--- | :--- | :--- |
| **SUPER_ADMIN** | Global | All Events | Platform governance, tenant quotas, global analytics |
| **ORGANIZATION_ADMIN** | Organization | Org Events | Create events, white-label branding, manager invites |
| **EVENT_MANAGER** | Organization | Single Event | Timeline builder, registration approval, QR check-in |
| **JUDGE** | Event | Track / Assigned | Multi-round rubric scoring, blind judging, feedback |
| **MENTOR** | Event | Assigned Teams | SLA ticket response, video office hours, code reviews |
| **SPONSOR** | Event | Sponsor Track | Challenge creation, resume pool access, recruiter notes |
| **VOLUNTEER** | Event | Event Floor | QR badge scanner, swag distribution, room mapping |
| **PARTICIPANT** | User Level | Event Registrant| Team matching, submission, developer passport |
| **RECRUITER** | Organization | Talent Suite | Candidate filtering, pipeline stages, export resumes |

---

## 4. Visual Design System (80–15–5 SaaS Rule)
- **80% Background & Surfaces**: `#050816` Deep Midnight, `#0F172A` Slate Cards.
- **15% Primary Engineering Blue (`#2563EB`)**: Navigation tabs, focus borders, category chips, analytics charts.
- **5% Accent Competition Orange (`#F97316`)**: Reserved strictly for high-conversion CTAs (Submit Project, Notifications Badge, Countdown Box, Leaderboard Rank #1, Pending Review Badges).
