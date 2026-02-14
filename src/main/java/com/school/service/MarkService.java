package com.school.service;

import com.school.dto.MarkDTO;
import com.school.dto.SubjectAverageDTO;
import com.school.entity.*;
import com.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TermRepository termRepository;

    @Transactional
    public MarkDTO create(MarkDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Term term = termRepository.findById(dto.getTermId())
                .orElseThrow(() -> new RuntimeException("Term not found"));
        
        Mark mark = Mark.builder()
                .score(dto.getScore())
                .examDate(dto.getExamDate())
                .student(student)
                .subject(subject)
                .term(term)
                .build();
        
        return toDTO(markRepository.save(mark));
    }

    @Transactional(readOnly = true)
    public MarkDTO getById(Long id) {
        return markRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Mark not found"));
    }

    @Transactional(readOnly = true)
    public List<MarkDTO> getAll() {
        return markRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarkDTO> getByStudentId(Long studentId) {
        return markRepository.findByStudentId(studentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarkDTO> getByStudentIdAndTermId(Long studentId, Long termId) {
        return markRepository.findByStudentIdAndTermId(studentId, termId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectAverageDTO> getAverageScorePerSubject(Long studentId) {
        List<Object[]> results = markRepository.calculateAverageScorePerSubject(studentId);
        return results.stream()
                .map(row -> SubjectAverageDTO.builder()
                        .subjectId(((Number) row[0]).longValue())
                        .subjectName((String) row[1])
                        .subjectCode((String) row[2])
                        .averageScore((BigDecimal) row[3])
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public MarkDTO update(Long id, MarkDTO dto) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mark not found"));
        
        mark.setScore(dto.getScore());
        mark.setExamDate(dto.getExamDate());
        
        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            mark.setStudent(student);
        }
        
        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            mark.setSubject(subject);
        }
        
        if (dto.getTermId() != null) {
            Term term = termRepository.findById(dto.getTermId())
                    .orElseThrow(() -> new RuntimeException("Term not found"));
            mark.setTerm(term);
        }
        
        return toDTO(markRepository.save(mark));
    }

    @Transactional
    public void delete(Long id) {
        markRepository.deleteById(id);
    }

    private MarkDTO toDTO(Mark mark) {
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
