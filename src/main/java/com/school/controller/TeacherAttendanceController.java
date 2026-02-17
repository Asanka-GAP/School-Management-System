package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.TeacherAttendanceDTO;
import com.school.enums.AttendanceStatus;
import com.school.service.TeacherAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teacher-attendance")
@RequiredArgsConstructor
public class TeacherAttendanceController {

    private final TeacherAttendanceService attendanceService;

    @LogApi
    @PostMapping
    public ResponseEntity<TeacherAttendanceDTO> create(@RequestBody TeacherAttendanceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<TeacherAttendanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<TeacherAttendanceDTO>> getAll() {
        return ResponseEntity.ok(attendanceService.getAll());
    }

    @LogApi
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TeacherAttendanceDTO>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(attendanceService.getByTeacherId(teacherId));
    }

    @LogApi
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TeacherAttendanceDTO>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getByDate(date));
    }

    @LogApi
    @GetMapping("/teacher/{teacherId}/range")
    public ResponseEntity<List<TeacherAttendanceDTO>> getByTeacherAndDateRange(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getByTeacherAndDateRange(teacherId, startDate, endDate));
    }

    @LogApi
    @PostMapping("/mark")
    public ResponseEntity<TeacherAttendanceDTO> markAttendance(
            @RequestParam Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam AttendanceStatus status) {
        return ResponseEntity.ok(attendanceService.markAttendance(teacherId, date, status));
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<TeacherAttendanceDTO> update(@PathVariable Long id, @RequestBody TeacherAttendanceDTO dto) {
        return ResponseEntity.ok(attendanceService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
