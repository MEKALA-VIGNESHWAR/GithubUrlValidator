# 🗄️ Multi-Tenant Hackathon OS Database Schema

## 1. Multi-Tenant Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    ORGANIZATIONS ||--o{ HACKATHONS : "owns"
    ORGANIZATIONS ||--o{ USERS : "members"
    HACKATHONS ||--o{ TRACKS : "contains"
    HACKATHONS ||--o{ TEAMS : "registers"
    HACKATHONS ||--o{ SUBMISSIONS : "receives"
    TEAMS ||--o{ TEAM_MEMBERS : "includes"
    SUBMISSIONS ||--o{ GITHUB_INTELLIGENCE : "analyzes"
    SUBMISSIONS ||--o{ AI_REVIEWS : "evaluates"
    SUBMISSIONS ||--o{ JUDGE_EVALUATIONS : "scores"
    SUBMISSIONS ||--o{ CERTIFICATES : "issues"

    ORGANIZATIONS {
        Long id PK
        String name
        String slug UK
        String logo
        String description
        String website
        Long owner_id
        LocalDateTime created_at
    }

    HACKATHONS {
        Long id PK
        Long organization_id FK
        String title
        String slug UK
        String event_type
        String format
        LocalDateTime start_date
        LocalDateTime end_date
        LocalDateTime submission_deadline
        Boolean is_published
    }

    USERS {
        Long id PK
        Long organization_id FK
        String username UK
        String email UK
        String password
        String profile_picture
        String provider_id
        String provider
        String role
        LocalDateTime created_at
    }

    SUBMISSIONS {
        Long id PK
        Long hackathon_id FK
        Long team_id FK
        String project_title
        String problem_statement
        String tech_stack
        String category
        String github_repo_url
        String status
        Integer stars
        Integer forks
        Double judge_rating
        Integer completion_rate
        LocalDateTime submitted_at
    }

    GITHUB_INTELLIGENCE {
        Long id PK
        Long submission_id FK
        Integer commit_count
        Integer open_issues
        Integer pull_requests
        String language_breakdown_json
        Double team_balance_score
        Boolean code_freeze_valid
    }

    AI_REVIEWS {
        Long id PK
        Long submission_id FK
        Double innovation_score
        Double technical_score
        Double documentation_score
        Double risk_score
        String ai_summary
        String sponsor_spotlight
    }

    JUDGE_EVALUATIONS {
        Long id PK
        Long submission_id FK
        Long judge_id FK
        Long round_id FK
        Double score
        String rubric_breakdown_json
        String comment
        Boolean anomaly_flagged
    }
```

---

## 2. Multi-Tenancy Strategy
- **Row-Level Tenant Isolation**: All core entities reference `organization_id` / `hackathon_id`.
- **Soft Delete Strategy**: Entities implement `is_deleted` (BOOLEAN) and `deleted_at` (TIMESTAMP).
- **Audit Columns**: `created_at`, `updated_at`, `created_by`, `updated_by` on all tables.
