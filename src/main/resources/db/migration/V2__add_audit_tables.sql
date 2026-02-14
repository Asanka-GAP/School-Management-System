------------------------------------------------------------
-- SEQUENCES FOR HISTORY TABLES
------------------------------------------------------------
CREATE SEQUENCE parent_history_seq START 1;
CREATE SEQUENCE student_history_seq START 1;
CREATE SEQUENCE teacher_history_seq START 1;
CREATE SEQUENCE subject_history_seq START 1;
CREATE SEQUENCE term_history_seq START 1;
CREATE SEQUENCE mark_history_seq START 1;
CREATE SEQUENCE batch_history_seq START 1;
CREATE SEQUENCE student_batch_history_seq START 1;
CREATE SEQUENCE teacher_batch_history_seq START 1;

------------------------------------------------------------
-- HISTORY TABLES
------------------------------------------------------------
CREATE TABLE parent_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('parent_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    phone_number VARCHAR(20),
    created_at TIMESTAMP
);

CREATE TABLE student_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('student_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    admission_number VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    date_of_birth DATE,
    admission_date DATE,
    status VARCHAR(20),
    parent_id BIGINT,
    created_at TIMESTAMP
);

CREATE TABLE teacher_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('teacher_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    specialization VARCHAR(100),
    created_at TIMESTAMP
);

CREATE TABLE subject_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('subject_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    name VARCHAR(100),
    code VARCHAR(20)
);

CREATE TABLE term_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('term_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    name VARCHAR(50),
    academic_year VARCHAR(9)
);

CREATE TABLE mark_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('mark_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    score NUMERIC(5,2),
    exam_date DATE,
    student_id BIGINT,
    subject_id BIGINT,
    term_id BIGINT,
    created_at TIMESTAMP
);

CREATE TABLE batch_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('batch_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    name VARCHAR(100),
    type VARCHAR(20),
    description TEXT,
    created_at TIMESTAMP
);

CREATE TABLE student_batch_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('student_batch_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    student_id BIGINT,
    batch_id BIGINT,
    assigned_at TIMESTAMP
);

CREATE TABLE teacher_batch_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('teacher_batch_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    teacher_id BIGINT,
    batch_id BIGINT,
    assigned_at TIMESTAMP
);

