# 🔌 HackForge API Tracker & REST Inventory

## 1. Authentication & OAuth Endpoints (`/api/auth` & `/oauth2`)

| HTTP Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user account | Public |
| `POST` | `/api/auth/login` | Authenticate username/password and return JWT | Public |
| `POST` | `/api/auth/google` | Authenticate Google OAuth account as `PARTICIPANT` | Public |
| `GET` | `/oauth2/authorization/google` | Trigger Spring Security OAuth2 browser redirect | Public |
| `GET` | `/login/oauth2/code/google` | Spring Security OAuth2 code exchange callback | Public |

---

## 2. Submission Endpoints (`/api/submissions`)

| HTTP Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/submissions` | Retrieve all project submissions | Public |
| `GET` | `/api/submissions/{id}` | Fetch submission details by ID | Public |
| `POST` | `/api/submissions` | Submit new HackForge project | Public / Participant |
| `PUT` | `/api/submissions/{id}` | Update existing project details | Admin / Judge |
| `PATCH` | `/api/submissions/{id}/status` | Update status (`APPROVED`, `REJECTED`) | Admin / Judge |
| `DELETE` | `/api/submissions/{id}` | Delete submission record | Admin / Judge |

---

## 3. Administrative & Reporting Endpoints (`/api/admin`)

| HTTP Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `PATCH` | `/api/admin/submissions/{id}/approve` | Approve submission | Admin |
| `PATCH` | `/api/admin/submissions/{id}/reject` | Reject submission | Admin |
| `POST` | `/api/admin/feedback` | Submit judge feedback comment | Admin / Judge |
| `POST` | `/api/admin/users/promote` | Promote participant to `ADMIN` or `JUDGE` | Admin Only |
| `GET` | `/api/admin/export/csv` | Download CSV summary report | Admin |
| `GET` | `/api/admin/export/pdf` | Download text/PDF summary report | Admin |

---

## 4. Analytics Endpoints (`/api/analytics`)

| HTTP Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/analytics/overview` | Fetch submission statistics & category breakdown | Admin / Judge |
