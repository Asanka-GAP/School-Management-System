package com.school.controller;

import com.school.dto.BatchDTO;
import com.school.enums.BatchType;
import com.school.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchDTO> create(@RequestBody BatchDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(batchService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAll() {
        return ResponseEntity.ok(batchService.getAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BatchDTO>> getByType(@PathVariable BatchType type) {
        return ResponseEntity.ok(batchService.getByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchDTO> update(@PathVariable Long id, @RequestBody BatchDTO dto) {
        return ResponseEntity.ok(batchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
