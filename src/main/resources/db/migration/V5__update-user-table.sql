-- V4__Update_User_Table.sql

-- Add columns for new fields
ALTER TABLE app_user
ADD COLUMN password VARCHAR(255),
ADD COLUMN locked BOOLEAN,
ADD COLUMN enabled BOOLEAN;

-- Update existing fields
ALTER TABLE app_user
MODIFY COLUMN email VARCHAR(255) UNIQUE,
MODIFY COLUMN role VARCHAR(50) NOT NULL;

-- Set default values for new columns
UPDATE app_user
SET locked = false, enabled = true;