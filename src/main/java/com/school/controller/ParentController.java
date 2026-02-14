package com.school.controller;

import com.school.dto.ParentDTO;
import com.school.dto.ParentWithChildrenDTO;
import com.school.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping
    public ResponseEntity<ParentDTO> create(@RequestBody ParentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parentService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAll() {
        return ResponseEntity.ok(parentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentDTO> update(@PathVariable Long id, @RequestBody ParentDTO dto) {
        return ResponseEntity.ok(parentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/children-marks")
    public ResponseEntity<ParentWithChildrenDTO> getParentWithChildrenAndMarks(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getParentWithChildrenAndMarks(id));
    }
}
