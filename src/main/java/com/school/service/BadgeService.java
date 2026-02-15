package com.school.service;

import com.school.dto.BadgeDTO;
import com.school.entity.Badge;
import com.school.enums.BadgeType;
import com.school.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    @Transactional
    public BadgeDTO create(BadgeDTO dto) {
        Badge badge = Badge.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .build();
        return toDTO(badgeRepository.save(badge));
    }

    @Transactional(readOnly = true)
    public BadgeDTO getById(Long id) {
        return badgeRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
    }

    @Transactional(readOnly = true)
    public List<BadgeDTO> getAll() {
        return badgeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BadgeDTO> getByType(BadgeType type) {
        return badgeRepository.findByType(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BadgeDTO update(Long id, BadgeDTO dto) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
        badge.setName(dto.getName());
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        return toDTO(badgeRepository.save(badge));
    }

    @Transactional
    public void delete(Long id) {
        badgeRepository.deleteById(id);
    }

    private BadgeDTO toDTO(Badge badge) {
        return BadgeDTO.builder()
                .id(badge.getId())
                .name(badge.getName())
                .type(badge.getType())
                .description(badge.getDescription())
                .build();
    }
}
