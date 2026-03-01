package com.school.service;

import com.school.dto.SubjectDTO;
import com.school.dto.TeacherDTO;
import com.school.entity.Subject;
import com.school.entity.Teacher;
import com.school.repository.SubjectRepository;
import com.school.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public SubjectDTO create(SubjectDTO dto) {
        Subject subject = Subject.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .build();
        Subject saved = subjectRepository.save(subject);
        
        if (dto.getTeacherIds() != null && !dto.getTeacherIds().isEmpty()) {
            for (Long teacherId : dto.getTeacherIds()) {
                Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
                if (!teacher.getSubjects().contains(saved)) {
                    teacher.getSubjects().add(saved);
                }
            }
        }
        
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public SubjectDTO getById(Long id) {
        return subjectRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> getAll() {
        return subjectRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubjectDTO update(Long id, SubjectDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setName(dto.getName());
        subject.setCode(dto.getCode());
        Subject saved = subjectRepository.save(subject);
        
        if (dto.getTeacherIds() != null) {
            List<Teacher> allTeachers = teacherRepository.findAll();
            for (Teacher teacher : allTeachers) {
                if (dto.getTeacherIds().contains(teacher.getId())) {
                    if (!teacher.getSubjects().contains(saved)) {
                        teacher.getSubjects().add(saved);
                    }
                } else {
                    teacher.getSubjects().remove(saved);
                }
            }
        }
        
        return toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TeacherDTO> getTeachersBySubject(Long subjectId) {
        return teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getSubjects().stream()
                        .anyMatch(subject -> subject.getId().equals(subjectId)))
                .map(teacher -> TeacherDTO.builder()
                        .id(teacher.getId())
                        .firstName(teacher.getFirstName())
                        .lastName(teacher.getLastName())
                        .email(teacher.getEmail())
                        .subjectIds(teacher.getSubjects().stream()
                                .map(s -> s.getId())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private SubjectDTO toDTO(Subject subject) {
        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .build();
    }
}
