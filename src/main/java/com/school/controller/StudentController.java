package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.StudentDTO;
import com.school.enums.Gender;
import com.school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @LogApi
    @PostMapping
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<StudentDTO>> getByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(studentService.getByGender(gender));
    }

    @GetMapping("/count/gender/{gender}")
    public ResponseEntity<Long> countByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(studentService.countByGender(gender));
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @LogApi
    @PostMapping("/{studentId}/badges/{badgeId}")
    public ResponseEntity<Void> assignBadge(@PathVariable Long studentId, @PathVariable Long badgeId) {
        studentService.assignBadge(studentId, badgeId);
        return ResponseEntity.ok().build();
    }

    @LogApi
    @GetMapping("/{id}/average")
    public ResponseEntity<BigDecimal> getAverage(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.calculateStudentAverage(id));
    }

    @LogApi
    @PostMapping("/{id}/auto-assign-badge")
    public ResponseEntity<Void> autoAssignBadge(@PathVariable Long id) {
        studentService.autoAssignBadgeBasedOnPerformance(id);
        return ResponseEntity.ok().build();
    }
}
