package com.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClassDTO {
    private Long id;
    private String className;
    private Integer grade;
    private String character;
    private Integer capacity;
    private Long supervisorId;
}
