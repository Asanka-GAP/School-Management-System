package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.MarkDTO;
import com.school.dto.SubjectAverageDTO;
import com.school.service.MarkService;
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
import java.util.List;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;

    @LogApi
    @PostMapping
    public ResponseEntity<MarkDTO> create(@RequestBody MarkDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(markService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<MarkDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<MarkDTO>> getAll() {
        return ResponseEntity.ok(markService.getAll());
    }

    @LogApi
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarkDTO>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(markService.getByStudentId(studentId));
    }

    @LogApi
    @GetMapping("/student/{studentId}/term/{termId}")
    public ResponseEntity<List<MarkDTO>> getByStudentIdAndTermId(
            @PathVariable Long studentId, 
            @PathVariable Long termId) {
        return ResponseEntity.ok(markService.getByStudentIdAndTermId(studentId, termId));
    }

    @LogApi
    @GetMapping("/student/{studentId}/average-per-subject")
    public ResponseEntity<List<SubjectAverageDTO>> getAveragePerSubject(@PathVariable Long studentId) {
        return ResponseEntity.ok(markService.getAverageScorePerSubject(studentId));
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<MarkDTO> update(@PathVariable Long id, @RequestBody MarkDTO dto) {
        return ResponseEntity.ok(markService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
