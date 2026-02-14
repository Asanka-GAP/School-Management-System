package com.school.service;

import com.school.dto.*;
import com.school.entity.*;
import com.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ParentDTO create(ParentDTO dto) {
        Parent parent = Parent.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        return toDTO(parentRepository.save(parent));
    }

    @Transactional(readOnly = true)
    public ParentDTO getById(Long id) {
        return parentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
    }

    @Transactional(readOnly = true)
    public List<ParentDTO> getAll() {
        return parentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParentDTO update(Long id, ParentDTO dto) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setEmail(dto.getEmail());
        parent.setPhoneNumber(dto.getPhoneNumber());
        return toDTO(parentRepository.save(parent));
    }

    @Transactional
    public void delete(Long id) {
        parentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ParentWithChildrenDTO getParentWithChildrenAndMarks(Long parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        
        List<Student> students = studentRepository.findByParentId(parentId);
        
        List<StudentDTO> studentDTOs = students.stream()
                .map(this::toStudentDTO)
                .collect(Collectors.toList());
        
        List<MarkDTO> markDTOs = students.stream()
                .flatMap(s -> s.getMarks().stream())
                .map(this::toMarkDTO)
                .collect(Collectors.toList());
        
        return ParentWithChildrenDTO.builder()
                .parent(toDTO(parent))
                .children(studentDTOs)
                .marks(markDTOs)
                .build();
    }

    private ParentDTO toDTO(Parent parent) {
        return ParentDTO.builder()
                .id(parent.getId())
                .firstName(parent.getFirstName())
                .lastName(parent.getLastName())
                .email(parent.getEmail())
                .phoneNumber(parent.getPhoneNumber())
                .build();
    }

    private StudentDTO toStudentDTO(Student student) {
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

    private MarkDTO toMarkDTO(Mark mark) {
        return MarkDTO.builder()
                .id(mark.getId())
                .score(mark.getScore())
                .examDate(mark.getExamDate())
                .studentId(mark.getStudent().getId())
                .subjectId(mark.getSubject().getId())
                .termId(mark.getTerm().getId())
                .build();
    }
}
