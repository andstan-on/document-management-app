-- Rename the 'fileType' column to 'file_type'
ALTER TABLE document RENAME COLUMN fileType TO file_type;

-- Rename the 'filePath' column to 'file_path'
ALTER TABLE document RENAME COLUMN filePath TO file_path;

-- Rename the 'fileName' column to 'file_name'
ALTER TABLE document RENAME COLUMN fileName TO file_name;