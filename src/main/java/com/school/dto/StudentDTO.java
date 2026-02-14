package com.school.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String admissionNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate admissionDate;
    private String status;
    private Long parentId;
}
