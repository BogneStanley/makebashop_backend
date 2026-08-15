#!/bin/bash

set -e

if [ $# -ne 1 ]; then
    echo "Usage:"
    echo "./uploads-restore.sh <uploads-file.tar.gz>"
    exit 1
fi

BACKUP_FILE="/backups/$1"

if [ ! -f "$BACKUP_FILE" ]; then
    echo "Backup file not found:"
    echo "$BACKUP_FILE"
    exit 1
fi

echo "Checking archive integrity..."
tar -tzf "$BACKUP_FILE" >/dev/null
echo "Archive OK."

echo ""
echo "WARNING!"
echo "This will overwrite existing uploaded files in '/app/uploads'."
read -p "Continue? (yes/no): " answer

if [ "$answer" != "yes" ]; then
    echo "Cancelled."
    exit 0
fi

echo "Restoring uploaded files..."
mkdir -p /app/uploads
tar -xzf "$BACKUP_FILE" -C /app

echo ""
echo "Uploads restore completed successfully!"
