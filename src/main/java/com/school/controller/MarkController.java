package com.school.controller;

import com.school.dto.MarkDTO;
import com.school.dto.SubjectAverageDTO;
import com.school.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;

    @PostMapping
    public ResponseEntity<MarkDTO> create(@RequestBody MarkDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(markService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MarkDTO>> getAll() {
        return ResponseEntity.ok(markService.getAll());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarkDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(markService.getByStudentId(studentId));
    }

    @GetMapping("/student/{studentId}/term/{termId}")
    public ResponseEntity<List<MarkDTO>> getByStudentIdAndTermId(
            @PathVariable Long studentId, 
            @PathVariable Long termId) {
        return ResponseEntity.ok(markService.getByStudentIdAndTermId(studentId, termId));
    }

    @GetMapping("/student/{studentId}/average-per-subject")
    public ResponseEntity<List<SubjectAverageDTO>> getAveragePerSubject(@PathVariable Long studentId) {
        return ResponseEntity.ok(markService.getAverageScorePerSubject(studentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarkDTO> update(@PathVariable Long id, @RequestBody MarkDTO dto) {
        return ResponseEntity.ok(markService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
