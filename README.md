# ⚡ HackForge — Enterprise Hackathon Operating System & SaaS Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/MEKALA-VIGNESHWAR/GithubUrlValidator)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-blue.svg)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Production-326CE5.svg)](https://kubernetes.io/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

**HackForge** is an enterprise-grade, full-stack hackathon management SaaS platform built for high availability, zero-trust security, real-time analytics, and seamless event execution. Designed for administrators, participants, judges, mentors, and sponsors, HackForge powers end-to-end hackathon lifecycles—from event creation and team registration to project submission, S3 file uploads, live scoring, real-time leaderboards, automated certificate issuance, and comprehensive observability.

---

## 🚀 Core Features

- 👑 **Multi-Role RBAC Dashboards**: Customized command centers for Admins, Judges, Mentors, Participants, and Event Sponsors.
- 🔐 **Production Security**: JWT Access & Refresh Token rotation, Redis-backed token revocation, BCrypt password hashing, Bucket4j rate limiting, CORS controls, CSP headers, and RFC7807 sanitized global exception handling.
- ⚡ **Realtime Engine**: WebSockets (STOMP over SockJS) for instant leaderboard updates, real-time announcements, and notification feeds.
- 🗄️ **Flyway Schema Versioning**: Automated PostgreSQL schema migration scripts with B-tree query indexing, FK constraints, and soft-delete capabilities.
- 📁 **Malware-Safe File Uploads**: Extension whitelist validation, MIME/magic byte checks, 15MB file caps, and AWS S3 storage integration.
- 🔄 **Asynchronous Reliability**: RabbitMQ message queue with Dead-Letter Queue (DLQ) support for background notifications and scheduled status transitions.
- 📊 **Enterprise Observability**: Actuator `/actuator/prometheus` metrics endpoint, pre-built Grafana dashboards, Logback JSON structured logging with MDC trace tracking, and Sentry error capture.
- 🐳 **Cloud-Native Deployment**: Multi-stage Dockerfiles, Nginx reverse proxy configuration with TLS/Gzip, Kubernetes manifests (Deployments, StatefulSets, Ingress, HPA), and GitHub Actions CI/CD workflows.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Frontend** | React 19, TypeScript/JSX, Vite, Tailwind CSS, Axios, SockJS / STOMP Client |
| **Backend** | Java 21, Spring Boot 3.2, Spring Security, Spring Data JPA, Hibernate, Bucket4j |
| **Database** | PostgreSQL 16, Flyway Migrations, HikariCP Connection Pool |
| **Caching & Queue** | Redis 7, RabbitMQ 3 (with Management Plugin) |
| **File Storage** | AWS S3 / MinIO S3 SDK |
| **Observability** | Prometheus, Grafana, Logback (JSON + MDC), Sentry |
| **Infrastructure** | Nginx Reverse Proxy, Docker, Docker Compose, Kubernetes, GitHub Actions |

---

## 🏛️ System Architecture

```
                               ┌────────────────────────────────┐
                               │     Web Browser / Client       │
                               └───────────────┬────────────────┘
                                               │
                                               ▼
                               ┌────────────────────────────────┐
                               │   Nginx Reverse Proxy / TLS    │
                               └───────┬────────────────┬───────┘
                                       │                │
                      /api/v1 REST     │                │ Static Assets / SPA
                                       ▼                ▼
                       ┌───────────────────┐  ┌───────────────────┐
                       │  Spring Boot API  │  │   React Frontend  │
                       └─────────┬─────────┘  └───────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┬───────────────────────┐
         ▼                       ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  PostgreSQL DB  │     │   Redis Cache   │     │  RabbitMQ Queue │     │ AWS S3 Bucket   │
│ (Flyway Schema) │     │ (Tokens/State)  │     │ (Async Workers) │     │ (File Storage)  │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## 🏁 Quick Start & Local Setup

### Prerequisites
- Java 21 JDK
- Node.js 20+ & npm
- Docker Engine & Docker Compose

### 1. Launch Local Infrastructure (PostgreSQL, Redis, RabbitMQ)
```bash
# Clone the repository
git clone https://github.com/MEKALA-VIGNESHWAR/GithubUrlValidator.git
cd "github url validator"

# Start database and message broker containers
docker-compose up -d
```

### 2. Run Backend (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
- **REST API Base**: `http://localhost:8080/api/v1`
- **Swagger Documentation**: `http://localhost:8080/swagger-ui.html`
- **Actuator Health Probes**: `http://localhost:8080/actuator/health`

### 3. Run Frontend (React Vite)
```bash
cd frontend
npm install
npm run dev
```
- Open `http://localhost:5173` in your browser.

---

## 🐳 Production Deployment

### Option A: Docker Compose Production Stack
```bash
# Copy environment configuration and configure secrets
cp .env.example .env

# Build and start full stack in production mode
docker-compose -f docker-compose.prod.yml up -d --build
```

### Option B: Kubernetes Deployment
```bash
# Apply namespace, configurations, and secrets
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml

# Deploy database, queue, cache, and application workloads
kubectl apply -f k8s/postgres-statefulset.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/hpa.yaml
kubectl apply -f k8s/ingress.yaml
```

---

## 🔑 Environment Variables Reference

A full reference of configuration keys is available in [.env.example](.env.example). Key variables include:

| Variable | Description | Default |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Spring Boot active profile (`local`, `dev`, `staging`, `prod`) | `local` |
| `DB_URL` | PostgreSQL JDBC Connection String | `jdbc:postgresql://localhost:5432/hackforge_dev` |
| `JWT_SECRET` | 64+ char HMAC secret for Access Tokens | Required in Prod |
| `JWT_REFRESH_SECRET` | 64+ char HMAC secret for Refresh Tokens | Required in Prod |
| `REDIS_HOST` | Redis Server Hostname | `localhost` |
| `RABBITMQ_HOST` | RabbitMQ Hostname | `localhost` |
| `AWS_S3_BUCKET` | S3 Storage Bucket Name | `hackforge-storage` |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins (comma-separated) | `http://localhost:5173` |

---

## 📚 Complete Project Documentation

Detailed operational documentation is located in the [docs/](docs/) directory:

- 📖 [Production Deployment Guide](docs/PRODUCTION_DEPLOYMENT_GUIDE.md)
- ⚙️ [Environment Variables Reference](docs/ENV_VARIABLES_REFERENCE.md)
- 🏗️ [Architecture Overview](docs/ARCHITECTURE_OVERVIEW.md)
- 🗄️ [Database Migration Guide](docs/DATABASE_MIGRATION_GUIDE.md)
- 💾 [Backup & Restore Guide](docs/BACKUP_AND_RESTORE_GUIDE.md)
- 🚨 [Incident Response Checklist](docs/INCIDENT_RESPONSE_CHECKLIST.md)
- 💻 [Local Setup Guide](docs/LOCAL_SETUP_GUIDE.md)
- ✅ [Staging & Production Release Checklist](docs/RELEASE_CHECKLIST.md)

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details. Built with ❤️ by the HackForge Core Engineering Team.