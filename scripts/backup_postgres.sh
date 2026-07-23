#!/usr/bin/env bash
# ==============================================================================
# HackForge PostgreSQL Automated Production Backup Script
# ==============================================================================
set -euo pipefail

BACKUP_DIR="${BACKUP_DIR:-/backups/postgres}"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-hackforge_prod}"
DB_USER="${DB_USER:-postgres}"

mkdir -p "${BACKUP_DIR}"

BACKUP_FILE="${BACKUP_DIR}/hackforge_backup_${TIMESTAMP}.sql.gz"

echo "Starting PostgreSQL backup for ${DB_NAME} at ${TIMESTAMP}..."

PGPASSWORD="${DB_PASSWORD:-}" pg_dump -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -F c "${DB_NAME}" | gzip > "${BACKUP_FILE}"

echo "Backup completed successfully: ${BACKUP_FILE}"
echo "Size: $(du -sh ${BACKUP_FILE} | cut -f1)"

# Keep last 14 days of backups
find "${BACKUP_DIR}" -type f -name "hackforge_backup_*.sql.gz" -mtime +14 -delete
echo "Old backups purged (retained 14 days)."
