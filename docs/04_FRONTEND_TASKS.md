# 💻 HackForge Frontend Architecture & Tasks

## 1. React Component Hierarchy

```
App.jsx (Session Gate & Global State)
├── LoginPage.jsx (Full-Screen Landing View for Unauthenticated Visitors)
├── Header.jsx (Brand Logo, Countdown Warning, Theme Toggle, Profile & Notifications Dropdowns)
├── SubmissionForm.jsx (Column 1: Multi-Section Form, Repo Validation & Asset Progress)
├── SubmissionList.jsx (Column 2: Search, Category Filters, Submission Cards)
├── SidebarWidgets.jsx (Column 3: Analytics Summary & Formula Leaderboard)
├── AdminDashboard.jsx (Admin View: Stats Summary, Action Table & Export Controls)
├── AuthModal.jsx (In-App Auth Modal for Quick Account Switching)
└── Toast.jsx (Global Toast Notification Feedback System)
```

---

## 2. Completed Frontend Tasks
- [x] Implemented single-page grid layout (`App.jsx`).
- [x] Created `LoginPage.jsx` as a dedicated landing screen featuring the white **"Continue with Google"** button.
- [x] Integrated `Header.jsx` with light/dark theme switcher (`data-theme="light"` / `data-theme="dark"`).
- [x] Built real-time GitHub repository URL validator (`https://github.com/owner/repo`).
- [x] Implemented file upload dropzone for PPT (≤50MB), PDF (≤20MB), and Video (≤200MB) with simulated progress indicators.
- [x] Built `SidebarWidgets.jsx` formula-ranked leaderboard: `Stars × 2 + Rating × 5 + Completeness × 3`.
- [x] Added `AdminDashboard.jsx` featuring CSV/PDF download handlers and inline status toggles.

---

## 3. Pending Frontend Optimizations
- [ ] Convert state management from local `useState` to React Context / Redux Toolkit for complex team workflows.
- [ ] Add `framer-motion` page transition animations between Participant View and Admin View.
- [ ] Enhance responsive mobile menu layout for mobile screen sizes (< 768px).
