#!/bin/bash

set -e


DATE=$(date +"%Y-%m-%d_%H-%M-%S")

BACKUP_DIR=/backups

FILE=$BACKUP_DIR/shop_$DATE.sql


echo "Starting backup"


PGPASSWORD=$POSTGRES_PASSWORD pg_dump \
-h postgres \
-U $POSTGRES_USER \
-d $POSTGRES_DB \
> $FILE


gzip $FILE


echo "Removing old backups"


find /backups \
-type f \
-name "*.gz" \
-mtime +7 \
-delete


echo "Backup finished"