alter table document
ADD COLUMN version INT;

alter table app_user
add column version INT;

alter table app_user
drop column password;