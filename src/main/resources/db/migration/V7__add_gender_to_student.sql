------------------------------------------------------------
-- ADD GENDER FIELD TO STUDENT TABLE
------------------------------------------------------------
ALTER TABLE student ADD COLUMN gender VARCHAR(10) CHECK (gender IN ('MALE','FEMALE','OTHER'));

-- ADD GENDER FIELD TO STUDENT HISTORY TABLE
ALTER TABLE student_history ADD COLUMN gender VARCHAR(10);

-- UPDATE AUDIT FUNCTION TO INCLUDE GENDER
CREATE OR REPLACE FUNCTION student_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, created_at)
        VALUES ('DELETE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.gender, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, created_at)
        VALUES ('UPDATE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.gender, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, created_at)
        VALUES ('INSERT', NEW.id, NEW.admission_number, NEW.first_name, NEW.last_name, NEW.date_of_birth, NEW.admission_date, NEW.status, NEW.parent_id, NEW.gender, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;
