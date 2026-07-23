# HackForge Environment Variables Reference

| Variable Name | Required | Default (Local) | Description |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Yes | `local` | Active Spring profile (`local`, `dev`, `staging`, `prod`) |
| `PORT` | No | `8080` | HTTP port server listens on |
| `DB_URL` | Yes | `jdbc:postgresql://localhost:5432/hackforge_dev` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | Yes | `postgres` | Database username |
| `DB_PASSWORD` | Yes | `postgres` | Database password |
| `JWT_SECRET` | Yes | 64-char hex string | HMAC secret key for Access Tokens |
| `JWT_REFRESH_SECRET` | Yes | 64-char hex string | HMAC secret key for Refresh Tokens |
| `JWT_EXPIRATION_MS` | No | `900000` (15 mins) | Access Token TTL in milliseconds |
| `JWT_REFRESH_EXPIRATION_MS` | No | `604800000` (7 days) | Refresh Token TTL in milliseconds |
| `REDIS_HOST` | Yes | `localhost` | Redis server hostname |
| `REDIS_PORT` | No | `6379` | Redis server port |
| `REDIS_PASSWORD` | No | Empty | Redis password authentication |
| `RABBITMQ_HOST` | Yes | `localhost` | RabbitMQ server hostname |
| `RABBITMQ_PORT` | No | `5672` | RabbitMQ server port |
| `RABBITMQ_USERNAME` | No | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | No | `guest` | RabbitMQ password |
| `AWS_S3_BUCKET` | Yes | `hackforge-storage` | AWS S3 / MinIO bucket name |
| `AWS_S3_REGION` | No | `us-east-1` | AWS S3 region |
| `AWS_ACCESS_KEY_ID` | Yes | - | AWS access key |
| `AWS_SECRET_ACCESS_KEY` | Yes | - | AWS secret key |
| `SENTRY_DSN` | No | Empty | Sentry error tracking DSN |
| `CORS_ALLOWED_ORIGINS` | Yes | `http://localhost:5173` | Comma-separated list of allowed origins |
