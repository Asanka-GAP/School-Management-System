package com.school.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkDTO {
    private Long id;
    private BigDecimal score;
    private LocalDate examDate;
    private Long studentId;
    private Long subjectId;
    private Long termId;
}
