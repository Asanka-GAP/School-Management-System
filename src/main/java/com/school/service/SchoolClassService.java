package com.school.service;

import com.school.dto.SchoolClassDTO;
import com.school.entity.SchoolClass;
import com.school.entity.Teacher;
import com.school.repository.SchoolClassRepository;
import com.school.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public SchoolClassDTO create(SchoolClassDTO dto) {
        String className = dto.getGrade() + dto.getCharacter();
        
        if (classRepository.findByClassName(className).isPresent()) {
            throw new RuntimeException("Class " + className + " already exists");
        }
        
        Teacher supervisor = teacherRepository.findById(dto.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Supervisor teacher not found"));
        
        supervisor.setIsSupervisor(true);
        teacherRepository.save(supervisor);
        
        SchoolClass schoolClass = SchoolClass.builder()
                .grade(dto.getGrade())
                .character(dto.getCharacter())
                .capacity(dto.getCapacity())
                .supervisor(supervisor)
                .build();
        
        return toDTO(classRepository.save(schoolClass));
    }

    @Transactional(readOnly = true)
    public SchoolClassDTO getById(Long id) {
        return classRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Class not found"));
    }

    @Transactional(readOnly = true)
    public List<SchoolClassDTO> getAll() {
        return classRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SchoolClassDTO> getByGrade(Integer grade) {
        return classRepository.findByGrade(grade).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SchoolClassDTO update(Long id, SchoolClassDTO dto) {
        SchoolClass schoolClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        
        String newClassName = dto.getGrade() + dto.getCharacter();
        if (!schoolClass.getClassName().equals(newClassName)) {
            if (classRepository.findByClassName(newClassName).isPresent()) {
                throw new RuntimeException("Class " + newClassName + " already exists");
            }
        }
        
        if (dto.getSupervisorId() != null) {
            Teacher supervisor = teacherRepository.findById(dto.getSupervisorId())
                    .orElseThrow(() -> new RuntimeException("Supervisor teacher not found"));
            supervisor.setIsSupervisor(true);
            teacherRepository.save(supervisor);
            schoolClass.setSupervisor(supervisor);
        }
        
        schoolClass.setGrade(dto.getGrade());
        schoolClass.setCharacter(dto.getCharacter());
        schoolClass.setCapacity(dto.getCapacity());
        
        return toDTO(classRepository.save(schoolClass));
    }

    @Transactional
    public void delete(Long id) {
        classRepository.deleteById(id);
    }

    private SchoolClassDTO toDTO(SchoolClass schoolClass) {
        return SchoolClassDTO.builder()
                .id(schoolClass.getId())
                .className(schoolClass.getClassName())
                .grade(schoolClass.getGrade())
                .character(schoolClass.getCharacter())
                .capacity(schoolClass.getCapacity())
                .supervisorId(schoolClass.getSupervisor() != null ? schoolClass.getSupervisor().getId() : null)
                .build();
    }
}
