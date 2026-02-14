package com.school.service;

import com.school.dto.SubjectDTO;
import com.school.entity.Subject;
import com.school.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectDTO create(SubjectDTO dto) {
        Subject subject = Subject.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .build();
        return toDTO(subjectRepository.save(subject));
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
        return toDTO(subjectRepository.save(subject));
    }

    @Transactional
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }

    private SubjectDTO toDTO(Subject subject) {
        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .build();
    }
}
