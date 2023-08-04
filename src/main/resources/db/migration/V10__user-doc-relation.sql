-- Add the user_id column to the document table
ALTER TABLE document
ADD COLUMN user_id VARCHAR(36) NOT NULL;

-- Add the foreign key constraint to the user_id column
ALTER TABLE document
ADD CONSTRAINT fk_document_user
FOREIGN KEY (user_id)
REFERENCES app_user(id)
ON DELETE CASCADE;