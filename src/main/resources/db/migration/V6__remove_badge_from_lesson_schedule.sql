------------------------------------------------------------
-- REMOVE BADGE_ID FROM LESSON_SCHEDULE
------------------------------------------------------------

-- Drop the foreign key constraint
ALTER TABLE lesson_schedule DROP CONSTRAINT IF EXISTS fk_schedule_badge;

-- Drop the index
DROP INDEX IF EXISTS idx_schedule_badge_id;

-- Drop the badge_id column
ALTER TABLE lesson_schedule DROP COLUMN IF EXISTS badge_id;

-- Update lesson_schedule_history table to remove badge_id
ALTER TABLE lesson_schedule_history DROP COLUMN IF EXISTS badge_id;

-- Recreate the lesson_schedule_audit_function without badge_id
CREATE OR REPLACE FUNCTION lesson_schedule_audit_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('DELETE', OLD.id, OLD.teacher_id, OLD.subject_id, OLD.day_of_week, OLD.start_time, OLD.end_time, OLD.class_room, OLD.is_active, OLD.created_at);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('UPDATE', OLD.id, OLD.teacher_id, OLD.subject_id, OLD.day_of_week, OLD.start_time, OLD.end_time, OLD.class_room, OLD.is_active, OLD.created_at);
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO lesson_schedule_history (operation_type, id, teacher_id, subject_id, day_of_week, start_time, end_time, class_room, is_active, created_at)
        VALUES ('INSERT', NEW.id, NEW.teacher_id, NEW.subject_id, NEW.day_of_week, NEW.start_time, NEW.end_time, NEW.class_room, NEW.is_active, NEW.created_at);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;
