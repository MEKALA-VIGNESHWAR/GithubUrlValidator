# HackForge Architecture Overview

HackForge is designed as a cloud-native, multi-tenant hackathon management SaaS platform built for high availability, security, and scalability.

```
[ Client Browser ]
       │
       ▼
 [ Nginx / Ingress ] ── (TLS Termination, Rate Limiting, Static Assets)
       │
       ├─────────────────────────────────┐
       ▼                                 ▼
 [ React Frontend ]             [ Spring Boot Backend ] ── (Actuator / Prometheus)
                                         │
       ┌───────────────────┬─────────────┼──────────────┐
       ▼                   ▼             ▼              ▼
 [ PostgreSQL ]        [ Redis ]    [ RabbitMQ ]   [ AWS S3 ]
 (Relational DB)     (Cache & Auth) (Async Queue)  (File Storage)
```

## Core Subsystems
1. **Security & Authentication Subsystem**:
   - Short-lived Access Tokens (15 min) + Refresh Token Rotation (7 days).
   - Redis-backed token revocation & blacklisting.
   - Fine-grained role hierarchy (`ADMIN`, `ORGANIZER`, `JUDGE`, `MENTOR`, `PARTICIPANT`).
   - Bucket4j rate limiting on sensitive routes.
2. **Database & Persistence Layer**:
   - PostgreSQL versioned migrations via Flyway.
   - B-tree indexing on high-cardinality search fields.
   - Soft delete flags for audit preservation.
3. **Asynchronous Messaging & Background Processing**:
   - RabbitMQ with Dead-Letter Queues (DLQ) for retries.
   - Scheduled tasks for hackathon lifecycle state transitions (`UPCOMING` -> `LIVE` -> `COMPLETED`).
4. **Observability & Operational Health**:
   - Prometheus metrics endpoint (`/actuator/prometheus`).
   - Logback structured JSON logging with MDC `traceId`.
   - Grafana monitoring dashboards and Sentry exception reporting.
