------------------------------------------------------------
-- REMOVE SPECIALIZATION FROM TEACHER TABLE
------------------------------------------------------------
ALTER TABLE teacher DROP COLUMN IF EXISTS specialization;

-- REMOVE SPECIALIZATION FROM TEACHER HISTORY TABLE
ALTER TABLE teacher_history DROP COLUMN IF EXISTS specialization;

-- UPDATE AUDIT FUNCTION TO EXCLUDE SPECIALIZATION
CREATE OR REPLACE FUNCTION teacher_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, created_at)
        VALUES ('DELETE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, created_at)
        VALUES ('UPDATE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, created_at)
        VALUES ('INSERT', NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;
