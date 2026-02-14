package com.school.service;

import com.school.dto.TermDTO;
import com.school.entity.Term;
import com.school.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    @Transactional
    public TermDTO create(TermDTO dto) {
        Term term = Term.builder()
                .name(dto.getName())
                .academicYear(dto.getAcademicYear())
                .build();
        return toDTO(termRepository.save(term));
    }

    @Transactional(readOnly = true)
    public TermDTO getById(Long id) {
        return termRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Term not found"));
    }

    @Transactional(readOnly = true)
    public List<TermDTO> getAll() {
        return termRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TermDTO update(Long id, TermDTO dto) {
        Term term = termRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Term not found"));
        term.setName(dto.getName());
        term.setAcademicYear(dto.getAcademicYear());
        return toDTO(termRepository.save(term));
    }

    @Transactional
    public void delete(Long id) {
        termRepository.deleteById(id);
    }

    private TermDTO toDTO(Term term) {
        return TermDTO.builder()
                .id(term.getId())
                .name(term.getName())
                .academicYear(term.getAcademicYear())
                .build();
    }
}
