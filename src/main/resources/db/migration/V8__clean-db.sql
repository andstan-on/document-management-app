-- Deletes all data from the 'token' table (also removes related data from 'document')
DELETE FROM token;

-- Deletes all data from the 'app_user' table (also removes related data from 'token' and 'document')
DELETE FROM app_user;

-- Deletes all data from the 'document' table
DELETE FROM document;