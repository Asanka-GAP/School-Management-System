package com.school.service;

import com.school.dto.LessonScheduleDTO;
import com.school.entity.*;
import com.school.enums.DayOfWeek;
import com.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonScheduleService {

    private final LessonScheduleRepository scheduleRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public LessonScheduleDTO create(LessonScheduleDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        LessonSchedule schedule = LessonSchedule.builder()
                .teacher(teacher)
                .subject(subject)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .classRoom(dto.getClassRoom())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        
        return toDTO(scheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public LessonScheduleDTO getById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    @Transactional(readOnly = true)
    public List<LessonScheduleDTO> getAll() {
        return scheduleRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonScheduleDTO> getByTeacherId(Long teacherId) {
        return scheduleRepository.findByTeacherId(teacherId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonScheduleDTO> getByDayOfWeek(DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonScheduleDTO> getByTeacherAndDay(Long teacherId, DayOfWeek dayOfWeek) {
        return scheduleRepository.findByTeacherIdAndDayOfWeek(teacherId, dayOfWeek).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonScheduleDTO update(Long id, LessonScheduleDTO dto) {
        LessonSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            schedule.setTeacher(teacher);
        }
        
        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            schedule.setSubject(subject);
        }
        
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setClassRoom(dto.getClassRoom());
        schedule.setIsActive(dto.getIsActive());
        
        return toDTO(scheduleRepository.save(schedule));
    }

    @Transactional
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    private LessonScheduleDTO toDTO(LessonSchedule schedule) {
        return LessonScheduleDTO.builder()
                .id(schedule.getId())
                .teacherId(schedule.getTeacher().getId())
                .teacherName(schedule.getTeacher().getFirstName() + " " + schedule.getTeacher().getLastName())
                .subjectId(schedule.getSubject().getId())
                .subjectName(schedule.getSubject().getName())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .classRoom(schedule.getClassRoom())
                .isActive(schedule.getIsActive())
                .build();
    }
}
