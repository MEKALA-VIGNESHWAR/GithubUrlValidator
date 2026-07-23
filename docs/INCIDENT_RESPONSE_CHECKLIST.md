# HackForge Incident Response Checklist

## Severe Incident Escalation Flow

### Step 1: Triage & Initial Assessment
- [ ] Check Sentry dashboard for active exception spikes.
- [ ] Check Grafana dashboard for API latency (`p95 > 1.5s`) and HTTP 5xx rates (`> 5%`).
- [ ] Check Actuator health endpoint: `https://hackforge.io/actuator/health`.

### Step 2: Service Recovery Actions
- [ ] **High Database Connection Exhaustion**: Increase HikariCP pool max size or restart idle connections.
- [ ] **High Memory Out-Of-Memory (OOM)**: Trigger zero-downtime rolling restart:
  ```bash
  kubectl rollout restart deployment/backend-deployment -n hackforge
  ```
- [ ] **Data Corruption Incident**: Execute disaster recovery restore script (`scripts/restore_postgres.sh`).

### Step 3: Post-Mortem & Audit
- [ ] Inspect security audit trail via `/api/v1/admin/audit-logs`.
- [ ] Document root cause analysis (RCA) within 24 hours.
