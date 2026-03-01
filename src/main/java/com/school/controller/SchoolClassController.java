package com.school.controller;

import com.school.dto.SchoolClassDTO;
import com.school.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService classService;

    @PostMapping
    public ResponseEntity<SchoolClassDTO> create(@RequestBody SchoolClassDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SchoolClassDTO>> getAll() {
        return ResponseEntity.ok(classService.getAll());
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<SchoolClassDTO>> getByGrade(@PathVariable Integer grade) {
        return ResponseEntity.ok(classService.getByGrade(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolClassDTO> update(@PathVariable Long id, @RequestBody SchoolClassDTO dto) {
        return ResponseEntity.ok(classService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
