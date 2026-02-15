------------------------------------------------------------
-- PARENT TABLE
------------------------------------------------------------
CREATE TABLE parent (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- STUDENT TABLE
------------------------------------------------------------
CREATE TABLE student (
    id BIGSERIAL PRIMARY KEY,
    admission_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    admission_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    parent_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_parent
        FOREIGN KEY (parent_id)
        REFERENCES parent(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_student_parent_id ON student(parent_id);

------------------------------------------------------------
-- TEACHER TABLE
------------------------------------------------------------
CREATE TABLE teacher (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    specialization VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- SUBJECT TABLE
------------------------------------------------------------
CREATE TABLE subject (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL
);

------------------------------------------------------------
-- TERM TABLE
------------------------------------------------------------
CREATE TABLE term (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    academic_year VARCHAR(9) NOT NULL
);

------------------------------------------------------------
-- MARK TABLE
------------------------------------------------------------
CREATE TABLE mark (
    id BIGSERIAL PRIMARY KEY,
    score NUMERIC(5,2) NOT NULL,
    exam_date DATE NOT NULL,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    term_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mark_student
        FOREIGN KEY (student_id)
        REFERENCES student(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_mark_subject
        FOREIGN KEY (subject_id)
        REFERENCES subject(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_mark_term
        FOREIGN KEY (term_id)
        REFERENCES term(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- BATCH TABLE
------------------------------------------------------------
CREATE TABLE batch (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('SUBJECT_WISE','TERM_WISE','YEAR_WISE')),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- STUDENT_BATCH TABLE
------------------------------------------------------------
CREATE TABLE student_batch (
    student_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, batch_id),
    CONSTRAINT fk_student_batch_student
        FOREIGN KEY (student_id)
        REFERENCES student(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_student_batch_batch
        FOREIGN KEY (batch_id)
        REFERENCES batch(id)
        ON DELETE CASCADE
);

------------------------------------------------------------
-- TEACHER_BATCH TABLE
------------------------------------------------------------
CREATE TABLE teacher_batch (
    teacher_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (teacher_id, batch_id),
    CONSTRAINT fk_teacher_batch_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teacher(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_teacher_batch_batch
        FOREIGN KEY (batch_id)
        REFERENCES batch(id)
        ON DELETE CASCADE
);
