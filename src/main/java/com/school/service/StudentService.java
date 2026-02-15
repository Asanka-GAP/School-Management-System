package com.school.service;

import com.school.dto.*;
import com.school.entity.*;
import com.school.enums.BadgeType;
import com.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final BadgeRepository badgeRepository;
    private final MarkRepository markRepository;

    @Transactional
    public StudentDTO create(StudentDTO dto) {
        Student student = Student.builder()
                .admissionNumber(dto.getAdmissionNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .admissionDate(dto.getAdmissionDate())
                .status(dto.getStatus())
                .build();
        
        if (dto.getParentId() != null) {
            Parent parent = parentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            student.setParent(parent);
        }
        
        return toDTO(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public StudentDTO getById(Long id) {
        return studentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDTO update(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setAdmissionNumber(dto.getAdmissionNumber());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setAdmissionDate(dto.getAdmissionDate());
        student.setStatus(dto.getStatus());
        
        if (dto.getParentId() != null) {
            Parent parent = parentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            student.setParent(parent);
        }
        
        return toDTO(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void assignBadge(Long studentId, Long badgeId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new RuntimeException("Badge not found"));
        
        if (!student.getBadges().contains(badge)) {
            student.getBadges().add(badge);
            studentRepository.save(student);
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateStudentAverage(Long studentId) {
        List<Mark> marks = markRepository.findByStudentId(studentId);
        if (marks.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = marks.stream()
                .map(Mark::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(BigDecimal.valueOf(marks.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Transactional
    public void autoAssignBadgeBasedOnPerformance(Long studentId) {
        BigDecimal average = calculateStudentAverage(studentId);
        
        if (average.compareTo(BigDecimal.valueOf(90)) > 0) {
            List<Badge> yearWiseBadges = badgeRepository.findByType(BadgeType.YEAR_WISE);
            if (!yearWiseBadges.isEmpty()) {
                assignBadge(studentId, yearWiseBadges.get(0).getId());
            }
        } else if (average.compareTo(BigDecimal.valueOf(85)) > 0) {
            List<Badge> subjectWiseBadges = badgeRepository.findByType(BadgeType.SUBJECT_WISE);
            if (!subjectWiseBadges.isEmpty()) {
                assignBadge(studentId, subjectWiseBadges.get(0).getId());
            }
        }
    }

    private StudentDTO toDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .admissionNumber(student.getAdmissionNumber())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .admissionDate(student.getAdmissionDate())
                .status(student.getStatus())
                .parentId(student.getParent() != null ? student.getParent().getId() : null)
                .build();
    }
}
