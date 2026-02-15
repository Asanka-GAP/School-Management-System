package com.school.repository;

import com.school.entity.TeacherAttendance;
import com.school.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {
    List<TeacherAttendance> findByTeacherId(Long teacherId);
    List<TeacherAttendance> findByAttendanceDate(LocalDate date);
    List<TeacherAttendance> findByTeacherIdAndAttendanceDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate);
    Optional<TeacherAttendance> findByTeacherIdAndAttendanceDate(Long teacherId, LocalDate date);
    List<TeacherAttendance> findByStatus(AttendanceStatus status);
}