------------------------------------------------------------
-- AUDIT FUNCTIONS
------------------------------------------------------------
CREATE OR REPLACE FUNCTION parent_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO parent_history (operation_type, id, first_name, last_name, email, phone_number, created_at)
        VALUES ('DELETE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.phone_number, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO parent_history (operation_type, id, first_name, last_name, email, phone_number, created_at)
        VALUES ('UPDATE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.phone_number, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO parent_history (operation_type, id, first_name, last_name, email, phone_number, created_at)
        VALUES ('INSERT', NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.phone_number, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION student_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, created_at)
        VALUES ('DELETE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, created_at)
        VALUES ('UPDATE', OLD.id, OLD.admission_number, OLD.first_name, OLD.last_name, OLD.date_of_birth, OLD.admission_date, OLD.status, OLD.parent_id, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO student_history (operation_type, id, admission_number, first_name, last_name, date_of_birth, admission_date, status, parent_id, created_at)
        VALUES ('INSERT', NEW.id, NEW.admission_number, NEW.first_name, NEW.last_name, NEW.date_of_birth, NEW.admission_date, NEW.status, NEW.parent_id, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION teacher_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, specialization, created_at)
        VALUES ('DELETE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.specialization, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, specialization, created_at)
        VALUES ('UPDATE', OLD.id, OLD.first_name, OLD.last_name, OLD.email, OLD.specialization, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_history (operation_type, id, first_name, last_name, email, specialization, created_at)
        VALUES ('INSERT', NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.specialization, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION subject_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO subject_history (operation_type, id, name, code)
        VALUES ('DELETE', OLD.id, OLD.name, OLD.code);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO subject_history (operation_type, id, name, code)
        VALUES ('UPDATE', OLD.id, OLD.name, OLD.code);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO subject_history (operation_type, id, name, code)
        VALUES ('INSERT', NEW.id, NEW.name, NEW.code);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION term_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO term_history (operation_type, id, name, academic_year)
        VALUES ('DELETE', OLD.id, OLD.name, OLD.academic_year);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO term_history (operation_type, id, name, academic_year)
        VALUES ('UPDATE', OLD.id, OLD.name, OLD.academic_year);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO term_history (operation_type, id, name, academic_year)
        VALUES ('INSERT', NEW.id, NEW.name, NEW.academic_year);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION mark_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO mark_history (operation_type, id, score, exam_date, student_id, subject_id, term_id, created_at)
        VALUES ('DELETE', OLD.id, OLD.score, OLD.exam_date, OLD.student_id, OLD.subject_id, OLD.term_id, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO mark_history (operation_type, id, score, exam_date, student_id, subject_id, term_id, created_at)
        VALUES ('UPDATE', OLD.id, OLD.score, OLD.exam_date, OLD.student_id, OLD.subject_id, OLD.term_id, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO mark_history (operation_type, id, score, exam_date, student_id, subject_id, term_id, created_at)
        VALUES ('INSERT', NEW.id, NEW.score, NEW.exam_date, NEW.student_id, NEW.subject_id, NEW.term_id, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION batch_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO batch_history (operation_type, id, name, type, description, created_at)
        VALUES ('DELETE', OLD.id, OLD.name, OLD.type, OLD.description, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO batch_history (operation_type, id, name, type, description, created_at)
        VALUES ('UPDATE', OLD.id, OLD.name, OLD.type, OLD.description, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO batch_history (operation_type, id, name, type, description, created_at)
        VALUES ('INSERT', NEW.id, NEW.name, NEW.type, NEW.description, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION student_batch_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO student_batch_history (operation_type, student_id, batch_id, assigned_at)
        VALUES ('DELETE', OLD.student_id, OLD.batch_id, OLD.assigned_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO student_batch_history (operation_type, student_id, batch_id, assigned_at)
        VALUES ('UPDATE', OLD.student_id, OLD.batch_id, OLD.assigned_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO student_batch_history (operation_type, student_id, batch_id, assigned_at)
        VALUES ('INSERT', NEW.student_id, NEW.batch_id, NEW.assigned_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION teacher_batch_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_batch_history (operation_type, teacher_id, batch_id, assigned_at)
        VALUES ('DELETE', OLD.teacher_id, OLD.batch_id, OLD.assigned_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_batch_history (operation_type, teacher_id, batch_id, assigned_at)
        VALUES ('UPDATE', OLD.teacher_id, OLD.batch_id, OLD.assigned_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_batch_history (operation_type, teacher_id, batch_id, assigned_at)
        VALUES ('INSERT', NEW.teacher_id, NEW.batch_id, NEW.assigned_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
-- TRIGGERS
------------------------------------------------------------
CREATE TRIGGER parent_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON parent
FOR EACH ROW
EXECUTE FUNCTION parent_audit_function();

CREATE TRIGGER student_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON student
FOR EACH ROW
EXECUTE FUNCTION student_audit_function();

CREATE TRIGGER teacher_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON teacher
FOR EACH ROW
EXECUTE FUNCTION teacher_audit_function();

CREATE TRIGGER subject_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON subject
FOR EACH ROW
EXECUTE FUNCTION subject_audit_function();

CREATE TRIGGER term_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON term
FOR EACH ROW
EXECUTE FUNCTION term_audit_function();

CREATE TRIGGER mark_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON mark
FOR EACH ROW
EXECUTE FUNCTION mark_audit_function();

CREATE TRIGGER batch_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON batch
FOR EACH ROW
EXECUTE FUNCTION batch_audit_function();

CREATE TRIGGER student_batch_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON student_batch
FOR EACH ROW
EXECUTE FUNCTION student_batch_audit_function();

CREATE TRIGGER teacher_batch_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON teacher_batch
FOR EACH ROW
EXECUTE FUNCTION teacher_batch_audit_function();
