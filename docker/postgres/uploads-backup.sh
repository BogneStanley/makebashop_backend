#!/bin/bash

set -e

DATE=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_DIR=/backups
UPLOADS_DIR=/app/uploads
FILE=$BACKUP_DIR/uploads_shop_$DATE.tar.gz

echo "Starting uploads backup..."

mkdir -p "$UPLOADS_DIR"
mkdir -p "$BACKUP_DIR"

tar -czf "$FILE" -C /app uploads

echo "Uploads backup created: $FILE"

echo "Removing old uploads backups (+7 days)..."
find /backups \
  -type f \
  -name "uploads_shop_*.tar.gz" \
  -mtime +7 \
  -delete

echo "Uploads backup finished successfully."
