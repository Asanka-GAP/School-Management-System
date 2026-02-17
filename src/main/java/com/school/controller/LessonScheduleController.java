package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.LessonScheduleDTO;
import com.school.enums.DayOfWeek;
import com.school.service.LessonScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lesson-schedules")
@RequiredArgsConstructor
public class LessonScheduleController {

    private final LessonScheduleService scheduleService;

    @LogApi
    @PostMapping
    public ResponseEntity<LessonScheduleDTO> create(@RequestBody LessonScheduleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<LessonScheduleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<LessonScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    @LogApi
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<LessonScheduleDTO>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(scheduleService.getByTeacherId(teacherId));
    }

    @LogApi
    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<List<LessonScheduleDTO>> getByDay(@PathVariable DayOfWeek dayOfWeek) {
        return ResponseEntity.ok(scheduleService.getByDayOfWeek(dayOfWeek));
    }

    @LogApi
    @GetMapping("/teacher/{teacherId}/day/{dayOfWeek}")
    public ResponseEntity<List<LessonScheduleDTO>> getByTeacherAndDay(
            @PathVariable Long teacherId,
            @PathVariable DayOfWeek dayOfWeek) {
        return ResponseEntity.ok(scheduleService.getByTeacherAndDay(teacherId, dayOfWeek));
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<LessonScheduleDTO> update(@PathVariable Long id, @RequestBody LessonScheduleDTO dto) {
        return ResponseEntity.ok(scheduleService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
