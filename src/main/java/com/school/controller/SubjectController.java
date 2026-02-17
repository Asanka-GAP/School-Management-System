package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.SubjectDTO;
import com.school.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @LogApi
    @PostMapping
    public ResponseEntity<SubjectDTO> create(@RequestBody SubjectDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAll() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> update(@PathVariable Long id, @RequestBody SubjectDTO dto) {
        return ResponseEntity.ok(subjectService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
