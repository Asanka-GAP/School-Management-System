package com.school.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectAverageDTO {
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private BigDecimal averageScore;
}
