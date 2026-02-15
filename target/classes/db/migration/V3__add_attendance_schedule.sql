------------------------------------------------------------
-- TEACHER ATTENDANCE TABLE
------------------------------------------------------------
CREATE TABLE teacher_attendance (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PRESENT','ABSENT','LATE','HALF_DAY','ON_LEAVE')),
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teacher(id)
        ON DELETE CASCADE,
    CONSTRAINT unique_teacher_date UNIQUE (teacher_id, attendance_date)
);

CREATE INDEX idx_attendance_teacher_id ON teacher_attendance(teacher_id);
CREATE INDEX idx_attendance_date ON teacher_attendance(attendance_date);

------------------------------------------------------------
-- LESSON SCHEDULE TABLE
------------------------------------------------------------
CREATE TABLE lesson_schedule (
    id BIGSERIAL PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    batch_id BIGINT,
    day_of_week VARCHAR(10) NOT NULL CHECK (day_of_week IN ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    class_room VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teacher(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_schedule_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_schedule_batch
        FOREIGN KEY (batch_id)
        REFERENCES batch(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_schedule_teacher_id ON lesson_schedule(teacher_id);
CREATE INDEX idx_schedule_day ON lesson_schedule(day_of_week);
CREATE INDEX idx_schedule_batch_id ON lesson_schedule(batch_id);

------------------------------------------------------------
-- HISTORY SEQUENCES
------------------------------------------------------------
CREATE SEQUENCE teacher_attendance_history_seq START 1;
CREATE SEQUENCE lesson_schedule_history_seq START 1;

------------------------------------------------------------
-- HISTORY TABLES
------------------------------------------------------------
CREATE TABLE teacher_attendance_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('teacher_attendance_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    teacher_id BIGINT,
    attendance_date DATE,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20),
    remarks TEXT,
    created_at TIMESTAMP
);

CREATE TABLE lesson_schedule_history (
    history_id BIGINT PRIMARY KEY DEFAULT nextval('lesson_schedule_history_seq'),
    operation_type VARCHAR(10) NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operated_by VARCHAR(100) DEFAULT 'SYSTEM',
    id BIGINT,
    teacher_id BIGINT,
    subject_id BIGINT,
    batch_id BIGINT,
    day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME,
    class_room VARCHAR(50),
    is_active BOOLEAN,
    created_at TIMESTAMP
);

------------------------------------------------------------
-- AUDIT FUNCTIONS
------------------------------------------------------------
CREATE OR REPLACE FUNCTION teacher_attendance_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teacher_attendance_history (operation_type, id, teacher_id, attendance_date, check_in_time, check_out_time, status, remarks, created_at)
        VALUES ('DELETE', OLD.id, OLD.teacher_id, OLD.attendance_date, OLD.check_in_time, OLD.check_out_time, OLD.status, OLD.remarks, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teacher_attendance_history (operation_type, id, teacher_id, attendance_date, check_in_time, check_out_time, status, remarks, created_at)
        VALUES ('UPDATE', OLD.id, OLD.teacher_id, OLD.attendance_date, OLD.check_in_time, OLD.check_out_time, OLD.status, OLD.remarks, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teacher_attendance_history (operation_type, id, teacher_id, attendance_date, check_in_time, check_out_time, status, remarks, created_at)
        VALUES ('INSERT', NEW.id, NEW.teacher_id, NEW.attendance_date, NEW.check_in_time, NEW.check_out_time, NEW.status, NEW.remarks, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION lesson_schedule_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, batch_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('DELETE', OLD.id, OLD.teacher_id, OLD.subject_id, OLD.batch_id, OLD.day_of_week, OLD.start_time, OLD.end_time, OLD.class_room, OLD.is_active, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, batch_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('UPDATE', OLD.id, OLD.teacher_id, OLD.subject_id, OLD.batch_id, OLD.day_of_week, OLD.start_time, OLD.end_time, OLD.class_room, OLD.is_active, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, batch_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('INSERT', NEW.id, NEW.teacher_id, NEW.subject_id, NEW.batch_id, NEW.day_of_week, NEW.start_time, NEW.end_time, NEW.class_room, NEW.is_active, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
-- TRIGGERS
------------------------------------------------------------
CREATE TRIGGER teacher_attendance_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON teacher_attendance
FOR EACH ROW
EXECUTE FUNCTION teacher_attendance_audit_function();

CREATE TRIGGER lesson_schedule_audit_trigger
AFTER INSERT OR UPDATE OR DELETE
ON lesson_schedule
FOR EACH ROW
EXECUTE FUNCTION lesson_schedule_audit_function();
