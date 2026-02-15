------------------------------------------------------------
-- RENAME BATCH TABLES TO BADGE TABLES
------------------------------------------------------------

-- Drop existing triggers first
DROP TRIGGER IF EXISTS batch_audit_trigger ON batch;
DROP TRIGGER IF EXISTS student_batch_audit_trigger ON student_batch;
DROP TRIGGER IF EXISTS teacher_batch_audit_trigger ON teacher_batch;

-- Drop existing functions
DROP FUNCTION IF EXISTS batch_audit_function();
DROP FUNCTION IF EXISTS student_batch_audit_function();
DROP FUNCTION IF EXISTS teacher_batch_audit_function();

-- Rename main tables
ALTER TABLE batch RENAME TO badge;
ALTER TABLE student_batch RENAME TO student_badge;
ALTER TABLE teacher_batch RENAME TO teacher_badge;

-- Rename history tables
ALTER TABLE batch_history RENAME TO badge_history;
ALTER TABLE student_batch_history RENAME TO student_badge_history;
ALTER TABLE teacher_batch_history RENAME TO teacher_badge_history;

-- Rename sequences
ALTER SEQUENCE batch_history_seq RENAME TO badge_history_seq;
ALTER SEQUENCE student_batch_history_seq RENAME TO student_badge_history_seq;
ALTER SEQUENCE teacher_batch_history_seq RENAME TO teacher_badge_history_seq;

-- Rename constraints in student_badge
ALTER TABLE student_badge RENAME CONSTRAINT fk_student_batch_student TO fk_student_badge_student;
ALTER TABLE student_badge RENAME CONSTRAINT fk_student_batch_batch TO fk_student_badge_badge;

-- Rename constraints in teacher_badge
ALTER TABLE teacher_badge RENAME CONSTRAINT fk_teacher_batch_teacher TO fk_teacher_badge_teacher;
ALTER TABLE teacher_badge RENAME CONSTRAINT fk_teacher_batch_batch TO fk_teacher_badge_badge;

-- Rename foreign key in lesson_schedule
ALTER TABLE lesson_schedule RENAME CONSTRAINT fk_schedule_batch TO fk_schedule_badge;

-- Rename columns in student_badge and teacher_badge
ALTER TABLE student_badge RENAME COLUMN batch_id TO badge_id;
ALTER TABLE teacher_badge RENAME COLUMN batch_id TO badge_id;
ALTER TABLE lesson_schedule RENAME COLUMN batch_id TO badge_id;

-- Rename columns in history tables
ALTER TABLE student_badge_history RENAME COLUMN batch_id TO badge_id;
ALTER TABLE teacher_badge_history RENAME COLUMN batch_id TO badge_id;
ALTER TABLE lesson_schedule_history RENAME COLUMN batch_id TO badge_id;

-- Recreate audit functions with new names
CREATE OR REPLACE FUNCTION badge_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO badge_history (operation_type, id, name, type, description, created_at)
        VALUES ('DELETE', OLD.id, OLD.name, OLD.type, OLD.description, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO badge_history (operation_type, id, name, type, description, created_at)
        VALUES ('UPDATE', OLD.id, OLD.name, OLD.type, OLD.description, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO badge_history (operation_type, id, name, type, description, created_at)
        VALUES ('INSERT', NEW.id, NEW.name, NEW.type, NEW.description, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION student_badge_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO student_badge_history (operation_type, student_id, badge_id, assigned_at)
        VALUES ('DELETE', OLD.student_id, OLD.badge_id, OLD.assigned_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO student_badge_history (operation_type, student_id, badge_id, assigned_at)
        VALUES ('UPDATE', OLD.student_id, OLD.badge_id, OLD.assigned_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO student_badge_history (operation_type, student_id, badge_id, assigned_at)
        VALUES ('INSERT', NEW.student_id, NEW.badge_id, NEW.assigned_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION teacher_badge_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_badge_history (operation_type, teacher_id, badge_id, assigned_at)
        VALUES ('DELETE', OLD.teacher_id, OLD.badge_id, OLD.assigned_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_badge_history (operation_type, teacher_id, badge_id, assigned_at)
        VALUES ('UPDATE', OLD.teacher_id, OLD.badge_id, OLD.assigned_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_badge_history (operation_type, teacher_id, badge_id, assigned_at)
        VALUES ('INSERT', NEW.teacher_id, NEW.badge_id, NEW.assigned_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Recreate triggers with new names
CREATE TRIGGER badge_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON badge
FOR EACH ROW
EXECUTE FUNCTION badge_audit_function();

CREATE TRIGGER student_badge_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON student_badge
FOR EACH ROW
EXECUTE FUNCTION student_badge_audit_function();

CREATE TRIGGER teacher_badge_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON teacher_badge
FOR EACH ROW
EXECUTE FUNCTION teacher_badge_audit_function();
