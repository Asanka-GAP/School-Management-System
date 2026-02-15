package com.school.controller;

import com.school.dto.StudentDTO;
import com.school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{studentId}/badges/{badgeId}")
    public ResponseEntity<Void> assignBadge(@PathVariable Long studentId, @PathVariable Long badgeId) {
        studentService.assignBadge(studentId, badgeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/average")
    public ResponseEntity<BigDecimal> getAverage(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.calculateStudentAverage(id));
    }

    @PostMapping("/{id}/auto-assign-badge")
    public ResponseEntity<Void> autoAssignBadge(@PathVariable Long id) {
        studentService.autoAssignBadgeBasedOnPerformance(id);
        return ResponseEntity.ok().build();
    }
}
