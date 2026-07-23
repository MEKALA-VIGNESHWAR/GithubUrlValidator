# HackForge Backup & Disaster Recovery Guide

This guide outlines procedures for automated backup generation and disaster recovery restoration for PostgreSQL databases.

## 1. Automated Backups
Automated backups are handled by `scripts/backup_postgres.sh`.

```bash
# Run manual backup
./scripts/backup_postgres.sh
```

### Cron Schedule Setup (Production Host)
Add the following line to `crontab -e` to run daily backups at 2:00 AM:
```cron
0 2 * * * DB_NAME=hackforge_prod DB_USER=postgres DB_PASSWORD=secr3t /app/scripts/backup_postgres.sh >> /var/log/hackforge_backup.log 2>&1
```

---

## 2. Disaster Recovery Restore Procedure
If database corruption or data loss occurs, restore using `scripts/restore_postgres.sh`.

```bash
# Restore specific backup file
./scripts/restore_postgres.sh /backups/postgres/hackforge_backup_20260723_020000.sql.gz
```
