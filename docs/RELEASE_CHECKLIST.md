# HackForge Staging & Production Release Checklist

## Pre-Release Phase
- [ ] All Flyway migration scripts (`V1`..`V4`) tested against PostgreSQL staging instance.
- [ ] Maven build passes with 0 failures (`mvn clean test`).
- [ ] Frontend Vite build compiles cleanly with zero linting errors (`npm run build`).
- [ ] Environment variables verified in `.env` (No hardcoded credentials).

## Deployment Phase
- [ ] Trigger CI pipeline (`ci.yml`) on `main` branch.
- [ ] Multi-stage Docker images pushed to container registry (`docker-build-push.yml`).
- [ ] Trigger Kubernetes deployment or Docker Compose production update.

## Post-Release Verification Phase
- [ ] Actuator health check returns `UP` with `readiness` and `liveness` state `ACCEPTING_TRAFFIC`.
- [ ] Swagger API docs load at `/swagger-ui.html`.
- [ ] Test end-to-end user registration and authentication flow.
- [ ] Test project submission with file upload to S3 / local storage.
- [ ] Prometheus metrics target `hackforge-backend` status is `UP`.
