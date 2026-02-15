package com.school.service;

import com.school.dto.TeacherDTO;
import com.school.entity.Badge;
import com.school.entity.Teacher;
import com.school.repository.BadgeRepository;
import com.school.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final BadgeRepository badgeRepository;

    @Transactional
    public TeacherDTO create(TeacherDTO dto) {
        Teacher teacher = Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .specialization(dto.getSpecialization())
                .build();
        return toDTO(teacherRepository.save(teacher));
    }

    @Transactional(readOnly = true)
    public TeacherDTO getById(Long id) {
        return teacherRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    @Transactional(readOnly = true)
    public List<TeacherDTO> getAll() {
        return teacherRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherDTO update(Long id, TeacherDTO dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setEmail(dto.getEmail());
        teacher.setSpecialization(dto.getSpecialization());
        return toDTO(teacherRepository.save(teacher));
    }

    @Transactional
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    @Transactional
    public void assignBadge(Long teacherId, Long badgeId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
        
        if (!teacher.getBadges().contains(badge)) {
            teacher.getBadges().add(badge);
            teacherRepository.save(teacher);
        }
    }

    private TeacherDTO toDTO(Teacher teacher) {
        return TeacherDTO.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .specialization(teacher.getSpecialization())
                .build();
    }
}
