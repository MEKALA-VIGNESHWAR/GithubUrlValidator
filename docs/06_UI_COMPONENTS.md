# 🎨 HackForge Innovation Command Center - UI/UX Design System

## 1. Core Visual Direction & personality
**HackForge** is designed as a **Futuristic Innovation Command Center & Event Operations Cockpit**. It eliminates standard admin dashboards, repetitive equal-sized cards, and generic CRUD tables, delivering an immersive dark productivity experience for builders, judges, mentors, and sponsors.

---

## 2. Design Tokens & Visual Language (80–15–5 Rule)

```css
:root {
  /* 80% Background & Surfaces (Deep Charcoal, Translucent Glass) */
  --bg-primary: #030611;
  --bg-secondary: #080d1e;
  --card-bg: rgba(15, 23, 42, 0.75);
  --card-border: rgba(255, 255, 255, 0.09);
  --card-border-glow: rgba(37, 99, 235, 0.3);
  --inner-bg: rgba(11, 17, 32, 0.85);

  /* 15% Primary Engineering Blue */
  --primary: #2563eb;
  --primary-glow: rgba(37, 99, 235, 0.35);
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --font-mono: 'JetBrains Mono', monospace;

  /* 5% Accent Competition & Urgency Orange */
  --accent: #f97316;
  --accent-glow: rgba(249, 115, 22, 0.4);
}
```

---

## 3. Typography & Spacing System
* **Display Typography**: `Plus Jakarta Sans` (800 / 700 / 600 weight) for bold headers and event titles.
* **Monospace Data Tokens**: `JetBrains Mono` for code snippets, commit dates, scores, and verification hashes.
* **12-Column Asymmetric Bento Grid**:
  - Hero Panels (`grid-column: span 8`)
  - Floating Cockpit Widgets (`grid-column: span 4`)
  - Full-Width Broadcast Sections (`grid-column: span 12`)

---

## 4. Page Layout Concepts

### 1. Landing / Public Event Page
* **Cinematic Hero Rail**: Animated gradient mesh, live event countdown, and total prize pool counter.
* **Track Badges**: Interactive glass chips highlighting AI, Blockchain, Web3, and Healthcare categories.

### 2. Organizer Mission Control Dashboard
* **Multi-Event Overview**: Live team map, registration funnel analytics, and operational alerts center.
* **Submission Velocity Chart**: Real-time project submission rate graphs using primary blue (`#2563eb`).

### 3. Participant Builder Cockpit
* **Submission Readiness Meter**: Live completeness score bar with missing field alerts.
* **GitHub Intelligence Sync**: Repository star counter, fork counter, and commit timeline preview.

### 4. Distraction-Free Judging Studio
* **Split Evaluation View**: Project details on left, demo video/PDF viewer in center, and rubric scoring panel on right.
* **AI Judge Briefing**: Automated summary cards displaying AI innovation, technical, and risk scores.

### 5. Demo Day Broadcast Control Room
* **Stage Status Bar**: Current team live indicator, countdown timer, and WebRTC video stream link.
* **Live Leaderboard**: Real-time score updates with pulse dot animations for Rank #1.

---

## 5. Motion Guidelines & Micro-Interactions
* **Pulse Ring Animation (`pulse-ring`)**: 1.8s infinite glowing ring on live status indicators.
* **Smooth Panel Expansion**: 250ms cubic-bezier transition (`cubic-bezier(0.4, 0, 0.2, 1)`) on card hovers.
* **Glass Hover Glow**: Translucent cards elevate by `-2px` with primary glow borders (`0 0 24px var(--primary-glow)`).
