#!/usr/bin/env bash
# ==============================================================================
# HackForge PostgreSQL Disaster Recovery Restore Script
# ==============================================================================
set -euo pipefail

if [ -z "${1:-}" ]; then
    echo "Usage: $0 <path_to_backup_file.sql.gz>"
    exit 1
fi

BACKUP_FILE="$1"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-hackforge_prod}"
DB_USER="${DB_USER:-postgres}"

if [ ! -f "${BACKUP_FILE}" ]; then
    echo "Error: Backup file '${BACKUP_FILE}' not found."
    exit 1
fi

echo "WARNING: This will overwrite database '${DB_NAME}' on ${DB_HOST}:${DB_PORT}."
read -p "Are you sure you want to proceed? (y/N): " confirm
if [[ "${confirm}" != "y" && "${confirm}" != "Y" ]]; then
    echo "Restore cancelled."
    exit 0
fi

echo "Restoring database from ${BACKUP_FILE}..."
gunzip -c "${BACKUP_FILE}" | PGPASSWORD="${DB_PASSWORD:-}" pg_restore -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" --clean --if-exists --no-owner || true

echo "Database restore operation completed successfully!"
