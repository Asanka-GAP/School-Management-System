------------------------------------------------------------
-- SCHOOL_CLASS TABLE
------------------------------------------------------------
CREATE TABLE school_class (
    id BIGSERIAL PRIMARY KEY,
    class_name VARCHAR(10) UNIQUE NOT NULL,
    grade INTEGER NOT NULL,
    character VARCHAR(1) NOT NULL,
    capacity INTEGER NOT NULL,
    supervisor_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_class_supervisor
        FOREIGN KEY (supervisor_id)
        REFERENCES teacher(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_class_supervisor_id ON school_class(supervisor_id);
CREATE INDEX idx_class_grade ON school_class(grade);

-- ADD CLASS_ID TO STUDENT TABLE
ALTER TABLE student ADD COLUMN class_id BIGINT;

ALTER TABLE student ADD CONSTRAINT fk_student_class
    FOREIGN KEY (class_id)
    REFERENCES school_class(id)
    ON DELETE SET NULL;

CREATE INDEX idx_student_class_id ON student(class_id);

-- ADD IS_SUPERVISOR FLAG TO TEACHER TABLE
ALTER TABLE teacher ADD COLUMN is_supervisor BOOLEAN DEFAULT FALSE;

------------------------------------------------------------
-- HISTORY SEQUENCE AND TABLE
------------------------------------------------------------
CREATE SEQUENCE school_class_history_seq START 1;

CREATE TABLE school_class_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('school_class_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    class_name VARCHAR(10),
    grade INTEGER,
    character VARCHAR(1),
    capacity INTEGER,
    supervisor_id BIGINT,
    created_at TIMESTAMP
);

-- UPDATE STUDENT HISTORY TABLE
ALTER TABLE student_history ADD COLUMN class_id BIGINT;

-- UPDATE TEACHER HISTORY TABLE
ALTER TABLE teacher_history ADD COLUMN is_supervisor BOOLEAN;

------------------------------------------------------------
-- AUDIT FUNCTION
------------------------------------------------------------
CREATE OR REPLACE FUNCTION school_class_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO school_class_history (operation_type, id, class_name, grade, character, capacity, supervisor_id, created_at)
        VALUES ('DELETE', OLD.id, OLD.class_name, OLD.grade, OLD.character, OLD.capacity, OLD.supervisor_id, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO school_class_history (operation_type, id, class_name, grade, character, capacity, supervisor_id, created_at)
        VALUES ('UPDATE', OLD.id, OLD.class_name, OLD.grade, OLD.character, OLD.capacity, OLD.supervisor_id, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO school_class_history (operation_type, id, class_name, grade, character, capacity, supervisor_id, created_at)
        VALUES ('INSERT', NEW.id, NEW.class_name, NEW.grade, NEW.character, NEW.capacity, NEW.supervisor_id, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- UPDATE STUDENT AUDIT FUNCTION
CREATE OR REPLACE FUNCTION student_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, class_id, created_at)
        VALUES ('DELETE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.gender, OLD.class_id, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, class_id, created_at)
        VALUES ('UPDATE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.gender, OLD.class_id, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, gender, class_id, created_at)
        VALUES ('INSERT', NEW.id, NEW.admission_number, NEW.first_name, NEW.last_name, NEW.date_of_birth, NEW.admission_date, NEW.status, NEW.parent_id, NEW.gender, NEW.class_id, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- UPDATE TEACHER AUDIT FUNCTION
CREATE OR REPLACE FUNCTION teacher_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, is_supervisor, created_at)
        VALUES ('DELETE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.is_supervisor, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, is_supervisor, created_at)
        VALUES ('UPDATE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.is_supervisor, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, is_supervisor, created_at)
        VALUES ('INSERT', NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.is_supervisor, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
-- TRIGGER
------------------------------------------------------------
CREATE TRIGGER school_class_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON school_class
FOR EACH ROW
EXECUTE FUNCTION school_class_audit_function();
