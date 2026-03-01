------------------------------------------------------------
-- MAKE ADMISSION_NUMBER NULLABLE
------------------------------------------------------------
ALTER TABLE student ALTER COLUMN admission_number DROP NOT NULL;
