package com.school.repository;

import com.school.entity.LessonSchedule;
import com.school.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {
    List<LessonSchedule> findByTeacherId(Long teacherId);
    List<LessonSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<LessonSchedule> findByTeacherIdAndDayOfWeek(Long teacherId, DayOfWeek dayOfWeek);
    List<LessonSchedule> findByBadgeId(Long badgeId);
    List<LessonSchedule> findByIsActive(Boolean isActive);
}
