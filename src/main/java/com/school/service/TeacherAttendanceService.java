package com.school.service;

import com.school.dto.TeacherAttendanceDTO;
import com.school.entity.Teacher;
import com.school.entity.TeacherAttendance;
import com.school.enums.AttendanceStatus;
import com.school.repository.TeacherAttendanceRepository;
import com.school.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherAttendanceService {

    private final TeacherAttendanceRepository attendanceRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public TeacherAttendanceDTO create(TeacherAttendanceDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        TeacherAttendance attendance = TeacherAttendance.builder()
                .teacher(teacher)
                .attendanceDate(dto.getAttendanceDate())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .status(dto.getStatus())
                .remarks(dto.getRemarks())
                .build();
        
        return toDTO(attendanceRepository.save(attendance));
    }

    @Transactional(readOnly = true)
    public TeacherAttendanceDTO getById(Long id) {
        return attendanceRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
    }

    @Transactional(readOnly = true)
    public List<TeacherAttendanceDTO> getAll() {
        return attendanceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherAttendanceDTO> getByTeacherId(Long teacherId) {
        return attendanceRepository.findByTeacherId(teacherId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherAttendanceDTO> getByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherAttendanceDTO> getByTeacherAndDateRange(Long teacherId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByTeacherIdAndAttendanceDateBetween(teacherId, startDate, endDate).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherAttendanceDTO update(Long id, TeacherAttendanceDTO dto) {
        TeacherAttendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        
        attendance.setAttendanceDate(dto.getAttendanceDate());
        attendance.setCheckInTime(dto.getCheckInTime());
        attendance.setCheckOutTime(dto.getCheckOutTime());
        attendance.setStatus(dto.getStatus());
        attendance.setRemarks(dto.getRemarks());
        
        return toDTO(attendanceRepository.save(attendance));
    }

    @Transactional
    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Transactional
    public TeacherAttendanceDTO markAttendance(Long teacherId, LocalDate date, AttendanceStatus status) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        TeacherAttendance attendance = attendanceRepository
                .findByTeacherIdAndAttendanceDate(teacherId, date)
                .orElse(TeacherAttendance.builder()
                        .teacher(teacher)
                        .attendanceDate(date)
                        .build());
        
        attendance.setStatus(status);
        return toDTO(attendanceRepository.save(attendance));
    }

    private TeacherAttendanceDTO toDTO(TeacherAttendance attendance) {
        return TeacherAttendanceDTO.builder()
                .id(attendance.getId())
                .teacherId(attendance.getTeacher().getId())
                .attendanceDate(attendance.getAttendanceDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .build();
    }
}
