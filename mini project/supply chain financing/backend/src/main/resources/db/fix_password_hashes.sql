-- Fix invalid password hashes in users table
-- Purpose: Convert non-BCrypt passwords to valid BCrypt hashes expected by Spring Security
--
-- This script updates ONLY rows where password does not match BCrypt format.
--
-- Sample credentials after running:
--   admin@scf.local  -> Admin@123
--   alice@example.com -> password123
--   bob@example.com   -> password123
--   carol@example.com -> password123
--   diana@example.com -> password123
--   eve@example.com   -> password123

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Fix admin account if hash is invalid
UPDATE users
SET password = crypt('Admin@123', gen_salt('bf', 10))
WHERE email = 'admin@scf.local'
  AND (password IS NULL OR password !~ '^\$2[aby]\$[0-9]{2}\$.{53}$');

-- Fix sample customer accounts if hash is invalid
UPDATE users
SET password = crypt('password123', gen_salt('bf', 10))
WHERE email IN (
    'alice@example.com',
    'bob@example.com',
    'carol@example.com',
    'diana@example.com',
    'eve@example.com'
)
  AND (password IS NULL OR password !~ '^\$2[aby]\$[0-9]{2}\$.{53}$');

-- Optional: show accounts still having invalid hashes (should return 0 rows for these users)
SELECT email, role
FROM users
WHERE email IN (
    'admin@scf.local',
    'alice@example.com',
    'bob@example.com',
    'carol@example.com',
    'diana@example.com',
    'eve@example.com'
)
  AND (password IS NULL OR password !~ '^\$2[aby]\$[0-9]{2}\$.{53}$');

