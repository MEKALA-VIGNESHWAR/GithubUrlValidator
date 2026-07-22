# 🚀 HackForge Deployment & Infrastructure Guide

## 1. Local Development Setup

### Backend (Spring Boot 3)
```bash
cd backend
mvn clean spring-boot:run
```
- Backend runs at `http://localhost:3000`
- Swagger OpenAPI Docs: `http://localhost:3000/swagger-ui.html`

### Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
- Frontend UI runs at `http://localhost:5173`

---

## 2. Docker Containerization

### `Dockerfile` (Backend)
```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### `docker-compose.yml`
```yaml
version: '3.8'

services:
  backend:
    build: ./backend
    ports:
      - "3000:3000"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SUPABASE_DB_PASSWORD=${SUPABASE_DB_PASSWORD}
      - GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
      - GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}

  frontend:
    build: ./frontend
    ports:
      - "5173:5173"
    depends_on:
      - backend
```

---

## 3. Environment Variables Reference

| Variable | Description | Example Value |
| :--- | :--- | :--- |
| `SUPABASE_DB_PASSWORD` | PostgreSQL pooler password | `Wemmbu@!77@^` |
| `GOOGLE_CLIENT_ID` | Google OAuth 2.0 Client ID | `181736...usercontent.com` |
| `GOOGLE_CLIENT_SECRET` | Google OAuth 2.0 Client Secret | `GOCSPX-wcRt...` |
| `JWT_SECRET` | Secret key for JWT signing | `c2VjdXJlX2p3dF9zZWNyZXRfa2V5X2hhY2tmb3JnZQ==` |

---

## 4. Google Cloud Console Redirect URIs
- **Authorized JavaScript Origins**: `http://localhost:5173`, `http://localhost:3000`
- **Authorized Redirect URIs**: `http://localhost:3000/login/oauth2/code/google`
