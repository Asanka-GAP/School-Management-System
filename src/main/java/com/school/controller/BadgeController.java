package com.school.controller;

import com.school.dto.BadgeDTO;
import com.school.enums.BadgeType;
import com.school.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping
    public ResponseEntity<BadgeDTO> create(@RequestBody BadgeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(badgeService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAll() {
        return ResponseEntity.ok(badgeService.getAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BadgeDTO>> getByType(@PathVariable BadgeType type) {
        return ResponseEntity.ok(badgeService.getByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BadgeDTO> update(@PathVariable Long id, @RequestBody BadgeDTO dto) {
        return ResponseEntity.ok(badgeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        badgeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
