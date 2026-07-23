# HackForge SaaS Production Deployment Guide

This guide details the steps required to deploy HackForge to production environments (Docker Compose or Kubernetes).

## Prerequisites
- Docker Engine 24+ & Docker Compose v2
- Kubernetes 1.28+ cluster & kubectl CLI
- Registered domain name with SSL certificates or Let's Encrypt `cert-manager` installed.
- PostgreSQL 16+ instance (or StatefulSet)
- Redis 7+ instance
- RabbitMQ 3+ instance

---

## 1. Quick Docker Compose Production Setup
```bash
# 1. Clone environment template and update production secrets
cp .env.example .env

# Edit .env with your production credentials
nano .env

# 2. Build and start full stack in production mode
docker-compose -f docker-compose.prod.yml up -d --build

# 3. Check container logs & status
docker-compose -f docker-compose.prod.yml ps
docker-compose -f docker-compose.prod.yml logs -f backend
```

---

## 2. Kubernetes Cluster Deployment
```bash
# 1. Apply namespace, ConfigMaps, and Secrets
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml

# 2. Deploy StatefulSets & Deployments
kubectl apply -f k8s/postgres-statefulset.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/hpa.yaml

# 3. Apply Ingress Controller
kubectl apply -f k8s/ingress.yaml

# 4. Verify deployment rollout
kubectl get pods -n hackforge -w
```
