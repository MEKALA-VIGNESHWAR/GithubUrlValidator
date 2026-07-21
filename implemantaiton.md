# Production-Level Upgrade Plan: GitHub URL Validator & Hackathon Platform

## Overview
Transforming the existing Spring Boot application into a production-grade, enterprise hackathon submission platform adhering to Clean Architecture, SOLID principles, and industry best practices.

---

## Detailed Roadmap

### Phase 1: Project Restructuring & Maven Dependencies
- Update `pom.xml` with starters: `security`, `validation`, `mail`, `cache`, `jjwt`, `springdoc`, `mysql-connector-j`, `bucket4j`.
- Setup environment profiles: `application-dev.yml`, `application-prod.yml`, `.env.example`.
- Restructure packages under `com.project`.

### Phase 2: Enums, Domain Entities & Mappers
- Create `SubmissionStatus` (`PENDING`, `APPROVED`, `REJECTED`) and `Role` (`ADMIN`, `JUDGE`, `PARTICIPANT`) enums.
- Create `User` entity & expand `Submission` entity with leaderName, email, college, description, techStack, demoVideoUrl, pptUrl, pdfUrl, submittedAt, status, GitHub repository metadata.
- Create `SubmissionMapper` and `UserMapper`.

### Phase 3: DTO Layer & Bean Validation
- Create Request & Response DTOs: `SubmissionRequestDTO`, `SubmissionResponseDTO`, `LoginRequestDTO`, `RegisterRequestDTO`, `AuthResponseDTO`, `AnalyticsResponseDTO`, `GithubRepoInfoDTO`.
- Apply Bean Validation (`@NotBlank`, `@Email`, `@Size`, `@Pattern`, `@Valid`).

### Phase 4: Spring Security & JWT Authentication/Authorization
- Implement `JwtUtils`, `JwtAuthenticationEntryPoint`, `JwtAuthenticationFilter`, `UserDetailsServiceImpl`.
- Configure `SecurityConfig` with Role-Based Access Control (`ADMIN`, `JUDGE`, `PARTICIPANT`).
- Build `AuthController` (`/auth/register`, `/auth/login`, `/auth/logout`).

### Phase 5: Extended CRUD APIs, Pagination & GitHub API Integration
- Implement `GithubApiService` (`https://api.github.com/repos/{owner}/{repo}`).
- Build `SubmissionController` supporting CRUD, Pagination (`?page=0&size=10`), Sorting (`?sort=submittedAt`), Search (`?team=`).

### Phase 6: File Uploads, Email Notifications, Analytics, Rate Limiting & Cache
- Build `FileStorageService` and `FileUploadController` (`POST /api/submissions/upload`).
- Build `EmailService` for creation, approval, and rejection HTML notifications.
- Build `AnalyticsService` and `AnalyticsController` (`GET /api/analytics`).
- Add `Bucket4jRateLimitFilter` (5 req/min) and Spring `@Cacheable`.

### Phase 7: Global Exception Handling & Swagger OpenAPI Docs
- Update `GlobalExceptionHandler` for validation, auth, file, and GitHub API errors.
- Configure `SwaggerConfig` exposing `/swagger-ui.html`.

### Phase 8: Dockerization, CI/CD & Automated Testing
- Create `Dockerfile`, `docker-compose.yml`, and `.github/workflows/deploy.yml`.
- Write Unit and Integration tests (`SubmissionServiceTest`, `AuthControllerTest`, `SubmissionControllerTest`).
- Run `./mvnw clean test`.