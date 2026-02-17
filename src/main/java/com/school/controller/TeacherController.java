package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.TeacherDTO;
import com.school.service.TeacherService;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @LogApi
    @PostMapping
    public ResponseEntity<TeacherDTO> create(@RequestBody TeacherDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAll() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> update(@PathVariable Long id, @RequestBody TeacherDTO dto) {
        return ResponseEntity.ok(teacherService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @LogApi
    @PostMapping("/{teacherId}/badges/{badgeId}")
    public ResponseEntity<Void> assignBadge(@PathVariable Long teacherId, @PathVariable Long badgeId) {
        teacherService.assignBadge(teacherId, badgeId);
        return ResponseEntity.ok().build();
    }
}
