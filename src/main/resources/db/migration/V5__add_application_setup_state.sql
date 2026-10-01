-- Some environments were provisioned with this table before V5 was recorded
-- in Flyway's schema history. Keep the migration safe in that situation.
CREATE TABLE IF NOT EXISTS application_setup (
    id SMALLINT PRIMARY KEY CHECK (id = 1),
    completed_at TIMESTAMP
);

-- Existing deployments with an administrator are already set up. New
-- installations keep completed_at NULL until POST /api/v1/setup succeeds.
INSERT INTO application_setup (id, completed_at)
VALUES (
    1,
    CASE WHEN EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN') THEN CURRENT_TIMESTAMP ELSE NULL END
)
ON CONFLICT (id) DO NOTHING;

-- Align a pre-existing singleton row with an already configured deployment.
UPDATE application_setup
SET completed_at = CURRENT_TIMESTAMP
WHERE id = 1
  AND completed_at IS NULL
  AND EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN');
