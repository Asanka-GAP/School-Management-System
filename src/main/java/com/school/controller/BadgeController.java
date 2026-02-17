package com.school.controller;

import com.school.annotation.LogApi;
import com.school.dto.BadgeDTO;
import com.school.enums.BadgeType;
import com.school.service.BadgeService;
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
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @LogApi
    @PostMapping
    public ResponseEntity<BadgeDTO> create(@RequestBody BadgeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(badgeService.create(dto));
    }

    @LogApi
    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(badgeService.getById(id));
    }

    @LogApi
    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAll() {
        return ResponseEntity.ok(badgeService.getAll());
    }

    @LogApi
    @GetMapping("/type/{type}")
    public ResponseEntity<List<BadgeDTO>> getByType(@PathVariable BadgeType type) {
        return ResponseEntity.ok(badgeService.getByType(type));
    }

    @LogApi
    @PutMapping("/{id}")
    public ResponseEntity<BadgeDTO> update(@PathVariable Long id, @RequestBody BadgeDTO dto) {
        return ResponseEntity.ok(badgeService.update(id, dto));
    }

    @LogApi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        badgeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
