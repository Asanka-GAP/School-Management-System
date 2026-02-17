package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.ParentDTO;
import com.school.dto.ParentWithChildrenDTO;
import com.school.service.ParentService;
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
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @LogApi
    @PostMapping
    public ResponseEntity<ParentDTO> create(@RequestBody ParentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parentService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAll() {
        return ResponseEntity.ok(parentService.getAll());
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<ParentDTO> update(@PathVariable Long id, @RequestBody ParentDTO dto) {
        return ResponseEntity.ok(parentService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @LogApi
    @GetMapping("/{id}/children-marks")
    public ResponseEntity<ParentWithChildrenDTO> getParentWithChildrenAndMarks(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getParentWithChildrenAndMarks(id));
    }
}
