# HackForge Local Setup & Developer Guide

## Prerequisites
- Java 21 JDK
- Node.js 20+
- Docker & Docker Compose

## 1. Quick Start with Docker
```bash
# Clone repository and boot local infrastructure
docker-compose up -d

# Verify services running:
# - PostgreSQL on port 5432
# - Redis on port 6379
# - RabbitMQ Management Console on http://localhost:15672
```

## 2. Start Backend Locally
```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```
- Swagger UI will be available at: `http://localhost:8080/swagger-ui.html`
- Health check: `http://localhost:8080/actuator/health`

## 3. Start Frontend Locally
```bash
cd frontend
npm install
npm run dev
```
- App will open at: `http://localhost:5173`
