#!/bin/bash

set -e

if [ $# -ne 1 ]; then
    echo "Usage:"
    echo "./restore.sh <backup-file.sql.gz>"
    exit 1
fi

BACKUP_FILE="/backups/$1"

if [ ! -f "$BACKUP_FILE" ]; then
    echo "Backup not found:"
    echo "$BACKUP_FILE"
    exit 1
fi

echo "Checking archive..."

gzip -t "$BACKUP_FILE"

echo "Archive OK."

echo ""
echo "WARNING!"
echo "This will overwrite the database '$POSTGRES_DB'."
read -p "Continue? (yes/no): " answer

if [ "$answer" != "yes" ]; then
    echo "Cancelled."
    exit 0
fi

echo "Restoring database..."

gunzip -c "$BACKUP_FILE" | \
PGPASSWORD=$POSTGRES_PASSWORD \
psql \
-h postgres \
-U "$POSTGRES_USER" \
-d "$POSTGRES_DB"

echo ""
echo "Restore completed successfully!"