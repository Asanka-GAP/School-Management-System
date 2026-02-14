package com.school.controller;

import com.school.dto.TermDTO;
import com.school.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @PostMapping
    public ResponseEntity<TermDTO> create(@RequestBody TermDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(termService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(termService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAll() {
        return ResponseEntity.ok(termService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TermDTO> update(@PathVariable Long id, @RequestBody TermDTO dto) {
        return ResponseEntity.ok(termService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        termService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
