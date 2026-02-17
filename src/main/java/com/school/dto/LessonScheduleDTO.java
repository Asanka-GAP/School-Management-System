package com.school.dto;

import com.school.enums.DayOfWeek;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonScheduleDTO {
    private Long id;
    private Long teacherId;
    private Long subjectId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classRoom;
    private Boolean isActive;
}
