# HackForge Database Migration Guide

HackForge uses **Flyway** for database migrations to enforce schema versioning and deterministic environment synchronization.

## Migration File Naming Convention
All SQL migration scripts live under `backend/src/main/resources/db/migration/` and follow strict Flyway naming conventions:
- `V<VERSION>__<DESCRIPTION>.sql` (e.g. `V1__init_schema.sql`, `V2__add_indexes_and_constraints.sql`)
- `V<VERSION_SUB>__<DESCRIPTION>.sql` (e.g. `V1_1__seed_system_roles.sql`)

## Existing Migrations
- `V1__init_schema.sql`: Core schema table structures (`users`, `hackathons`, `submissions`, `teams`, `certificates`, `notifications`, `bookmarks`, `file_entities`).
- `V2__add_indexes_and_constraints.sql`: Database B-tree performance indexes.
- `V3__audit_logs_and_refresh_tokens.sql`: Security audit logging and refresh token store.
- `V1_1__seed_system_roles.sql`: Required seed administrator account and default organization.

## Adding a New Migration
1. Create a new SQL file in `backend/src/main/resources/db/migration/` with incremented version number (e.g. `V5__add_mentor_feedback_table.sql`).
2. Write raw PostgreSQL DDL.
3. Test locally by booting Spring Boot:
   ```bash
   mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. Check Flyway status in startup log output:
   `Successfully applied 1 migration to schema "public"`
