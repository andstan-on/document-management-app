ALTER TABLE document DROP COLUMN approval_status;

ALTER TABLE document
ADD COLUMN approval_status VARCHAR(50) NOT NULL;