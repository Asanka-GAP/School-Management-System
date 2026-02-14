package com.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermDTO {
    private Long id;
    private String name;
    private String academicYear;
}
