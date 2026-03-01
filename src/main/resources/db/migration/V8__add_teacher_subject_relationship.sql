------------------------------------------------------------
-- TEACHER_SUBJECT TABLE (Many-to-Many)
------------------------------------------------------------
CREATE TABLE teacher_subject (
    teacher_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (teacher_id, subject_id),
    CONSTRAINT fk_teacher_subject_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teacher(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_teacher_subject_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_teacher_subject_teacher_id ON teacher_subject(teacher_id);
CREATE INDEX idx_teacher_subject_subject_id ON teacher_subject(subject_id);

------------------------------------------------------------
-- HISTORY SEQUENCE AND TABLE
------------------------------------------------------------
CREATE SEQUENCE teacher_subject_history_seq START 1;

CREATE TABLE teacher_subject_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('teacher_subject_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    teacher_id BIGINT,
    subject_id BIGINT,
    assigned_at TIMESTAMP
);

------------------------------------------------------------
-- AUDIT FUNCTION
------------------------------------------------------------
CREATE OR REPLACE FUNCTION teacher_subject_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_subject_history (operation_type, teacher_id, subject_id, assigned_at)
        VALUES ('DELETE', OLD.teacher_id, OLD.subject_id, OLD.assigned_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_subject_history (operation_type, teacher_id, subject_id, assigned_at)
        VALUES ('UPDATE', OLD.teacher_id, OLD.subject_id, OLD.assigned_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_subject_history (operation_type, teacher_id, subject_id, assigned_at)
        VALUES ('INSERT', NEW.teacher_id, NEW.subject_id, NEW.assigned_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
-- TRIGGER
------------------------------------------------------------
CREATE TRIGGER teacher_subject_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON teacher_subject
FOR EACH ROW
EXECUTE FUNCTION teacher_subject_audit_function();
