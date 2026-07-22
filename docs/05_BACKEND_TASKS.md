# ⚙️ HackForge Backend Architecture & Tasks

## 1. Spring Boot Architecture Overview

```
com.example.demo
├── controller (REST Controllers: AuthController, SubmissionController, AdminController, AnalyticsController)
├── service (Business Services: AuthService, SubmissionService, AnalyticsService, GithubApiService)
├── repository (Spring Data JPA Repositories: UserRepository, SubmissionRepository, TeamRepository, etc.)
├── entity (JPA Entities: User, Submission, Team, TeamMember, FileEntity, Feedback, Notification)
├── enums (Enums: Role, AuthProvider, SubmissionStatus, Category, FileType)
├── dto (Request/Response Data Transfer Objects)
└── security (Security Architecture)
    ├── SecurityConfig.java (Spring Security 6 SecurityFilterChain)
    ├── JwtUtils.java (JJWT Token Generation & Validation)
    ├── JwtAuthenticationFilter.java (OncePerRequestFilter with shouldNotFilter)
    ├── JwtAuthenticationEntryPoint.java (Custom 401 Handler)
    ├── OAuth2SuccessHandler.java (Google OAuth2 Success Redirect Handler)
    └── OAuth2FailureHandler.java (Google OAuth2 Failure Handler)
```

---

## 2. Completed Backend Tasks
- [x] Configured Spring Security 6 `SecurityFilterChain` with CORS, CSRF disabled, and `SessionCreationPolicy.IF_REQUIRED`.
- [x] Added `OAuth2SuccessHandler` to parse Google user details, auto-register as `PARTICIPANT`, generate JWT token, and redirect to frontend.
- [x] Overrode `shouldNotFilter` in `JwtAuthenticationFilter` to bypass `/oauth2/*`, `/login/oauth2/*`, and `/api/auth/*`.
- [x] Created `POST /api/auth/google` endpoint for participant authentication.
- [x] Created `POST /api/admin/users/promote` endpoint allowing existing admins to assign `ADMIN` or `JUDGE` roles.
- [x] Implemented CSV and PDF report generation endpoints (`/api/admin/export/csv`, `/api/admin/export/pdf`).

---

## 3. Security Rules Enforced
- **Strict Role Assignment**: Google OAuth accounts are ALWAYS assigned `Role.PARTICIPANT`. Self-assignment of `Role.ADMIN` is strictly blocked.
- **Admin Promotion**: `Role.ADMIN` can only be granted by an existing administrator via `/api/admin/users/promote`.
